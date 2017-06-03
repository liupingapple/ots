package ots

// ForV2: need to redesign this class
class School {

	String name
    String president // 校长或者机构负责人
	String contactPerson
	String email
	String telephone1
	String telephone2
    String address
    String webSite
	
	AdminUser administrator  // 培训机构登录系统的管理员,role=schoolAdmin，可以在系统维护该机构下的教师，学生

	// ForV2: 培训机构需要划分等级吗，可以预留此字段
    String level = CONSTANT.ACCOUNT_LEVELs[0]

	// ForV2: it's fine to use hasMany students and teachers
    static hasMany = [students: Student, teachers: Teacher]

    static constraints = {
		name()
		contactPerson()
		telephone1()
		telephone2()
		administrator(nullable:true, blank:true)
		email(nullable:true, blank:true)
        address(nullable:true, blank:true)
        president(nullable:true, blank:true)
        webSite(nullable:true, blank:true, url:true)
        level(inList:CONSTANT.ACCOUNT_LEVELs)
    }
}
