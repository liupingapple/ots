package ots

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class Assignment {

	String name
	String assignedBy
	Date dueDate = new Date()
	int timeLimit // time limit for finishing the quiz 答题限时（比如考试，或者课堂练习）
	int questionLimit // questions limit must answered 必须完成多少道题目
	String comment  // 答题前，老师对这套题目的description
	
	// TO-DO: Remove lesson. ForV2: keep it for now, but lesson is not used
	Lesson lesson // no need to belongs to a Lesson
	
	// ForV2: We no need below fields now, we have implemented the report of Assignment and it works well
	// TO-DO: Add more fields as summary // TODO def as transients, and provide getXXX to init the value
	// String questionType
	// int totalKPs
	// int effectiveKPs
	// int totalQuiz
	// int studentNum
	// BigDecimal avgScores
	// BigDecimal avgQuizNum
	// int totalAvailableQuestions
	// Date assignedDate
	// int level                      // TODO what level means?
	// Ways to keep track of top 10 KPs needed to be focused on 

	int totalAvailableQuestions = 0
	int totalKnowledgePoints = 0
	
	// Parameters to control the picking of questions
	int numberOfQuestionsPerPage = 10
	BigDecimal targetCorrectRate = 0.95
	int minAssociatedQuestionsToEvaluate = 3  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	int maxAssociatedQuestionsToEvaluate = 10  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	int minTotalQuestionsToEvaluate = 5  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	int maxTotalQuestionsToEvaluate = 20  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	BigDecimal evaluationPower = 1  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	BigDecimal focusPower = 2.0
	
	// because assignment has many AssignmentTemplates, we pick the 1st AssignmentTemplate's subject as is
	// user will not update Assignement.subject
	String subject
	int difficultyBar = 1
	String questionType	
	
	// 二维码值
	String qrCode
	// 生成新的练习的URL，用于二维码, ref to KnowledgePointController.weixinPracticeLink()
	String qrCodeNewQuizURL
	
	// please don't use below transients field students, it's wrong by test, get NPE exception when access quizes
	Set<Student> students = [] as HashSet
	static transients = ['students']
	
	// TODO
	// static transients = ['students', 'totalKPs', 'effectiveKPs', 'totalQuiz', 'studentNum', 'avgScores', 'avgQuizNum', 'level']
	
	// TO-DO: Use AssignmentStatus instead of Quizes to get the Students
	// ForV2: I think no problems to use Quiz - taliu
	static hasMany = [quizes: Quiz, templates: AssignmentTemplate]

	static constraints = {
		name(nullable:false, blank:false)
		assignedBy(nullable:true, blank:true)
		dueDate(nullable:true, blank:true)
		timeLimit (nullable:true) 
		questionLimit (nullable:true) 
		lesson(nullable:true)
		comment(nullable:true, blank:true)
		questionType(nullable:true)
		
		qrCode(nullable:true, validator: { return it == null || (Integer.parseInt(it) >= 80001 && Integer.parseInt(it) <= 90000) })
		qrCodeNewQuizURL(nullable:true)
	}
	
	Set<Student> getStudents()
	{		
		quizes.each {
			if (it?.student) {
				students.add(it.student)
			}
		}
	}
	
	String toString () {
		name
	}
	
	@Override
	boolean equals(final Object that) {
		EqualsBuilder.reflectionEquals(this, that, ["id"])
	}

	@Override
	int hashCode() {
		HashCodeBuilder.reflectionHashCode(this, ["id"])
	}
	
}
