package ots

import java.util.Iterator
import java.io.File.TempDirectory;
import java.text.SimpleDateFormat

import org.springframework.dao.DataIntegrityViolationException

class QuestionController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def beforeInterceptor = [action:this.&checkUser]

	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'endUser',action:'login')
			return false
		} else if (session.user instanceof Student) {
			flash.message = "Sorry, you are not allowed to access QuestionController"
			redirect(controller:"student", action:"quizList", id:session.user.id)
			return false
		}
		// println "${session.user} is allowed to access QuestionController"
	}

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
		def quesList = new ArrayList()
				
		// in case session.question_xxx values will be assigned with blank string when delete()/edit() actions
		// blank string for 'sort' will cause the error: could not resolve property:  of: ots.Question
		// so remove the params and session's related values if blank
		
		if (params.searchCategory) {
			session.question_searchCategory = params.searchCategory
		} else if (!session.question_searchCategory) {
			session.removeAttribute("question_searchCategory")
			params.remove("searchCategory")
		}
		
		if (params.searchable) {
			session.question_searchable = params.searchable
		} else if (!session.question_searchable) {
			session.removeAttribute("question_searchable")
			params.remove("searchable")
		}
		
		if (params.sort) {
			session.question_sort = params.sort
		} else if (!session.question_sort) {
			session.removeAttribute("question_sort")
			params.remove("sort")
		}
		
		if (params.order) {
			session.question_order = params.order			
		} else if (!session.question_order) {
			session.removeAttribute("question_order")
			params.remove("order")
		}
				
		def c = Question.createCriteria()
		def nextResults = c.list(max: 500, offset: 0, sort: params.sort, order: params.order) {
			and {
				if (session.user.role == "inputer") {
					eq("inputBy", session.user.userName)
				} else if (session.user.role == "linker") {
					or {
						eq("inputBy", session.user.userName)
						isNull("inputBy")
					}
				} else if (session.user.role == "checker") {
					or {
						eq("reviewedBy", session.user.userName)
						isNull("reviewedBy")
					}
				}
				
				if (params.searchable && params.searchCategory) {
					if (params.searchCategory == "idlt") {
						lt("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "id") {
						eq("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "idgt") {
						gt("id", Long.parseLong(params.searchable))
					} 
					else if (params.searchCategory == "qrCode") {
						eq("qrCode", params.searchable.trim())
					} 
					else if (params.searchCategory.startsWith("qrCode_")) {
						// qrCode is String type, how to change to int compare? use Hibernate Query Language (HQL) not work
					}
					else if (params.searchCategory == "inputBy") {
						if (params.searchable) { 
							eq(params.searchCategory, params.searchable.trim())
						} else {
							isNull(params.searchCategory)
						}
					} else if (params.searchCategory == "inputDatelt") {
						def fDate = formatter.parse(params.searchable + " 00:00:00")
						lt("inputDate", fDate)
					} else if (params.searchCategory == "inputDate") {
						def fDate1 = formatter.parse(params.searchable + " 00:00:00")
						def fDate2 = formatter.parse(params.searchable + " 23:59:59")
						lt("inputDate", fDate2)
						gt("inputDate", fDate1)
					} else if (params.searchCategory == "inputDategt") {
						def fDate = formatter.parse(params.searchable + " 23:59:59")
						gt("inputDate", fDate)
					} else if (params.searchCategory == "knowledgePoints") {
						knowledgePoints {
							ilike("name", "${params.searchable}%")
						}
					} else {
						ilike("${params.searchCategory}", "%${params.searchable}%")
					}
				} else {
					if(params.searchCategory) {
						if (params.searchCategory == "knowledgePoints") {
							isEmpty(params.searchCategory)
						} else {
							or {
								isNull(params.searchCategory)
								eq("${params.searchCategory}", "")
							}
						}
					}
				}
			}
		}

		nextResults = nextResults.unique{it}
		nextResults?.each{ ques -> quesList.add(ques.id)}
		session["QuestionList"] = quesList
		
		c = Question.createCriteria()
		def results 
				
		results = c.list(max: params.max, offset: params.offset, sort: params.sort, order: params.order) {
			and {
				if (session.user.role == "inputer") {
					eq("inputBy", session.user.userName)
				} else if (session.user.role == "linker") {
					or {
						eq("inputBy", session.user.userName)
						isNull("inputBy")
					}
				} else if (session.user.role == "checker") {
					or {
						eq("reviewedBy", session.user.userName)
						isNull("reviewedBy")
					}
				}
				
				if (params.searchable && params.searchCategory) {
					if (params.searchCategory == "idlt") {
						lt("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "id") {
						eq("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "idgt") {
						gt("id", Long.parseLong(params.searchable))
					} 
					else if (params.searchCategory == "qrCode") {
						eq("qrCode", params.searchable.trim())
					}
					else if (params.searchCategory == "qrCode_gt") {
						// qrCode is String type, how to change to int compare? use Hibernate Query Language (HQL) not work
						gt("qrCode", params.searchable)
					}
					else if (params.searchCategory == "qrCode_lt") {
						// qrCode is String type, how to change to int compare? use Hibernate Query Language (HQL) not work
						lt("qrCode", params.searchable)
					}
					else if (params.searchCategory == "inputBy") {
						if (params.searchable) { 
							eq(params.searchCategory, params.searchable.trim())
						} else {
							isNull(params.searchCategory)
						}
					} else if (params.searchCategory == "inputDatelt") {
						def fDate = formatter.parse(params.searchable + " 00:00:00")
						lt("inputDate", fDate)
					} else if (params.searchCategory == "inputDate") {
						def fDate1 = formatter.parse(params.searchable + " 00:00:00")
						def fDate2 = formatter.parse(params.searchable + " 23:59:59")
						lt("inputDate", fDate2)
						gt("inputDate", fDate1)
					} else if (params.searchCategory == "inputDategt") {
						def fDate = formatter.parse(params.searchable + " 23:59:59")
						gt("inputDate", fDate)
					} else if (params.searchCategory == "knowledgePoints") {
						knowledgePoints {
							ilike("name", "${params.searchable}%")
						}
					} else {
						ilike("${params.searchCategory}", "%${params.searchable}%")
					}
				} else {
					if(params.searchCategory) {
						if (params.searchCategory == "knowledgePoints") {
							isEmpty(params.searchCategory)
						} else {
							or {
								isNull(params.searchCategory)
								eq("${params.searchCategory}", "")
							}
						}
					}
				}
			}
		}
		
		// does not work
		/*if (params.searchCategory == "qrCode_gt" && params.searchable) {
			results = Question.findAll("from Question as q where cast(q.qrCode as signed integer) > "+params.searchable, [max: params.max, offset: params.offset])
		}
		else if (params.searchCategory == "qrCode_lt" && params.searchable) {
			results = Question.findAll("from Question as q where q.qrCode < ?",[params.searchable])
			println "results: ${results}"
		}*/
		
		results = results.unique{it}
		
		[searchCategory:params.searchCategory, searchKeyword: params.searchable, questionInstanceList: results, questionInstanceTotal: results.totalCount]
    }

	def judge() {
		
		def questionInstance = Question.get(params.questionInstanceId)
		
		Iterator<Answer> ansIt =  questionInstance.answers.iterator()
			
		if (questionInstance.type == "单选") {
			boolean isRight=false
			while(ansIt.hasNext()){
				Answer temp = (Answer)ansIt.next()
				if(temp.checkUserAnswer(params.answerByUser)) {
					isRight=true;
				}
			}
			
			if(isRight){
				flash.message = "Great, your answer is right!"
			}
			else {
				flash.message = "Sorry, your answer is wrong. Please try again."
			}
		} 
		else if (questionInstance.type == "填空") {
			questionInstance.errorRate = 0
			def tmpMsg = ""
			def index = 0
			while(ansIt.hasNext()){
				index++
				Answer temp = (Answer)ansIt.next() 
				if(temp.checkUserAnswer(params["answerByUser"+temp.id])) {
					tmpMsg += "<p> correct at #"+index+" (your answer is:"+params["answerByUser"+temp.id]+")"
				}
				else {
					questionInstance.errorRate++
					tmpMsg += "<p> wrong at #"+index+" (your answer is:"+params["answerByUser"+temp.id]+")"
				}	
				
			}
						
			if(questionInstance.errorRate == 0){
				flash.message = "Great, your answer is right!"
			}
			else {
				flash.message = "Has wrong answer(s). Please try again. "+tmpMsg
			}
		}
		
		render(view: "test", model: [questionInstance: questionInstance])
		return
	}

	def index() {
		if (!(session.user instanceof AdminUser)) {
			flash.message = "Sorry, you are not Admin User, please try to log in with an Admin account"
			redirect(controller:'adminUser', action:'login')
			session.user = null
			return false
		}
		redirect(action: "list", params: params)
	}

	def create() {
		Question newQuestion = new Question(params)
		def defaultVal = session["defaultValue"]
		if (defaultVal) {
			newQuestion.instructions = defaultVal["instructions"]
			newQuestion.source = defaultVal["source"]
			newQuestion.term = defaultVal["term"]
		}
		
		def childQuestionType = ""
		if (params.parentQuestionId) {
			def t = Question.get(params.parentQuestionId)?.type
			if (t == CONSTANT.YDLJ_DUOXTK || t == CONSTANT.YDLJ_SZMTK || t == CONSTANT.YDLJ_TIANKONG ) {
				childQuestionType = CONSTANT.FBLANK_QUESTION
			} else {
				childQuestionType = CONSTANT.RADIO_QUESTION
			}
		}
				
		//setDuplicateFields newQuestion
		[questionInstance: newQuestion, toCreateQuestionType:params.toCreateQuestionType, parentQuestionId:params.parentQuestionId, childQuestionType:childQuestionType]
	}
	
	def save() {
		def questionInstance = new Question(params)

		def c = Question.createCriteria()
		def currentMaxQID = c.get { projections { max('qID') } }
		if (!currentMaxQID) {
			currentMaxQID = 0
		} 
		questionInstance.qID = currentMaxQID + 1
		
		questionInstance.inputBy = session.user.userName
		questionInstance.inputDate = new Date()
		reorderAndCleanupAnswers(questionInstance)
		questionInstance.answers.removeAll { it.deleted }
		
		if (params.parentQuestionId) {
			Question parentQuestion = Question.get(params.parentQuestionId)
			parentQuestion.addToChildQuestions(questionInstance)
		}
		
		questionInstance.description = questionInstance.description
		if (!questionInstance.description) {
			flash.message = "questionInstance.description could not be null"
		}
		
		if (!questionInstance.save()) {
			render(view: "create", model: [questionInstance: questionInstance])
			return
		}

		def defaultVal = [instructions: questionInstance.instructions, source: questionInstance.source, term: questionInstance.term]
		session["defaultValue"] = defaultVal

		flash.message = message(code: 'default.created.message', args: [
			message(code: 'question.label', default: 'Question'),
			questionInstance.id
		])
		
		redirect(action: "show", id: questionInstance.id)
	}
	
	def test(Long id) {
		def questionInstance = Question.get(id)
		if (!questionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			redirect(action: "list")
			return
		}		
		
		[questionInstance: questionInstance]
	}
	
	def uncheck(Long id) {
		uncheckIt(id)
		redirect(action: "list", params: [sort:session.question_sort, order:session.question_order, searchCategory:session.question_searchCategory, searchable:session.question_searchable])
	}

	def uncheckAndNext(Long id) {
		uncheckIt(id)
		showNext(id)
	}

	def reviewed(Long id) {
		reviewIt(id)
		redirect(action: "list", params: [sort:session.question_sort, order:session.question_order, searchCategory:session.question_searchCategory, searchable:session.question_searchable])
	}

	def uncheckIt(Long id) {
		def questionInstance = Question.get(id)
		questionInstance.reviewedBy = null
		
		if (!questionInstance.save(flush: true)) {
			flash.message = "Uncheck of question " + id + " failed!"
			render(view: "show", model: [questionInstance: questionInstance])
		}
	}

	def reviewIt(Long id) {
		def questionInstance = Question.get(id)
		questionInstance.reviewedBy = session.user.userName
		
		if (!questionInstance.save(flush: true)) {
			flash.message = "Check of question " + id + " failed!"
			render(view: "show", model: [questionInstance: questionInstance])
		}
	}

	def link(Long id) {
		def questionInstance = Question.get(id)
		session["linkingQuestionID"] = questionInstance.id
		redirect(controller:"knowledgePoint", action: "list")
	}
	
	def showAnsweredStats(Long id) {
		def questionInstance = Question.get(id)
		def quizQRecords = QuizQuestionRecord.findAllByQuestion(questionInstance)
		
		def userInputMap = [:] as HashMap
		int correctNum = 0, failedNum = 0
		StringBuffer correctAnsweredStu = new StringBuffer("Correct answered students: ")
		StringBuffer failedAnsweredStu = new StringBuffer("Wrong answered students: ")
		quizQRecords.each { quizQRecord->
			quizQRecord.stuAnswers.each { stuA->
				if (stuA.correct) {
					correctNum++
					correctAnsweredStu.append(quizQRecord.quiz.student).append("&nbsp;&nbsp;")
				} else {
					failedNum++
					failedAnsweredStu.append(quizQRecord.quiz.student).append("&nbsp;&nbsp;")
				}
				
				def userInputX = stuA.userInput
				if (questionInstance.type == CONSTANT.RADIO_QUESTION) {
					userInputX = Answer.get(stuA.userInput).toString()
				}
				
				def uinputNum = userInputMap.get(userInputX)
								
				if (!uinputNum) {
					userInputMap.put(userInputX, 1)
				} else {
					userInputMap.put(userInputX, uinputNum+1)
				}				
			}
		}
		
		if (userInputMap.size() < 1) {
			render "So far none student has practiced this question"
		} else {
			render """
					<p>- Total correct#:${correctNum}
					<p>- Total failed#:${failedNum}
					<p>- [studentAnswer : count]: ${userInputMap}
					<p>- ${correctAnsweredStu.toString()}
					<p>- ${failedAnsweredStu.toString()}
					"""
		}
	}
	
	def showPrevious(Long id) {
		ArrayList quesList = session["QuestionList"]
		def lastIndex = quesList.indexOf(id)
		def ques = Question.get(quesList[lastIndex-1])
		
		if (lastIndex > 0 && lastIndex < quesList.size) {
			if (session.user.role == "inputer" && ques.reviewedBy != null) {
				flash.readOnly = true
			} else {
				flash.readOnly = false
			}
			session["showQuestionID"] = ques.id
			render(view: "show", model: [questionInstance: ques])
		} else {
			flash.message = "No more questions to show."
			redirect(action: "list", params: [sort:session.question_sort, order:session.question_order, searchCategory:session.question_searchCategory, searchable:session.question_searchable])
		}
	}
	
	def showNext(Long id) {
		ArrayList quesList = session["QuestionList"]
		def lastIndex = quesList.indexOf(id)		
		def ques = Question.get(quesList[lastIndex+1])
		
		if (lastIndex > -1 && lastIndex < quesList.size - 1) {
			if (session.user.role == "inputer" && ques.reviewedBy != null) {
				flash.readOnly = true
			} else {
				flash.readOnly = false
			}
			session["showQuestionID"] = ques.id
			render(view: "show", model: [questionInstance: ques])
		} else {
			flash.message = "No more questions to show."
			redirect(action: "list", params: [sort:session.question_sort, order:session.question_order, searchCategory:session.question_searchCategory, searchable:session.question_searchable])
		}
	}

	def reviewedAndNext(Long id) {
		reviewIt(id)
		showNext(id)

		/*
		def c = Question.createCriteria()
		def results = c.list {
			isNull("reviewedBy")
			maxResults(1)
			order("inputDate", "desc")
		}

		def questionInstance = results[0]
		if (questionInstance != null) {
			session["showQuestionID"] = questionInstance.id
			render(view: "show", model: [questionInstance: questionInstance])
		} else {
			flash.message = "No more questions to review."
			redirect(action: "list")
		}
		*/
	}
	
	def show(Long id) {
		def questionInstance = Question.get(id)
		if (!questionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			redirect(action: "list")
			return
		}

		if (session.user.role == "inputer" && questionInstance.reviewedBy != null) {
			flash.readOnly = true
		} else {
			flash.readOnly = false
		}
		
		session["showQuestionID"] = questionInstance.id
		[questionInstance: questionInstance]
	}

	def edit(Long id) {
		def questionInstance = Question.get(id)
		if (!questionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			redirect(action: "list", params: [sort:session.question_sort, order:session.question_order, searchCategory:session.question_searchCategory, searchable:session.question_searchable])
			return
		}
		
		[questionInstance: questionInstance, parentQuestionId:questionInstance?.parentQuestion?.id]
	}

	def reorderAndCleanupAnswers(Question questionInstance) {
		Iterator<Answer> ansIt =  questionInstance.answers?.iterator();
		int index=1;
		char serialNum = 'A'
		while(ansIt?.hasNext()){
			Answer ans = (Answer)ansIt.next();
			if (ans.content == null || ans.deleted == true) {
				ans.deleted = true
			} else {
				if (questionInstance.type == "单选") {
					ans.serialNum = "${serialNum}"
				} else {
					ans.serialNum = "${index}"
				}
				index++
				serialNum++
			}
		}
	}
	
	def update(Long id, Long version) {
		def questionInstance = Question.get(id)
		if (!questionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (questionInstance.version > version) {
				questionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[
							message(code: 'question.label', default: 'Question')] as Object[],
						"Another user has updated this Question while you were editing")
				render(view: "edit", model: [questionInstance: questionInstance])
				return
			}
		}

		questionInstance.properties = params
		
		if (!questionInstance.description) {
			flash.message = "questionInstance.description could not be null"
			questionInstance.errors.rejectValue("description", "questionInstance.description could not be null")
			render(view: "edit", model: [questionInstance: questionInstance])
			return
		}
		
		def defaultVal = [instructions: questionInstance.instructions, source: questionInstance.source, term: questionInstance.term]
		session["defaultValue"] = defaultVal
		
		reorderAndCleanupAnswers(questionInstance)
		questionInstance.answers.removeAll { it.deleted }		
		
		if (!questionInstance.save(flush: true)) {			
			render(view: "edit", model: [questionInstance: questionInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [
			message(code: 'question.label', default: 'Question'),
			questionInstance.id
		])
		
		redirect(action: "show", id: questionInstance.id)
	}

	def delete(Long id) {
		def questionInstance = Question.get(id)
		if (!questionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			redirect(action: "list")
			return
		}

		try {
			Iterator<KnowledgePoint> kpIt =  questionInstance.knowledgePoints?.iterator();
			while(kpIt?.hasNext()){
				KnowledgePoint kp = (KnowledgePoint)kpIt.next();
				kp.associatedQuestion--
				kp.totalQuestion--
				kp.save()
			}

			questionInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			
			redirect(action: "list", params: [sort:session.question_sort, order:session.question_order, searchCategory:session.question_searchCategory, searchable:session.question_searchable])
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [
				message(code: 'question.label', default: 'Question'),
				id
			])
			redirect(action: "show", id: id)
		}
	}
}
