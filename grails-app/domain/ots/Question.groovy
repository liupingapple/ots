package ots

class Question {
	
	long qID = -1  // permanent ID; Will stay the same during import/export
	String instructions // Instructions on how the question should be answered
	
	String type = CONSTANT.RADIO_QUESTION
	String description
	String source
	String term
	String analysis
	List answers = new ArrayList() // TODO: change to Set answers = new HashSet<Answer>()  ?

	Date inputDate
	String inputBy
	String reviewedBy

	boolean plainText = true
	
	BigDecimal difficultyLevel = 5 // if no set
	BigDecimal weeklyScore = 0
	int weeklyTryout = 0
	int totalPracticed = 0
	BigDecimal totalScore = 0
	BigDecimal errorRate = -1
	
	byte[] img
	String imgType

	byte[] audio
	String audioType
	
	// 二维码值
	String qrCode
	// 扫描二维码后推送的视频ID ---> 此字段修改为 视频ID, 不是URL，现在采用默认的URL为: WeiXinUtil.WEIXIN_SRV_URL + '/video?vid=' + 下面这个字段的值
	String qrCodeVideoURL
	
	static mapping = {
		img sqlType: "MediumBlob"
		audio sqlType: "MediumBlob" 
		answers cascade:"all-delete-orphan"
		
		//id generator:'assigned' //disable primary key generation
	}

	// 阅读题有子Question，分两类阅读理解题
	// 一类是 父Question是一段文字，没有答案，子Question是一些选择题or填空题
	// 一类是阅读填空，没有子Question，一个空是一分，区别现有的填空题，
	static hasMany = [answers : Answer, knowledgePoints : KnowledgePoint, childQuestions : Question]
	static belongsTo = [ parentQuestion: Question ]

    static constraints = {
		qID(nullable: false, unique : true)
		instructions(nullable: true)
		description(nullable: true, maxSize:15000 )
		type(nullable: false, blank: false, maxSize:40, inList: CONSTANT.QUESTION_TYPEs)
		source(nullable: true, maxSize:60)
		term(nullable: true, maxSize:40)
		analysis(nullable: true, maxSize:3000) 
		errorRate(display:false)
		inputBy(nullable: true, maxSize:40)  //TODO remove it
		inputDate(nullable: true)
		
		difficultyLevel(nullable: true, scale:1)

		reviewedBy(nullable: true, maxSize:40)
		
		img(nullable:true) // maxSize: 262144 /* 256K */
		imgType(nullable:true, maxSize:40)

		audio(nullable:true) // , maxSize: 262144 /* 256K */
		audioType(nullable:true, maxSize:40)
		
		qrCode(nullable : true, validator: { return it == null || (Integer.parseInt(it) >= 1 && Integer.parseInt(it) <= 60000) })
		qrCodeVideoURL(nullable : true)
    }
	
	public String toString() {
		return "${id}: ${description}"
	}
}
