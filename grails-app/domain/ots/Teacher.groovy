package ots

import java.util.HashSet;

class Teacher extends EndUser {
	
	String teacherType = CONSTANT.TEACHER_TYPEs[0]
	String degree
	String nativePlace // 籍贯
	String residentAddr // 居住地址
	Date entryTime 
	School school
	
	String coveredSubjects  // All the 科目 covered, seperated by space
	 
	// TODO 收藏知识点用于创建作业
	// Set kpStore = new HashSet<KnowledgePoint>() // 收藏知识点用于创建作业
	// static transients = ['kpStore']
	
	boolean toShowKnowledgeGraph = false
	
	String status = CONSTANT.TEACHER_STATUSes[0]
   
	// static belongsTo = [school : School]
	static hasMany = [students : Student, tclasses : TClass]//[lessons : Lesson]

    static constraints = {
		teacherType( nullable:true, blank:true, inList: CONSTANT.TEACHER_TYPEs )
		degree(nullable:true, blank:true, inList: CONSTANT.DEGREEs )
		nativePlace(nullable:true, blank:true, inList: CONSTANT.PROVINCEs )
		residentAddr(nullable:true, blank:true, inList:CONSTANT.PROVINCEs )
		entryTime(nullable:true)
		nativePlace(nullable:true, blank:true, inList:CONSTANT.PROVINCEs )
		status(nullable:true, inList:CONSTANT.TEACHER_STATUSes)
        school(nullable:true)		
		coveredSubjects(nullable:true)
    }
	
	@Override
	public String toString() {
		return userName;
	}
}
