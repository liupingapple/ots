package ots

import java.math.RoundingMode;
import java.text.SimpleDateFormat;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import org.springframework.dao.DataIntegrityViolationException

class QuizQuestionRecordController {

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
		redirect(action: "answered", params: params)
        //params.max = Math.min(max ?: 10, 100)
        //[quizQuestionRecordInstanceList: QuizQuestionRecord.list(params), quizQuestionRecordInstanceTotal: QuizQuestionRecord.count()]
    }
	
	def report4XRecord(Integer maxRecord) {

		//params.maxRecord = Math.min(maxRecord ?: 20, 100)
		if (!params.minStuNum) {			
			params.minStuNum = 2
		}
		
		def fDate1 = new Date()-365, fDate2 = new Date()+1
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (params.searchDate1) {
			fDate1 = formatter.parse(params.searchDate1 + " 00:00:00")
		}
		if (params.searchDate2) {
			fDate2 = formatter.parse(params.searchDate2 + " 23:59:59")
		}

		def results = StuAnswer.withCriteria(max: params.maxRecord, offset: params.offset) {
			projections {
				quizQuestionRecord {
					groupProperty('question', 'question')
					if (params.searchQuestion) {
						question {
							like('description', "%${params.searchQuestion}%")
						}
					}
					if (params.searchChosenFor) {
						chosenFor {
							like('name', "%${params.searchChosenFor}%")
						}
					}
					quiz { 
						groupProperty('student', 'stu') 
					}
				}
			}

			eq('correct', false)
			
			quizQuestionRecord {
				quiz {
					between('answeredDate', fDate1, fDate2)
				}
				
				if (params.selected_kps) {
					
					def kps = []
					kps << params.selected_kps
					kps = kps.flatten()
					
					if (kps.size() > 3 || kps.size() <= 0) {
						eq("chosenFor", null) // always false
						flash.message = "只能最多选择3个知识点，请重新选择"
					}
					else {
						flash.message = "您选择的知识点的错题集"
					}
					
					or {
						kps.each { kpid ->
							KnowledgePoint kp = KnowledgePoint.get(kpid)
							eq("chosenFor", kp)

							/*kp.childPoints.each {  kp2->
								eq("chosenFor", kp2)
							}*/
						}
					}
				}
			}

			if (session.user instanceof Teacher) {
				Teacher teacher = Teacher.get(session.user.id)

				quizQuestionRecord {
					quiz {
						student { eq('teacher', teacher) }
					}
				}
			}
		}

		def failedQuestionStuMap = [:]
		results.each { items->
			def existingVal = failedQuestionStuMap.get(items[0])
			if (existingVal) {
				existingVal << items[1]
				failedQuestionStuMap.put(items[0], existingVal)
			} else {
				failedQuestionStuMap.put(items[0], [items[1]])
			}
		}

		if (failedQuestionStuMap.size() > 0) {

			def failedQuestionStuMapBak = failedQuestionStuMap.clone()

			failedQuestionStuMapBak.each { k,v ->
				if (v.size() < params.int('minStuNum')) {
					failedQuestionStuMap.remove(k)
				}
			}

			failedQuestionStuMap = failedQuestionStuMap.sort{a, b -> b.value.size() - a.value.size()}
		}

		[params:params, failedQuestionStuMap:failedQuestionStuMap]
	}

    def create() {
        [quizQuestionRecordInstance: new QuizQuestionRecord(params)]
    }

    def save() {
        def quizQuestionRecordInstance = new QuizQuestionRecord(params)
        if (!quizQuestionRecordInstance.save(flush: true)) {
            render(view: "create", model: [quizQuestionRecordInstance: quizQuestionRecordInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), quizQuestionRecordInstance.id])
        redirect(action: "show", id: quizQuestionRecordInstance.id)
    }

    def show(Long id) {
        def quizQuestionRecordInstance = QuizQuestionRecord.get(id)
        if (!quizQuestionRecordInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), id])
            redirect(action: "list")
            return
        }

        [quizQuestionRecordInstance: quizQuestionRecordInstance]
    }
	
	def answering(Integer max)
	{
		Quiz quiz = Quiz.load(params.quiz_id)
		
		params.max = Math.min(max ?: session.numberOfQuestionsPerPage, 300)
		def offset = params.int('offset') ?: 0	
					
		// we should prevent user using BACK button in browser and re-submit quiz, if quiz is submitted, it will not set to inprogress anymore
		if (quiz.status != CONSTANT.QUIZ_STATUS_SUBMITTED && quiz.status != CONSTANT.QUIZ_STATUS_REVIEWED) {
			quiz.status = CONSTANT.QUIZ_STATUS_INPROGRESS
		}
		
		def qrMap = [0 : []] as TreeMap
		
		// for 阅读理解，question has child questions
		def qrList = []
		quiz.records.each {
			qrList << it
		}
				
		//<!-- 如果是来自微信练习，name:"C_FOR_WX", 只需要列出知识点相关的 childQuestion, 不相关的childQuestion不列出 -->
		def kpList = params.kpList?.split("X")
		
		qrList.each { pqr->
			
			if (pqr.question.childQuestions?.size() > 0) 
			{
				def needToAddChildQR = []
				def existChildQR = []
				pqr.question.childQuestions?.each { cq->
					def exists = QuizQuestionRecord.findByQuizAndQuestion(quiz, cq)
					
					if (!exists) {
						if (quiz?.assignment?.name == "C_FOR_WX" && kpList?.size()) {
							boolean found = false
							kpList.each { passed_kpid ->
								cq.knowledgePoints.each { cpkp ->
									if (""+cpkp.id == passed_kpid) {
										found = true
									}
								}
							}
							
							if (found) {
								needToAddChildQR <<  new QuizQuestionRecord(quiz:quiz, question:cq, chosenFor: pqr.chosenFor)
							} 
							else {
								println "No need to add this child question because its KP is not required in this quiz: childQuestion=${cq}"
							}
							found = false
						} 
						else {
							needToAddChildQR <<  new QuizQuestionRecord(quiz:quiz, question:cq, chosenFor: pqr.chosenFor)
						}
					} else {
						existChildQR << exists
					}
				}
				
				if (needToAddChildQR.size() > 0) {
					needToAddChildQR.each {
						quiz.addToRecords(it)
					}
					
					qrMap.put(pqr.question.id, needToAddChildQR)
					
					quiz.save(failOnError:true)
				} else {
					qrMap.put(pqr.question.id, existChildQR)
				}
			} else if (!pqr.question.parentQuestion){			
				qrMap[0] << pqr
			}
		}
				
		// 排序
		qrMap.each { quesId, v ->
			qrMap[quesId] = qrMap[quesId].sort{a, b -> a.id - b.id}
		}
		
		//// def qrList = QuizQuestionRecord.findAllByQuiz(quiz,[sort:'id', max:params.max, offset:offset])
		// def qrList = QuizQuestionRecord.findAllByQuiz(quiz,[sort:'id']) // disable pagination 
				
		def selectQuestionsMethod = ""
		if (quiz.assignment.subject == CONSTANT.COURSE_ENG2 && quiz.assignment.difficultyBar) {
			selectQuestionsMethod += "亲！您目前的阅读水平是： ${quiz.student.masterLevel}"			
			selectQuestionsMethod += "<br>作业难度调节值：${quiz.assignment.difficultyBar-0.5} - ${quiz.assignment.difficultyBar+0.5}"
			selectQuestionsMethod += "<br>该练习出题难度范围是：${quiz.student.masterLevel + quiz.assignment.difficultyBar - 0.5} - ${quiz.student.masterLevel + quiz.assignment.difficultyBar + 0.5}"
			selectQuestionsMethod += "<br>挑战自己吧!"
			// selectQuestionsMethod += "<br>题目难度范围: ${quiz.student.masterLevel+quiz.assignment.difficultyBar-0.5} ~ ${quiz.student.masterLevel+quiz.assignment.difficultyBar+0.5}"
		}
		quiz.student.practiceHistory?.split("\\s+").each {
			if (it) {
				Question q = Question.findByQID(it)
				if (q?.knowledgePoints.contains(quiz.toBeFocusedKnowledge)) {
					flash.selectQuestionsMethod += "\nPractice history: ${q} "
				}
			}			
		}
				
		// println "qrMap: ${qrMap}"
		// println "selectQuestionsMethod: ${selectQuestionsMethod}"
		// println "qrList: ${qrList}"
		// kpList is from WeiXinController
		[qrMap: qrMap, quiz: quiz, selectQuestionsMethod:selectQuestionsMethod, kpList:params.kpList, openId:params.openId, sourceq:params.sourceq,
			params:[quiz_id:params.quiz_id, max:params.max, offset:offset, timeCounter:quiz.timeTaken]]
	}

	def saveRecords() {
		Quiz quiz = Quiz.get(params.int('quiz_id'))
		savingRecords(quiz)
		session.assignment = quiz.assignment
		flash.message = "Your answers for ${quiz} were saved."
		redirect(controller:'quiz', action:'list', params:[stuId:quiz.student.id])
	}
	
	def backToList() {
		if (params.quiz_action == "保存") {
			Quiz quiz = Quiz.get(params.quiz_id)
			quiz.status = CONSTANT.QUIZ_STATUS_SUBMITTED
			def qrList = QuizQuestionRecord.findAllByQuiz(quiz,[sort:'id'])
			// update KCP ...
			savingKCP(qrList, quiz)
			quiz.answeredDate = new Date()
			quiz.save() // Need to make sure the answeredDate is larger than the createDate of all KCPs
		}
		redirect(controller:'quiz', action:'list', params:[stuId:session.user.id])
	}
	
	void savingRecords(Quiz quiz) {
		// if only one question in quiz, below code will cause error because params.qqr is not a list. need to fix
		def qqrList = []
		qqrList << params.qqr
		qqrList = qqrList.flatten()
		for (qrid in qqrList) {

			QuizQuestionRecord quizQuestionRecordInstance = QuizQuestionRecord.get(qrid)
			
			def uinput = ''
			quizQuestionRecordInstance.question.answers.each {
				if (it.correct) {
					uinput = params["answerByUser"+it.id]
					if (quizQuestionRecordInstance.question.type == CONSTANT.RADIO_QUESTION) {
						// param uaq<questionId> is defined in CvtqTagLib.groovy
						uinput = params["uaq"+quizQuestionRecordInstance.question.id]
					}
					if (uinput) {
						StuAnswer stuAnswer = StuAnswer.findByQuizQuestionRecordAndRefAnswer(quizQuestionRecordInstance, it)
						if (stuAnswer) {
							stuAnswer.userInput = uinput
							stuAnswer.save()
						} else {
							def newStuAnswer = new StuAnswer(quizQuestionRecord:quizQuestionRecordInstance, refAnswer:it, userInput:uinput)
							newStuAnswer.save()
							// 对于多选题或者填空题，一个quiz的question有多个答案组合
							quizQuestionRecordInstance.addToStuAnswers(newStuAnswer)
						}
					} else {
						flash.message = "You have not finished the question: ${quizQuestionRecordInstance.question}"
					}
				}
			}
		}

		quiz.timeTaken = params.int('timeCounter') ?: 0
		// quiz.save()
	}
		
	def resetAnswer() {
		Quiz quiz = Quiz.get(params.quiz_id)
		params.max = Math.min(params.int('max') ?: quiz.student.numberOfQuestionsPerPage, 300)
		def offset = params.int('offset') ?: 0		
		
		def qrList = QuizQuestionRecord.findAllByQuiz(quiz)
		qrList.each { qrecord ->
			qrecord.stuAnswers.each { stuAns -> 
				stuAns.userInput = ''
			}
		}
		redirect(action:"answering", params:[quiz_id:params.quiz_id,max:params.max, offset:offset])
	}
	
	// just for test
	def autoFillIn_4_Test() {
		Quiz quiz = Quiz.get(params.quiz_id)
		params.max = Math.min(params.int('max') ?: quiz.student.numberOfQuestionsPerPage, 300)
		def offset = params.int('offset') ?: 0		
		
		def qrList = QuizQuestionRecord.findAllByQuiz(quiz)
		qrList = qrList.findAll{qr-> qr.question.answers?.size() > 0}.asList()
		qrList.eachWithIndex { qrecord,i ->
			StuAnswer stuAns = StuAnswer.findByQuizQuestionRecord(qrecord)
			if (!stuAns) 
			{
				Answer refAns
				qrecord.question.answers.each { a->
					if (a.correct) {
						refAns = a
					}
				}
				if (!refAns) {
					println "Init data error!! No ref answer found for ${qrecord.question}"
					return
				}
				stuAns = new StuAnswer(quizQuestionRecord:qrecord, refAnswer:refAns)
			}
			
			stuAns.userInput = ""+stuAns.refAnswer?.id
			if (i%6 == 0 && !params.all_correct)
			{
				if (stuAns.refAnswer.serialNum == "D" || stuAns.refAnswer.serialNum == "C") {
					stuAns.userInput = ""+(stuAns.refAnswer.id - 1)
				} else {
					stuAns.userInput = ""+(stuAns.refAnswer.id + 1)
				}
			}
			stuAns.save()
		}
		
		flash.message = "Please NOTE: user answers were saved !"
		
		redirect(action:"answering", params:[quiz_id:params.quiz_id,max:params.max, offset:offset])
	}
	
	def answered() {
		String quiz_action = "保存"
		Quiz quiz = Quiz.get(params.int('quiz_id'))
		
		def qrList = QuizQuestionRecord.findAllByQuiz(quiz,[sort:'id'])
		
		// so parent question's KP will not be added to aStatus.coveredKPs in savingKCP() to calculate and check if 薄弱知识点,
		qrList = qrList.findAll{qr-> qr.question.answers?.size() > 0}.asList() 
		// quiz.totalQuestionNum is based on all questions which need user to answer
		quiz.totalQuestionNum = qrList.size()
		
		// if only show the answered results, no timeCounter param and no need to save quiz info
		// if timeCounter param existing, it indicates in the answering page, and quiz info need to be saved
		if (params['timeCounter'] ){
			if (quiz.status == CONSTANT.QUIZ_STATUS_SUBMITTED) {
				flash.message = "当前练习已于${quiz.answeredDate}完成，请不要重复交卷 ！以下是您上次交卷结果。"
			} else {
				savingRecords(quiz)

				quiz.totalAnswerNum = 0
				quiz.correctNum = 0
				quiz.notAnsweredNum = 0
				
				quiz.records.each { record ->
					// 单选
					if (record.question.type == CONSTANT.RADIO_QUESTION) {
						record.stuAnswers.each { stua ->
							quiz.totalAnswerNum++
							if (stua.correct) {
								quiz.correctNum++
							}
						}
					} 
					// 填空
					else if (record.question.type == CONSTANT.FBLANK_QUESTION) {
						
						boolean answeredFlag = false
						boolean allBlanksCorrect = true
						
						record.stuAnswers.each { stua ->
							
							if (!answeredFlag) {
								quiz.totalAnswerNum++								
								answeredFlag = true
							}
							if (!stua.correct) {
								allBlanksCorrect = false
							}
						}
						
						if (answeredFlag && allBlanksCorrect) {
							quiz.correctNum++
						}
					}
					
					Student stu = Student.get(quiz.student.id)
					if (stu.practiceHistory.length() > CONSTANT.Practice_History_Limit) {
						stu.practiceHistory = stu.practiceHistory.substring((CONSTANT.Practice_History_Limit / 2).toInteger().intValue())
					}
					stu.practiceHistory += record.question.qID+" "
					stu.save()
				}

				quiz.notAnsweredNum = quiz.totalQuestionNum - quiz.totalAnswerNum

				if (quiz.notAnsweredNum > 0) {
					flash.message = "您需要答完所有题目才能成功交卷，请继续完成未答题目"
					redirect(action: "answering", params:[quiz_id:quiz.id, timeCounter:quiz.timeTaken, max:params.max, offset:params.offset, stuId:session.user.id])
				} else {
					// saving quiz
					quiz.score = (quiz.totalQuestionNum == 0?:100.0*quiz.correctNum/quiz.totalQuestionNum)
					quiz.status = CONSTANT.QUIZ_STATUS_SUBMITTED

					// error answered questions
					quiz.records.each { record ->
						record.stuAnswers.each { stua ->
							if (stua.userInput && !stua.correct) {
								quiz.student.addToFailedRecords(record)
							}
						}
					}
					
					savingKCP(qrList, quiz)
					
					quiz.answeredDate = new Date()
					
					/*// 计算 quiz.latestCoverage
					def touchedQuestions = [] as HashSet
										
					def results = QuizQuestionRecord.where {quiz.student == quiz.student && quiz.assignment == quiz.assignment}
					results.list().each {
						touchedQuestions << it.question
					}
					
					if (quiz.assignment.totalAvailableQuestions) {
						quiz.latestCoverage = touchedQuestions.size()/quiz.assignment.totalAvailableQuestions
						AssignmentStatus aStatus = AssignmentStatus.findByStudentAndAssignment(quiz.student, quiz.assignment)
						aStatus.coverageRate = quiz.latestCoverage
						
						def kcp = KnowledgeCheckPoint.findByQuizAndKnowledge(quiz, aStatus.toBeFocusedKnowledge)
						if (kcp?.familyRecentCorrectRate > 0) {
							quiz.masterRate = kcp.familyRecentCorrectRate
							aStatus.masterRate = quiz.masterRate
						} 
						
						aStatus.save()
						
						quiz.masterLevel = quiz.student.masterLevel
					}*/
					quiz.save(failOnError:true)
				}
			}
		}
		
		if (quiz.answeredDate) {
			quiz_action = "统计" // 题目做完了，'返回'改成'统计'
		}

		// def qcolumns = [['string', 'result_type'], ['number', 'question_num']]
		// def qdata = [ ['答对题数', quiz.correctNum], ['答错题数', quiz.totalQuestionNum - quiz.correctNum - quiz.notAnsweredNum], ['未答题数', quiz.notAnsweredNum]]
					
		def kpNum = 0;
		def questionNum = 0;
		quiz.assignment.templates.each { template->
			template.knowledgePoints.each{ kp->
				kpNum++
				questionNum += kp.totalQuestion
			}
		}
		
		def aStatus = AssignmentStatus.findByAssignmentAndStudent(quiz.assignment, quiz.student)
		
		def qrMap = [0 : []] as TreeMap
		
		// for 阅读理解，question has child questions
		quiz.records.each { pqr->
			if (pqr.question.childQuestions?.size() > 0)
			{
				def existChildQR = [] as ArrayList
				pqr.question.childQuestions?.each { cq->
					def exists = QuizQuestionRecord.findByQuizAndQuestion(quiz, cq)
					if (!exists) {
						println "WARNING : This question was not added in quiz.records, question=${cq}"
						// qrMap.put("Error: why no this question added in quiz.records, question=${cq}", pqr)
					} else {
						existChildQR << exists
					}
				}				
				
				qrMap.put(pqr.question.id, existChildQR)
			} else if (!pqr.question.parentQuestion){
				qrMap[0] << pqr
			}
		}
		
		// 排序
		qrMap.each { quesId, v ->
			qrMap[quesId] = qrMap[quesId].sort{a, b -> a.id - b.id}
		}
				
		// for parent question which has no answers, collect child question's correct rate for it
		// 可以进一步优化，如果KP加入一定的识别信息，比如KP加入新的字段，表示只是支持适用于阅读理解题干，
		// println "in answered: qrMap=${qrMap}"
		qrMap.each { quesId, v ->
				Question.get(quesId)?.knowledgePoints.each { kp->
				
					quiz.assignment.templates.each {  
						it.knowledgePoints.each {  inkp->
							if (kp == inkp) {
								
								KnowledgeCheckPoint kcp = KnowledgeCheckPoint.findByKnowledgeAndStudentAndQuiz(kp, quiz.student, quiz)
							
								if (!kcp) {
									kcp = new KnowledgeCheckPoint(knowledge:kp, student:quiz.student, quiz:quiz, datePracticed:new Date())
								}
								
								kcp.familyRecentCorrectRate = quiz.score/100
								kcp.recentCorrectRate = quiz.score/100
								kcp.save(failOnError:true)				// failOnError:true
							}
							
						}
					}
			}
		}
		
        [qrMap: qrMap, quiz_id:params.quiz_id, quiz:quiz, kpList:params.kpList, openId:params.openId, sourceq:params.sourceq,
			 quiz_action:quiz_action, kpNum:kpNum, questionNum:questionNum, aStatus:aStatus]
	}
	
	/**
	 * For each test, a new set of checkpoints will be generated which mostly reflect what are tested this time
	 * The checkpoints for all the ancestors should be created also. 
	 * But a condition needs to be added so that only all its children's recentCorrectRate are available, 
	 * then its recentCorrectRate can then be generated. 
	 * This is mainly to avoid the situation that we treat a parent node as 100% mastered, even though one of its child is never tested so far.
	 * 
	 * last record < evaluated number
	 * Need to accumulate, no value set for recentCorrectRate
	 * simply add the total and correct ones to the latest record. If the new number > evaluated number, calculate the recentCorrectRate
	 * 
	 * last record >=  evaluated number
	 * recentCorrect = recentCorrect - (newTotal * old_recentCorrectRate * evaluationPower) + newCorrectOnes * evaluationPower
	 * Recalculate the recentCorrectRate
	 * 
	 * @param qrList
	 * @param quiz
	 */
	private void savingKCP(List qrList, Quiz quiz)
	{
		def currentKCPs = [:]
		def newAssignment = false
		AssignmentStatus aStatus = AssignmentStatus.findByAssignmentAndStudent(quiz.assignment, quiz.student)

		if (!aStatus) {
			// Handle the case for Self Practice where the student can choose assignment defined by system
			println "==== WARNING: savingKCP() ... please note: aStatus is null"
			newAssignment = true
			aStatus = new AssignmentStatus(assignment:quiz.assignment)
			quiz.student.addToAStatus(aStatus)
		} else {
			if (!aStatus.totalKnowledgePoints) {
				newAssignment = true
			}	
		}
		
		qrList.each { record ->
			
			// Only consider one kp
			// def kp = record.chosenFor
						
			record.question.knowledgePoints.each { kp ->

				KnowledgeCheckPoint currentKCP = currentKCPs[kp.id]
				if (!currentKCP) {
					currentKCP = new KnowledgeCheckPoint(knowledge:kp, student:quiz.student, quiz:quiz)
					currentKCPs[kp.id] = currentKCP
				}
				
				currentKCP.quesRecords.add(record)
				record.stuAnswers.each { stuAnswer->
					
					if (stuAnswer?.userInput) {
						currentKCP.totalQuestions++
					}
	
					if (stuAnswer?.correct) {
						currentKCP.correctQuestions++
					}
				}
				
				updateParentKCP(currentKCPs, kp, record, quiz) // will create parent kcp and add to currentKCPs
			}
		}
		
		currentKCPs.each { key, currentKCP ->
			def kp = KnowledgePoint.load(key)
			
			currentKCP.quesRecords.each {record->
				record.stuAnswers.each {  stuAnswer->
				
					if (stuAnswer?.userInput) {
						currentKCP.familyTotal++
					}
		
					if (stuAnswer?.correct) {
						currentKCP.familyCorrect++
					}
				}
				
			}

			// println "$kp\t$currentKCP.familyTotal"
			
			KnowledgeCheckPoint lastKCP = KnowledgeCheckPoint.findByKnowledgeAndStudentAndQuizNotEqual(kp, quiz.student, quiz, [sort:'id', order:'desc'])
			if (!lastKCP) { lastKCP = currentKCP }
			
			currentKCP.totalSum = currentKCP.totalQuestions + lastKCP.totalSum
			currentKCP.recentTotal = currentKCP.totalQuestions + lastKCP.recentTotal * quiz.student.evaluationPower
			currentKCP.recentCorrect = currentKCP.correctQuestions + lastKCP.recentCorrect * quiz.student.evaluationPower
			if (currentKCP.totalSum >= quiz.student.minAssociatedQuestionsToEvaluate && currentKCP.recentTotal) {
				currentKCP.recentCorrectRate = currentKCP.recentCorrect/currentKCP.recentTotal
			} else {
				currentKCP.recentCorrectRate = -1
			}

			currentKCP.familyTotalSum = currentKCP.familyTotal + lastKCP.familyTotalSum
			currentKCP.familyRecentTotal = currentKCP.familyTotal + lastKCP.familyRecentTotal * quiz.student.evaluationPower
			currentKCP.familyRecentCorrect = currentKCP.familyCorrect + lastKCP.familyRecentCorrect * quiz.student.evaluationPower
			if (currentKCP.familyTotalSum >= quiz.student.minTotalQuestionsToEvaluate && currentKCP.familyRecentTotal) {
				currentKCP.familyRecentCorrectRate = currentKCP.familyRecentCorrect/currentKCP.familyRecentTotal
			} else {
				currentKCP.familyRecentCorrectRate = -1
			}

			currentKCP.datePracticed = new Date()
			currentKCP.save()			
		}

		aStatus.coveredKnowledgPoints = aStatus.coveredKPs?.size()
		
		aStatus.relativeTargetCorrectRate = 100
		
		aStatus.totalKnowledgePoints = 0 // Need to be removed later
		aStatus.availableQuestions = 0 // Need to be removed later
				
		// 对于微信练习
		if (params.kpList && quiz.assignment.name.endsWith('_FOR_WX')) {
			String[] kpIds = params.kpList?.split("X")
			kpIds.each { kpid->
				KnowledgePoint kp = KnowledgePoint.get(Integer.parseInt(kpid))
				aStatus.totalKnowledgePoints += kp.degree * 100 + kp.masterRatio
				aStatus.availableQuestions += kp.totalQuestion
				updateChildKCP(currentKCPs, kp, quiz, aStatus)
			}
			
			kpIds.each { kpid->
				KnowledgePoint kp = KnowledgePoint.get(Integer.parseInt(kpid))
				if (!aStatus.toBeFocusedKnowledge) {
						aStatus.toBeFocusedKnowledge = kp
				}
				markToBeFocusedKCP(currentKCPs, kp, aStatus.toBeFocusedKnowledge, false)
			}
		}
		else  // 对于电脑老师分配的作业练习
		{
			// go through KP - use quiz.assignment.templates, don't use use aStatus.coveredKPs(可能含有一些相关性不强的KP???)		
			quiz.assignment.templates.each { template ->
				template.knowledgePoints.each {
					//if (newAssignment) { // Need to be uncommented later
						aStatus.totalKnowledgePoints += it.degree * 100 + it.masterRatio
						aStatus.availableQuestions += it.totalQuestion
					//}
					updateChildKCP(currentKCPs, it, quiz, aStatus)
				}
			}
	
			quiz.assignment.templates.each { template ->
				template.knowledgePoints.each {
					if (!aStatus.toBeFocusedKnowledge) {
						aStatus.toBeFocusedKnowledge = it
					}
					markToBeFocusedKCP(currentKCPs, it, aStatus.toBeFocusedKnowledge, false)
				}
			}
		}

		aStatus.finishedQuestions += quiz.totalQuestionNum
		aStatus.correctQuestions += quiz.correctNum
			
		def masterRateCk = currentKCPs[aStatus.toBeFocusedKnowledge?.id]?.familyRecentCorrectRate
		
		if (masterRateCk) {
			// save masterRate to quiz so we can get the history masterRate of each quiz
			if (masterRateCk > 0) {
				aStatus.masterRate = masterRateCk * 100 
			} else {
				aStatus.masterRate = aStatus.correctQuestions / aStatus.finishedQuestions * 100
			}
			
			quiz.masterRate = aStatus.masterRate
		} 
				
		if (aStatus.coverageRate < 100) {
			aStatus.coveredKnowledgPoints = aStatus.coveredKPs.size()
			if (aStatus.totalKnowledgePoints > aStatus.coveredKnowledgPoints) {
				aStatus.coverageRate = aStatus.coveredKnowledgPoints / aStatus.totalKnowledgePoints * 100
			} else {
				aStatus.coverageRate = 100
			}
			quiz.latestCoverage = aStatus.coverageRate
		}
		
		if (aStatus.finishedQuestions >= aStatus.assignment.questionLimit && aStatus.masterRate >= aStatus.assignment.targetCorrectRate * 100 
			&& aStatus.coverageRate >= 100) {
			aStatus.status = CONSTANT.ASSIGNMENT_STATUS_DONE
		} else {
			aStatus.status = CONSTANT.ASSIGNMENT_STATUS_INPROGRESS
		}
		
		aStatus.save()
		
		if (quiz.toBeFocusedKnowledge != aStatus.toBeFocusedKnowledge) {
			quiz.toBeFocusedKnowledge = aStatus.toBeFocusedKnowledge
		}
		
		/*
		println "================ show KCP for quiz:${quiz} ========================"
		def quizKCPs = KnowledgeCheckPoint.findAllByQuiz(quiz)
		quizKCPs.each {
			println "\tRate: ${it.recentCorrectRate} \trecentTotal: ${it.recentTotal} \trecentCorrect: ${it.recentCorrect} \tTotal: ${it.totalQuestions} \tCorrect: ${it.correctQuestions} \tfamilyTotal: ${it.familyTotal} \tfamilyCorrect: ${it.familyCorrect} \tKP: ${it.knowledge}"
		}
		*/
	}

	private void updateParentKCP(currentKCPs, kp, record, quiz)
	{
		kp.parentPoints.each {				
			KnowledgeCheckPoint currentKCP = currentKCPs[it.id]
			
			if (!currentKCP) {
				currentKCP = new KnowledgeCheckPoint(knowledge:it, student:quiz.student, quiz:quiz)
				currentKCPs[it.id] = currentKCP
			}
		
			updateParentKCP(currentKCPs, it, record, quiz)
			// currentKCP.addToRecords(record)
			currentKCP.quesRecords.add(record)
		}
		
	}

	private void updateChildKCP(currentKCPs, kp, quiz, aStatus)
	{
		KnowledgeCheckPoint currentKCP = currentKCPs[kp.id]
		
		if (currentKCP) {
			
			kp.childPoints.each {
				updateChildKCP(currentKCPs, it, quiz, aStatus)
			}
			
			if (currentKCP.familyTotal > 0) {
				aStatus.addToCoveredKPs(kp)
			}
		
			if (currentKCP.familyRecentCorrectRate >= 0) {
				
				if (!aStatus.toBeFocusedKnowledge || currentKCP.familyRecentCorrectRate < aStatus.relativeTargetCorrectRate) {
					aStatus.relativeTargetCorrectRate = currentKCP.familyRecentCorrectRate
					aStatus.toBeFocusedKnowledge = kp
				} else if (currentKCP.familyRecentCorrectRate == aStatus.relativeTargetCorrectRate) {
					KnowledgePoint focusKP = aStatus.toBeFocusedKnowledge
					if (kp.familySize < focusKP.familySize || kp.totalQuestion > focusKP.totalQuestion) {
						aStatus.toBeFocusedKnowledge = kp
					}
				}
			}
		}
	}

	// use updateChildKCP instead
	/*
	private void updateFamilyKCP(currentKCPs, kp, quiz, aStatus)
	{
		KnowledgeCheckPoint currentKCP = currentKCPs[kp.id]
		
		if (!currentKCP) {
			currentKCP = new KnowledgeCheckPoint(knowledge:kp, student:quiz.student, quiz:quiz)
			currentKCPs[kp.id] = currentKCP
		}
		
		if (currentKCP.familyRecentTotal + currentKCP.recentTotal == 0) {
			
			kp.childPoints.each {
				updateFamilyKCP(currentKCPs, it, quiz, aStatus)
				currentKCPs[it.id].quesRecords.each { record ->
					// currentKCP.addToRecords(record)
					currentKCP.quesRecords.add(record)
				}
			}
		
			currentKCP.quesRecords.each {record->
				record.stuAnswers.each {  stuAnswer->
				
					if (stuAnswer?.userInput) {
						currentKCP.familyTotal++
					}
		
					if (stuAnswer?.correct) {
						currentKCP.familyCorrect++
					}
				}
			}

			println "$kp\t$currentKCP.familyTotal"
			
			if (currentKCP.familyTotal > 0) {
				aStatus.addToCoveredKPs(kp)
				KnowledgeCheckPoint lastKCP = KnowledgeCheckPoint.findByKnowledgeAndStudentAndQuizNotEqual(kp, quiz.student, quiz, [sort:'id', order:'desc'])
				if (!lastKCP) { lastKCP = currentKCP }
				
				currentKCP.totalSum = currentKCP.totalQuestions + lastKCP.totalSum 
				currentKCP.recentTotal = currentKCP.totalQuestions + lastKCP.recentTotal * quiz.student.evaluationPower
				currentKCP.recentCorrect = currentKCP.correctQuestions + lastKCP.recentCorrect * quiz.student.evaluationPower
				if (currentKCP.totalSum >= quiz.student.minAssociatedQuestionsToEvaluate) {
					currentKCP.recentCorrectRate = currentKCP.recentCorrect/currentKCP.recentTotal
				} else {
					currentKCP.recentCorrectRate = -1
				}

				currentKCP.familyTotalSum = currentKCP.familyTotal + lastKCP.familyTotalSum
				currentKCP.familyRecentTotal = currentKCP.familyTotal + lastKCP.familyRecentTotal * quiz.student.evaluationPower
				currentKCP.familyRecentCorrect = currentKCP.familyCorrect + lastKCP.familyRecentCorrect * quiz.student.evaluationPower
				if (currentKCP.familyTotalSum >= quiz.student.minTotalQuestionsToEvaluate) {
					currentKCP.familyRecentCorrectRate = currentKCP.familyRecentCorrect/currentKCP.familyRecentTotal
					if (!aStatus.toBeFocusedKnowledge || currentKCP.familyRecentCorrectRate < aStatus.relativeTargetCorrectRate) {
						aStatus.relativeTargetCorrectRate = currentKCP.familyRecentCorrectRate
						aStatus.toBeFocusedKnowledge = kp
					} else if (currentKCP.familyRecentCorrectRate == aStatus.relativeTargetCorrectRate) {
						KnowledgePoint focusKP = aStatus.toBeFocusedKnowledge
						if (kp.familySize < focusKP.familySize || kp.totalQuestion > focusKP.totalQuestion) {
							aStatus.toBeFocusedKnowledge = kp
						}
					}
				} else {
					currentKCP.familyRecentCorrectRate = -1
				}
	
				currentKCP.save(flush:true, failOnError:true)
			}
		}
	}*/

	private void markToBeFocusedKCP(currentKCPs, kp, toBeFocusedKnowledge, needToFocus)
	{
		if (kp.id == toBeFocusedKnowledge?.id) { needToFocus = true }
		
		KnowledgeCheckPoint currentKCP = currentKCPs[kp.id]
		
		if (currentKCP && !currentKCP.isFocused) {
			
			kp.childPoints.each {
				markToBeFocusedKCP(currentKCPs, it, toBeFocusedKnowledge, needToFocus)
			}
	
			if (!currentKCP.isFocused && needToFocus && currentKCP.familyTotal > 0) {
				currentKCP.isFocused = true
				currentKCP.save()
			}
		}
	}

    def edit(Long id) {
        def quizQuestionRecordInstance = QuizQuestionRecord.get(id)
        if (!quizQuestionRecordInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), id])
            redirect(action: "list")
            return
        }

        [quizQuestionRecordInstance: quizQuestionRecordInstance]
    }

    def update(Long id, Long version) {
        def quizQuestionRecordInstance = QuizQuestionRecord.get(id)
        if (!quizQuestionRecordInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (quizQuestionRecordInstance.version > version) {
                quizQuestionRecordInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord')] as Object[],
                          "Another user has updated this QuizQuestionRecord while you were editing")
                render(view: "edit", model: [quizQuestionRecordInstance: quizQuestionRecordInstance])
                return
            }
        }

        quizQuestionRecordInstance.properties = params

        if (!quizQuestionRecordInstance.save(flush: true)) {
            render(view: "edit", model: [quizQuestionRecordInstance: quizQuestionRecordInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), quizQuestionRecordInstance.id])
        redirect(action: "show", id: quizQuestionRecordInstance.id)
    }

    def delete(Long id) {
        def quizQuestionRecordInstance = QuizQuestionRecord.get(id)
        if (!quizQuestionRecordInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), id])
            redirect(action: "list")
            return
        }

        try {
            quizQuestionRecordInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord'), id])
            redirect(action: "show", id: id)
        }
    }
}
