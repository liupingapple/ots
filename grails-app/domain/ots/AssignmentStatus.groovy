package ots

import java.util.Date;

class AssignmentStatus {

	Assignment assignment
	String status
	String comment
	// ForV2
	Date dateCreated
	Date recentCheckDate  // check it weekly to decide whether to advance student's level
	
	// TODO to remove below 2 ones, use Assignment.totalAvailableQuestions and Assignment.totalKnowledgePoints insteadly
	int availableQuestions = 0
	int totalKnowledgePoints = 0 // please note: used in savingKCP(): if (!aStatus.totalKnowledgePoints) {newAssignment = true}
	
	int finishedQuestions = 0
	int correctQuestions = 0
	
	int coveredKnowledgPoints = 0
	BigDecimal masterRate = 0
	BigDecimal coverageRate = 0
	BigDecimal relativeTargetCorrectRate = 0.95
	KnowledgePoint toBeFocusedKnowledge
	
	// Assignment has difficultyBar(changed from difficultyRange) already, no need to use AssignmentStatus.difficultyRange anymore
	// int difficultyRange = 1
			
	// TODO change coveredKPs to be transients and add getCoveredKPs() method?, no need to addToCoveredKPs code anymore
	static hasMany = [coveredKPs : KnowledgePoint]	
	static belongsTo = [student : Student]
	
    static constraints = {
		assignment(nullable: false)
		comment(nullable: true, blank: true)
		status(nullable: true, blank: true, inList:CONSTANT.ASSIGNMENT_STATUS_LIST)
		toBeFocusedKnowledge(nullable: true)
		recentCheckDate(nullable: true)
    }
		
	// ForV2: No need to add below fields - taliu
	// TODO 错题列表. Not sure when this will be invoked. And will it impact performance?
	// It will be invoked in '智能练习' section '练习笔记' module, invoke assignmentStatus.errAnsweredQuestions will call assignmentStatus.getErrAnsweredQuestions() internally
	// The performance impact is depending on the related getXXX, I think it's fine as it only iterator few data
/*	Set errAnsweredQuestions = new HashSet<Question>();	
	Set errAnsweredKPs = new HashSet<KnowledgePoint>();
	Set coveredKPs = new HashSet<KnowledgePoint>();
	Set favoriteQuestions = new HashSet<favoriteQuestions>(); // depend on QuizQuestionRecord.favoriteFlag
	static transients = ['errAnsweredQuestions', 'errAnsweredKPs', 'coveredKPs', 'favoriteQuestions']
	
	HashSet<Question> getErrAnsweredQuestions()
	{
		def errAll = [] as HashSet<Question>
		def quizList = Quiz.findAllByStudentAndAssignment(student, assignment)
		quizList.each { quiz ->
			quiz.records.each { record ->
				if (!record.correctAnswered) {
					errAll << record.question
				}
			}
		}
		
		return errAll
	}
	
	HashSet<KnowledgePoint> getErrAnsweredKPs()
	{
		def errAll = [] as HashSet<KnowledgePoint>
		def quizList = Quiz.findAllByStudentAndAssignment(student, assignment)
		quizList.each { quiz ->
			quiz.records.each { record ->
				if (!record.correctAnswered) {
					errAll << record.chosenFor
				}
			}
		}
		
		return errAll
	}
	
	HashSet<KnowledgePoint> getCoveredKPs()
	{
		def kps = [] as HashSet<KnowledgePoint>
		def quizList = Quiz.findAllByStudentAndAssignment(student, assignment)
		quizList.each { quiz ->
			quiz.records.each { record ->
				kps << record.chosenFor
			}
		}
		
		return kps
	}
	
	HashSet<Questions> getFavoriteQuestions()
	{
		
	}
	*/
			
	static mapping = {
		assignment index: 'assignment_idx'
	}
}
