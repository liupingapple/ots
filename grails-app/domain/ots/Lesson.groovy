package ots

import java.util.Date;

// TODO: We don't use this for now
// 一堂课或一次训练，A lesson is a fixed period of time when people are taught about a particular subject or taught how to do something
class Lesson {

    String name //该堂课的名称
	String classroom // 这堂堂课在哪里教学的
	String briefComment
	
	String lectureNote //讲义
	String lectureFile //教学视频
	
	String terms
			
	Date beginDate //测验开始时间
	Date endDate // 测验结束时间
	
	Date dateCreated
		
	// TODO We don't use Lesson for now, so comment out the dependence	
	// static belongsTo = [teacher: Teacher]
	// static hasMany = [students:Student, knowledgePoints: KnowledgePoint]
	
	static mapping = {
		students joinTable: [key: "m_lesson_id", column:'m_student_id']
		knowledgePoints joinTable: [key: "m_lesson_id", column:"m_knowledge_point_id"]
	}
	
    static constraints = {
		name(nullable:false, blank: false)
		classroom(nullable:true, blank:true)
		beginDate(nullable:true, blank:true)
		endDate(nullable:true, blank:true)	
		terms nullable:true, inList:CONSTANT.TERMs
		lectureNote(nullable:true, blank:true)
		lectureFile(nullable:true, blank:true)
		briefComment(nullable:true, blank:true, maxSize:1000)
    }
}
