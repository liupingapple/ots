package ots

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException
//import org.grails.plugins.google.visualization.data.Cell

class AssignmentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		def onePage = Assignment.where{assignedBy == session.user.userName}.list(params)
		def all = Assignment.where{assignedBy == session.user.userName}.list()
		
        [assignmentInstanceList: onePage, assignmentInstanceTotal: all.size()]
    }

	def filterTemplate = {
		def c = AssignmentTemplate.createCriteria()
		def assignmentTemplateList = c.list(sort: 'templateName') {
			and {
				ilike("templateName", "%${params.nameFilter}%")
			}
		}
		render(template: 'assignmentTemplate_list', model:  [assignmentTemplateList: assignmentTemplateList])
	}

    def create() {
        [assignmentInstance: new Assignment(params), assignmentTemplateList: AssignmentTemplate.list(sort: 'templateName')]
    }

    def save() {
        def assignmentInstance = new Assignment(params)	
		
		assignmentInstance.questionLimit = Math.max(assignmentInstance.numberOfQuestionsPerPage, assignmentInstance.questionLimit)
		assignmentInstance.targetCorrectRate = assignmentInstance.targetCorrectRate/100 
		
		// because assignment has many AssignmentTemplates, we pick the 1st AssignmentTemplate's subject as is
		// user will not update Assignement.subject
		assignmentInstance.subject = assignmentInstance.templates*.subject[0]
		
		// for totalAvailableQuestions
		def totalAssociatedQuestions = [] as HashSet
		def totalKnowledgePoints = [] as HashSet
		
		def r = QuizQuestionRecord.createCriteria()
		assignmentInstance.templates.each { template->
			template.knowledgePoints.each { kp->
				totalKnowledgePoints << kp
				if (kp.associatedQuestion > 0) {
					kp.questions.each { ques->
						totalAssociatedQuestions << ques
					}
				}
			}
		}
		assignmentInstance.totalAvailableQuestions = totalAssociatedQuestions.size()
		assignmentInstance.totalKnowledgePoints = totalKnowledgePoints.size()
				
        if (!assignmentInstance.save(flush: true)) {
            render(view: "create", model: [assignmentInstance: assignmentInstance])
            return
        }
		
		if (session.user instanceof Student) {
			new AssignmentStatus(student:session.user, assignment:assignmentInstance ).save(flush:true, failOnError:true)
		}

        flash.message = message(code: 'default.created.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.id])
        redirect(action: "show", id: assignmentInstance.id)
    }

    def show(Long id) {
        def assignmentInstance = Assignment.get(id)
        if (!assignmentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignment.label', default: 'Assignment'), id])
            redirect(action: "list")
            return
        }

        [assignmentInstance: assignmentInstance]
    }
	
	def assignToStu() {
		Teacher teacher = Teacher.get(params.int('teacherId'))
		Assignment asmt = Assignment.get(params.int('asmtId'))

		// 知识点结构图
		def orgColumns = [['string', 'KP'], ['string', 'ParentKP'], ['string', 'ToolTip']]
		def orgData = []
		
		// No KP Org chart available now
		/*if (teacher.toShowKnowledgeGraph) {
			asmt.templates.each {
				addChildToOrgData(orgData, it.knowledgePoints, "")
			}
			//println "orgData: ${orgData}"
		}*/
		
		HashSet<Student> existing_students = []

		AssignmentStatus.findAllByAssignment(asmt)?.each {
			existing_students.add(it.student)
		}

		HashSet<Student> available_students = []
		teacher.students.each {

			if (!existing_students*.id.contains(it.id)) {
				available_students.add(it)
			}
		}

		[asmt:asmt, teacherId: teacher.id,  available_students:available_students, existing_students:existing_students, orgColumns:orgColumns, orgData:orgData, fromController:params.fromController, toShowKnowledgeGraph:teacher.toShowKnowledgeGraph]
	}

	/*	
	private void addChildToOrgData(List orgData, knowledgePoints, kpname)
	{		
		knowledgePoints.each {		
			if (it.totalQuestion > 0) {
				orgData.add([
					new org.grails.plugins.google.visualization.data.Cell(value:it.name, label:it.name),
					kpname,
					it.toString()
				])
				if (it.familySize > 1) {
					addChildToOrgData(orgData, it.childPoints, it.name)
				}				
			}
		}
	}
	*/
	
	def assignToStuSave() {
		//println "assignToStuSave params: ${params}"
		Assignment asmt = Assignment.get(params.int('asmtId'))
				
		// delete AssignmentStatus from available student list
		def avai_stus = []
		if (params.available_students) {
			avai_stus << params.avai_stus
		}
		avai_stus = avai_stus.flatten()
		avai_stus.each {
			AssignmentStatus.deleteAll(AssignmentStatus.findAllByAssignmentAndStudent(asmt, Student.get(Integer.parseInt(it))))
		}
		
		// add AssignmentStatus from selected student list if havn't added before
		def sel_stus = []
		if (params.selected_students) {
			sel_stus << params.selected_students
		}
		sel_stus = sel_stus.flatten()
		
		sel_stus.each {
			def aStatus = AssignmentStatus.findAllByAssignmentAndStudent(asmt, Student.get(Integer.parseInt(it)))
			if (!aStatus) {
				new AssignmentStatus(student:Student.get(Integer.parseInt(it)), assignment:asmt, status:CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN ).save(flush:true, failOnError:true)
			}
		}
		
		flash.message = "保存成功"
			
		redirect(action: "assignToStu", params:params)
	}

    def edit(Long id) {
        def assignmentInstance = Assignment.get(id)
        if (!assignmentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignment.label', default: 'Assignment'), id])
            redirect(action: "list")
            return
        }

        [assignmentInstance: assignmentInstance, assignmentTemplateList: AssignmentTemplate.list(sort: 'templateName')]
    }

    def update(Long id, Long version) {
        def assignmentInstance = Assignment.get(id)
        if (!assignmentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignment.label', default: 'Assignment'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (assignmentInstance.version > version) {
                assignmentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'assignment.label', default: 'Assignment')] as Object[],
                          "Another user has updated this Assignment while you were editing")
                render(view: "edit", model: [assignmentInstance: assignmentInstance])
                return
            }
        }

        assignmentInstance.properties = params
		
		assignmentInstance.questionLimit = Math.max(assignmentInstance.numberOfQuestionsPerPage, assignmentInstance.questionLimit)
		assignmentInstance.targetCorrectRate = assignmentInstance.targetCorrectRate/100
		
		// because assignment has many AssignmentTemplates, we pick the 1st AssignmentTemplate's subject as is
		// user will not update Assignement.subject
		assignmentInstance.subject = assignmentInstance.templates*.subject[0]
		
		// for totalAvailableQuestions
		def totalAssociatedQuestions = [] as HashSet
		def totalKnowledgePoints = [] as HashSet
		
		def r = QuizQuestionRecord.createCriteria()
		assignmentInstance.templates.each { template->
			template.knowledgePoints.each { kp->
				totalKnowledgePoints << kp
				if (kp.associatedQuestion > 0) {
					kp.questions.each { ques->
						totalAssociatedQuestions << ques
					}
				}
			}
		}
		assignmentInstance.totalAvailableQuestions = totalAssociatedQuestions.size()
		assignmentInstance.totalKnowledgePoints = totalKnowledgePoints.size()
		
        if (!assignmentInstance.save(flush: true)) {
            render(view: "edit", model: [assignmentInstance: assignmentInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.id])
        redirect(action: "show", id: assignmentInstance.id)
    }

    def delete(Long id) {
        def assignmentInstance = Assignment.get(id)
        if (!assignmentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignment.label', default: 'Assignment'), id])
            redirect(action: "list")
            return
        }

        try {
            assignmentInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'assignment.label', default: 'Assignment'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'assignment.label', default: 'Assignment'), id])
            redirect(action: "show", id: id)
        }
    }
}
