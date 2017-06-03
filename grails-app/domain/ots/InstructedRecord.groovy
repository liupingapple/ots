package ots

/**
 * 老师填写辅导记录，学生对辅导记录做评价
 * @author taliu
 *
 */
class InstructedRecord {
	
	Teacher teacher
	Student student
	Date fromDate   // 辅导开始时间  （年/与/日 时:分:秒）
	Date toDate     // 辅导结束时间
	String content  // 辅导知识点以及内容
	
	int evaLevel       // 学生对老师辅导记录评级（三星，四星，。。。）
	String evaContent // 学生对老师辅导记录评语

    static constraints = {
		
		teacher nullable:false 
		student nullable:false
		fromDate nullable:false
		toDate nullable:false
		teacher nullable:false
		content nullable:true
		
		evaLevel nullable:true
		evaContent nullable:true
    }
}
