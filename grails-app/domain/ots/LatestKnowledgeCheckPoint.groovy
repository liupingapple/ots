package ots

import java.util.Date;
import java.util.HashSet;

class LatestKnowledgeCheckPoint {

	Date datePracticed	// Latest practice date
	KnowledgePoint knowledge
	Student student
	
	int totalQuestions = 0 // for this KP
	int totalCorrect = 0 // correct ones for this KP
	
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
	
	static belongsTo = [student: Student]

	// TODO: remove recentCorrectRate_idx, dateCreated_idx
	static mapping = {
		recentCorrectRate index: 'recentCorrectRate_idx'
		student index: 'student_idx'
		knowledge index: 'knowledge_idx'
		datePracticed index: 'datePracticed_idx'
		sort id: "desc"
	}
	
    static constraints = { 
		knowledge nullable:false, blank:false
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
