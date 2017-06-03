package ots

class CvtqTagLib {
	def cvtq = { attrs ->
		def question = attrs.question
		def quizQuestionRecord = attrs.quizQuestionRecord
		def showChecks = attrs.showChecks
		def showKP = attrs.showKP
		def correctAns

		if (!question) {
			question = quizQuestionRecord.question
		}

		// 是否已经作答，-1 :还没有答题，1:答题而且答案正确，0:答题但是答案错误
		int questionRsMark = -1
		String rsCkMsg = ""
		String descHTMLWithUserAnswers = question.description
		
		// 文言文 convert [xx] to underline <u>xx</u>
		descHTMLWithUserAnswers = descHTMLWithUserAnswers.replaceAll("\\[", "<u>&nbsp;<strong>").replaceAll("\\]", "</strong>&nbsp;</u>")

		Iterator<Answer> ansIt =  question.answers.iterator()

		def showChecks_org = showChecks
		def showKP_org = showKP
		while(ansIt.hasNext()){
			Answer answer = (Answer)ansIt.next()
			if (answer.correct){
				correctAns = answer.serialNum
			}
			
			String answerByUser = ""
			StuAnswer stuAnswer = null
			if (quizQuestionRecord) {
				if (question.type == CONSTANT.RADIO_QUESTION) {
					stuAnswer = StuAnswer.findByQuizQuestionRecord(quizQuestionRecord)
				} else {
					stuAnswer = StuAnswer.findByQuizQuestionRecordAndRefAnswer(quizQuestionRecord, answer)
				}
				answerByUser = stuAnswer?.userInput
				
				if (quizQuestionRecord?.quiz?.status == CONSTANT.QUIZ_STATUS_NOTBEGIN) {
					showChecks = false;
					showKP = false;
				}
			}
			
			// for questions have no student's answer, don't showChecks and KPs in case it's hasn't been answered by student
//			if (!answerByUser && questionRsMark == -1) {
//				showChecks = false;
//				showKP = false;
//			}
						
			// revoke the set to original values if student has answered the question
			if (questionRsMark != -1) {
				showChecks = showChecks_org;
				showKP = showKP_org;
			}			
			
			if (question.type == CONSTANT.RADIO_QUESTION) {
				// 选择题
				if (!answerByUser && params['uaq'+question.id]) {
					answerByUser = params['uaq'+question.id]
				}
				answerByUser = answerByUser == null?"":answerByUser

				String checked = ""
				if (answerByUser == ""+answer.id) {
					checked = "checked"
				}
				// specify required='required' if all questions need to be answered mandatory
				descHTMLWithUserAnswers += "<ol class='property-value' ><input type='radio' "+checked+" name='uaq"+question.id+"' required='required' value='"+answer.id+"' />"
				descHTMLWithUserAnswers += "&nbsp;&nbsp;"+answer.showInQuestion()+"</ol>"
			}
			else if (question.type == CONSTANT.FBLANK_QUESTION){ // 填空题
				if (!answerByUser && params['answerByUser'+answer.id]) {
					answerByUser = params['answerByUser'+answer.id]
				}
				answerByUser = answerByUser == null?"":answerByUser

				descHTMLWithUserAnswers = descHTMLWithUserAnswers.toString().replaceFirst("____", "<input type='text' class='qblank' name='answerByUser"+answer.id+"' value='"+answerByUser.encodeAsHTML()+"'/>")
			}

			if (showChecks) { // && stuAnswer // 有些填空题学生只答了部分空，没有答的空stuAnswer是空的，但是系统还是会提示该空错误并给出正确答案参考
				if(stuAnswer?.correct && answer.correct) {
					// if previous answered wrongly, treat the whole question answered wrongly, no need to reset questionRsMark
					if (questionRsMark != 0) {
						questionRsMark = 1
					}
					rsCkMsg += "<p> <font color=green>&nbsp;&nbsp;&nbsp; - Correct!"
					if (question.type == CONSTANT.FBLANK_QUESTION){
						rsCkMsg += " at #${answer.serialNum}"
					}
				}
				else {
					if (question.type == CONSTANT.RADIO_QUESTION && ""+answerByUser != ""+answer.id){
						// do nothing. 对于单选题，选中的那个题目是 answerByUser==answer.id的
					} else {
						questionRsMark = 0
						rsCkMsg += "<p> <font color=red>&nbsp;&nbsp;&nbsp; - Wrong!"
						if (question.type == CONSTANT.FBLANK_QUESTION){
							def correctAnswer = answer.content
							// Not Regular Expression for contains() 
							if (correctAnswer.contains("||")) {
								// Regular Expression for replaceAll()
								correctAnswer = correctAnswer.replaceAll("\\|\\|", " 或 ")
							}
							rsCkMsg += " at #${answer.serialNum} (your answer is:"+answerByUser+", the correct answer is: ${correctAnswer})"
						}
					}
				}
			}

			if (question.type == CONSTANT.FBLANK_QUESTION && descHTMLWithUserAnswers.indexOf("____") < 0)
			{
				break
			}
		}
		
		if (showChecks && questionRsMark != 1 && question.type != CONSTANT.FBLANK_QUESTION) {
			rsCkMsg += " The correct answer is: ${correctAns}</font>"
		}

		descHTMLWithUserAnswers = descHTMLWithUserAnswers.replaceAll("\n","<br>")
		out << descHTMLWithUserAnswers
		if (rsCkMsg)  {
			out << rsCkMsg
		}

		String msgKP = ""
		if (showKP) {
			msgKP += "<br> <font color=green size='-1'> --------------- <i>相关知识点  </i> ---------------</font>" // (鼠标悬浮知识点名称可查看知识点更详细内容)
			question.knowledgePoints.eachWithIndex { kp, i ->
				msgKP += "<li data-toggle='tooltip' data-placement='right' title='"+kp.content.encodeAsHTML()+"'><font color=blue size='-1'>${kp}</font>"
				// keep question knowledgePoints for student
				KnowledgeCheckPoint kcpHasRecentCorrectRate = KnowledgeCheckPoint.findByKnowledgeAndStudentAndRecentCorrectRateGreaterThan(kp, quizQuestionRecord.quiz.student, -1, [sort:'id', order:'desc'])

				if (kcpHasRecentCorrectRate) {
					def rate = new java.text.DecimalFormat("##.#").format(100*kcpHasRecentCorrectRate.recentCorrectRate)
					msgKP += " <font color='orange' size='-1'>&nbsp;[近期成绩:<i>${rate}%</i>]</font>"
				} else {
					msgKP += " <font color='orange' size='-1'>&nbsp;[近期成绩: - ]</font>"
				}
				msgKP+"</li>"
			}
		}

		// don't show KP msg if user answer is correct
		if (showKP && questionRsMark != 1) {
			out << msgKP
		}
	}
}
