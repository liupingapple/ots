package ots

import org.springframework.dao.DataIntegrityViolationException
import java.text.SimpleDateFormat

class BatchOperationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [batchOperationInstanceList: BatchOperation.list(params), batchOperationInstanceTotal: BatchOperation.count()]
    }

    def create() {
        [batchOperationInstance: new BatchOperation(params)]
    }

    def save() {
        def batchOperationInstance = new BatchOperation(params)
        if (!batchOperationInstance.save(flush: true)) {
            render(view: "create", model: [batchOperationInstance: batchOperationInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), batchOperationInstance.id])
        redirect(action: "show", id: batchOperationInstance.id)
    }

    def show(Long id) {
        def batchOperationInstance = BatchOperation.get(id)
        if (!batchOperationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), id])
            redirect(action: "list")
            return
        }

        [batchOperationInstance: batchOperationInstance]
    }

    def edit(Long id) {
        def batchOperationInstance = BatchOperation.get(id)
        if (!batchOperationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), id])
            redirect(action: "list")
            return
        }

        [batchOperationInstance: batchOperationInstance]
    }

    def update(Long id, Long version) {
        def batchOperationInstance = BatchOperation.get(id)
        if (!batchOperationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (batchOperationInstance.version > version) {
                batchOperationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'batchOperation.label', default: 'BatchOperation')] as Object[],
                          "Another user has updated this BatchOperation while you were editing")
                render(view: "edit", model: [batchOperationInstance: batchOperationInstance])
                return
            }
        }

        batchOperationInstance.properties = params

        if (!batchOperationInstance.save(flush: true)) {
            render(view: "edit", model: [batchOperationInstance: batchOperationInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), batchOperationInstance.id])
        redirect(action: "show", id: batchOperationInstance.id)
    }

    def delete(Long id) {
        def batchOperationInstance = BatchOperation.get(id)
        if (!batchOperationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), id])
            redirect(action: "list")
            return
        }

        try {
            batchOperationInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'batchOperation.label', default: 'BatchOperation'), id])
            redirect(action: "show", id: id)
        }
    }
	
	def executeBatch() {
		def batchOperationInstance = new BatchOperation(params)
		if (batchOperationInstance.batchOperation == "Clean up Questions") {
			def deletedNumber = 0
			def quesIndex = 0
			def results = Question.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Deleting...\n"
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++
				if (quesIndex < batchOperationInstance.startIndex) {
					continue
				} else if (quesIndex > batchOperationInstance.endIndex) {
					break
				}
				sb << "${ques.id} - ${ques.type}\n"
				
				Iterator<KnowledgePoint> kpIt =  ques.knowledgePoints.iterator();
				while(kpIt.hasNext()){
					KnowledgePoint kp = (KnowledgePoint)kpIt.next();
					kp.associatedQuestion--
					kp.totalQuestion--
					kp.save()
				}
				
				ques.delete()
				deletedNumber++
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} questions deleted!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}
	
		} else if (batchOperationInstance.batchOperation == "Regenerate Question ID") {
			def generatedNumber = 0
			def quesIndex = 0
			def results = Question.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Generating...\n"
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++
				if (quesIndex < batchOperationInstance.startIndex) {
					continue
				} else if (quesIndex > batchOperationInstance.endIndex) {
					break
				}
				
				ques.qID = quesIndex
				ques.save()
				generatedNumber++
				sb << "${ques.id} - ${ques.qID}\n"
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${generatedNumber} questions' qID generated!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}
	
		} else if (batchOperationInstance.batchOperation == "Export Question Parent/Child Relationship") {
			def exportedNumber = 0
			def quesIndex = 0
			def results = Question.createCriteria().list {
				order("qID", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++
				if (quesIndex < batchOperationInstance.startIndex) {
					continue
				} else if (quesIndex > batchOperationInstance.endIndex) {
					break
				}
				
				sb << ques.qID << "\t" << ques.parentQuestion?.qID 
				Iterator<Question> subQIt =  ques.childQuestions.iterator();
				while(subQIt.hasNext()){
					Question subQ = (Question)subQIt.next();
					sb << "\t" << subQ.qID
				}
				sb << "\n"
				exportedNumber++
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${exportedNumber} questions exported!"
				
		} else if (batchOperationInstance.batchOperation == "Import Question Parent/Child Relationship") {
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			StringBuilder sb = new StringBuilder("Passed Ones:\n")
			StringBuilder failedOnes = new StringBuilder("Failed Ones:\n")
	
			def itemList = batchOperationInstance.batchInput.split("\n")
			for (items in itemList) {
				def fields = items.trim().split("\t")
				long currentID = Long.parseLong(fields[0])
				Question ques = Question.findByQID(currentID)
				if (fields[1] && fields[1] != "null") {
					ques.parentQuestion = Question.findByQID(Long.parseLong(fields[1]))
				} else {
					ques.parentQuestion = null
				}
				for (int i=2; i<=fields.size()-1; i++) {
					ques.addToChildQuestions(Question.findByQID(Long.parseLong(fields[i])))
				}
				
				if (!ques.save()) {
					ques.errors.each {
						println it
					}
					failedNumber++
					failedOnes += "${items}"
				} else {
					passedNumber++
					sb << items
				}
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${passedNumber} Questions have been imported!\n Pass = ${passedNumber}; Fail = ${failedNumber}.\n"
			if (failedOnes.toString() != "Failed Ones:\n") {
				batchOperationInstance.batchResult += failedOnes.toString()
			}
				
		} else if (batchOperationInstance.batchOperation == "Clean up Questions with no mapped Knowledge Points") { 
			def deletedNumber = 0
			def quesIndex = 0
			def results = Question.createCriteria().list {
				isEmpty("knowledgePoints")
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Deleting...\n"
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++
				if (quesIndex < batchOperationInstance.startIndex) {
					continue
				} else if (quesIndex > batchOperationInstance.endIndex) {
					break
				}
				sb << "${ques.id} - ${ques.type}\n"
				
				Iterator<KnowledgePoint> kpIt =  ques.knowledgePoints.iterator();
				while(kpIt.hasNext()){
					KnowledgePoint kp = (KnowledgePoint)kpIt.next();
					kp.associatedQuestion--
					kp.totalQuestion--
					kp.save()
				}
				
				ques.delete()
				deletedNumber++
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} questions deleted!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}
			
		} else if (batchOperationInstance.batchOperation == "Update total Questions for Knowledge Points") {
			def updatedNumber = 0
			def mappingForKP = [:]
			def kpFamily = [:]
			def validKPFamily = [:]
			def results = KnowledgePoint.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Updating...\n"
			
			for ( i in 1..batchOperationInstance.traverseDepth ) {
				def kpIndex = 0
				Iterator<KnowledgePoint> kpIt =  results.iterator();
				while(kpIt.hasNext()){
					KnowledgePoint kp = (KnowledgePoint)kpIt.next();
					kpIndex++
					if (kpIndex < batchOperationInstance.startIndex) {
						continue
					} else if (kpIndex > batchOperationInstance.endIndex) {
						break
					}
					
					if (!mappingForKP[kp.id]) {
						mappingForKP[kp.id] = new HashSet()
						kp.questions?.each {mappingForKP[kp.id].add(it.id)}
					}
					
					if (!kpFamily[kp.id]) {
						kpFamily[kp.id] = new HashSet()
						kpFamily[kp.id].add(kp.id)
					}

					if (!validKPFamily[kp.id]) {
						validKPFamily[kp.id] = new HashSet()
						if (kp.totalQuestion > 0) {
							validKPFamily[kp.id].add(kp.id)
						}
					}

					kp.childPoints?.each {
						if (mappingForKP[it.id]) {
							mappingForKP[kp.id].addAll(mappingForKP[it.id])
						}
						if (kpFamily[it.id]) {
							kpFamily[kp.id].addAll(kpFamily[it.id])
						}
						if (validKPFamily[it.id]) {
							validKPFamily[kp.id].addAll(validKPFamily[it.id])
						}
					}
								
					if (i == batchOperationInstance.traverseDepth && updatedNumber < batchOperationInstance.endIndex &&
						(kp.totalQuestion < mappingForKP[kp.id].size() ||
						 kp.familySize < kpFamily[kp.id].size() ||
					 	 kp.degree * 100 + kp.masterRatio < validKPFamily[kp.id].size())) {
						sb << "${kp.id} - ${kp.name} - "
						sb << "${mappingForKP[kp.id].size()} - ${kpFamily[kp.id].size()} - ${validKPFamily[kp.id].size()} -"
						sb << "${kp.totalQuestion} - ${kp.familySize} - ${kp.masterRatio}\n"
						kp.totalQuestion = mappingForKP[kp.id].size()
						kp.familySize = kpFamily[kp.id].size()
						if (validKPFamily[kp.id].size() < 1100) {
							kp.degree = validKPFamily[kp.id].size() / 100
							kp.masterRatio = validKPFamily[kp.id].size() % 100
						} else {
							kp.degree = 10
							kp.masterRatio = 100
						}
						kp.save(flush: true)
						updatedNumber++
					}
				}
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${updatedNumber} knowledge points updated!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Clean up Knowledge Points") {
			def deletedNumber = 0
			def kpIndex = 0
			def results = KnowledgePoint.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Deleting...\n"
			Iterator<KnowledgePoint> kpIt =  results.iterator();
			while(kpIt.hasNext()){
				KnowledgePoint kp = (KnowledgePoint)kpIt.next();
				kpIndex++
				if (kpIndex < batchOperationInstance.startIndex) {
					continue
				} else if (kpIndex > batchOperationInstance.endIndex) {
					break
				}
				sb << "${kp.id} - ${kp.name}\n"
				kp.parentPoints.removeAll{it}
				kp.childPoints.removeAll{it}
				kp.delete()
				deletedNumber++
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} knowledge points deleted!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Clean up Knowledge Point Checkpoints") {
			def deletedNumber = 0
			def stuIndex = 0
			int actionCounter = 0
			StringBuilder sb = new StringBuilder()
			sb << "Total KP Checkpoints number: " + KnowledgeCheckPoint.count()
			sb << "\nDeleting...\n"
			
			def itemList = [batchOperationInstance.batchInput]
			if (batchOperationInstance.batchInput?.contains("\n")) {
				itemList = batchOperationInstance.batchInput.split("\n")
			}
			
			for (userName in itemList) {
				Student stu = Student.findByUserName(userName.trim())
				if (!stu) {break}

				stuIndex++
				if (stuIndex < batchOperationInstance.startIndex) {
					continue
				} else if (stuIndex > batchOperationInstance.endIndex) {
					break
				}
				sb << "${stu.id} - ${stu.userName}\n"
				stu.kcps.removeAll{it}
				stu.save()
				
				def kcpList = KnowledgeCheckPoint.where {student == stu}.list()
				
				kcpList.each{kcp -> 
					if (actionCounter < batchOperationInstance.endIndex) {
						def tmp=[]
						kcp.records.each { tmp << it }
						tmp.each { 
							kcp.removeFromRecords(it)
							actionCounter++
							// kcp.save()
						}
						kcp.delete()
					}
				}
				
				deletedNumber++
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "The knowledge point checkpoints for ${deletedNumber} students have been cleaned up!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Clean up Knowledge Points with no associated Questions") {
			def deletedNumber = 0
			def kpIndex = 0
			def results = KnowledgePoint.createCriteria().list {
				and {
					isEmpty("childPoints")
					eq("associatedQuestion", 0)
				}
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Deleting...\n"
			Iterator<KnowledgePoint> kpIt =  results.iterator();
			while(kpIt.hasNext()){
				KnowledgePoint kp = (KnowledgePoint)kpIt.next();
				kpIndex++
				if (kpIndex < batchOperationInstance.startIndex) {
					continue
				} else if (kpIndex > batchOperationInstance.endIndex) {
					break
				}
				
				Iterator<KnowledgePoint> kParentIt =  kp.parentPoints?.iterator();
				while(kParentIt?.hasNext()){
					KnowledgePoint kParent = (KnowledgePoint)kParentIt.next();
					kParent.removeFromChildPoints(kp)
					kParent.save()
				}
		
				sb << "${kp.id} - ${kp.name}\n"
				kp.parentPoints.removeAll{it}
				kp.childPoints.removeAll{it}
				kp.delete()
				deletedNumber++
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} knowledge points deleted!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Clean up mappings between Question and Knowledge Points") {
			def deletedNumber = 0
			def quesIndex = 0
			def results = Question.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Cleaning up...\n"
			Iterator<Question> quesIt =  results.iterator();
			while(quesIt.hasNext()){
				Question ques = (Question)quesIt.next();
				quesIndex++
				if (quesIndex < batchOperationInstance.startIndex) {
					continue
				} else if (quesIndex > batchOperationInstance.endIndex) {
					break
				}

				if (ques.knowledgePoints) {
					Iterator<KnowledgePoint> kpIt =  ques.knowledgePoints?.iterator();
					while(kpIt?.hasNext()){
						KnowledgePoint kp = (KnowledgePoint)kpIt.next();
						kp.associatedQuestion--
						kp.totalQuestion--
						kp.save()
					}

					ques.knowledgePoints.removeAll{it}
					println ques.knowledgePoints
					
					if (!ques.save(flush: true)) {
						ques.errors.each {
							println it
						}
					} else {
						deletedNumber++
						sb << "${ques.id} - ${ques.type}\n"
					}
				}
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} questions cleaned up!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Clean up parent/child relationships among Knowledge Points") {
			def deletedNumber = 0
			def kpIndex = 0
			def results = KnowledgePoint.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Cleaning up...\n"
			Iterator<KnowledgePoint> kpIt =  results.iterator();
			while(kpIt.hasNext()){
				KnowledgePoint kp = (KnowledgePoint)kpIt.next();
				kpIndex++
				if (kpIndex < batchOperationInstance.startIndex) {
					continue
				} else if (kpIndex > batchOperationInstance.endIndex) {
					break
				}
				
				if (kp.parentPoints || kp.childPoints) {
					kp.parentPoints.removeAll{it}
					kp.childPoints.removeAll{it}
					kp.save()
					sb << "${kp.id} - ${kp.name}\n"
					deletedNumber++
				}
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} knowledge points cleaned up!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}
			
		} else if (batchOperationInstance.batchOperation == "Clean up QuizQuestionRecord") {
			def deletedNumber = 0
			def kpIndex = 0
			def results = QuizQuestionRecord.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Deleting...\n"
			Iterator<QuizQuestionRecord> kpIt =  results.iterator();
			while(kpIt.hasNext()){
				QuizQuestionRecord kp = (QuizQuestionRecord)kpIt.next();
				kpIndex++
				if (kpIndex < batchOperationInstance.startIndex) {
					continue
				} else if (kpIndex > batchOperationInstance.endIndex) {
					break
				}
				sb << "${kp.id}\n"
				kp.delete()
				deletedNumber++
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} QuizQuestionRecords deleted!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Clean up Quiz") {

			def deletedNumber = 0
			def kpIndex = 0
			def results = Quiz.createCriteria().list {
				order("id", "asc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "Deleting...\n"
			Iterator<Quiz> kpIt =  results.iterator();
			while(kpIt.hasNext()){
				Quiz kp = (Quiz)kpIt.next();
				kpIndex++
				if (kpIndex < batchOperationInstance.startIndex) {
					continue
				} else if (kpIndex > batchOperationInstance.endIndex) {
					break
				}
				sb << "${kp.id}\n"
				kp.delete()
				deletedNumber++
			}
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${deletedNumber} Quiz deleted!"
		
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Export the K Values") {

			def quizNumber = 0
			StringBuilder sb = new StringBuilder()
			sb << "Assignment\tQuiz\tK Value\tAccumulated K Value\n"

			def itemList = [batchOperationInstance.batchInput]
			if (batchOperationInstance.batchInput?.contains("\n")) {
				itemList = batchOperationInstance.batchInput.split("\n")
			}
			
			for (userName in itemList) {
				Student stu = Student.findByUserName(userName.trim())
				if (!stu) {break}

				def assignmentResult = AssignmentStatus.where{student == stu}.list()
				
				assignmentResult.each{ aStatus ->
					def quizResults = Quiz.createCriteria().list {
						eq("student", stu)
						eq("assignment", aStatus.assignment)
						order("answeredDate", "asc")
					}
					
					def quizKSum = 0
					Iterator<Quiz> quizIt =  quizResults.iterator()
					while(quizIt.hasNext()){
						Quiz quiz = (Quiz)quizIt.next()
						
						if (quiz.reviewComment) {
							quizKSum += Integer.parseInt(quiz.reviewComment)
							sb << "${aStatus.assignment}\t${quiz.name}\t${quiz.reviewComment}\t" + quizKSum + "\n"
							quizNumber++
						}
					}

				}
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${quizNumber} Quizs have K Value!"

		} else if (batchOperationInstance.batchOperation == "Resort Quiz Names") {
		
			def quizNumber = 0
			StringBuilder sb = new StringBuilder()
			sb << "Updating...\n"

			def itemList = batchOperationInstance.batchInput.split("\n")
			for (userName in itemList) {
				Student stu = Student.findByUserName(userName.trim())
				if (!stu) {break}

				def assignmentResult = AssignmentStatus.where{student == stu}.list()
				
				assignmentResult.each{ aStatus ->
					def quizResults = Quiz.createCriteria().list {
						eq("student", stu)
						eq("assignment", aStatus.assignment)
						order("answeredDate", "asc")
					}
					
					def quizIndex = 1
					Iterator<Quiz> quizIt =  quizResults.iterator()
					while(quizIt.hasNext()){
						Quiz quiz = (Quiz)quizIt.next()
						
						if (quiz.name != "${quiz.assignment}练习" + quizIndex) {
							sb << "${stu} - ${quiz.name} --> ${quiz.assignment}练习" + quizIndex + "\n"
							quiz.name = "${quiz.assignment}练习" + quizIndex
							quiz.save()
							quizNumber++
						} else {
							sb << "${stu} - ${quiz.name}\n"
						}
						
						quizIndex++
					}
					
					sb << "\n"
				}
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${quizNumber} Quizs updated!"
			
		} else if (batchOperationInstance.batchOperation == "Remove Teacher Accounts") {
		
					def deletedNumber = 0
					StringBuilder sb = new StringBuilder()
					sb << "Removing teachers...\n"
					
					def itemList = [batchOperationInstance.batchInput]
					if (batchOperationInstance.batchInput?.contains("\n")) {
						itemList = batchOperationInstance.batchInput.split("\n")
					}
					
					for (userName in itemList) {
						def userList = userName.trim().split("\t")
						def teacherName = userList[0]
						def replaceName = "te" 
						if (userList.size() > 1) {
							replaceName = userList[1]
						}
						Teacher teacher = Teacher.findByUserName(teacherName)
						if (!teacher) {
							sb << "Can't find teacher with user name: ${teacherName}!\n"
							continue
						}
						
						if (teacher.students) {
							sb << "Can't remove teacher with user name: ${teacherName}! The teacher still has the following students: \n"
							teacher.students.each {
								sb << "${it.userName}/${it.password}\n"
							}
							continue
						}
						
						teacher.delete(flush: true)
						def query = Assignment.where {
							assignedBy == teacherName
						}
						int totalUpdate = query.updateAll(assignedBy:replaceName)
						deletedNumber += 1
						sb << "\nThe teacher: ${teacherName} has been removed! ${totalUpdate} assignments have been put under $replaceName\n"

					}
						
					batchOperationInstance.batchOutput = sb.toString()
					batchOperationInstance.batchResult = "${deletedNumber} teachers have been removed!"
		
		} else if (batchOperationInstance.batchOperation == "Generate Assignment Templates") {
			
			StringBuilder sb = new StringBuilder()
			sb << "[ID]\tTemplate Name\tCreated By\tKnowledge Points...\n"
			ots.KnowledgePoint.list(max: 10000, sort: 'totalQuestion', order: 'desc').eachWithIndex {item, i ->
				sb << "[${i}]\t"
				sb << "${item.name} (节点数:${item.familySize} 题数:${item.totalQuestion})\t"
				sb << "系统\t"
				sb << "[${item.name}]\n"
			}
			
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "AssignmentTemplate have been exported!"

			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Export Assignment Templates") {
		
			def itemIndex = 0
			def exportCount = 0
			def c = AssignmentTemplate.createCriteria()
			def results = c.list {
				maxResults(10000)
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[ID]\tTemplate Name\tCreated By\tKnowledge Points...\n"
			def itemIt =  results.iterator();
			while(itemIt.hasNext()){
				def item = itemIt.next();
				itemIndex++;
				if (itemIndex < batchOperationInstance.startIndex) {
					continue
				} else if (itemIndex > batchOperationInstance.endIndex) {
					break
				}
				exportCount++;
				sb << "[${item.id}]\t"
				sb << "${item.templateName}\t"
				sb << "${item.createdBy}"
				item.knowledgePoints.each{sb << "\t[${it.name}]"}
				sb << "\n"
			}
			
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${exportCount} AssignmentTemplate have been exported!"

			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}
			
		} else if (batchOperationInstance.batchOperation == "Import Assignment Templates") {
		
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			StringBuilder sb = new StringBuilder("Passed Ones:\n")
			StringBuilder failedOnes = new StringBuilder("Failed Ones:\n")
	
			def itemList = batchOperationInstance.batchInput.split("\n")
			for (items in itemList) {
				if (items.contains("[ID]")) {
					continue
				}
				def fields = items.trim().split("\t")
				if (fields.size() < 4) {
					continue
				}
				
				def currentID = fields[0]
				def newItem = new AssignmentTemplate()
				newItem.templateName = fields[1]
				newItem.createdBy = fields[2]
				for (i in 3..fields.size()-1) {
					KnowledgePoint kp = KnowledgePoint.findByName(fields[i].replace('[', '').replace(']', ''))
					if (kp) {
						newItem.addToKnowledgePoints(kp)
					} else {
						println "KP not found for Name: ${fields[i]}"
					}
				}
				
				if (!newItem.save()) {
					newItem.errors.each {
						println it
					}
					failedNumber++
					failedOnes << "${fields[0]}" // items
				} else {
					passedNumber++
					sb << items
				}
				
				batchOperationInstance.batchOutput = sb.toString()
				batchOperationInstance.batchResult = "${passedNumber} AssignmentTemplate have been imported!\n Pass = ${passedNumber}; Fail = ${failedNumber}.\n"
				if (failedOnes.toString() != "Failed Ones:\n") {
					batchOperationInstance.batchResult += failedOnes.toString()
				}
				
				if (!batchOperationInstance.save(flush: true)) {
					flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
					render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				}
			}
			
		} else if (batchOperationInstance.batchOperation == "Export Assignments") {
		
			def itemIndex = 0
			def exportCount = 0
			def c = Assignment.createCriteria()
			def results = c.list {
				maxResults(10000)
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[ID]\tName\tAssigned By\tTime Limit\tQuestion Limit\tAssignment Template\n"
			def itemIt =  results.iterator();
			while(itemIt.hasNext()){
				def item = itemIt.next();
				itemIndex++;
				if (itemIndex < batchOperationInstance.startIndex) {
					continue
				} else if (itemIndex > batchOperationInstance.endIndex) {
					break
				}
				exportCount++;
				sb << "[${item.id}]\t"
				sb << "${item.name}\t"
				sb << "${item.assignedBy?:''}\t"
				sb << "${item.timeLimit}\t"
				sb << "${item.questionLimit}\t"
				sb << "${item.comment?:''}"
				item.templates.each{sb << "\t${it.templateName}"}
				sb << "\n"
			}
			
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${exportCount} Assignment have been exported!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}
			
		} else if (batchOperationInstance.batchOperation == "Import Assignments") {
		
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			StringBuilder sb = new StringBuilder("Passed Ones:\n")
			StringBuilder failedOnes = new StringBuilder("Failed Ones:\n")
	
			def itemList = batchOperationInstance.batchInput.split("\n")
			for (items in itemList) {
				if (items.contains("[ID]")) {
					continue
				}
				def fields = items.trim().split("\t")
				def currentID = fields[0]
				def newItem = new Assignment()
				newItem.name = fields[1]
				newItem.assignedBy = fields[2]
				newItem.timeLimit = fields[3]
				newItem.questionLimit = fields[4]
				newItem.comment = fields[5]
				for (i in 6..fields.size()-1) {
					newItem.addToTemplates(AssignmentTemplate.findByTemplateName(fields[i]))
				}
				
				if (!newItem.save()) {
					newItem.errors.each {
						println it
					}
					failedNumber++
					failedOnes += "${items}"
				} else {
					passedNumber++
					sb << items
				}
				
				batchOperationInstance.batchOutput = sb.toString()
				batchOperationInstance.batchResult = "${passedNumber} AssignmentTemplate have been imported!\n Pass = ${passedNumber}; Fail = ${failedNumber}.\n"
				if (failedOnes.toString() != "Failed Ones:\n") {
					batchOperationInstance.batchResult += failedOnes.toString()
				}
				
				if (!batchOperationInstance.save(flush: true)) {
					flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
					render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				}
			}
	
		} else if (batchOperationInstance.batchOperation == "Formalize Knowledge Points Names") {

			def itemIndex = 0
			def exportCount = 0
			def c = KnowledgePoint.createCriteria()
			def results = c.list {
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[ID]\t[Original Name]\t[Updated Name][Original Content]\t[Updated Content]\n"
			def itemIt =  results.iterator();
			while(itemIt.hasNext()){
				def item = itemIt.next();
				itemIndex++;
				if (itemIndex < batchOperationInstance.startIndex) {
					continue
				} else if (itemIndex > batchOperationInstance.endIndex) {
					break
				}
				
				if (item.name.contains("[") || item.name.contains("]") || item.name.contains("^\"") || item.name.contains("\"\$") || item.content == "null") {
					exportCount++;
					sb << "[${item.id}]\t"
					sb << "[${item.name}]\t"
					
					item.name = item.name.replaceAll("^\"|\"\$", "").replace('[', ':').replace(']', '') 
					sb << "[${item.name}]\t"
					sb << "[${item.content}]\t"
					
					if (item.content == "null") {
						item.content = null
					}
					if (item.content) {
						sb << "[${item.content}]\n"
					} else {
						sb << "\n"
					}
					
					if (!item.save()) {
						item.errors.each {
							sb << it
						}
					} 
				}
			}
			
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${exportCount} AdminUser have been updated!"
			
		} else if (batchOperationInstance.batchOperation == "Export Knowledge Points Statistics") {

			def itemIndex = 0
			def exportCount = 0
			def c = KnowledgePoint.createCriteria()
			def results = c.list {
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[ID]\t[Knowledge Point]\tFamilySize\tTotalQuestions\tAssociatedQuestion\tDegree\tMasterRatio\n"
			def itemIt =  results.iterator();
			while(itemIt.hasNext()){
				def item = itemIt.next();
				itemIndex++;
				if (itemIndex < batchOperationInstance.startIndex) {
					continue
				} else if (itemIndex > batchOperationInstance.endIndex) {
					break
				}
				exportCount++;
				sb << "[${item.id}]\t"
				sb << "[${item.name}]\t"
				sb << "${item.familySize}\t"
				sb << "${item.totalQuestion}\t"
				sb << "${item.associatedQuestion}\t"
				sb << "${item.degree}\t"
				sb << "${item.masterRatio}\n"
			}
			
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${exportCount} Knowledge Points have been exported!"
			
		} else if (batchOperationInstance.batchOperation == "Export AdminUser") {
		
			def itemIndex = 0
			def exportCount = 0
			def c = AdminUser.createCriteria()
			def results = c.list {
				maxResults(10000)
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "[ID]\tUser Name\tPassword\tFull Name\tEmail\tRole\n"
			def itemIt =  results.iterator();
			while(itemIt.hasNext()){
				def item = itemIt.next();
				itemIndex++;
				if (itemIndex < batchOperationInstance.startIndex) {
					continue
				} else if (itemIndex > batchOperationInstance.endIndex) {
					break
				}
				exportCount++;
				sb << "[${item.id}]\t"
				sb << "${item.userName}\t"
				sb << "${item.password}\t"
				sb << "${item.fullName}\t"
				sb << "${item.email}\t"
				sb << "${item.role}\n"
			}
			
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${exportCount} AdminUser have been exported!"
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Import Knowledge Points Statistics") {
		
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			StringBuilder sb = new StringBuilder("Passed Ones:\n")
			StringBuilder failedOnes = new StringBuilder("Failed Ones:\n")
	
			def itemList = batchOperationInstance.batchInput.split("\n")
			for (items in itemList) {
				if (items.contains("[ID]")) {
					continue
				}
				def fields = items.trim().split("\t")
				KnowledgePoint kp = KnowledgePoint.findByName(fields[1].replace('[', '').replace(']', ''))
				if (!kp) {
					failedNumber++
					failedOnes << "${items}"
				} else {
					kp.familySize = Integer.parseInt(fields[2])
					kp.totalQuestion = Integer.parseInt(fields[3])
					kp.associatedQuestion = Integer.parseInt(fields[4])
					kp.degree = Integer.parseInt(fields[5])
					kp.masterRatio = Integer.parseInt(fields[6])
					
					if (!kp.save()) {
						kp.errors.each {
							println it
						}
						failedNumber++
						failedOnes << "${items}"
					} else {
						passedNumber++
						sb << items
					}
				}
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${passedNumber} Knowledge Points have been imported!\n Pass = ${passedNumber}; Fail = ${failedNumber}.\n"
			if (failedOnes.toString() != "Failed Ones:\n") {
				batchOperationInstance.batchResult += failedOnes.toString()
			}
			
		} else if (batchOperationInstance.batchOperation == "Import AdminUser") {
		
			def failedNumber = 0
			def passedNumber = 0
			def indexNum = 1
			StringBuilder sb = new StringBuilder("Passed Ones:\n")
			StringBuilder failedOnes = new StringBuilder("Failed Ones:\n")

			def itemList = batchOperationInstance.batchInput.split("\n")
			for (items in itemList) {
				if (items.contains("[ID]")) {
					continue
				}
				def fields = items.trim().split("\t")
				def currentID = fields[0]
				def newItem = new AdminUser()
				newItem.userName = fields[1]
				newItem.password = fields[2]
				newItem.fullName = fields[3]
				newItem.email = fields[4]
				newItem.role = fields[5]
				
				if (!newItem.save()) {
					newItem.errors.each {
						println it
					}
					failedNumber++
					failedOnes += "${items}"
				} else {
					passedNumber++
					sb << items
				}
			}
				
			batchOperationInstance.batchOutput = sb.toString()
			batchOperationInstance.batchResult = "${passedNumber} AdminUser have been imported!\n Pass = ${passedNumber}; Fail = ${failedNumber}.\n"
			if (failedOnes.toString() != "Failed Ones:\n") {
				batchOperationInstance.batchResult += failedOnes.toString()
			}
			
			if (!batchOperationInstance.save(flush: true)) {
				flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
				render(view: "create", model: [batchOperationInstance: batchOperationInstance])
				return
			}

		} else if (batchOperationInstance.batchOperation == "Load Sample Knowledge Node Content") {
			flash.message = ""
			loadKnowledgeNodeContent()
		} else if (batchOperationInstance.batchOperation == "Load Sample Knowledge Parent Relationship") {
			flash.message = ""
			loadKnowledgeParentRelationship()
		} else if (batchOperationInstance.batchOperation == "Load Sample Questions (单选)") {
			flash.message = ""
			loadQuestions()
		} else if (batchOperationInstance.batchOperation == "Load Sample Question Knowledge Mapping (单选)") {
			flash.message = ""
			loadQuestionKnowledgeMapping()			
		} else if (batchOperationInstance.batchOperation == "Load Sample Questions (填空)") {
			flash.message = ""
			loadTiankongQuestions()
		} else if (batchOperationInstance.batchOperation == "Load Sample Question Knowledge Mapping (填空)") {
			flash.message = ""
			loadTiankongQuestionKnowledgeMapping()			
		} else if (batchOperationInstance.batchOperation == "Load Sample Data") {
		
			flash.message = ""

			// Import nodes content
			loadKnowledgeNodeContent()
			
			// Import parent relationship
			loadKnowledgeParentRelationship()

			// Import questions
			loadQuestions()

			// Import question mapping
			loadQuestionKnowledgeMapping()
			
			
			// Update total questions for knowledge points
			if (flash.message) {
				def updatedNumber = 0
				def mappingForKP = [:]
				def kpFamily = [:]
				def results = KnowledgePoint.createCriteria().list {
					order("id", "asc")
				}
		
				StringBuilder sb = new StringBuilder()
				sb << "Updating...\n"
				
				for ( i in 1..batchOperationInstance.traverseDepth ) {
					def kpIndex = 0
					Iterator<KnowledgePoint> kpIt =  results.iterator();
					while(kpIt.hasNext()){
						KnowledgePoint kp = (KnowledgePoint)kpIt.next();
						kpIndex++
						if (kpIndex < batchOperationInstance.startIndex) {
							continue
						} else if (kpIndex > batchOperationInstance.endIndex) {
							break
						}
						
						if (!mappingForKP[kp.name]) {
							mappingForKP[kp.name] = new HashSet()
							kp.questions?.each {mappingForKP[kp.name].add(it.id)}
						}
						
						if (!kpFamily[kp.name]) {
							kpFamily[kp.name] = new HashSet()
							kpFamily[kp.name].add(kp.id)
						}
						
						kp.childPoints?.each {
							if (mappingForKP[it.name]) {
								mappingForKP[kp.name].addAll(mappingForKP[it.name])
							}
							if (kpFamily[it.name]) {
								kpFamily[kp.name].addAll(kpFamily[it.name])
							}
						}
									
						if (i == batchOperationInstance.traverseDepth &&
							(kp.totalQuestion != mappingForKP[kp.name].size() || kp.familySize != kpFamily[kp.name].size())) {
							sb << "${kp.id} - ${kp.name}\n"
							kp.totalQuestion = mappingForKP[kp.name].size()
							kp.familySize = kpFamily[kp.name].size()
							kp.save()
							updatedNumber++
						}
					}
				}
				batchOperationInstance.batchOutput = sb.toString()
				batchOperationInstance.batchResult = "${updatedNumber} knowledge points updated!"
				
				if (!batchOperationInstance.save(flush: true)) {
					flash.message = "Sorry. Batch ${batchOperationInstance.batchOperation} Failed!"
					render(view: "create", model: [batchOperationInstance: batchOperationInstance])
					return
				}
			}
		}
		
		render(view: "create", model: [batchOperationInstance: batchOperationInstance])

	}
	
	private void loadKnowledgeNodeContent() {
		
		for (nodesContent in [(new SampleData_KnowledgePoint_Nodes1()).content, (new SampleData_KnowledgePoint_Nodes2()).content]) {
			def kpList = nodesContent.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def nodeIndex = 1
			def lineIndex = 0
			def failedOnes = "Failed:"
			def skippedLines = "Skipped:@@"
			KnowledgePoint lastKp = null
			
			for (kp in kpList) {
				lineIndex++
				if (kp.contains("代码\t名称\t内容") || !(kp.contains("[") && kp.contains("]"))) {
					skippedLines += " ${lineIndex}: ${kp}@@"
					if (lastKp) {
						lastKp.content += "\n" + kp
						lastKp.save(flush: true)
					}
					continue
				}
				def fields = kp.split("\t")
				KnowledgePoint newKp = new KnowledgePoint()
				//newKp.name = fields[0].trim()
				newKp.name = fields[0].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', '')
				if (newKp.name != fields[1].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', '')) {
					println "Warning: " + kp
				}
				if (fields.length > 2) {
					newKp.content = fields[2]?.trim().replaceAll("^\"|\"\$", "")
				}
				
				if (!newKp.save(flush: true)) {
					failedNumber++
					failedOnes += " ${nodeIndex}(${fields[0]})"
				} else {
					passedNumber++
					lastKp = newKp
				}
				
				nodeIndex++
			}
			
			flash.message += "Batch Import of node content finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}."
		}

	}

	private void loadKnowledgeParentRelationship() {
		
		for (parentRelations in [(new SampleData_KnowledgePoint_Parent_Relations1()).content, (new SampleData_KnowledgePoint_Parent_Relations2()).content]) {
			def kppList = parentRelations.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def nodeIndex = 1
			def lineIndex = 0
			def failedOnes = "Failed:"
			def skippedLines = "Skipped:@@"
			
			for (kp in kppList) {
				lineIndex++
				if (kp.contains("下游点\t上游点") || !(kp.contains("[") && kp.contains("]"))) {
					skippedLines += " ${lineIndex}: ${kp}@@"
					continue
				}
				def fields = kp.split("\t")
				KnowledgePoint childKp = KnowledgePoint.findByName(fields[0].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				KnowledgePoint parentKp = KnowledgePoint.findByName(fields[1].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))

				if (childKp && parentKp) {
					childKp.addToParentPoints(parentKp)
					parentKp.addToChildPoints(childKp)

					if (!childKp.save(flush: true) || !parentKp.save(flush: true)) {
						failedNumber++
						failedOnes += " ${nodeIndex}(${kp})"
					} else {
						passedNumber++
					}
				} else {
					failedNumber++
					failedOnes += " ${nodeIndex}(${kp})"
				}
				
				nodeIndex++
			}
			
			flash.message += "Batch Import of parent relationship finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}."
		}
	}
	
	private void loadQuestions() {
		
		for (questionContents in [(new SampleData_Question1()).content, (new SampleData_Question2()).content, (new SampleData_Question3()).content, (new SampleData_Question4()).content, 
								  (new SampleData_Question5()).content, (new SampleData_Question6()).content, (new SampleData_Question7()).content, (new SampleData_Question8()).content,
								  (new SampleData_Question9()).content]) {
			//for (questionContents in [SampleData_Question6.content]) {
				def quesList = questionContents.split("\n")
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
					currentQID = fields[0]
					Question newQuestion = new Question()
					newQuestion.type = fields[1]
					newQuestion.description = fields[2].replaceAll("<\\\\n>", "\r\n").replaceAll("\"", "")
	
					indexNum = 'A'
	
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
	
					newQuestion.source = fields[8]
	
					def inputer = AdminUser.findByUserName(fields[9])
					if (inputer) {
						newQuestion.inputBy = inputer
					}
	
					if (fields.length > 10) {
						newQuestion.analysis = fields[10].replaceAll("<\\\\n>", "\r\n")
					}
	
					def nextIndex = 11 // Extra fields which are optional
					if (fields.length > 11) {
						newQuestion.term = fields[nextIndex]
						nextIndex++
					}
					if (fields.length > nextIndex) {
						newQuestion.errorRate = Integer.parseInt(fields[nextIndex])
						nextIndex++
					}
					if (fields.length > nextIndex) {
						newQuestion.reviewedBy = fields[nextIndex]
						if (newQuestion.reviewedBy == "null") {
							newQuestion.reviewedBy = null
						}
						nextIndex++
					}
					if (fields.length > nextIndex) {
						def iDate
						try {
							iDate = dateFormat.parse(fields[nextIndex])
						} catch (Exception e) {
							iDate = dateFormat1.parse(fields[nextIndex])
						}
						newQuestion.inputDate = iDate
						nextIndex++
					}
	
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
				
				flash.message += "Batch Question Import finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}."
	
			}
	}

	private void loadTiankongQuestions() {
		
		for (questionContents in [(new SampleData_Question9()).content]) {
			//for (questionContents in [SampleData_Question6.content]) {
				def quesList = questionContents.split("\n")
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
					currentQID = fields[0]
					Question newQuestion = new Question()
					newQuestion.type = fields[1]
					newQuestion.description = fields[2].replaceAll("<\\\\n>", "\r\n").replaceAll("\"", "")

					if (newQuestion.type == "单选") {
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
	
					newQuestion.source = fields[8]
	
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
				
				flash.message += "Batch Question Import finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}."
	
			}
	}

	private void loadQuestionKnowledgeMapping() {
		
		for (questionMapping in [(new SampleData_QuestionMapping1()).content, (new SampleData_QuestionMapping2()).content, (new SampleData_QuestionMapping3()).content, (new SampleData_QuestionMapping4()).content, 
								 (new SampleData_QuestionMapping5()).content, (new SampleData_QuestionMapping6()).content, (new SampleData_QuestionMapping7()).content, (new SampleData_QuestionMapping8()).content, 
								 (new SampleData_QuestionMapping9()).content, (new SampleData_QuestionMapping10()).content]) {
			def quesList = questionMapping.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def failedOnes = "Failed:"
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
			
			for (quest in quesList) {
				if (!quest.contains("[") || !quest.contains("]") || quest.contains("[Question ID]\t题目")) {
					continue
				}
				def fields = quest.trim().split("\t")
				Question ques = Question.findByDescription(fields[1].replaceAll("\"", "").replaceAll("<\\\\n>", "\r\n"))
				KnowledgePoint kp = KnowledgePoint.findByName(fields[2].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				if (!ques || !kp) {
					failedOnes += " @@@(${quest})"
					continue
				}
				ques.inputBy = session.user
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
			
			flash.message += "Batch Import of Question Mapping finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}."
		}
	}

	private void loadTiankongQuestionKnowledgeMapping() {
		
		for (questionMapping in [(new SampleData_QuestionMapping10()).content]) {
			def quesList = questionMapping.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def failedOnes = "Failed:"
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
			
			for (quest in quesList) {
				if (!quest.contains("[") || !quest.contains("]") || quest.contains("[Question ID]\t题目")) {
					continue
				}
				def fields = quest.trim().split("\t")
				Question ques = Question.findByDescription(fields[1].replaceAll("\"", "").replaceAll("<\\\\n>", "\r\n"))
				KnowledgePoint kp = KnowledgePoint.findByName(fields[2].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				if (!ques || !kp) {
					failedOnes += " @@@(${quest})"
					continue
				}
				ques.inputBy = session.user
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
			
			flash.message += "Batch Import of Question Mapping finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}."
		}
	}

}

