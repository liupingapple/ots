package ots

import java.text.SimpleDateFormat

import org.springframework.dao.DataIntegrityViolationException

class BatchQuestionOpController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def beforeInterceptor = [action:this.&checkUser]
	
	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'adminUser',action:'login')
			return false
		} else if (!session.user.admin) {
			flash.message = "Sorry, but you need admistrator privilege for Batch Question Import/Export."
			redirect(controller:'question', action:'list')
			return false
		}
	}
		
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [batchQuestionOpInstanceList: BatchQuestionOp.list(params), batchQuestionOpInstanceTotal: BatchQuestionOp.count()]
    }

    def create() {
        [batchQuestionOpInstance: new BatchQuestionOp(params)]
    }

	def executeBatch() {
		def batchQuestionOpInstance = new BatchQuestionOp(params)
		if (!batchQuestionOpInstance.save(flush: true)) {
			flash.message = "Sorry. Batch ${batchQuestionOpInstance.operation} Failed!" 
			//render(view: "create", model: [batchQuestionOpInstance: batchQuestionOpInstance])
			//return
		}

		/*if (batchQuestionOpInstance.operation == "Export in custom format") {
				
			def quesIndex = 0
			def exportCount = 0
			def c = Question.createCriteria()
			def results = c.list {
				if (batchQuestionOpInstance.type == '单选' || batchQuestionOpInstance.type == '填空') {
					eq("type", batchQuestionOpInstance.type)
				} 
				else if (batchQuestionOpInstance.type == '阅读理解题干') {
					like("type", "阅读理解%")
				}
				else if (batchQuestionOpInstance.type == '阅读理解子题-单选' || batchQuestionOpInstance.type == '阅读理解子题-单选') {
					eq("type", batchQuestionOpInstance.type.replace("阅读理解子题-", ""))
					isNotNull("parentQuestion")
				}
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++;
				if (quesIndex < batchQuestionOpInstance.startIndex) {
					continue
				} else if (quesIndex > batchQuestionOpInstance.endIndex) {
					break
				}
				exportCount++;
				sb << "===\n"
				sb << ques.qID
				sb << "\n------\n"
				sb << ques.type
				sb << "\n------\n"
				sb << ques.instructions
				sb << "\n------\n"
				sb << ques.description
				sb << "\n------\n"
				Iterator<Answer> ansIt =  ques.answers.iterator();
				while(ansIt.hasNext()){
					Answer ans = (Answer)ansIt.next();
					if (ans.correct && ques.type == "单选") {
						sb << "//"
					}
					sb << ans.content
					sb << "\n"
				}
				sb << "------\n"
				sb << ques.analysis
				sb << "\n------\n"
				if (batchQuestionOpInstance.extraFields.contains("source")) {
					sb << ques.source << "\n"
				}
				if (batchQuestionOpInstance.extraFields.contains("term")) {
					sb << ques.term << "\n"
				}
				if (batchQuestionOpInstance.extraFields.contains("errorRate")) {
					sb << ques.errorRate << "\n"
				}
				if (batchQuestionOpInstance.extraFields.contains("reviewedBy")) {
					sb << ques.reviewedBy << "\n"
				}
				if (batchQuestionOpInstance.extraFields.contains("inputBy")) {
					sb << ques.inputBy << "\n"
				}
				if (batchQuestionOpInstance.extraFields.contains("inputDate")) {
					sb << ques.inputDate << "\n"
				}
			}
			
			batchQuestionOpInstance.batchContent = sb.toString()
			flash.message = "Batch ${batchQuestionOpInstance.operation} finished. Total = ${exportCount}"
		} else if (batchQuestionOpInstance.operation == "Import in custom format") {
			def quesList = batchQuestionOpInstance.batchContent.split("===")
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			def failedOnes = "Failed:"
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
			
			for (quest in quesList) {
				if (!quest.contains("------")) {
					continue
				}
				def fields = quest.split("------")
				Question newQuestion = new Question(params)
				newQuestion.qID = Long.parseLong(fields[0].trim())
				newQuestion.type = fields[1].trim()
				newQuestion.instructions = fields[2]?.trim()
				if (newQuestion.instructions == "null") {
					newQuestion.instructions = null
				}
				newQuestion.description = fields[3].trim()

				if (batchQuestionOpInstance.type == "单选") {
					indexNum = 'A'
				} else {
					indexNum = 1
				}
				
				if (fields[4].trim()) {
					def ansList = fields[4].trim().split("\n")
					for (ans in ansList) {
						def newAns = new Answer()
						if (batchQuestionOpInstance.type == "单选") {
							if (ans.contains("//")) {
								newAns.content = ans.replaceFirst("//", "").trim()
								newAns.correct = true
							} else {
								newAns.content = ans.trim()
							}
						} else {
							newAns.content = ans.trim()
							newAns.correct = true
						}
						newAns.serialNum = "${indexNum}"
						newQuestion.addToAnswers(newAns)
						indexNum++
					}
				}
				
				if (fields[5].trim() != "null") {
					newQuestion.analysis = fields[2].trim()
				}
				
				def extraFields = fields[6].trim().split("\n") 
				def fldIndex = 0
				if (batchQuestionOpInstance.extraFields.contains("source")) {
					newQuestion.source = extraFields[fldIndex].trim()
					fldIndex++
				}
				if (batchQuestionOpInstance.extraFields.contains("term")) {
					newQuestion.term = extraFields[fldIndex].trim()
					fldIndex++
				}
				if (batchQuestionOpInstance.extraFields.contains("errorRate")) {
					newQuestion.errorRate = Float.parseFloat(extraFields[fldIndex].trim())
					fldIndex++
				}
				if (batchQuestionOpInstance.extraFields.contains("reviewedBy")) {
					newQuestion.reviewedBy = extraFields[fldIndex].trim()
					if (newQuestion.reviewedBy == "null") {
						newQuestion.reviewedBy = null
					}
					fldIndex++
				}
				if (batchQuestionOpInstance.extraFields.contains("inputBy")) {
					def inputer = AdminUser.findByUserName(extraFields[fldIndex].trim())
					if (inputer) {
						newQuestion.inputBy = inputer
					}
					fldIndex++
				}
				if (batchQuestionOpInstance.extraFields.contains("inputDate")) {
					newQuestion.inputDate = dateFormat.parse(extraFields[fldIndex].trim())
					fldIndex++
				}

				//Overridden values				
				newQuestion.type = batchQuestionOpInstance.type
				
				if (!newQuestion.save(flush: true)) {
					newQuestion.errors.each {
						println it
					}

					failedNumber++
					failedOnes += " @@@(${quest})"
				} else {
					passedNumber++
				}
			}
			
			flash.message = "Batch ${batchQuestionOpInstance.operation} finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
		} else */
		if (batchQuestionOpInstance.operation == "Export in Excel format") {
				
			def quesIndex = 0
			def exportCount = 0
			def c = Question.createCriteria()
			def results = c.list {
				if (batchQuestionOpInstance.type && batchQuestionOpInstance.type != "") {
					and {
						eq("type", batchQuestionOpInstance.type)
						isNull("parentQuestion")
					}
				}
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[QID]\t题型\t题目\tA\tB\tC\tD\t正确答案\t解析\tSource\tTerm\tError Rate\tInput By\tReviewed By\tInput Date\n"
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++;
				if (quesIndex < batchQuestionOpInstance.startIndex) {
					continue
				} else if (quesIndex > batchQuestionOpInstance.endIndex) {
					break
				}
				exportCount++;
				sb << "[${ques.qID}]\t"
				sb << "${ques.type}\t"
				sb << "${ques.description?.replaceAll("\r\n", "<\\\\n>")}\t"
				def ExtraAnswers = 4
				def AnswerIndex = 'A'
				def correctAnswer = ""
				Iterator<Answer> ansIt =  ques.answers.iterator();
				while(ansIt.hasNext()){
					Answer ans = (Answer)ansIt.next();
					if (ans.correct && ques.type == "单选") {
						correctAnswer = AnswerIndex
					}
					sb << "${ans.content}\t"
					ExtraAnswers--
					AnswerIndex++
				}
				for (int i = 0; i < ExtraAnswers; i++) {
					sb << "------\t"
				}
				sb << "${correctAnswer}\t"
				
				if (ques.analysis) {
					sb << "${ques.analysis.replaceAll("\r\n", "<\\\\n>")}"
				}

				sb << "\t${ques.source}"
				
				if (batchQuestionOpInstance.extraFields.contains("term")) {
					if (ques.term) {
						sb << "\t${ques.term}"
					} else {
						sb << "\t"
					}
				}
				if (batchQuestionOpInstance.extraFields.contains("errorRate")) {
					sb << "\t${ques.errorRate}"
				}
				
				if (batchQuestionOpInstance.extraFields.contains("inputBy")) {
					sb << "\t${ques.inputBy}"
				}
				
				if (batchQuestionOpInstance.extraFields.contains("reviewedBy")) {
					sb << "\t${ques.reviewedBy}"
				}
				if (batchQuestionOpInstance.extraFields.contains("inputDate")) {
					sb << "\t${ques.inputDate}"
				}
				sb << "\n"
			}
			
			batchQuestionOpInstance.batchContent = sb.toString()
			flash.message = "Great News. Batch ${batchQuestionOpInstance.operation} Succeed! Total = ${exportCount}"
		} else if (batchQuestionOpInstance.operation == "Import in Excel format") {
			def quesList = batchQuestionOpInstance.batchContent.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			def failedOnes = "Failed:"
			def currentQID
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd hh:mm")
			
			for (quest in quesList) {
				if (quest.contains("题目\tA\tB\tC\tD")) {
					continue
				}
				
				def fields = quest.trim().split("\t")
				currentQID = Long.parseLong(fields[0].trim().replace('[', '').replace(']', ''))
				Question newQuestion = new Question()
				newQuestion.qID = currentQID
				newQuestion.type = fields[1]
				newQuestion.description = fields[2].replaceAll("<\\\\n>", "\r\n")

				if (batchQuestionOpInstance.type == "单选") {
					indexNum = 'A'
				} else {
					indexNum = 1
				}

				for (int i = 3; i < 7; i++) {
					def ans = fields[i] 
					if (ans != "------") {
						def newAns = new Answer()
						newAns.content = ans
						if (newQuestion.type == "单选") {
							if (fields[7] == "${indexNum}") {
								newAns.correct = true
							}
						} else {
							newAns.correct = true
						}
						newAns.serialNum = "${indexNum}"
						newQuestion.addToAnswers(newAns)
						indexNum++
					}
				}

				if (fields.length > 8) {
					newQuestion.analysis = fields[8].replaceAll("<\\\\n>", "\r\n")
				}

				//example of Extra Fields: source, term, errorRate, reviewedBy, inputBy, inputDate
				def nextIndex = 9 // Extra fields which are optional
				
				if (fields.length > nextIndex && batchQuestionOpInstance.extraFields.contains("source")) {
					newQuestion.source = fields[nextIndex]
					nextIndex++
				}
				
				if (newQuestion.source?.trim() == "Reading") {
					println "skip the child question for 阅读理解"
					continue
				}

				if (fields.length > nextIndex && batchQuestionOpInstance.extraFields.contains("term")) {
					newQuestion.term = fields[nextIndex]
					nextIndex++
				}

				if (fields.length > nextIndex && batchQuestionOpInstance.extraFields.contains("errorRate")) {
					if (fields[nextIndex] == "") {
						fields[nextIndex] = "-1"
					}
					newQuestion.errorRate = Integer.parseInt(fields[nextIndex])
					nextIndex++
				}
				
				if (fields.length > nextIndex && batchQuestionOpInstance.extraFields.contains("inputBy")) {
					def inputer = AdminUser.findByUserName(fields[nextIndex])
					if (inputer) {
						newQuestion.inputBy = inputer.userName
					}
				}

				if (fields.length > nextIndex && batchQuestionOpInstance.extraFields.contains("reviewedBy")) {
					newQuestion.reviewedBy = fields[nextIndex]
					if (newQuestion.reviewedBy == "null") {
						newQuestion.reviewedBy = null
					}
					nextIndex++
				}
				
				if (fields.length > nextIndex && batchQuestionOpInstance.extraFields.contains("inputDate")) {
					def iDate
					newQuestion.inputDate = new Date()
					/*try {
						iDate = dateFormat.parse(fields[nextIndex])
					} catch (Exception e) {
						iDate = dateFormat1.parse(fields[nextIndex])
					}
					newQuestion.inputDate = iDate
					nextIndex++*/
				}

				if (!newQuestion.save()) {
					newQuestion.errors.each {
						println it
					}
					failedNumber++
					failedOnes += " @@@(${quest})"
				} else {
					passedNumber++
				}
			}
			
			flash.message = "Batch ${batchQuestionOpInstance.operation} finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
		} else if (batchQuestionOpInstance.operation == "Export Question - Knowledge mapping") {
			
			def quesIndex = 0
			def exportCount = 0
			def c = Question.createCriteria()
			def results = c.list {
				and {
					if (batchQuestionOpInstance.type && batchQuestionOpInstance.type != "") {
						eq("type", batchQuestionOpInstance.type)
					}
				}
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[Question ID]\t题目\t[知识点名称]\n"
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++;
				if (quesIndex < batchQuestionOpInstance.startIndex) {
					continue
				} else if (quesIndex > batchQuestionOpInstance.endIndex) {
					break
				}

				Iterator<KnowledgePoint> kpIt =  ques.knowledgePoints.iterator();
				while(kpIt.hasNext()){
					exportCount++;
					KnowledgePoint kp = (KnowledgePoint)kpIt.next();
					sb << "[${ques.id}]\t"
					sb << "${ques.description.replaceAll("\r\n", "<\\\\n>")}\t"
					sb << "[${kp.name}]\n"
				}
			}
			
			batchQuestionOpInstance.batchContent = sb.toString()
			flash.message = "Batch ${batchQuestionOpInstance.operation} finished. Total = ${exportCount}"
			
		} else if (batchQuestionOpInstance.operation == "Import Question - Knowledge mapping") {
			def quesList = batchQuestionOpInstance.batchContent.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def failedOnes = "Failed:"
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
			
			for (quest in quesList) {
				if (!quest.contains("[") || !quest.contains("]") || quest.contains("[Question ID]\t题目")) {
					continue
				}
				def fields = quest.trim().split("\t")
				Question ques = Question.findByDescription(fields[1].replaceAll("<\\\\n>", "\r\n"))
				KnowledgePoint kp = KnowledgePoint.findByName(fields[2].replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				if (!ques || !kp) {
					failedOnes += " @@@(${quest})"
					continue
				}
				ques.inputBy = session.user // will convert to string automatically?
				ques.addToKnowledgePoints(kp) 
				kp.associatedQuestion++
				kp.totalQuestion++
		
				if (!ques.save() || !kp.save()) {
					ques.errors.each {
						println it
					}

					failedNumber++
					failedOnes += " @@@(${quest})"
				} else {
					passedNumber++
				}
			}
			
			flash.message = "Batch ${batchQuestionOpInstance.operation} finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
		}
		
		println "flash.message: ${flash.message}"
		render(view: "create", model: [batchQuestionOpInstance: batchQuestionOpInstance])
	}
		
	def transferYDLJ() {
		
		if (true) {
			
			flash.message = "只提供给开发人员操作，需要修改必要的代码"
			redirect(action: "list")
			return
			
		}
		
			
		def failedNumber = 0
		def passedNumber = 0
		def failedOnes = "Failed:"
		
		String sourceDB = "otsv2_151211"
		String targetDB = "otsv2"
		println "sourceDB: ${sourceDB}"
		println "targetDB: ${targetDB}"
		
		//**** Source DB ****//
		groovy.sql.Sql sourceDBSql = groovy.sql.Sql.newInstance(
			"jdbc:mysql://localhost/${sourceDB}?useUnicode=yes&characterEncoding=UTF-8",
			"ots",
			"welcomeots",
			"com.mysql.jdbc.Driver")
		
		
		//**** Target DB ****//
		groovy.sql.Sql targetDBSql = groovy.sql.Sql.newInstance(
			"jdbc:mysql://localhost/${targetDB}?useUnicode=yes&characterEncoding=UTF-8",
			"ots",
			"welcomeots",
			"com.mysql.jdbc.Driver")
		
		Question.withTransaction { status ->
			println "begin to transfer YDLJ ..."
			sourceDBSql.eachRow("select * from question where type like '阅读理解%'",
				{ p->						
					println "source parent question.id = ${p.id}"			
					Question toSaveParentQuest = new Question( 
							analysis:	p.analysis,
							audio:		p.audio,
							audioType:	p.audio_type,
							description:	p.description,
							errorRate:		p.error_rate,
							img:		p.img,
							imgType:	p.img_type,
							inputBy:	p.input_by,
							inputDate:	new Date(),
							instructions:	p.instructions,
							parentQuestion: null,
							plainText:	p.plain_text,
							qID:		p.qid * -1,
							reviewedBy:	p.reviewed_by,
							source:		p.source,
							term:		p.term,
							totalPracticed:	p.total_practiced,
							type:		p.type,
							weeklyScore:	p.weekly_score,
							weeklyTryout:	p.weekly_tryout,
							difficultyLevel:	p.difficulty_level,
							qrCcode:		p.qr_code,
							qrCodeVideoURL:	p.qr_code_videourl,
							totalScore:	p.total_score
						)
					
					if (toSaveParentQuest.save(failOnError : true)) {
						passedNumber++
						
						// answers for parent question
						sourceDBSql.eachRow("select * from answer where question_id=${p.id}",
							{ a->
								println "\tsource parent question.id = ${p.id}, answer=${a.content}"
								Answer answer = new Answer(
									content:	a.content,
									correct:	a.correct,
									// question :	toSaveParentQuest,
									serialNum:	a.serial_num,
								);
							
								toSaveParentQuest.addToAnswers(answer)
								toSaveParentQuest.save(failOnError : true)
							
								/*if (answer.save(failOnError : true)) {
									passedNumber++
									toSaveParentQuest.addToAnswers(answer)
								}
								else {
									failedNumber++
									failedOnes += answer.errors
									status.setRollbackOnly()
									return
								}*/
															
							}
						);
					
						// question_knowledge_points for parent Question
						sourceDBSql.eachRow("select m.*, k.name name from question_knowledge_points m, knowledge_point k where m.knowledge_point_id=k.id and m.question_id=${p.id}",
							{ qk->
								println "\tsource parent question.id = ${p.id}, knowledge_point=${qk.name}"
								KnowledgePoint kp = KnowledgePoint.findByName(qk.name)
								toSaveParentQuest.addToKnowledgePoints(kp)
								kp.associatedQuestion++
								kp.totalQuestion++
								if (toSaveParentQuest.save()) {
									kp.save()
									passedNumber++
								}
								else {
									failedNumber++
									failedOnes += toSaveParentQuest.errors
									status.setRollbackOnly()
									return
								}
							}
						);
					
					// ===========================================================================================	
					
						// child questions for parent question
						sourceDBSql.eachRow("select * from question where parent_question_id=${p.id}",
							{ c->
								println "\tsource child question.id = ${c.id}"
								Question toSaveChildQuest = new Question(
									analysis:	c.analysis,
									audio:		c.audio,
									audioType:	c.audio_type,
									description:	c.description,
									errorRate:		c.error_rate,
									img:		c.img,
									imgType:	c.img_type,
									inputBy:	c.input_by,
									inputDate:	new Date(),
									instructions:	c.instructions,
									parentQuestion: toSaveParentQuest,
									plainText:	c.plain_text,
									qID:		c.qid * -1,
									reviewedBy:	c.reviewed_by,
									source:		c.source,
									term:		c.term,
									totalPracticed:	c.total_practiced,
									type:		c.type,
									weeklyScore:	c.weekly_score,
									weeklyTryout:	c.weekly_tryout,
									difficultyLevel:	c.difficulty_level,
									qrCcode:		c.qr_code,
									qrCodeVideoURL:	c.qr_code_videourl,
									totalScore:	c.total_score
								)
								
								if (toSaveChildQuest.save(failOnError : true)) {
									passedNumber++
									
									// answers for child question
									sourceDBSql.eachRow("select * from answer where question_id=${c.id}",
										{ a->
											println "\t\tsource child question.id = ${c.id}, answer=${a.content}"
											Answer answer = new Answer(
												content:	a.content,
												correct:	a.correct,
												// question :	toSaveChildQuest,
												serialNum:	a.serial_num,
											);
										
											toSaveChildQuest.addToAnswers(answer)
											toSaveChildQuest.save(failOnError : true)
										
											// by answer.save() will fail if no quesstion specified above, but we can call toSaveChildQuest.addToAnswers(answer)
											/*if (answer.save(failOnError : true)) {
												passedNumber++												
											}
											else {
												failedNumber++
												failedOnes += answer.errors
												status.setRollbackOnly()
												return
											}*/
										}
									);
								
									// question_knowledge_points for child Question
									sourceDBSql.eachRow("select m.*, k.name name from question_knowledge_points m, knowledge_point k where m.knowledge_point_id=k.id and m.question_id=${c.id}",
										{ qk->
											println "\tsource parent question.id = ${c.id}, knowledge_points=${qk.knowledge_point_id}"
											KnowledgePoint kp = KnowledgePoint.findByName(qk.name)
											toSaveChildQuest.addToKnowledgePoints(kp)
											kp.associatedQuestion++
											kp.totalQuestion++
											if (toSaveChildQuest.save()) {
												kp.save()
												passedNumber++
											}
											else {
												failedNumber++
												failedOnes += toSaveChildQuest.errors
												status.setRollbackOnly()
												return
											}
										}
									);
																	
								} else {
									failedNumber++
									failedOnes += toSaveChildQuest.errors
									status.setRollbackOnly()
									return
								}
							}
						);
																	
					} else {
						failedNumber++
						failedOnes += toSaveParentQuest.errors
						status.setRollbackOnly()	
						return											
					}
				}
			);
				
		}		
		
		flash.message = "transfer finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
		redirect(action: "list")
	}

    def save() {
        def batchQuestionOpInstance = new BatchQuestionOp(params)
        if (!batchQuestionOpInstance.save(flush: true)) {
            render(view: "create", model: [batchQuestionOpInstance: batchQuestionOpInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), batchQuestionOpInstance.id])
        redirect(action: "show", id: batchQuestionOpInstance.id)
    }

    def show(Long id) {
        def batchQuestionOpInstance = BatchQuestionOp.get(id)
        if (!batchQuestionOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), id])
            redirect(action: "list")
            return
        }

        [batchQuestionOpInstance: batchQuestionOpInstance]
    }

    def edit(Long id) {
        def batchQuestionOpInstance = BatchQuestionOp.get(id)
        if (!batchQuestionOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), id])
            redirect(action: "list")
            return
        }

        [batchQuestionOpInstance: batchQuestionOpInstance]
    }

    def update(Long id, Long version) {
        def batchQuestionOpInstance = BatchQuestionOp.get(id)
        if (!batchQuestionOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (batchQuestionOpInstance.version > version) {
                batchQuestionOpInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp')] as Object[],
                          "Another user has updated this BatchQuestionOp while you were editing")
                render(view: "edit", model: [batchQuestionOpInstance: batchQuestionOpInstance])
                return
            }
        }

        batchQuestionOpInstance.properties = params

        if (!batchQuestionOpInstance.save(flush: true)) {
            render(view: "edit", model: [batchQuestionOpInstance: batchQuestionOpInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), batchQuestionOpInstance.id])
        redirect(action: "show", id: batchQuestionOpInstance.id)
    }

    def delete(Long id) {
        def batchQuestionOpInstance = BatchQuestionOp.get(id)
        if (!batchQuestionOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), id])
            redirect(action: "list")
            return
        }

        try {
            batchQuestionOpInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp'), id])
            redirect(action: "show", id: id)
        }
    }
	
}
