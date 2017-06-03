package ots

//import org.grails.plugins.google.visualization.data.Cell
import org.springframework.dao.DataIntegrityViolationException
import java.text.DecimalFormat
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

class QuizController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def beforeInterceptor = [action:this.&checkUser]

	def checkUser() {
		if (!session.user) {
			if (params.openId) {
				Student stu = Student.findByWeixinOpenidAndWeixinOpenidIsNotNull(params.openId)
				session.user = stu
				println "This is WeiXin user ${params.openId}, stu: ${stu}"
			}
			else {
				// i.e. user not logged in
				redirect(controller:'endUser',action:'login')
				return false
			}
		}
	}
		
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		
		Student stu = Student.get(params.int('stuId'))
		if (!stu) {
			stu = Student.get(session.user.id)
		}
		
		Assignment assignm = Assignment.get(session.assignment?.id)
			
		def c = Quiz.createCriteria()
		def results = c.list(max: params.max, offset: params.offset, sort: params.sort == null?'answeredDate':params.sort, order: params.order == null? 'desc' : params.order) {
			eq("student", stu)
			if (assignm) {
				eq("assignment", assignm)
			}
		}
		
		session.totalQuizNumber = results.totalCount
		AssignmentStatus aStatus = AssignmentStatus.findByAssignmentAndStudent(assignm, stu)
		
		[quizInstanceList: results, quizInstanceTotal: session.totalQuizNumber, assignmentStatus: aStatus, assignmentInstance: assignm, params:[stuId:stu?.id]]
    }
	
	def continuePractice() {
		
		if (!session.totalQuizNumber) {
			list(0)
		}
		
		if (params.aStatusId) {
			Assignment assignment = null
			AssignmentStatus aStatus = null
			aStatus = AssignmentStatus.get(params.int('aStatusId'))
			assignment = aStatus.assignment
			session.assignment = assignment
			session.assignmentStatus = aStatus
		}
		
		params.assignment = session.assignment
		
		// if in progress quiz exists, take it firstly, otherwise generate new one
		def inprogressQuiz = Quiz.findByStudentAndAssignmentAndStatus(session.user, session.assignment, CONSTANT.QUIZ_STATUS_INPROGRESS)		
		if (inprogressQuiz) {
			params.max = Math.min(params.int('max') ?: session.user.numberOfQuestionsPerPage, 300)
			def offset = params.int('offset') ?: 0
			redirect(controller: "QuizQuestionRecord", action: "answering", params:[quiz_id:inprogressQuiz.id, max:params.max, offset:offset, stuId:session.user.id])
		} else {
			params.name  = "${session.assignment}练习"
			focusGenerate()
		}
	}
	
    def create() {
		Quiz newQuiz = new Quiz(params)
		newQuiz.name = "自主练习" + (Quiz.all.size()+1)

		def asmtList = Assignment.createCriteria().list(sort: "id", order: "desc") {
			or {
				eq('assignedBy', "su")
				eq('assignedBy', session.teacher.userName)
			}
			ilike('name', '自主%')
		}

		AssignmentStatus.findAllByStudent(session.user).each {
			asmtList << it.assignment
		}

        [quizInstance: newQuiz, asmtList:asmtList]
    }

	def fillInRandomList(kp, qSet) {
		kp.questions.each{qSet[it.id] = kp}
		
		kp?.childPoints.each {
			fillInRandomList(it, qSet)
		}
	}

	def randomGenerate() {
		def quizInstance = new Quiz(params)
		
		if (!quizInstance) {
			render(view: "create", model: [quizInstance: quizInstance])
			return
		}
		else {
			quizInstance.student = session.user
			quizInstance.score = 0
			quizInstance.type = CONSTANT.QUIZ_TYPE_SELFPRAC
			quizInstance.status = CONSTANT.QUIZ_STATUS_NOTBEGIN
			int qnum = Math.max(quizInstance.student.numberOfQuestionsPerPage, quizInstance.assignment.questionLimit)
			
			String question_type = params['question_type']
			if (!question_type) {
				question_type = ""
			}

			def newQ
			def qSet = [:]
			HashSet selectedQSet = new HashSet() 		
			quizInstance.assignment.templates.each { template ->
				template.knowledgePoints.each{fillInRandomList(it, qSet)}
			}

			def qlist = qSet.keySet().toList()
			Random randomizer = new Random();

			if (qlist.size() == 0) {
				flash.message = message(code: 'quiz.no_question_found', default: 'No questions found!!')
			} else {
				for ( i in 0..qnum*2 ) {
					if (selectedQSet.size() < qnum) {
						newQ = qlist.get(randomizer.nextInt(qlist.size()))
						selectedQSet.add(Question.get(newQ))
					} else {
						break
					}
				}
				
				if (selectedQSet.size() > 0) {
					
					// quizInstance.totalQuestionNum = selectedQSet.size()
					quizInstance.name = "Q"+(Quiz.countByStudentAndAssignment(quizInstance.student, quizInstance.assignment) + 1)+"-随机"
					
					selectedQSet.each {
						quizInstance.addToRecords(new QuizQuestionRecord(quiz:quizInstance, question:it, chosenFor: qSet[it.id]))
					}
					
					quizInstance.save(flush: true)
					
					if (selectedQSet.size() < qnum) {
						flash.message = "Sorry, we only find ${selectedQSet.size()} questions."
					}
					redirect(action: "show", id: quizInstance.id)
					return
				} else {
					flash.message = "Not enough questions. Please try again."
				}
			}

			redirect(action: "list")
		}
	}

	def fillInKPList(stu, kp, lowKPList, neverVisitKPList, highKPSet) {
		if (kp.associatedQuestion > 0) {
			KnowledgeCheckPoint currentKCP = KnowledgeCheckPoint.findByKnowledgeAndStudent(kp, stu, [sort:'id', order:'desc'])
			if (currentKCP == null) {
				neverVisitKPList.add(kp)
			} else if (currentKCP?.recentCorrectRate < stu.targetCorrectRate) {
				if (!lowKPList.contains(currentKCP)) {
					lowKPList.add(currentKCP)
				}
			} else {
				highKPSet.add(kp)
			} 
		}
		
		kp?.childPoints.each {
			fillInKPList(stu, it, lowKPList, neverVisitKPList, highKPSet)
		}
	}

	def findFocusingKnowledge(kps, KCPMapping, Student stu, KPSum) {
		KnowledgePoint kp = null
		def minCorrectRate = 2
		
		kps.each {
			if (!kp) {
				kp = it
				minCorrectRate = KCPMapping[kp.id].familyRecentCorrectRate
			}
			if (it.totalQuestion > stu.numberOfQuestionsPerPage) {
				def tm = stu.focusPower * it.familySize
				def fa = tm / (tm + KPSum - it.familySize)
				if (fa >= 0.2) {
					if (KCPMapping[it.id].familyRecentCorrectRate >=0 && KCPMapping[it.id].familyRecentCorrectRate < minCorrectRate) {
						kp = it
						minCorrectRate = KCPMapping[it.id].familyRecentCorrectRate
					}
					
					if (it.childPoints.size() > 0) {
						KnowledgePoint childKP =  findFocusingKnowledge(it.childPoints, KCPMapping, stu, KPSum)
						if (childKP && KCPMapping[childKP.id].familyRecentCorrectRate < minCorrectRate) {
							kp = childKP
							minCorrectRate = KCPMapping[childKP.id].familyRecentCorrectRate
						}
					}
				}
			}
		}
		
		return kp
	}
	
	def findParentKPs(kp, parentKPSet) {
		parentKPSet.add(kp.id)
		kp.childPoints.each {
			if (it.totalQuestion > 0 && it.familySize > 1) {
				findParentKPs(it, parentKPSet)
			}
		}
	}

	private int buildRestrictedKPs(kp, coveredKPSet, parentKPSet, restrictedKPSet) {
		def outOfRange = false
		def KPwithQuestions = 0
		 
		kp.parentPoints.each { parentKP ->
			if (!parentKPSet.contains(parentKP.id) && !coveredKPSet.contains(parentKP.id)) {
				outOfRange = true
			}
		}
		
		if (!outOfRange) {
			restrictedKPSet.add(kp.id)
			if (kp.associatedQuestion > 0) {
				KPwithQuestions = 1
			}
			kp.childPoints.each {
				KPwithQuestions += buildRestrictedKPs(it, coveredKPSet, parentKPSet, restrictedKPSet)
			}
		}
		
		return KPwithQuestions
	}

	def groupingKnowledgePoints(Student stu, KnowledgePoint kp, KPGroups, focusGroups, needToFocus, targetCorrectRate, relativeTargetCorrectRate, toBeFocusedKnowledge, focusQNumber, leftQNumber, restrictQuestion, restrictedKPSet) {

		def needToContinue = true
		def minFocusSize1 = Math.min(focusGroups.B2.size(), Math.min(focusGroups.C2.size(), Math.min(focusGroups.A2.size(), focusGroups.D1.size())))
		def minFocusSize2 = Math.min(focusGroups.B4.size(), Math.min(focusGroups.C4.size(), focusGroups.A4.size()))
		def minLeftSize1 = Math.min(KPGroups.B2.size(), Math.min(KPGroups.C2.size(), Math.min(KPGroups.A2.size(), KPGroups.D1.size())))
		def minLeftSize2 = Math.min(KPGroups.B4.size(), Math.min(KPGroups.C4.size(), KPGroups.A4.size()))
		def focusQNum = focusQNumber - minFocusSize1
		def leftQNum = leftQNumber - minLeftSize1  
		def focusQNum2 = focusQNum - minFocusSize2
		def leftQNum2 = leftQNum - minLeftSize2

		
//		println "kp: $kp"
//		println "focusQNum: $focusQNum"
//		println "leftQNum: $leftQNum"
//		println "focusQNum2: $focusQNum2"
//		println "leftQNum2: $leftQNum2"
		
		
		if (kp.id == toBeFocusedKnowledge?.id) { needToFocus = true } 
		
		// Taylor commented: leftQNum<=0 changed to leftQNum<0 in case leftQNum=0 when numberOfQuestionsPerPage=1 for 阅读理解练习
		if (focusQNum <= 0 && leftQNum < 0) {
			println "groupingKnowledgePoints: (focusQNum <= 0 && leftQNum < 0), return ...."
			return
		}
		
		if (kp.associatedQuestion > 0) {
			KnowledgeCheckPoint currentKCP = KnowledgeCheckPoint.findByKnowledgeAndStudent(kp, stu, [sort:'id', order:'desc'])
			// println "DEBUG - in groupingKnowledgePoints, currentKCP: ${currentKCP}"
			if (currentKCP == null) {
				if (needToFocus) {
					if (focusGroups.D1.size() < focusQNum) {
						focusGroups.D1 << kp
						if (focusGroups.D1.size() == focusQNum) {
							needToContinue = false
						}
					} else {
						needToContinue = false
					}
				} else {
					// Taylor commented: KPGroups.D1.size() <= leftQNum changed to KPGroups.D1.size() < leftQNum in case leftQNum=0 when numberOfQuestionsPerPage=1 for 阅读理解练习
					if (KPGroups.D1.size() <= leftQNum) {
						KPGroups.D1 << kp
						if (KPGroups.D1.size() == leftQNum) {
							needToContinue = false
						}
					} else {
						needToContinue = false
					}
				}
			} else if (currentKCP?.familyRecentCorrectRate < 0) {
				if (currentKCP.recentCorrectRate > targetCorrectRate) {
					if (needToFocus) {
						if (focusGroups.C1.size() < focusQNum2) {
							focusGroups.C1 << currentKCP
						}
					} else {
						if (KPGroups.C1.size() < leftQNum2) {
							KPGroups.C1 << currentKCP
						}
					}
				} else if (currentKCP.recentCorrectRate >= 0) {
					if (needToFocus) {
						if (focusGroups.C2.size() < focusQNum) {
							focusGroups.C2 << currentKCP
						}
					} else {
						if (KPGroups.C2.size() < leftQNum) {
							KPGroups.C2 << currentKCP
						}
					}
				} else if (currentKCP.recentTotal == 0) {
					if (needToFocus) {
						if (focusGroups.C4.size() < focusQNum2) {
							focusGroups.C4 << currentKCP
						}
					} else {
						if (KPGroups.C4.size() < leftQNum2) {
							KPGroups.C4 << currentKCP
						}
					}
				} else {
					if (needToFocus) {
						if (focusGroups.C5.size() < focusQNum2) {
							focusGroups.C5 << currentKCP
						}
					} else {
						if (KPGroups.C5.size() < leftQNum2) {
							KPGroups.C5 << currentKCP
						}
					}
				}
				
				if (KPGroups.C1.size() + KPGroups.C2.size() + KPGroups.C4.size() + KPGroups.C5.size() == 0) {
					KPGroups.D1 << currentKCP
					// println "DEBUG - Taylor added, if none KP is available, KPGroups.D1: ${KPGroups.D1}"
				}
			} else if (currentKCP?.familyRecentCorrectRate <= relativeTargetCorrectRate) {
				if (currentKCP.recentCorrectRate > targetCorrectRate) {
					if (needToFocus) {
						if (focusGroups.B1.size() < focusQNum2) {
							focusGroups.B1 << currentKCP
						}
					} else {
						if (KPGroups.B1.size() < leftQNum2) {
							KPGroups.B1 << currentKCP
						}
					}
				} else if (currentKCP.recentCorrectRate >= 0) {
					if (needToFocus) {
						if (focusGroups.B2.size() < focusQNum) {
							focusGroups.B2 << currentKCP
						}
					} else {
						if (KPGroups.B2.size() < leftQNum) {
							KPGroups.B2 << currentKCP
						}
					}
				} else if (currentKCP.recentTotal == 0) {
					if (needToFocus) {
						if (focusGroups.B4.size() < focusQNum2) {
							focusGroups.B4 << currentKCP
						}
					} else {
						if (KPGroups.B4.size() < leftQNum2) {
							KPGroups.B4 << currentKCP
						}
					}
				} else {
					if (needToFocus) {
						if (focusGroups.B5.size() < focusQNum2) {
							focusGroups.B5 << currentKCP
						}
					} else {
						if (KPGroups.B5.size() < leftQNum2) {
							KPGroups.B5 << currentKCP
						}
					}
				}
				
				if (KPGroups.B1.size() + KPGroups.B2.size() + KPGroups.B4.size() + KPGroups.B5.size() == 0) {
					KPGroups.B5 << currentKCP
					// println "DEBUG - Taylor added, if none KP is available, KPGroups.B5: ${KPGroups.B5}"
				}
			} else {
				if (currentKCP.recentCorrectRate > targetCorrectRate) {
					if (needToFocus) {
						if (focusGroups.A1.size() < focusQNum2) {
							focusGroups.A1 << currentKCP
						}
					} else {
						if (KPGroups.A1.size() < leftQNum2) {
							KPGroups.A1 << currentKCP
						}
					}
				} else if (currentKCP.recentCorrectRate >= 0) {
					if (needToFocus) {
						if (focusGroups.A2.size() < focusQNum) {
							focusGroups.A2 << currentKCP
						}
					} else {
						if (KPGroups.A2.size() < leftQNum) {
							KPGroups.A2 << currentKCP
						}
					}
				} else if (currentKCP.recentTotal == 0) {
					if (needToFocus) {
						if (focusGroups.A4.size() < focusQNum2) {
							focusGroups.A4 << currentKCP
						}
					} else {
						if (KPGroups.A4.size() < leftQNum2) {
							KPGroups.A4 << currentKCP
						}
					}
				} else {
					if (needToFocus) {
						if (focusGroups.A5.size() < focusQNum2) {
							focusGroups.A5 << currentKCP
						}
					} else {
						if (KPGroups.A5.size() < leftQNum2) {
							KPGroups.A5 << currentKCP
						}
					}
				}
				
				if (KPGroups.A1.size() + KPGroups.A2.size() + KPGroups.A4.size() + KPGroups.A5.size() == 0) {
					KPGroups.A5 << currentKCP
					// println "DEBUG - Taylor added, if none KP is available, KPGroups.A5: ${KPGroups.A5}"
				}
			}
		}
		// println "DEBUG - KPGroups: ${KPGroups}"
		if (needToContinue || kp == toBeFocusedKnowledge) {
			// println "DEBUG - check child points, kp is: ${kp}"
			kp.childPoints.each {
				// println "DEBUG - check child points, kp.child is: ${it}"
				if (!restrictQuestion || (restrictQuestion && restrictedKPSet.contains(it.id))) {
					if (it.totalQuestion > 0) {
						// println "DEBUG - groupingKnowledgePoints child points, kp.child is: ${it}"
						groupingKnowledgePoints(stu, it, KPGroups, focusGroups, needToFocus, targetCorrectRate, relativeTargetCorrectRate, toBeFocusedKnowledge, focusQNumber, leftQNumber, restrictQuestion, restrictedKPSet)
					}
				}
			}
		}
	}

	int calculateNumberOfQuestions(Student stu, kps, focusingKP, numberOfQuestionsPerPage) {
		int focusedQNum1, focusedQNum2
		
		if (focusingKP) {
			// a1 = max(1,k * t*m/(t*m+n))
			float qNum = numberOfQuestionsPerPage * stu.focusPower * focusingKP.totalQuestion
			int normalN = - focusingKP.totalQuestion
			kps.each {
				normalN += it.totalQuestion
			} 
			
			qNum = qNum / (stu.focusPower * focusingKP.totalQuestion + normalN)
			focusedQNum1 = qNum.round()
			
			qNum = numberOfQuestionsPerPage * stu.focusPower * focusingKP.familySize
			normalN = - focusingKP.familySize
			kps.each {
				normalN += it.familySize
			}
			
			qNum = qNum / (stu.focusPower * focusingKP.familySize + normalN)
			focusedQNum2 = qNum.round()
			
			//println "a1: $focusedQNum1   a2: $focusedQNum2"
			Math.max(Math.max(focusedQNum1, focusedQNum2), 2)
		} else {
			return 0
		}
	}
	
	private int parallelChooseQuestions(restrictQuestion, restrictedKPSet, groups, selectedQSet, chosenKPforQ, upToNumber, qType, fromGroups, qTypeStr, stuLevel, difficultyBar, practiceHistory) {
		int en = selectedQSet.size()
		int D1Value = 0
				 
		if ( en < upToNumber) {
			// println "DEBUG - parallelChooseQuestions en:${en} < upToNumber:${upToNumber}"
			Question newQ
			KnowledgePoint kp
			Random randomizer = new Random();
			int index
			int maxLoopNum = upToNumber - en + 1 + qType
			if (restrictQuestion) {
				maxLoopNum += maxLoopNum
			}
			def okToAdd = true
			
			BigDecimal offset = 0.5
			for (i in 0..maxLoopNum) {
				for (group in fromGroups) {
					if (groups[group]) {
						// println "DEBUG - parallelChooseQuestions groups[${group}]: ${groups[group]}"
						index = i % groups[group].size()
						kp = groups[group][index]
						if (selectedQSet.size() < upToNumber) {
							if (qType < 30) {
								newQ = kp.questions.toList().get(randomizer.nextInt(kp.associatedQuestion))
	
								if (qType > 0) {
									if (qType == 2 && newQ.type == "单选") {
										okToAdd = true
									} else if  (qType == 20 && newQ.type == "填空") {
										okToAdd = true
									} else {
										okToAdd = false
									}
								} else {
									okToAdd = true
								}
							} else {
								BigDecimal rangeMidPoint = stuLevel + difficultyBar // difficultyBar will be increased in outside loop
								offset = offset + i/maxLoopNum
								
								List qList = []
								
								kp.questions.each { ques->
									def toAddQ
									if (ques.parentQuestion) {
										toAddQ = ques.parentQuestion
									} else {
										toAddQ = ques
									}
									
									if (!qTypeStr || toAddQ.type == qTypeStr || (qTypeStr == CONSTANT.YDLJ_QUESTION && toAddQ.type.contains(qTypeStr)) ) {
										if (toAddQ.difficultyLevel > rangeMidPoint - offset && toAddQ.difficultyLevel < rangeMidPoint + offset) {
											qList << toAddQ
										}
									}
								}
								
//								if (qTypeStr) { 
//									qList = qList.findAll { q -> (q.type == qTypeStr || qTypeStr == CONSTANT.YDLJ_QUESTION && q.type.contains(qTypeStr)) && q.difficultyLevel > rangeMidPoint - offset && q.difficultyLevel < rangeMidPoint + offset}.asList()
//								} else {
//									qList = qList.findAll { q -> q.difficultyLevel > rangeMidPoint - offset && q.difficultyLevel < rangeMidPoint + offset}.asList()
//								}
											
								// println "DEBUG - kp:${kp},  qList: ${qList*.id}"
																
								if (qList.size() > 0) {
									newQ = qList.get(randomizer.nextInt(qList.size()))
									if (practiceHistory?.contains(newQ.qID+" ")) {
										okToAdd = false
									}
								} else {
									okToAdd = false;
								}
							}

							if (okToAdd && restrictQuestion) {
								newQ.knowledgePoints.each {
									if (!restrictedKPSet.contains(it.id)) {
										okToAdd = false
									}
								}
							}
							
							if (okToAdd && selectedQSet.add(newQ)) {
								chosenKPforQ[newQ] = kp
								if (group != "D1") {
									D1Value++
								}
							}
						} else {
							break
						}
					}
				}
				if (selectedQSet.size() >= upToNumber) { break }
			}
		}
		
		return D1Value
	}

	private int serialChooseQuestions(restrictQuestion, restrictedKPSet, groups, selectedQSet, chosenKPforQ, upToNumber, qType, fromGroups, qTypeStr, stuLevel, difficultyBar, practiceHistory) {
		int en = selectedQSet.size()
		int D1Value = 0
				 
		if ( en < upToNumber) {
			Question newQ
			Random randomizer = new Random();
			int index
			int maxLoopNum = upToNumber - en + 5 + qType
			if (restrictQuestion) {
				maxLoopNum += maxLoopNum
			}
			def okToAdd = true
			
			BigDecimal offset = 0.5
			for (i in 0..maxLoopNum) {
				for (group in fromGroups) {
					for (kp in groups[group]) {
						println "DEBUG- serialChooseQuestions groups[${group}] - kp: ${kp}, qType: ${qType}"
						if (selectedQSet.size() < upToNumber) {
							if (qType < 30) {
								newQ = kp.questions.toList().get(randomizer.nextInt(kp.associatedQuestion))
								
								if (qType > 0) {
									if (qType == 2 && newQ.type == "单选") {
										okToAdd = true
									} else if  (qType == 20 && newQ.type == "填空") {
										okToAdd = true
									} else {
										okToAdd = false
									}
								} else {
									okToAdd = true
								}
							} else {
								BigDecimal rangeMidPoint = stuLevel + difficultyBar // difficultyBar will be increased in outside loop
								offset = offset + i/maxLoopNum
								
								List qList = []
								
								kp.questions.each { ques->
									def toAddQ
									if (ques.parentQuestion) {
										toAddQ = ques.parentQuestion
									} else {
										toAddQ = ques
									}
									
									if (!qTypeStr || toAddQ.type == qTypeStr || (qTypeStr == CONSTANT.YDLJ_QUESTION && toAddQ.type.contains(qTypeStr)) ) {
										if (toAddQ.difficultyLevel > rangeMidPoint - offset && toAddQ.difficultyLevel < rangeMidPoint + offset) {
											qList << toAddQ
										}
									}
								}
								
//								if (qTypeStr) { 
//									qList = qList.findAll { q -> (q.type == qTypeStr || qTypeStr == CONSTANT.YDLJ_QUESTION && q.type.contains(qTypeStr)) && q.difficultyLevel > rangeMidPoint - offset && q.difficultyLevel < rangeMidPoint + offset}.asList()
//								} else {
//									qList = qList.findAll { q -> q.difficultyLevel > rangeMidPoint - offset && q.difficultyLevel < rangeMidPoint + offset}.asList()
//								}
											
								// println "DEBUG - kp:${kp},  qList: ${qList*.id}"
																
								if (qList.size() > 0) {
									newQ = qList.get(randomizer.nextInt(qList.size()))
									if (practiceHistory?.contains(newQ.qID+" ")) {
										okToAdd = false
									}
								} else {
									okToAdd = false;
								}
							}

							if (okToAdd && restrictQuestion) {
								newQ.knowledgePoints.each {
									if (!restrictedKPSet.contains(it.id)) {
										okToAdd = false
									}
								}
							}

							if (okToAdd && selectedQSet.add(newQ)) {
								chosenKPforQ[newQ] = kp
								if (group != "D1") {
									D1Value++
								}
							}
						} else {
							break
						}
					}
				}
								
				if (selectedQSet.size() >= upToNumber) { break }
			}
		}
		
		return D1Value
	}

	def focusGenerate() {
		def quizInstance = new Quiz(params)
		
		if (!quizInstance) {
			render(view: "create", model: [quizInstance: quizInstance])
			return
		}
		else {
			def KPGroups = [:]
			def focusGroups = [:]
			def chosenKPforQ = [:]
			def KCPMapping = new HashMap()
			Student stu = Student.get(session.user.id)
			KnowledgePoint toBeFocusedKnowledge = null
			BigDecimal relativeTargetCorrectRate = 0.95
						
			if (!session.assignment) {
				session.assignment = params.assignment
			}
			quizInstance.assignment = Assignment.get(session.assignment.id)
			int qnum = 10
			if (quizInstance.assignment.questionType?.contains("阅读")) {
				qnum = Math.min(stu.numberOfQuestionsPerPage, quizInstance.assignment.numberOfQuestionsPerPage)
			} else {
				qnum = Math.max(stu.numberOfQuestionsPerPage, quizInstance.assignment.numberOfQuestionsPerPage)
			}
			BigDecimal targetCorrectRate = Math.min(stu.targetCorrectRate, quizInstance.assignment.targetCorrectRate)

			AssignmentStatus aStatus = AssignmentStatus.get(session.assignmentStatus.id)
			if (aStatus) {
				relativeTargetCorrectRate = aStatus.relativeTargetCorrectRate
				toBeFocusedKnowledge = aStatus.toBeFocusedKnowledge
			} 
			// Taylor - 16/06/18
			else {
				aStatus = AssignmentStatus.findByAssignmentAndStudent(quizInstance.assignment, stu)
			}
			// println "DEBUG - toBeFocusedKnowledge: ${toBeFocusedKnowledge}"
			
			def newQ
			HashSet selectedQSet = new HashSet<Question>()
			
			HashSet coveredKPSet = new HashSet()
			HashSet parentKPSet = new HashSet()
			HashSet restrictedKPSet = new HashSet()
			def restrictQuestion = !stu.toShowKnowledgeGraph  // Will create a more meaningful field in next version
			
			if (restrictQuestion) {
				def effectiveKPNumber = 0
				def assignmentResult = AssignmentStatus.where{student == stu}.list()
				assignmentResult.each {
					if (it != aStatus) {
						it.coveredKPs.each { kp ->
							coveredKPSet.add(kp.id)
						}
					}
				}
				
				// println coveredKPSet.size()
				// println coveredKPSet
				
				quizInstance.assignment.templates.each { template ->
					template.knowledgePoints.each{findParentKPs(it, parentKPSet)}
				}

				// println parentKPSet.size()
				// println parentKPSet
				
				quizInstance.assignment.templates.each { template ->
					template.knowledgePoints.each{ rootKP ->
						rootKP.childPoints.each {
							effectiveKPNumber += buildRestrictedKPs(it, coveredKPSet, parentKPSet, restrictedKPSet)
						}
						restrictedKPSet.add(rootKP.id)
					}
				}

				// println restrictedKPSet.size()
				// println restrictedKPSet
				if (effectiveKPNumber < 10) {
					restrictQuestion = false // Too few KPs to choose.
				} else {
					restrictedKPSet += coveredKPSet
				}
				
				//println restrictedKPSet.size()
				//println restrictedKPSet
			}
			
			//println "Relative Rate: ${relativeTargetCorrectRate}"
			//println "Focused Knowledge Point: ${toBeFocusedKnowledge}"

			quizInstance.student = stu
			quizInstance.score = 0
			quizInstance.type = CONSTANT.QUIZ_TYPE_SELFPRAC
			quizInstance.status = CONSTANT.QUIZ_STATUS_NOTBEGIN
			quizInstance.toBeFocusedKnowledge = toBeFocusedKnowledge
			
			focusGroups["A1"] = new HashSet()
			focusGroups["A2"] = new HashSet()
			focusGroups["A4"] = new HashSet()
			focusGroups["A5"] = new HashSet()
			focusGroups["B1"] = new HashSet()
			focusGroups["B2"] = new HashSet()
			focusGroups["B4"] = new HashSet()
			focusGroups["B5"] = new HashSet()
			focusGroups["C1"] = new HashSet()
			focusGroups["C2"] = new HashSet()
			focusGroups["C4"] = new HashSet()
			focusGroups["C5"] = new HashSet()
			focusGroups["D1"] = new HashSet()

			KPGroups["A1"] = new HashSet()
			KPGroups["A2"] = new HashSet()
			KPGroups["A4"] = new HashSet()
			KPGroups["A5"] = new HashSet()
			KPGroups["B1"] = new HashSet()
			KPGroups["B2"] = new HashSet()
			KPGroups["B4"] = new HashSet()
			KPGroups["B5"] = new HashSet()
			KPGroups["C1"] = new HashSet()
			KPGroups["C2"] = new HashSet()
			KPGroups["C4"] = new HashSet()
			KPGroups["C5"] = new HashSet()
			KPGroups["D1"] = new HashSet()

			def kpList = []
			
			quizInstance.assignment.templates.each {
				kpList.add(it.knowledgePoints)				
			}
			
			// println "DEBUG - kpList: ${kpList}"
						
			int focusQNumber = 1
			if (!quizInstance.assignment.questionType?.contains("阅读")) {
				focusQNumber = calculateNumberOfQuestions(stu, kpList.flatten(), toBeFocusedKnowledge, qnum)
			}
			
			// println "DEBUG - focusQNumber: ${focusQNumber}"
			
			// println "QNum: $qnum"
			// println "focusQNumber: $focusQNumber"
			// println "toBeFocusedKnowledge: $toBeFocusedKnowledge"
			
			
			quizInstance.assignment.templates.each { template ->
				template.knowledgePoints.each{
					groupingKnowledgePoints(stu, it, KPGroups, focusGroups, false, targetCorrectRate, relativeTargetCorrectRate, toBeFocusedKnowledge, focusQNumber, qnum-focusQNumber, restrictQuestion, restrictedKPSet)
				}
			}
						
			// println "DEBUG - KPGroups: ${KPGroups}"
			// println "DEBUG - focusGroups: ${focusGroups}"
			// println "DEBUG - toBeFocusedKnowledge: ${toBeFocusedKnowledge}"

			KPGroups.keySet().each { key ->
				KPGroups[key] = KPGroups[key] - focusGroups[key]
				focusGroups[key] = focusGroups[key].toList()
				KPGroups[key] = KPGroups[key].toList() 
				if (key != "D1") {
					focusGroups[key] = focusGroups[key].sort{a,b -> ((a.recentCorrectRate <=> b.recentCorrectRate) ?: (a.familyCorrect <=> b.familyCorrect))}
					KPGroups[key] = KPGroups[key].sort{a,b -> ((a.recentCorrectRate <=> b.recentCorrectRate) ?: (a.familyCorrect <=> b.familyCorrect))}
					focusGroups[key] = focusGroups[key].collect{it.knowledge}
					KPGroups[key] = KPGroups[key].collect{it.knowledge}
				}
			}
						
			// println "DEBUG - KPGroups: ${KPGroups}"
			// println "DEBUG - focusGroups: ${focusGroups}"

			/*
			println "=== Focusing Groups ==="
			focusGroups.keySet().each {
				println "$it: ${focusGroups[it]}"
			}

			println "=== Normal Groups ==="
			KPGroups.keySet().each {
				println "$it: ${KPGroups[it]}"
			}
			*/
			 
			// println "DEBUG - quizInstance.assignment.questionType = ${quizInstance.assignment.questionType}"
			// will calculate loop number by qType value
			int qType = 0
			if (quizInstance.assignment.questionType?.contains("阅读")) {
				qType = 30				
			} else if (quizInstance.assignment.questionType?.contains("填空")) {
				qType = 20
			} else if (quizInstance.assignment.questionType?.contains("单选")) {
				qType = 2
			}
			
			if (qType == 30 && toBeFocusedKnowledge) {
				if (toBeFocusedKnowledge.childPoints.size() > 0) {
					focusGroups["A1"] += toBeFocusedKnowledge.childPoints
					KPGroups["A1"] += toBeFocusedKnowledge.childPoints
				} else {				
					focusGroups["A1"] << toBeFocusedKnowledge
					KPGroups["A1"] << toBeFocusedKnowledge
				}
			}

			int weakQNumber = 0
			String qTypeStr = quizInstance.assignment.questionType
			BigDecimal stuLevel = stu.masterLevel
			BigDecimal difficultyBar = quizInstance.assignment.difficultyBar
			String practiceHistory = stu.practiceHistory
						
			for (i in 0..10) {
				// Increase the range if can't find the questions in the last round
				if (difficultyBar > 0) {
					difficultyBar += i
				} else if (difficultyBar < 0) {
					difficultyBar -= i
				}
				
				// println "DEBUG - == Choosing from Focus Groups =="
				weakQNumber += parallelChooseQuestions(restrictQuestion, restrictedKPSet, focusGroups, selectedQSet, chosenKPforQ, focusQNumber, qType, ["B2", "C2", "A2", "D1"], qTypeStr, stuLevel, difficultyBar, practiceHistory)
				parallelChooseQuestions(restrictQuestion, restrictedKPSet, focusGroups, selectedQSet, chosenKPforQ, focusQNumber, qType, ["B4", "C4", "A4"], qTypeStr, stuLevel, difficultyBar, practiceHistory)
				serialChooseQuestions(restrictQuestion, restrictedKPSet, focusGroups, selectedQSet, chosenKPforQ, focusQNumber, qType, ["B5", "C5", "A5", "B1", "C1", "A1"], qTypeStr, stuLevel, difficultyBar, practiceHistory)
				
				// println "DEBUG - restrictQuestion: ${restrictQuestion}"
				// println "DEBUG - restrictedKPSet: ${restrictedKPSet}"
				// println "DEBUG - focusGroups: ${focusGroups}"
				// println "DEBUG - selectedQSet.size(): ${selectedQSet.size()}"
				// println "DEBUG - chosenKPforQ: ${chosenKPforQ}"
				// println "DEBUG - weakQNumber: ${weakQNumber}"
				
				// println "DEBUG - == Choosing from Normal Groups =="
				weakQNumber += parallelChooseQuestions(restrictQuestion, restrictedKPSet, KPGroups, selectedQSet, chosenKPforQ, qnum, qType, ["B2", "C2", "A2", "D1"], qTypeStr, stuLevel, difficultyBar, practiceHistory)
				parallelChooseQuestions(restrictQuestion, restrictedKPSet, KPGroups, selectedQSet, chosenKPforQ, qnum, qType, ["B4", "C4", "A4"], qTypeStr, stuLevel, difficultyBar, practiceHistory)
				serialChooseQuestions(restrictQuestion, restrictedKPSet, KPGroups, selectedQSet, chosenKPforQ, qnum, qType, ["B5", "C5", "A5", "B1", "C1", "A1"], qTypeStr, stuLevel, difficultyBar, practiceHistory)
				
				if (selectedQSet.size() < qnum) {
					if (i == 9 && qType < 30) {
						qType = 0 // why do this ???
					}
				} else {
					break
				}
			}
									
			// println "QSet: $selectedQSet"
			if (selectedQSet.size() > 0) {				
				// quizInstance.totalQuestionNum = selectedQSet.size()	
				// session.totalQuizQuestions = quizInstance.totalQuestionNum
				session.totalQuizNumber = session.totalQuizNumber + 1
				quizInstance.name = params.name +  (Quiz.countByStudentAndAssignment(stu, quizInstance.assignment) + 1)  // not to use:  session.totalQuizNumber
				quizInstance.reviewComment = qnum - weakQNumber
				//println "遍历值: ${quizInstance.reviewComment}"
								
				quizInstance.save(failOnError:true)
								
				selectedQSet.each {
					if (it) {
						quizInstance.addToRecords(new QuizQuestionRecord(quiz:quizInstance, question:it, chosenFor: chosenKPforQ[it]))
						if (stu.practiceHistory.length() > CONSTANT.Practice_History_Limit) {
							stu.practiceHistory = stu.practiceHistory.substring((CONSTANT.Practice_History_Limit / 2).toInteger().intValue())
						}
						stu.practiceHistory += it.qID+" "
					}					
				}
				
				if (selectedQSet.size() < qnum) {
					flash.message = "Congratulations! You only need to practice ${selectedQSet.size()} more questions."
				}
				
				if (restrictQuestion) {
					flash.message = "不含超范围题目"
				}
								
				params.max = Math.min(params.int('max') ?: stu.numberOfQuestionsPerPage, 300)
				def offset = params.int('offset') ?: 0
				redirect(controller: "QuizQuestionRecord", action: "answering", params:[quiz_id:quizInstance.id, max:params.max, offset:offset, stuId:stu.id])
			} else {
				flash.message = "No new questions can be found. Please check if your coverage rate reached 100% and contact your teacher for help."
				redirect(action: "list")
			}
		}
	}

	def smartGenerate() {
		def quizInstance = new Quiz(params)

		if (!quizInstance) {
			render(view: "create", model: [quizInstance: quizInstance])
			return
		}
		else {			
			def lowKPList = []
			def chosenKPforQ = [:]
			def neverVisitKPList = new HashSet()
			def highKPSet = new HashSet()
			def stu = session.user
			int qnum = Math.max(stu.numberOfQuestionsPerPage, quizInstance.assignment.questionLimit)
			def newQ
			
			quizInstance.student = stu
			quizInstance.score = 0
			quizInstance.type = CONSTANT.QUIZ_TYPE_SELFPRAC
			quizInstance.status = CONSTANT.QUIZ_STATUS_NOTBEGIN
	
			HashSet selectedQSet = new HashSet<Question>()
			Random randomizer = new Random();

			quizInstance.assignment.templates.each { template ->
				template.knowledgePoints.each{fillInKPList(stu, it, lowKPList, neverVisitKPList, highKPSet)}
			}

			for ( i in 0..qnum ) {
				//Get one that never tested
				if (neverVisitKPList) {
					neverVisitKPList.each {
						if (selectedQSet.size() < qnum) {
							newQ = it.questions.toList().get(randomizer.nextInt(it.questions.size()))
							if (selectedQSet.add(newQ)) {
								chosenKPforQ[newQ] = it
							}
						}
					}
				}

				if (selectedQSet.size() >= qnum) {
					break
				}

				def toBeFocusedKP = true
				if (lowKPList) {
					lowKPList.sort{it.recentCorrectRate}.each {
						if (selectedQSet.size() < qnum) {
							def ql = it.knowledge.questions.toList()
							//newQ = ql.get(randomizer.nextInt(ql.size()))
							int aa = randomizer.nextInt(ql.size())
							newQ = ql.get(aa)
							if (selectedQSet.add(newQ)) {
								chosenKPforQ[newQ] = it.knowledge
							}
							
							if (it.recentCorrectRate > 0 && toBeFocusedKP && selectedQSet.size() < qnum) {
								newQ = ql.get(randomizer.nextInt(ql.size()))
								if (selectedQSet.add(newQ)) {
									chosenKPforQ[newQ] = it.knowledge
									toBeFocusedKP = false
								}
							}
						}
						
					}
				}
				
				if (selectedQSet.size() >= qnum) {
					break
				}
				
				highKPSet.each {
					if (selectedQSet.size() < qnum) {
						newQ = it.questions.toList().get(randomizer.nextInt(it.questions.size()))
						if (selectedQSet.add(newQ)) {
							chosenKPforQ[newQ] = it
						}
					}
				}
				
				if (selectedQSet.size() >= qnum) {
					break
				}
			}

			if (selectedQSet.size() > 0) {
				
				// quizInstance.totalQuestionNum = selectedQSet.size() // not correct for 阅读理解题, leave to calculate it when student answering quiz
				quizInstance.name = "Q"+(Quiz.countByStudentAndAssignment(stu, quizInstance.assignment) + 1)+"-智选"
				
				selectedQSet.each {
					quizInstance.addToRecords(new QuizQuestionRecord(quiz:quizInstance, question:it, chosenFor: chosenKPforQ[it]))
				}
				
				quizInstance.save(flush: true, failOnError: true)
				
				if (selectedQSet.size() < qnum) {
					flash.message = "Congratulations! You only need to practice ${selectedQSet.size()} more questions."
				}
				redirect(action: "show", id: quizInstance.id)
			} else {
				flash.message = "Congratulations! You have mastered all the knowledgs. No more exercises needed."
				redirect(action: "list")
			}			
		}
	}

    def save() {
        def quizInstance = new Quiz(params)
		quizInstance.student = session.user
		quizInstance.score = 0
		quizInstance.type = CONSTANT.QUIZ_TYPE_SELFPRAC
		quizInstance.status = CONSTANT.QUIZ_STATUS_NOTBEGIN
		smartGenerate(quizInstance)
    }
	
    def show(Long id) {
        def quizInstance = Quiz.get(id)
        if (!quizInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quiz.label', default: 'Quiz'), id])
            redirect(action: "list")
            return
        }
				
		// if some errors occurred during quiz generating (e.g. network issue, terminated the process: focusGenerate()-->answering()), 
		// the quiz's status will be NOTBEGIN, we cannot make sure if the quiz misses something, we can only delete this and re-generate again.
		if (quizInstance.status == CONSTANT.QUIZ_STATUS_NOTBEGIN) {
			if (session.user instanceof Student) {				
				flash.message = "This quiz might be generated error before and hasn't been taken, system helps you to generate another one"
				quizInstance.delete(flash:true, failOnError:true);
				continuePractice()
			} else {
				def stuId = quizInstance.student.id
				flash.message = "This quiz might be generated error before and hasn't been taken, system will delete this one"
				quizInstance.delete(flash:true, failOnError:true);
				redirect(action: "list", params:['stuId':stuId])
			}
			
			return
		}
		
		def chosenKPs = [] as HashSet
		def currentKCPList = []
		def kcpQSMap = [:]
		def kcpMap = [:]
		
		String QS_currQuizKPs = '本次练习涵盖的知识点'
		String QS_currQuizKPsP = '本次练习涵盖知识点及其上层'
		String QS_toBeFocusedKPsPC = '需强化知识点及其周边'
		String QS_currAssRootKPsC = '当前作业范围中的顶层知识点'
		String QS_allToBeFocusedKPs = '每次练习中的薄弱知识点'
		String QS_currAssKPs = '当前掌握度较低的知识点'
		
		kcpQSMap.put(QS_currQuizKPs, [])
		kcpQSMap.put(QS_currQuizKPsP, [])
		kcpQSMap.put(QS_toBeFocusedKPsPC, [])
		kcpQSMap.put(QS_currAssRootKPsC, [])
		kcpQSMap.put(QS_allToBeFocusedKPs, [])
		kcpQSMap.put(QS_currAssKPs, [])
		
		// ALL KPs in this quiz
		quizInstance.records.each { record->
			chosenKPs << record.chosenFor
		}

		def tempMap = [:]
		def quizKCPs = KnowledgeCheckPoint.findAllByQuiz(quizInstance)
		quizKCPs.each {
			if (!kcpMap[it.knowledge.id]) {
				kcpMap[it.knowledge.id] = it
			}

			if (chosenKPs.contains(it.knowledge)) {
				currentKCPList.add(it)
				
				if (it.familyRecentCorrectRate >= 0) {
					//kcpQSMap[QS_currQuizKPs] << it.knowledge
					tempMap.put(it.knowledge, it.familyRecentCorrectRate)
				}
			}
		}
		// default is '本次练习涵盖的知识点'
		tempMap = tempMap.sort({a, b -> a.value.compareTo(b.value)})
		kcpQSMap[QS_currQuizKPs] = tempMap.keySet().asList()
		
		// kcp.isFocused for '本次练习成绩'
		markAllToBeFocusedKCP(kcpMap, quizInstance.toBeFocusedKnowledge)

		/* Disable 知识点结构图 for now
		// 知识点结构图
		def orgColumns = [['string', 'KP'], ['string', 'ParentKP'], ['string', 'ToolTip']]		
		def orgData = []
		BigDecimal targetCorrectRate = Math.min(quizInstance.student.targetCorrectRate, quizInstance.assignment.targetCorrectRate)
		
		if (assignStatus.totalKnowledgePoints < 200) {
			quizInstance.assignment.templates.each {
				addChildToOrgData(orgData, it.knowledgePoints, quizInstance, "", targetCorrectRate, 0)
			}
		} else {
			quizInstance.assignment.templates.each {
				addChildToOrgData(orgData, it.knowledgePoints, quizInstance, "", targetCorrectRate, assignStatus.availableQuestions/assignStatus.totalKnowledgePoints)
			}
		}
		*/
		
		session.kcpQSMap = kcpQSMap
		session.qs_last_selection = null
				
        [quizInstance: quizInstance, 
			currentKCPList: currentKCPList.sort{it.recentCorrectRate}, 
			failedRecords: quizInstance.student.failedRecords,
			//orgColumns:orgColumns, 
			//orgData:orgData, 
			assignStatus: session.assignmentStatus,
			kcpQSMap: kcpQSMap]
    }
	
	def show_overall_kcp(Long id)
	{
		Quiz quizInstance = Quiz.get(id)
		
		def studentInstance = quizInstance.student
		//AssignmentStatus assignStatus = AssignmentStatus.findByStudentAndAssignment(studentInstance, quizInstance.assignment)
		
		// latest KCPs for all Quizes, used for 历次练习成绩汇总(本作业截止到本次练习历次练习各个知识点正确率)
		List<KnowledgeCheckPoint> overallKCPList = []
		def quizList = Quiz.findAllByStudentAndAssignmentAndAnsweredDateLessThanEquals(quizInstance.student, quizInstance.assignment, quizInstance.answeredDate, [sort:'answeredDate', order:'desc'])
		def maxKcpRate = Integer.parseInt(params.max_kcp_rate) / 100
		def kcpMap = [:]
		def chosenKPs = [] as HashSet
		
		quizList.eachWithIndex { quiz, i ->
			quiz.records.each { record->
				chosenKPs << record.chosenFor
			}
	
			def quizKCPs = KnowledgeCheckPoint.findAllByQuiz(quiz)
			quizKCPs.each {
				if (!kcpMap[it.knowledge.id] && chosenKPs.contains(it.knowledge)) {
					kcpMap[it.knowledge.id] = it
					if (it.recentCorrectRate >= 0 && it.recentCorrectRate <= maxKcpRate) {
						it.isFocused = false
						overallKCPList << it
					}
				}
			}
		}
		
		// kcp.isFocused for '历次练习成绩'
		markAllToBeFocusedKCP(kcpMap, quizInstance.toBeFocusedKnowledge)
		render(template:'all_quiz_score_table', model:["overallKCPList":overallKCPList.sort{it.recentCorrectRate}, "failedRecords":studentInstance.failedRecords, "quizInstance":quizInstance])
	}
	
	def show_kcp_zwd_col(Long id)
	{
		def maxKcpRate = Integer.parseInt(params.max_kcp_rate) / 100
		def quizNumber = Integer.parseInt(params.quiz_number)
		
		Quiz quizInstance = Quiz.get(id)
		
		def studentInstance = quizInstance.student
		AssignmentStatus assignStatus = AssignmentStatus.findByStudentAndAssignment(studentInstance, quizInstance.assignment)
		
		//本作业截止到本次练习历次练习各个知识点正确率
		def maxKPToShow = studentInstance.maxKPToShow

		def chosenKPs = [] as HashSet
		def allQuizList = Quiz.findAllByStudentAndAssignment(quizInstance.student, quizInstance.assignment)
		allQuizList.each { quiz ->
			quiz.records.each { record->
				chosenKPs << record.chosenFor
			}
		}

		def quizList = Quiz.findAllByStudentAndAssignmentAndAnsweredDateLessThanEquals(quizInstance.student, quizInstance.assignment, quizInstance.answeredDate, [max:quizNumber, sort:'answeredDate', order:'desc'])
		quizNumber = quizList.size()
		
		def kcph = [[:]] //kcp history
		def zwdLineData = [:] //每个Assignment目前错误率最高的<maxKPToShow>个知识点
		def kcpMap = [:]

		quizList.eachWithIndex { quiz, i ->

			kcph << [:]
			if (i == 0) {
				zwdLineData.put("本次掌握度",[])
			} else {
				zwdLineData.put("前${i}次掌握度",[])
			}
			
			def quizKCPs = KnowledgeCheckPoint.findAllByQuiz(quiz)
			quizKCPs.each {
				if (chosenKPs.contains(it.knowledge)) {
				if (!kcpMap[it.knowledge.id] && it.familyRecentCorrectRate >= 0 && it.familyRecentCorrectRate <= maxKcpRate) {
					kcpMap[it.knowledge.id] = it
				}
				
				if (it.familyRecentCorrectRate >= 0 && it.familyRecentCorrectRate <= maxKcpRate) {
					kcph[i][it.knowledge.id] = 100*it.familyRecentCorrectRate
					for (int j=i-1; j>-1; j--) {
						if (!kcph[j][it.knowledge.id]) {
							kcph[j][it.knowledge.id] = 100*it.familyRecentCorrectRate
						} else {
							break
						}
					}
				}
				}
			}
		}
		//println "zwdLineData: ${zwdLineData}"
		
		kcpMap = kcpMap.sort({ a, b ->
			a.value.familyRecentCorrectRate?.compareTo(b.value.familyRecentCorrectRate)
		})
		
		def zwdColCol = ["截止本次练习知识点掌握度"]
		def zwdColData = [:]
		
		def zwdLineCol = []
		kcpMap.each { key, value ->
			zwdColData << ["${KnowledgePoint.load(key).name.encodeAsHTML()}":[
					100*value.familyRecentCorrectRate
				]]
			
			zwdLineData["本次掌握度"] << 100*value.familyRecentCorrectRate

			// for zwdLine
			for (int i in 1..quizNumber-1) {
				//println "kcph[i]: ${kcph[i]}"
				def xlist = zwdLineData.get("前${i}次掌握度")
				//println "xlist: ${xlist}"
				if (xlist != null) {
					if (kcph[i][key]) {
						xlist << kcph[i][key]
					} else {
						xlist << 100*value.familyRecentCorrectRate
					}
				} else {
					println "xlist is null: ${xlist}"
				}
			}
			
			zwdLineCol << KnowledgePoint.load(key).name
		}
		
		/*		
		println "zwdColCol: ${zwdColCol}"
		println "zwdColData: ${zwdColData}" 
		println "zwdLineCol: ${zwdLineCol}"
		println "zwdLineData: ${zwdLineData}"
		*/
		
		render(template:'kcp_zwd_col', model:[kcpMap:kcpMap, "zwdColCol":zwdColCol, "zwdColData":zwdColData, "zwdLineCol":zwdLineCol, "zwdLineData":zwdLineData, "assignStatus":assignStatus])
	}
	
	def show_kcp_qs_col(Long id)
	{		
		Quiz quizInstance = Quiz.get(id)

		def kcpQSMap = session.kcpQSMap
		def qsSelection = params.qs_selection		
		String QS_currQuizKPs = '本次练习涵盖的知识点'
		String QS_currQuizKPsP = '本次练习涵盖知识点及其上层'
		String QS_toBeFocusedKPsPC = '需强化知识点及其周边'
		String QS_currAssRootKPsC = '当前作业范围中的顶层知识点'
		String QS_allToBeFocusedKPs = '每次练习中的薄弱知识点'
		String QS_currAssKPs = '当前掌握度较低的知识点'
		
		if (!kcpQSMap[qsSelection]) {
			def tempMap = [:]
			if (qsSelection == QS_currQuizKPsP) {
				kcpQSMap[QS_currQuizKPs].each { k ->
					def kp = KnowledgePoint.get(k.id)
					kcpQSMap[QS_currQuizKPsP] << kp
					kp.parentPoints?.each {
						def pkcp = KnowledgeCheckPoint.findByKnowledgeAndQuiz(it, quizInstance)
						if (pkcp?.familyRecentCorrectRate >= 0) {
							kcpQSMap[QS_currQuizKPsP] << it
							tempMap.put(it, pkcp.familyRecentCorrectRate)
						}
					}
				}
			} else if (qsSelection == QS_toBeFocusedKPsPC) {
				kcpQSMap[QS_toBeFocusedKPsPC] << quizInstance.toBeFocusedKnowledge
				
				quizInstance.toBeFocusedKnowledge.parentPoints?.each {
					kcpQSMap[QS_toBeFocusedKPsPC] << it
				}
				
				quizInstance.toBeFocusedKnowledge.childPoints?.each { c->
					def ckcp = KnowledgeCheckPoint.findByKnowledgeAndQuiz(c, quizInstance)
					if (ckcp?.familyRecentCorrectRate >= 0) {
						kcpQSMap[QS_toBeFocusedKPsPC] << c
						tempMap.put(c, ckcp.familyRecentCorrectRate)
					}
				}
			} else if (qsSelection == QS_allToBeFocusedKPs) {
				def quizList = Quiz.findAllByStudentAndAssignmentAndAnsweredDateLessThanEquals(quizInstance.student, quizInstance.assignment, quizInstance.answeredDate, [sort:'answeredDate', order:'desc'])
				quizList.each { quiz ->
					kcpQSMap[QS_allToBeFocusedKPs] << quiz.toBeFocusedKnowledge
				}
			} else if (qsSelection == QS_currAssRootKPsC) {
				def assignment = Assignment.get(session.assignment.id)
				assignment.templates.each { t ->
					t.knowledgePoints.each { kp->
						def tkcp = KnowledgeCheckPoint.findByKnowledgeAndQuiz(kp, quizInstance)
						if (tkcp?.familyRecentCorrectRate >= 0) {
							kcpQSMap[QS_currAssRootKPsC] << kp
							tempMap.put(kp, tkcp.familyRecentCorrectRate)
							
							kp.childPoints?.each { c->
								def ckcp = KnowledgeCheckPoint.findByKnowledgeAndQuiz(c, quizInstance)
								if (ckcp?.familyRecentCorrectRate >= 0) {
									kcpQSMap[QS_currAssRootKPsC] << c
									tempMap.put(c, ckcp.familyRecentCorrectRate)
								}
							}
						}
					}
				}
				
			} else if (qsSelection == QS_currAssKPs) {
				def assignmentStatus = AssignmentStatus.get(session.assignmentStatus.id)
				assignmentStatus.coveredKPs.each { kp ->
					def latestKCP = KnowledgeCheckPoint.findByKnowledgeAndQuiz(kp, quizInstance)
					if (!latestKCP) {
						latestKCP = KnowledgeCheckPoint.findByKnowledgeAndStudentAndDatePracticedLessThanEquals(kp, quizInstance.student, quizInstance.answeredDate, [sort:'id', order:'desc'])
					}
					if (latestKCP?.familyRecentCorrectRate >= 0 && latestKCP?.familyRecentCorrectRate < quizInstance.student.targetCorrectRate) {
							kcpQSMap[QS_currAssKPs] << kp
							tempMap.put(kp, latestKCP.familyRecentCorrectRate)
					}
				}
			}

			kcpQSMap[qsSelection].each {
				if (!tempMap.get(it)) {
					def latestKCP = KnowledgeCheckPoint.findByKnowledgeAndQuiz(it, quizInstance)
					if (latestKCP?.familyRecentCorrectRate >= 0) {
						tempMap.put(it, latestKCP.familyRecentCorrectRate)
					}
				}
			}
			
			//tempMap 是当前 quiz的各个知识点的成绩，之前的quiz这些知识点的成绩是在prepareKcpQSData()中计算
			tempMap = tempMap.sort({a, b -> a.value.compareTo(b.value)})
			kcpQSMap[qsSelection] = tempMap.keySet().asList()
	
			session.kcpQSMap = kcpQSMap
		} 
		
		def KPs = []
		
		if (!params.selectedKPs) {
			if (qsSelection != session.qs_last_selection) {
				KPs = kcpQSMap[qsSelection]
				session.qs_last_selection = qsSelection
			} else {
				KPs = flash.selected_KPs
			}
		} else {
			if (params.selectedKPs instanceof String[]) {
				params.selectedKPs.each {
					KPs << KnowledgePoint.get(Integer.parseInt(it))
				}
			} else {
				KPs << KnowledgePoint.get(params.int('selectedKPs'))
			}
		}
		
		// println "KPs: ${KPs}"
		// sort by the latest quiz's rate
		/*
		def tempMap = [:]
		KPs.each {
			def skcp = KnowledgeCheckPoint.findByQuizAndKnowledge(quizInstance, it)
			// for some case like QS_allToBeFocusedKPs = '所有作业中的薄弱知识点', the KP may not be covered in current quiz
			if (!skcp) {
				skcp = KnowledgeCheckPoint.findByKnowledgeAndFamilyRecentCorrectRateGreaterThan(it, 0, [sort:"quiz.id", order:"desc"])
			}
			tempMap.put(it, skcp.familyRecentCorrectRate)
		}
		tempMap = tempMap.sort({a, b -> a.value.compareTo(b.value)})
		
		KPs = tempMap.keySet().asList()
		*/
		flash.selected_KPs = KPs
				
		def quizNumber = 5		
		if (params.quiz_number) {
			quizNumber = Integer.parseInt(params.quiz_number)
		} 
		
		// computer the quizzes before quizInstance
		def kcp_qs_data = prepareKcpQSData(KPs, quizInstance, quizNumber)
		render(template:'kcp_qs_col', model:["qsXlabel":kcp_qs_data[0], "qsData":kcp_qs_data[1], "qs_quizs":kcp_qs_data[2], "qs_selection":params.qs_selection, "quiz_number":quizNumber])
	}
	
	def show_kcp_qs_kplist(Long id)
	{
		Quiz quizInstance = Quiz.load(id)
		
		//println "params.qs_selection: ${params.qs_selection}, params.qs_kp_num: ${params.qs_kp_num}"	
		
		def KPs = session.kcpQSMap.get(params.qs_selection)
		//KPs.sort({KnowledgePoint a, KnowledgePoint b -> a.name.compareTo(b.name)}) // no need to short by name
		
		// sort by the latest quiz's rate
		def tempMap = [:]
		KPs.each {
			tempMap.put(it, KnowledgeCheckPoint.findByKnowledge(it, [sort:"quiz.id", order:"desc"]).familyRecentCorrectRate)	
		}
		tempMap = tempMap.sort({a, b -> a.value.compareTo(b.value)})
		
		KPs = tempMap.keySet().asList()
				
		render(template:'kcp_qs_modal', model:["qs_selection":params.qs_selection, "quiz_number":params.quiz_number, "KPs":KPs, "quizInstance":quizInstance])
	}
	
	def filterChartKPs =
	{
		render(template: 'kcp_qs_selection', model:  [chartKPSet: session.chartKPSet?.findAll { kp -> kp.name.contains(params.nameFilter) }])
	}

	private void markAllToBeFocusedKCP(currentKCPs, kp)
	{
		if (kp) {
			if (currentKCPs[kp.id]) {
				currentKCPs[kp.id].isFocused = true
			}

			kp.childPoints.each {
				if (currentKCPs[it.id]?.familyTotalSum > 0) {
					markAllToBeFocusedKCP(currentKCPs, it)
				}
			}
		}
	}

	private void getAllKnowledgePoints(kp, allKPs)
	{
		allKPs.add(kp)
		kp.childPoints.each {
			getAllKnowledgePoints(it, allKPs)
		}
	}
	
	/*
	private void addChildToOrgData(List orgData, knowledgePoints, Quiz quizInstance, kp, targetCorrectRate, minQuestions)
	{
		DecimalFormat df = new DecimalFormat("####.#")
		
		knowledgePoints.each {
			if (it.totalQuestion > minQuestions) {
				KnowledgeCheckPoint currentKCP = KnowledgeCheckPoint.findByKnowledgeAndStudentAndDatePracticedLessThanEquals(it, quizInstance.student, quizInstance.answeredDate, [sort:'id', order:'desc'])
				
				def valStr = ""
				if (it.associatedQuestion > 0 ) {
					valStr = "<div style='color:orange; font-style:italic'>Never Visited!</div>"
				}

				if (currentKCP) {
					def color1 = "green"
					def color2 = "green"
					def valStr1 = "${currentKCP.correctQuestions}:${currentKCP.totalQuestions}|${df.format(currentKCP.recentCorrect)}/${df.format(currentKCP.recentTotal)}|${currentKCP.totalSum}-"
					def valStr2 = "${currentKCP.familyCorrect}:${currentKCP.familyTotal}|${df.format(currentKCP.familyRecentCorrect)}/${df.format(currentKCP.familyRecentTotal)}|${currentKCP.familyTotalSum}-"

					if (currentKCP.quiz.id != quizInstance.id) {
						color1 = "blue"
						color2 = "blue"
					}

					def val = currentKCP?.recentCorrectRate
					if (val >= 0){
						if (val < targetCorrectRate) {
							color1 = 'red'
						}
						valStr1 += "${df.format(val*100)}%"
					}

					val = currentKCP?.familyRecentCorrectRate
					if (val >= 0){
						if (val < targetCorrectRate) {
							color2 = 'red'
						}
						valStr2 += "${df.format(val*100)}%"
					}

					valStr = "<div style='color:$color1; font-style:italic'>$valStr1</div><div style='color:$color2; font-style:italic'>$valStr2</div>"
				}

				orgData.add([
					new Cell(value:it.name, label:"$it$valStr"),
					kp,
					it.toString()
				])
				if (it.familySize > 1) {
					addChildToOrgData(orgData, it.childPoints, quizInstance, it.name, targetCorrectRate, minQuestions)
				}
			}
		}
	}
	*/
	
	private List prepareKcpQSData(knowledgePoints, Quiz quizInstance, quizNumber)
	{
		// 知识点进步趋势图
		def qsXlabel = []
		def qsData = [:]
		def quizes = []
		def lastRate = [:]
		int j = 1
				
		knowledgePoints.each { kp ->
			qsData.put(kp.name.encodeAsHTML(), [])
		}
		
		// the quizzes before quizInstance
		quizes = Quiz.findAllByStudentAndAssignmentAndAnsweredDateLessThanEquals(quizInstance.student, quizInstance.assignment, quizInstance.answeredDate, [max:quizNumber, sort:'answeredDate', order:'desc'])
		quizes = quizes.reverse()
		
		quizes.eachWithIndex { quiz, quizSeq ->
			def xItem = quiz.name
			def idx = xItem.indexOf("练习") == -1 ? xItem.length() - 4 : xItem.indexOf("练习")
			if (quizes.size() > 15) {
				qsXlabel << quizSeq + 1
			} else {
				qsXlabel << xItem.substring(idx) // qsXlabel << "练习#"+(quizSeq+1)
			}
			
			knowledgePoints.eachWithIndex { kp, i ->
				
				def rate = KnowledgeCheckPoint.findByKnowledgeAndQuizAndFamilyRecentCorrectRateGreaterThan(kp, quiz, -1)?.familyRecentCorrectRate
				
				if (rate != null) {
					rate *= 100;
				} 
				// don't set rate=0 if it is null
				
				// rate by each quiz and each KP
				qsData[kp.name.encodeAsHTML()] << rate
			}
		}
		
		// println "qsXlabel: ${qsXlabel}"
		// println "qsData: ${qsData}"

		[qsXlabel, qsData, quizes]
	}
	
	/* below is for google visualization data
	private List prepareKcpQSData(knowledgePoints, Quiz quizInstance, quizNumber)
	{
		// 知识点进步趋势图
		def kcpLinesColumns = [['string', 'Quiz']]
		def kcpLinesData = []
		def quizes = []
		def lastRate = [:]
		int j = 1
				
		quizes = Quiz.findAllByStudentAndAssignmentAndAnsweredDateLessThanEquals(quizInstance.student, quizInstance.assignment, quizInstance.answeredDate, [max:quizNumber, sort:'answeredDate', order:'desc'])
		quizes = quizes.reverse()
		knowledgePoints.each { kcpLinesColumns << ['number', it?.name?.encodeAsJavaScript()]} // it.id
		
		quizes.each { quiz ->
			def kd = [j]
			def hasValue = false

			knowledgePoints.eachWithIndex { kp, i ->

				def rate = KnowledgeCheckPoint.findByKnowledgeAndQuiz(kp, quiz)?.familyRecentCorrectRate
				if (rate != null) {
					rate *= 100;
					hasValue = true
				}
				if (rate < 0) {
					rate = lastRate[i]
				}
				kd << rate
				lastRate[i] =  rate
			}
			if (hasValue) {
				kcpLinesData << kd
				j++
			}
		}

		int kpColNum = kcpLinesColumns.size

		for (int i=kpColNum -1; i>0; i--) {
			def isEmpty = true
			kcpLinesData.each { kd ->
				if (kd[i]) {
					isEmpty = false
				}
			}

			if (isEmpty) {
				kcpLinesColumns.remove(i)
				kcpLinesData.each { kd ->
					kd.remove(i)
				}
			}
		}
		
		// println kcpLinesColumns
		// println kcpLinesData
				
//		def kpref = ''
//		for (int i=1; i < kcpLinesColumns.size; i++) {
//			def kpid = kcpLinesColumns[i][1]
//			def kpDesc = KnowledgePoint.get(kpid)
//			kpref += kpid+" : "+kpDesc+"\n"
//		}

		[kcpLinesColumns, kcpLinesData, quizes]
	}*/
		
	def charts()
	{
		// only a redirect to a charts testing page
	}

    def edit(Long id) {
        def quizInstance = Quiz.get(id)
        if (!quizInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quiz.label', default: 'Quiz'), id])
            redirect(action: "list")
            return
        }

        [quizInstance: quizInstance]
    }

    def update(Long id, Long version) {
        def quizInstance = Quiz.get(id)
        if (!quizInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quiz.label', default: 'Quiz'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (quizInstance.version > version) {
                quizInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'quiz.label', default: 'Quiz')] as Object[],
                          "Another user has updated this Quiz while you were editing")
                render(view: "edit", model: [quizInstance: quizInstance])
                return
            }
        }

        quizInstance.properties = params

        if (!quizInstance.save(flush: true)) {
            render(view: "edit", model: [quizInstance: quizInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'quiz.label', default: 'Quiz'), quizInstance.id])
        redirect(action: "show", id: quizInstance.id)
    }

    def delete(Long id) {
        def quizInstance = Quiz.get(id)
        if (!quizInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quiz.label', default: 'Quiz'), id])
            redirect(action: "list")
            return
        }

        try {
            quizInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'quiz.label', default: 'Quiz'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'quiz.label', default: 'Quiz'), id])
            redirect(action: "show", id: id)
        }
    }
}
