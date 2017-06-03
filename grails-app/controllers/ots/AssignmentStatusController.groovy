package ots

import java.text.SimpleDateFormat;

import org.springframework.dao.DataIntegrityViolationException

class AssignmentStatusController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		
		def studentInstance
		if (params.stuId) {			
			studentInstance = Student.get(params.int('stuId'))
		} else if (session.user instanceof Student){
			studentInstance = session.user
		}
		
		def assignmentResult = AssignmentStatus.createCriteria()
				
		def quizInstanceTotal = Quiz.countByStudent(studentInstance)
		def assignmentStatusInstanceList = assignmentResult.list(max: params.max, offset: params.offset){
			if (studentInstance) {
				eq("student", studentInstance)
			}
			order("status", "desc")
			order("id", "desc")
		}
		
		def totalAssiList	
		if(studentInstance) {
			totalAssiList = AssignmentStatus.where{student == studentInstance}
		} else {
			totalAssiList = AssignmentStatus.where{}
		}
		
		def assignmentStatusInstanceTotal = totalAssiList.count()
		def finishedQuestionsTotal = 0
		def correctQuestionsTotal = 0
		def ongoingTotal = 0
		def notBeginTotal = 0
		def finishedTotal = 0
		def focusKPSet = [] as HashSet
		
		totalAssiList.each{
			finishedQuestionsTotal += it.finishedQuestions
			correctQuestionsTotal += it.correctQuestions
			if (it.status == CONSTANT.ASSIGNMENT_STATUS_INPROGRESS) {
				ongoingTotal++
			} else if (it.status == CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN) {
				notBeginTotal++
			} else if (it.status == CONSTANT.ASSIGNMENT_STATUS_DONE) {
				finishedTotal++
			}
			focusKPSet << it.toBeFocusedKnowledge
		}
		
		def ratings = ["", "&#9733;".decodeHTML(), "&#9733;".multiply(2).decodeHTML(), "&#9733;".multiply(3).decodeHTML(),
						"&#9733;".multiply(4).decodeHTML(), "&#9733;".multiply(5).decodeHTML()]
		
		// assignmentStatusInstanceList = assignmentResult.list(params)
		
        [studentInstance:studentInstance, assignmentStatusInstanceList: assignmentStatusInstanceList, assignmentStatusInstanceTotal: assignmentStatusInstanceTotal, quizInstanceTotal: quizInstanceTotal, 
			finishedQuestionsTotal: finishedQuestionsTotal, correctQuestionsTotal: correctQuestionsTotal, ongoingTotal: ongoingTotal, notBeginTotal: notBeginTotal, finishedTotal: finishedTotal,
			focusKPSet: focusKPSet, ratings: ratings]
    }
	
	def list4report(Integer max)
	{
		// list by different students
		
		// list by different teachers
		
		// list by different assignment status
		
		// list&order by score
		
		params.max = Math.min(max ?: 10, 100)
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
		def aList = new ArrayList()
						
		def c = AssignmentStatus.createCriteria()
		def results = c.list(max: params.max, offset: params.offset, sort: params.sort, order: params.order) {
			and {
				if (session.user instanceof Teacher) {
					student {
						eq("teacher", session.user)
					}
				} else if (session.user instanceof Student) {
					eq("student", session.user)
				} else if (session.user instanceof AdminUser && session.user.role == 'admin') {
					// no filter for admin
				} else {
					render "could not generate assignment status report by user: ${session.user}"
				}
				
				if (params.searchValue && params.searchCategory) {
					if (params.searchCategory == "teacher") {
						if (session.user instanceof Teacher) {
							flash.message = "您只能查询你教的学生"
						} else {						
							student {
								eq("teacher", Teacher.findByUserName(params.searchValue.trim()))
							}
						}						
					} 
					else if (params.searchCategory == "student") {
						if (session.user instanceof Student) {
							flash.message = "您只能查询你自己的记录"
						} else {
							eq("student", Student.findByUserName(params.searchValue.trim()))
						}
					}
					else if (params.searchCategory == "studentFullName") {
						if (session.user instanceof Student) {
							flash.message = "您只能查询你自己的记录"
						} else {
							eq("student", Student.findByFullName(params.searchValue.trim()))
						}
					}
					else if (params.searchCategory == "toBeFocusedKnowledge") {
						toBeFocusedKnowledge {
							ilike("name", "%${params.searchValue}%")
						}
					} else {
						// status
						ilike("${params.searchCategory}", "%${params.searchValue}%")
					}
				} 
			}
		}
		
		// println results		
		// results = results.unique{it}
		
		[searchCategory:params.searchCategory, searchValue: params.searchValue, aList: results, aTotal: results.totalCount]	
	}

    def create() {
        [assignmentStatusInstance: new AssignmentStatus(params)]
    }

    def save() {
        def assignmentStatusInstance = new AssignmentStatus(params)
        if (!assignmentStatusInstance.save(flush: true)) {
            render(view: "create", model: [assignmentStatusInstance: assignmentStatusInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), assignmentStatusInstance.id])
        redirect(action: "show", id: assignmentStatusInstance.id)
    }
	
	def evaluateInstructed()
	{
		Student stu = Student.load(params.int('stuId'))
		
		if (stu.briefComment && (params.instructed_evaluating_rate || params.instructed_evaluating_text)) {
			def idx = stu.briefComment.indexOf(Util.token1)
			def recentRecord = stu.briefComment.substring(0, idx) //stu.briefComment?.split(Util.token1, 2)[0]
			// println "recentRecord: ${recentRecord}"
			
			def ritems = recentRecord.split(Util.token2)
			def updRecord = recentRecord
			if (ritems.size() > 4) {
				updRecord = recentRecord.substring(0, recentRecord.indexOf(Util.token2+ritems[4]))
			}
			// println "updRecord-1: ${updRecord}"
			updRecord = updRecord + Util.token2 + params.instructed_evaluating_rate + Util.token2 + params.instructed_evaluating_text
			// println "updRecord-2: ${updRecord}"
			
			stu.briefComment = updRecord + stu.briefComment.substring(idx)
			// println "new stu.briefComment: ${stu.briefComment}"
			stu.save()
		}
		
		redirect(action:'list', params: [stuId: stu.id])
	}
	
	// use telephone2 for msg field at present, telephone2:maxlength=255
	def leaveMsgToTeacher()
	{
		Student stu = Student.load(params.int('stuId'))
				
		if (params.stuToTeacherMsg) {
			
			def msgList = Util.parseLeaveMsg(stu)	
			
			StringBuffer sb = new StringBuffer()
			if (msgList[0]) {
				sb.append(msgList[0]).append(Util.token2)
			}
			if (msgList[1]) {
				sb.append(msgList[1])
			}
			
			def content = params.stuToTeacherMsg
			
			if (content.length() > 127) {
				flash.message = "您输入的文本太长了"
				content = content.substring(0, 127)
			}
			
			sb.append(Util.token1)
				.append(new Date().format("yyyy-MM-dd HH:mm:ss")).append(Util.token2).append(content)
				
			stu.telephone2 = sb.toString()
			stu.save()
		}

		redirect(action:'list', params: [stuId: stu.id])
	}

    def show(Long id) {
        def assignmentStatusInstance = AssignmentStatus.get(id)
        if (!assignmentStatusInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), id])
            redirect(action: "list")
            return
        }

		if (session.user instanceof AdminUser) {
			[assignmentStatusInstance: assignmentStatusInstance]
		} else {
			session.assignment = assignmentStatusInstance.assignment 
			session.assignmentStatus = assignmentStatusInstance
			redirect(controller:"quiz", action:"list", params:['stuId': assignmentStatusInstance.student.id])
		} 
    }

    def edit(Long id) {
        def assignmentStatusInstance = AssignmentStatus.get(id)
        if (!assignmentStatusInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), id])
            redirect(action: "list")
            return
        }
		
        [assignmentStatusInstance: assignmentStatusInstance]
    }

    def update(Long id, Long version) {
        def assignmentStatusInstance = AssignmentStatus.get(id)
        if (!assignmentStatusInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (assignmentStatusInstance.version > version) {
                assignmentStatusInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'assignmentStatus.label', default: 'AssignmentStatus')] as Object[],
                          "Another user has updated this AssignmentStatus while you were editing")
                render(view: "edit", model: [assignmentStatusInstance: assignmentStatusInstance])
                return
            }
        }

        assignmentStatusInstance.properties = params

        if (!assignmentStatusInstance.save(flush: true)) {
            render(view: "edit", model: [assignmentStatusInstance: assignmentStatusInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), assignmentStatusInstance.id])
        redirect(action: "show", id: assignmentStatusInstance.id)
    }

    def delete(Long id) {
        def assignmentStatusInstance = AssignmentStatus.get(id)
        if (!assignmentStatusInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), id])
            redirect(action: "list")
            return
        }

        try {
            assignmentStatusInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'assignmentStatus.label', default: 'AssignmentStatus'), id])
            redirect(action: "show", id: id)
        }
    }
}
