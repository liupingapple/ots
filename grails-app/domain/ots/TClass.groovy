package ots

// TODO: We have requirements to add "班级" into the system, so the teachers can filter students based on it
// 班级，TClass is a group of pupils or students who are taught together
class TClass {

    String className
	    
    String schedule // 上课时间安排等

	// 班长
    Student monitor
	
	// 班主任
    Teacher inChargeTeacher
    String briefComment
    
    // optional
    int studentCount
    BigDecimal totalQuestionsPracticed
    int totalErrorRate
    String teachingPlan // 教学模板
	
	// TODO We don't use Lesson for now, so comment out the dependence
    static hasMany = [students : Student]  // lessons: Lesson
	// static belongsTo = [teacher: Teacher]
	
    static constraints = {
        monitor nullable:true
		schedule nullable:true
        inChargeTeacher nullable:true
		studentCount nullable:true
		totalQuestionsPracticed nullable:true
		totalErrorRate nullable:true
		teachingPlan nullable:true
        briefComment(blank:true, nullable: true, maxSize:2000)		
    }
}
