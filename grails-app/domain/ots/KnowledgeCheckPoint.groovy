package ots

// TO-DO: Add another class to save the history 
class KnowledgeCheckPoint {
	
	// TO-DO: update the name to datePracticed  TODO if use the name dateCreated, Grails will assign the timestamp to it automatically, so i think no need to change the name
	// Actually, this is the reason I want to rename it. With the old name, we lost control of the exact value for this field.
	Date datePracticed
	KnowledgePoint knowledge
	Student student
	
	int totalQuestions = 0 // in this Quiz
	int correctQuestions = 0 // correct ones in this Quiz
	int totalSum = 0  // the accumulated total questions, if it meets stu.minAssociatedQuestionsToEvaluate, calculate recentCorrectRate
	
	// correct rate based on the recent answered questions ~ minAssociatedQuestionsToEvaluate
	// default value is -1, indicates null recentCorrectRate (in case DB treats null as 0)
	BigDecimal recentCorrectRate = -1
	BigDecimal recentTotal = 0  // Total + evaluationPower * recenTotal(-1)
	BigDecimal recentCorrect = 0 // Correct + evaluationPower * recentCorrect(-1)
	
	int familyTotal = 0
	int familyCorrect = 0
	int familyTotalSum = 0
	BigDecimal familyRecentTotal = 0
	BigDecimal familyRecentCorrect = 0
	BigDecimal familyRecentCorrectRate = -1
	
	boolean isFocused = false // whether the kp belongs to the focused group
	
	// TO-DO: only need to save it in History
	Quiz quiz // kcp is generated for this quiz

	HashSet quesRecords = new HashSet<QuizQuestionRecord>()
	static transients = ['quesRecords']

	static belongsTo = [student: Student]

	// TODO: remove recentCorrectRate_idx, dateCreated_idx
	static mapping = {
		recentCorrectRate index: 'recentCorrectRate_idx'
		student index: 'student_idx'
		knowledge index: 'knowledge_idx'
		quiz index: 'quiz_idx'
		datePracticed index: 'datePracticed_idx'
		sort id: "desc"
	}
	
    static constraints = { 
		knowledge nullable:false, blank:false
		quiz nullable:false
		recentCorrectRate scale:2
		recentCorrect scale:2		
		recentTotal scale:2
		familyRecentCorrect scale:2
		familyRecentTotal scale:2
		familyRecentCorrectRate scale:2
    }
	
	String toString () {
		"$knowledge"
	}
}
