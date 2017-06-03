package ots

import org.springframework.dao.DataIntegrityViolationException

class ChapterController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [chapterInstanceList: Chapter.list(params), chapterInstanceTotal: Chapter.count()]
    }

    def create() {
        [chapterInstance: new Chapter(params)]
    }

    def save() {
        def chapterInstance = new Chapter(params)
        if (!chapterInstance.save(flush: true)) {
            render(view: "create", model: [chapterInstance: chapterInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'chapter.label', default: 'Chapter'), chapterInstance.id])
        redirect(action: "show", id: chapterInstance.id)
    }

    def show(Long id) {
        def chapterInstance = Chapter.get(id)
		if (params.weixinOpenId) {
			session.weixinOpenId = params.weixinOpenId
		}
        if (!chapterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapter.label', default: 'Chapter'), id])
            redirect(action: "list")
            return
        }
		
		// Below codes are for '水平测试'
		def params4newQuiz = null
		
		if (session?.user instanceof AdminUser) {
			println "ChapterController.show() AdminUser -  no need to generateQuiz"
		}
		else {
			params4newQuiz = generateQuiz(chapterInstance, params)
		}		
		
        [chapterInstance: chapterInstance, params4newQuiz:params4newQuiz]
    }
	
	Map generateQuiz(Chapter chapterInstance, params)
	{
		Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(session.weixinOpenId)
		if (!stu) {
			println "Error: Student not found for session.weixinOpenId=${session.weixinOpenId}"
			return null
		}
		
		session.user = stu
		
		Assignment chapterTopLevelAssi = Assignment.findByNameAndAssignedBy("chapter_top_level_assi", "tewx")
		if (!chapterTopLevelAssi) {
			chapterTopLevelAssi = new Assignment(subject:CONSTANT.COURSE_ENG1, name:"chapter_top_level_assi", assignedBy:"tewx", totalKnowledgePoints:1, numberOfQuestionsPerPage:1).save()
		}
		def nameTag = "Chapter"
		def name = nameTag+(Quiz.countByStudentAndAssignment(stu, chapterTopLevelAssi) + 1)
		
		AssignmentStatus aStatus = AssignmentStatus.findByStudentAndAssignment(stu, chapterTopLevelAssi)
		if (!aStatus) {
			aStatus = new AssignmentStatus(student:stu, assignment:chapterTopLevelAssi, status:CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN ).save()
		}
		session.assignment = chapterTopLevelAssi
		session.assignmentStatus = aStatus
		
		Quiz newQuiz = new Quiz(student:stu, name:name, assignment:chapterTopLevelAssi, status:CONSTANT.QUIZ_STATUS_NOTBEGIN, type:CONSTANT.QUIZ_TYPE_SELFPRAC)
		
		def questionKPMap = [:]
		int questionNumLimit = 5;
		if (!session.numberOfQuestionsPerPage) {
			session.numberOfQuestionsPerPage = questionNumLimit
		}
		
		int assNum = chapterInstance.assignment.size()
		int aNum = 0
				
		// To get question
		chapterInstance.assignment?.each { assi ->	
			boolean added = false
			assi.templates.each { template->
				template.knowledgePoints.each { kp->
					kp.questions.each { ques->
						if (ques.qrCode != null && ques.qrCodeVideoURL !=null){
							if (!added || assNum < questionNumLimit) {
								questionKPMap.put(ques, kp)	
								added = true
							}
						}
					}
				}				
			}
		}
		
		// println "questionKPMap ======== ${questionKPMap}"
		
		def msg = ""
		// if selected question number is less than the limit, try to check KPs' 2 level parents
		if (questionKPMap.size() < questionNumLimit) {
			println "current questionKPMap.size(): ${questionKPMap.size()}"
			msg = "您所选择的知识点题目比较少，章节知识点所含练习少于${questionNumLimit}"
		}
		
		int questionNum = questionKPMap.size()
		
		if (questionKPMap.size() > 0) {
			newQuiz.totalQuestionNum = questionNum
			newQuiz.save()
			// take the first 5 ones
			questionKPMap.take(questionNumLimit).each { selectedQ, kp ->
				newQuiz.addToRecords(new QuizQuestionRecord(quiz:newQuiz, question:selectedQ, chosenFor: kp).save(failOnError:true))
			}
			
			newQuiz.save()
			//redirect(controller:"quizQuestionRecord", action:"answering", params:[quiz_id:newQuiz.id, openId:session.weixinOpenId, kpList: params.kpList, sourceq:params.sourceq, msg:msg])
			return [quiz_id:newQuiz.id, openId:session.weixinOpenId, msg: msg]
		}
		else {
			// returnNewQuizLink = "<font size='5'>没有找到需要练习的题目</font>"
			return [quiz_id:-1, openId:session.weixinOpenId, msg: '没有找到需要练习的题目']
		}
	}

    def edit(Long id) {
        def chapterInstance = Chapter.get(id)
        if (!chapterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapter.label', default: 'Chapter'), id])
            redirect(action: "list")
            return
        }

        [chapterInstance: chapterInstance]
    }

    def update(Long id, Long version) {
        def chapterInstance = Chapter.get(id)
        if (!chapterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapter.label', default: 'Chapter'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (chapterInstance.version > version) {
                chapterInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'chapter.label', default: 'Chapter')] as Object[],
                          "Another user has updated this Chapter while you were editing")
                render(view: "edit", model: [chapterInstance: chapterInstance])
                return
            }
        }

        chapterInstance.properties = params

        if (!chapterInstance.save(flush: true)) {
            render(view: "edit", model: [chapterInstance: chapterInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'chapter.label', default: 'Chapter'), chapterInstance.id])
        redirect(action: "show", id: chapterInstance.id)
    }

    def delete(Long id) {
        def chapterInstance = Chapter.get(id)
        if (!chapterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapter.label', default: 'Chapter'), id])
            redirect(action: "list")
            return
        }

        try {
            chapterInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'chapter.label', default: 'Chapter'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'chapter.label', default: 'Chapter'), id])
            redirect(action: "show", id: id)
        }
    }
}
