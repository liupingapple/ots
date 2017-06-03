package ots

class Student extends EndUser {
		
	String homeProvince
	String homeCity
	String homeAddr
	String parents  // 暂时没有用，预留以后使用。比如系统可以将学生根据家长分类
	String parentLink //如果没有家长用户电话，家长姓名，邮箱，电话等相关信息。家长主要电话可以放在telephone2里
	
	String term //学期, 以后备用
	School school // 培训机构或者学校
	
	Teacher teacher  // based on current design, one student has one in charge teacher

	// Personal preferences
	int numberOfQuestionsPerPage = 10
	BigDecimal targetCorrectRate = 0.95
	int minAssociatedQuestionsToEvaluate = 1  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	int maxAssociatedQuestionsToEvaluate = 5  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	int minTotalQuestionsToEvaluate = 5  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	int maxTotalQuestionsToEvaluate = 10  // Used to calculate recentCorrectRate in KnowlegeCheckPoint
	BigDecimal evaluationPower = 0.618  // Used to calculate recentCorrectRate in KnowlegeCheckPoint 1/n of old scores will be replaced
	BigDecimal focusPower = 2.0
	int repeatPower = 2  // Control how many times more a KP needs to be repeated in case the question is answered wrong
	
	int maxKPToShow = 35
	boolean toShowKnowledgeGraph = true

	BigDecimal masterLevel = 0  // 根据学生等级找试题等级
	boolean enableLeveling = true
	BigDecimal Scores = 0  // Level difference * average score (min average score = 10)
	int practiceNumber = 0
	String practiceHistory = "" // space separated list of qID
	String coveredSubjects = "" // space separated list of 科目
		 
	String level = CONSTANT.ACCOUNT_LEVELs[0]
		
	// static belongsTo = [school:School, parents:Parent]
	
	static hasMany = [aStatus: AssignmentStatus, kcps: KnowledgeCheckPoint, failedRecords: QuizQuestionRecord] // removed lessons: Lesson
	
    static constraints = {
		term (nullable:true, blank:true, inList:CONSTANT.TERMs)	
		homeProvince(nullable: true, inList:CONSTANT.PROVINCEs, maxSize:30)
		homeCity(nullable: true, maxSize:30)
		homeAddr(nullable: true, maxSize:30)
		
		school(nullable: true)
		parents(nullable: true, maxSize:80)
		parentLink(nullable:true, blank:true, maxSize:100)
		
		teacher(nullable: true)
		
		practiceHistory(nullable: true, maxSize:6000)
		coveredSubjects(nullable: true)
		
		level(inList:CONSTANT.ACCOUNT_LEVELs)
    }
	
	@Override
	public String toString() {
		userName
	}
}
