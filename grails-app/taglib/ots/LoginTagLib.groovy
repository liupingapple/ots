package ots

class LoginTagLib {
	def loginControl = {
		if(request.getSession(false) && session.user){
			// out << "${session.user.fullName} - " // fix for bootstrap
			if (session.user instanceof AdminUser) {
			  out << """${link(action:"logout", controller:"adminUser"){""+"[${session.user.role} / ${session.user.fullName} - "+"退出]"}}"""
			} else {
			  out << """${link(action:"logout", controller:"endUser"){""+"[${session.user.fullName} - "+"退出]"}}"""
			}
		} else {
			// out << """[${link(action:"login", controller:"adminUser"){"Login"}}]"""
		   out << "<g:link uri='/'/>"
		}
	}
	
	def preference = {
		if(request.getSession(false) && session.user){
			if (session.user instanceof AdminUser) {
			  out << """${link(action:"show", controller:"adminUser", id:"$session.user.id"){"设置"}}"""
			} else if (session.user instanceof Student) {
			  out << """${link(action:"show", controller:"student", id:"$session.user.id"){"设置"}}"""
			} else if (session.user instanceof Teacher) {
			  out << """${link(action:"show", controller:"teacher", id:"$session.user.id"){"设置"}}"""
			}
		}
	}
}
