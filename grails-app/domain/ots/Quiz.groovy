package ots

import java.math.BigDecimal;

class Quiz {
	String name //测验名称或者课程名称
	String status
	String type
	int timeTaken // seconds
	Date answeredDate  // the date student finished the quiz or last Date answered if the quiz is pending
	String reviewComment // 答题完之后，老师 or Parents给的评语
	Date dateCreated
	Assignment assignment
	KnowledgePoint toBeFocusedKnowledge
	
	int correctNum
	BigDecimal score
	int notAnsweredNum
	int totalAnswerNum  //有些填空题一个question（questionRecord）有多个answer
	int totalQuestionNum
	Student student

	// Steve Ding committed 43d320b to add latestCoverage and latestCorrectRate
	
	// Taylor comment: 截止到当前练习，覆盖率, 这个覆盖率是截止到某个练习时间点的，区别于AssignmentStatus.coverageRate
	// 覆盖率 = A / B； A 是指作业范围中，在本级至少做过1道题目的知识点的数目；B 是指作业范围中，本级至少有1道题关联的知识点数目（即AQ>0）。
	// 覆盖率反映的是，在某一个知识点的集合中，在本级有关联题目的知识点中，有多少的知识点被“接触”过（就是知识点的直接关联题目被训练过）
	// 这个值由同一截止时间的 AssignmentStatus.coverageRate 的值设置，保留了截止到这个quiz时的coverageRate
	BigDecimal latestCoverage = -1
	
	// 截止到当前练习，正确率? 已经有score了，Why Steve added this field?
	BigDecimal latestCorrectRate = -1
	
	// Taylor added masterRate:  ForV2: 截止到当前这个练习quiz，知识点chosenFor的掌握度与覆盖率
	// 掌握度是指 = min(作业范围中的所有知识点的familyRecentCorrectRate)；也就是作业范围中，familyRecentCorrectRate 数值最小的那个值，也即作业范围中最薄弱的那个知识点的 familyRecentCorrectRate
	// 其实，任何一个知识点或知识点集合，都可以有一个掌握度指标；就是集合中最薄弱的那个知识点的 familyRecentCorrectRate；如果集合中只有1个知识点，那么就是该知识点的familyRecentCorrectRate.
	// 这个值由同一截止时间的 AssignmentStatus.masterRate 的值设置，保留了截止到这个quiz时间点的masterRate
	BigDecimal masterRate = 0
	
	// for 阅读理解题，掌握水平，即当前的 Student.masterLevel, 以后student.masterLevel改变了，quiz.masterLevel仍保留了该学生masterLevel历史值
	BigDecimal masterLevel = 0 
	
	static hasMany = [records: QuizQuestionRecord]
	
	static mapping = {
		student index: 'quiz_student_idx'
		sort dateCreated: "desc"
	}
	
	static constraints = {
		name(nullable:false, blank: false)
		student(nullable:false, blank: false)
		type(inList:CONSTANT.QUIZ_TYPEs)
		status(inList:CONSTANT.QUIZ_STATUSes)
		timeTaken(nullable:true, blank:true)
		answeredDate(nullable:true)
		assignment(nullable:true)
		reviewComment(nullable:true, blank:true, maxSize:1000) 
		score(nullable:true, scale:1)
		toBeFocusedKnowledge(nullable:true)		
		
		totalQuestionNum(nullable:true)
		latestCoverage(nullable:true)
		latestCorrectRate(nullable:true)
		masterRate(nullable : true)		
	}

	void reSetMyAnswer()
	{
		records.each { record ->
			record.stuAnswers.each { recordStuAnswer ->
				recordStuAnswer.userInput = null
			}
		}
	}

	@Override
	String toString() {
		name
	}
}
