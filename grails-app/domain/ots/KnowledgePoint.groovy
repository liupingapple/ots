package ots

class KnowledgePoint {

    static constraints = {
		name(blank : false, unique : true)
		content(nullable:true, maxSize:1000)	
		
		qrCode(nullable : true, validator: { return it == null || (Integer.parseInt(it) >= 60001 && Integer.parseInt(it) <= 80000) })
		qrCodeVideoURL(nullable : true)
    }
	
	String name
	String content
	int degree
	int masterRatio
	int associatedQuestion = 0
	int totalQuestion = 0
	int familySize = 1
	int validFamilySize = 0
	long totalAccessCount = 0
	long totalFailed = 0
	BigDecimal failedRate = -1
	
	// 二维码值
	String qrCode
	// 扫描二维码后推送的视频ID ---> 此字段修改为 视频ID, 不是URL，现在采用默认的URL为: WeiXinUtil.WEIXIN_SRV_URL + '/video?vid=' + 下面这个字段的值
	String qrCodeVideoURL
		
	static hasMany = [childPoints: KnowledgePoint, parentPoints: KnowledgePoint, questions: Question]
	static belongsTo = Question

	static mapping = { childPoints joinTable: [name:'child_point', key:'knowledge_Id', column:'child_Id']
					   parentPoints joinTable: [name:'parent_point', key:'knowledge_Id', column:'parent_Id'] 
					   sort "id"
					  }
	
	String toString () {
		"${name} (${associatedQuestion}/${totalQuestion})" + (familySize>1?" - ${familySize}":"")
	}
	
//	@Override
//	boolean equals(final Object that) {
//		EqualsBuilder.reflectionEquals(this, that, ["id"])
//	}
//
//
//	@Override
//	int hashCode() {
//		HashCodeBuilder.reflectionHashCode(this, ["id"])
//	}
}
