package ots

import java.util.Date;

class EndUser {
	
	String userName
	String password
	
	String fullName
	String email
	
	int identificationNum //身份证号
	Date birthDate
	String sex
	
	String role = "endUser"
	
	String telephone1
	String telephone2
	
	Date dateCreated
	
	// 微信普通用户的标识，对当前公众号唯一，微信用户对不同的公众号有不同的openid，如果高手云公众平台换了公众号，则这个weixinOpenid要重新更新
	// only update on WeiXinController, 老师也可以关注微信公众号并进行相关操作
	String weixinOpenid  
	
	Date lastAccessDate // 上次访问系统日期
	Date loginDate      // 这次登录日期
	Date expiredDate    // 缴费用户到期日期
	
	boolean active = true

	// 备注
	// TO-DO: Reimplement the messaging and class records 
	String briefComment
	
	def beforeInsert = {
		// password = password.encodeAsSHA()
    }	
	
    static constraints = {
		
		userName(blank: false, nullable:false, unique:true, maxSize:60)
		password (blank:false, nullable: false, password: true, maxSize:60)
		fullName (blank: false, nullable:false)
		email(email:true)
		
		sex(nullable:true, blank:true, inList:[CONSTANT.MALE, CONSTANT.FEMALE])
		birthDate(nullable:true, blank:true)
		identificationNum(nullable: true, matches: "\\d{18}")
		
		telephone1(nullable: true, blank:true, maxSize:60)
		telephone2(blank:true, nullable: true, maxSize:60)
		
		lastAccessDate(nullable:true)
		loginDate(nullable:true)
		expiredDate (nullable:true)
		dateCreated(nullable: true)
		
		weixinOpenid(nullable:true, maxSize:100, unique: true)
		
		active()
		
		briefComment(blank:true, nullable: true, maxSize:2000)		
    }
	
	static mapping = {
		tablePerHierarchy false
	}
}
