package ots

import java.util.Date;

class AssignmentTemplate {

	String templateName
	String createdBy
	Date dateCreated
	
	String subject // 课程或者科目，比如：“英语词汇与填空”，“英语阅读理解”，“文言文”，“语文”，“化学”
	
	// ForV2: add school
	// please update existing AssignmentTemplate data in DB by Sql in case the FOREIGN KEY (`school_id`) REFERENCES `school` (`id`) created failed
	// update assignment_template set school_id=89;
	School school
	
	static hasMany = [knowledgePoints: KnowledgePoint]

    static constraints = {
		templateName(nullable:false, blank:false, unique:true)
		createdBy()
		subject(nullable:true, inList: CONSTANT.COURSE_LIST)		
		school(nullable:true)		
    }
	
	String toString () {
		templateName
	}
}
