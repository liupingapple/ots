package ots

import java.util.Date;

class Message {
	Long parentId // parent message ID
	EndUser msgFrom // userName
	EndUser msgTo   // userName
	Date msgDate
	
	String subject
	String content
	
	Integer level = 1
	static transients = ["level"]
	
    static constraints = {
		parentId unique:false, nullable:true, blank:true
		subject unique:false, nullable:true, blank:true
		content unique:false, nullable:true, blank:true
		msgFrom unique:false, nullable:false, blank:false
		msgTo unique:false, nullable:false, blank:false
		msgDate unique:false, nullable:false, blank:false
    }
	
	String toString () {
		"From "+msgFrom+":"+subject
	}
}
