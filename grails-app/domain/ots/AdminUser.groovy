package ots

/**
 * Change history
 * -----------------------------------------
 * <YY-MM-DD>	<User>			<Desc>
 * ----------------------------------------
 * 14-?			Steve&Taylor	Add TODO ones
 * 13-?			Taylor			Creation
 *
 */
class AdminUser {

	String userName
	String password
	String fullName
	String email
	
	// ForV2: Add some other contact information, 
	String phone
	String contactType // Could include multiple types, separated by \t, like QQ\tWeChat
	String contanctID  // Could contain multiple ID, separated by \t
	
	String role = "inputer"
	String toString() {
		"${userName}"
	}

	// ForV2: Add school
	School school
	
	static transients = ['admin']
	
	boolean isAdmin(){
		return role == "admin"
	}
	
	// TODO: Need to remove the following 1:many relationship ? ForV2 removed below relationship
	// static hasMany = [question : Question, batchOp : BatchQuestionOp]

	static constraints = {
		userName(blank:false, nullable:false, unique: true)
		fullName()
		password(blank:false, password: true)
		email(email:true)
		// ForV2: Remove the inList to provide more flexibility. Need to provide more roles for different schools
		role() // inList:["admin", "checker", "linker", "inputer", "schoolAdmin"]
		
		phone(nullable:true)
		
		contactType(nullable:true)
		contanctID(nullable:true)
		school(nullable:true)		
	}	

}
