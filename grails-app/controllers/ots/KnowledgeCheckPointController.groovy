package ots

import java.text.SimpleDateFormat
import grails.gorm.*
import org.springframework.dao.DataIntegrityViolationException

class KnowledgeCheckPointController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def beforeInterceptor = [action:this.&checkUser]

	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'endUser',action:'login')
			return false
		}
		
	}

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [knowledgeCheckPointInstanceList: KnowledgeCheckPoint.list(params), knowledgeCheckPointInstanceTotal: KnowledgeCheckPoint.count()]
    }
	
	// 学生历次练习成绩
	def report4allStuQuiz(Integer maxRecord)
	{		
		if (!(session.user instanceof Teacher)) {
			flash.message = "Please note: Now this report is only supported for teacher"
		}
		
		// in case of err: failed to lazily initialize a collection of role: ots.Teacher.students, no session or session was closed
		// get teacher instance from DB
		Teacher teacher = Teacher.get(session.user.id)
				
		params.maxRecord = Math.min(maxRecord ?: 10, 100)		
				
		def selectedStudents = []
		def selectedAssignments = []	
			
		def fDate1 = new Date()-365, fDate2 = new Date()+1
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (params.searchDate1) {
			fDate1 = formatter.parse(params.searchDate1 + " 00:00:00")
		}
		if (params.searchDate2) {
			fDate2 = formatter.parse(params.searchDate2 + " 23:59:59")
		}
		
		def latestAllSelectedStuKCPs = []
		
		if (!params.searchAssignments) {
			selectedAssignments = Assignment.findAllByAssignedBy(teacher.userName)
		} else {
			def assi = []
			assi << params.searchAssignments
			assi = assi.flatten()
			assi.each {
				def a = Assignment.get(it)
				selectedAssignments << a
			}
		}
		
		if (!params.searchStudents) {
			selectedStudents = teacher.students
		} else {
			def stus = []
			stus << params.searchStudents
			stus = stus.flatten()
			
			stus.each {
				def stu = Student.get(it)
				selectedStudents << stu
			}
		}
		
		selectedStudents.each { stu->
			// get the latest Quiz by fDate2 and assignment
			selectedAssignments.each { a->
				
				def quiz = Quiz.findByStudentAndStatusAndAssignmentAndAnsweredDateBetween(stu, CONSTANT.QUIZ_STATUS_SUBMITTED, a, fDate1, fDate2, [sort:'answeredDate', order:'desc'])
						
				println "quiz: ${quiz}"		
				// get the latest KCPs with questions answered of this student
				if (quiz) {
					def stuLatestKCPs = KnowledgeCheckPoint.findAllByQuizAndTotalSumGreaterThan(quiz, 0)
					latestAllSelectedStuKCPs += stuLatestKCPs
				}
			}
		}
						
		def c = KnowledgeCheckPoint.createCriteria()
		
		def results = c.list(max: params.maxRecord, offset: params.offset) {
						
			order("stuCount", "desc")
			order("avgR", "asc")
			
			projections {
				groupProperty("knowledge", "kp") // item-0
				
				avg("recentCorrectRate", "avgR") // item-1
				min("recentCorrectRate", "minR")  // item-2 
				max("recentCorrectRate", "maxR") // item-3, please don't conflict with max=param.max
				countDistinct("student", "stuCount") // item-4
			}
			
			if (latestAllSelectedStuKCPs.size() > 0) {
				'in'('id', latestAllSelectedStuKCPs*.id)	
			} else {
				'in' ('id', -1L) // id will not be -1, this condition is to be always false
			}
			gt('recentCorrectRate', new BigDecimal(-1))	
		}
		
		def fDetails = [:]
		def qnumDetails = [:]
		
		// poor performance, use latestAllSelectedStuKCPs as allSelectedStuKCPs		
		/*def allSelectedStuKCPs = []
		selectedStudents.each { stu->
			// get the latest Quiz by fDate2 and assignment
			selectedAssignments.each { a->
				
				def quizList = Quiz.findAllByStudentAndStatusAndAssignmentAndAnsweredDateBetween(stu, CONSTANT.QUIZ_STATUS_SUBMITTED, a, fDate1, fDate2, [max:5, sort:'answeredDate', order:'desc'])
						
				// get the latest KCPs with questions answered of this student
				if (quizList) {
					quizList.each {  quiz->						
						def stuKCPs = KnowledgeCheckPoint.findAllByQuizAndTotalSumGreaterThan(quiz, 0)
						if (stuKCPs) {
							allSelectedStuKCPs += stuKCPs
						}
					}
				}
			}
		}
		*/
		
		latestAllSelectedStuKCPs.each { r ->			
			// 学生正确率详情			
			def fval = [:]
			fval.put(r.student.fullName, "<span data-toggle='tooltip' data-placement='right' title='"+r.quiz+"'><font color=blue size='-1'>${r.recentCorrectRate}</font></span>")
			
			if (fDetails.get(r.knowledge.id)) {			
				def existingVal = fDetails.get(r.knowledge.id)
				if (existingVal.get(r.student.fullName)){
					existingVal.put(r.student.fullName, existingVal.get(r.student.fullName)+", <span data-toggle='tooltip' data-placement='right' title='"+r.quiz+"'><font color=blue size='-1'>${r.recentCorrectRate}</font></span>")
				} else {
					existingVal.put(r.student.fullName, "<span data-toggle='tooltip' data-placement='right' title='"+r.quiz+"'><font color=blue size='-1'>${r.recentCorrectRate}</font></span>")
				}
			} else {
				fDetails.put(r.knowledge.id, fval)
			}
			
			// 学生题目数
			if (qnumDetails.get(r.knowledge.id)) {
				def existingVal = qnumDetails.get(r.knowledge.id)
				existingVal.put(r.student.fullName, r.totalSum)
			} else {			
				def qnumStuVal = [:]
				qnumStuVal.put(r.student.fullName, r.totalSum)				
				qnumDetails.put(r.knowledge.id, qnumStuVal)
			}								
		}
		
		[params:params, results: results, resultsTotalCount: results.totalCount, teacher:teacher, 
			selectedStudents:selectedStudents, selectedAssignments:selectedAssignments, 
			fDetails:fDetails, qnumDetails:qnumDetails]
	}

    def create() {
        [knowledgeCheckPointInstance: new KnowledgeCheckPoint(params)]
    }

    def save() {
        def knowledgeCheckPointInstance = new KnowledgeCheckPoint(params)
        if (!knowledgeCheckPointInstance.save(flush: true)) {
            render(view: "create", model: [knowledgeCheckPointInstance: knowledgeCheckPointInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), knowledgeCheckPointInstance.id])
        redirect(action: "show", id: knowledgeCheckPointInstance.id)
    }

    def show(Long id) {
        def knowledgeCheckPointInstance = KnowledgeCheckPoint.get(id)
        if (!knowledgeCheckPointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), id])
            redirect(action: "list")
            return
        }

        [knowledgeCheckPointInstance: knowledgeCheckPointInstance]
    }

    def edit(Long id) {
        def knowledgeCheckPointInstance = KnowledgeCheckPoint.get(id)
        if (!knowledgeCheckPointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), id])
            redirect(action: "list")
            return
        }

        [knowledgeCheckPointInstance: knowledgeCheckPointInstance]
    }

    def update(Long id, Long version) {
        def knowledgeCheckPointInstance = KnowledgeCheckPoint.get(id)
        if (!knowledgeCheckPointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (knowledgeCheckPointInstance.version > version) {
                knowledgeCheckPointInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint')] as Object[],
                          "Another user has updated this KnowledgeCheckPoint while you were editing")
                render(view: "edit", model: [knowledgeCheckPointInstance: knowledgeCheckPointInstance])
                return
            }
        }

        knowledgeCheckPointInstance.properties = params

        if (!knowledgeCheckPointInstance.save(flush: true)) {
            render(view: "edit", model: [knowledgeCheckPointInstance: knowledgeCheckPointInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), knowledgeCheckPointInstance.id])
        redirect(action: "show", id: knowledgeCheckPointInstance.id)
    }

    def delete(Long id) {
        def knowledgeCheckPointInstance = KnowledgeCheckPoint.get(id)
        if (!knowledgeCheckPointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), id])
            redirect(action: "list")
            return
        }

        try {
            knowledgeCheckPointInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint'), id])
            redirect(action: "show", id: id)
        }
    }
}
