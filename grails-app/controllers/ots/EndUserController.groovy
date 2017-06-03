package ots

import org.springframework.dao.DataIntegrityViolationException

class EndUserController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def beforeInterceptor = [action:this.&checkUser, except: [
			'login',
			'logout',
			'authenticate',
			'create',
			'save'
		]]

	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'endUser',action:'login')
			return false
		} 
		
	}

	def login = {
		
	}

	def logout = {
		if (session.user) {
			flash.message = "再见, ${session.user?.fullName}"
		} else {
			flash.message = "再见!"
		}
		
		// clear session store attr...
		session.user = null
		session.assignment = null
		session.assignmentStatus = null
		session.totalQuizNumber = null
		session.kcpQSMap = null
				
		redirect(controller:'endUser', action:"login")
	}

	def authenticate = {
		def user = EndUser.findByUserNameAndPassword(params.userName, params.password)
		if(user){
			user.lastAccessDate = user.loginDate
			user.loginDate = new Date()
						
			// first time login in, the user has one week trial
			if (!user.expiredDate) {
				user.expiredDate = user.lastAccessDate ? user.lastAccessDate : new Date() + 7
			}
			
			session.user = user
			flash.message = "您好, ${user.fullName}!"
	
			if(session.user instanceof Student){
				//redirect(controller:"student", action:"quizList", id:session.user.id)
				session.teacher = user.teacher
				session.numberOfQuestionsPerPage = user.numberOfQuestionsPerPage
				
				// check if student will be terminated
				int days = user.expiredDate - new Date()
				// println "There are ${days} authorized days left for student ${user}"
				user.active = true
				
				// 用户缴费验证，will be re-enable in the future
				/*if (days >= 0 && days <= 7) {
					flash.message = "您的账户${user}还有${days}天将到期，请及时续费以便高手云系统继续给您提供服务"
				} else if (days >= -7 && days < 0) {
					flash.message = "您的账户${user}已经过期${-days}天，请及时续费以便高手云系统继续给您提供服务"
				} else if (days >= -14 && days < -7) { 
					flash.message = "您的账户${user}已经过期${-days}天，请及时续费以便高手云系统继续给您提供服务，如不续费，您的账户将会于近期被关闭"
				} else if (days < -14) { 
					flash.message = "您的账户${user}已经过期${-days}天，需要续费后才能登录系统！"
					user.active = false				
				}*/ 
				
				
				def parsedMsg = Util.parseLeaveMsg(user)
				if (parsedMsg[0] && parsedMsg[0] > parsedMsg[2]) {
					def teMsg = "${user.teacher.fullName}留言: " + parsedMsg[1] 
					if (flash.message) {
						flash.message = flash.message+"。。。。。。${teMsg}"
					} else {					
						flash.message = teMsg
					}
				}
				
				if (user.active) {
					// redirect(controller:"endUser", action:"homepage")  // not good as http://job.bootcss.com/assets/css/app.min.css is not accessible
					// redirect(controller:"student", action:"hpage")
					redirect(controller:"assignmentStatus", action:"list")
				}
				else {
					redirect(action:"login")
				}
				
			} else if (session.user instanceof Teacher) {
				redirect(controller:"teacher", action:"thome", id:session.user.id)
			} else if (session.user instanceof School) {
				redirect(controller:"school", action:"show", id:session.user.id)
			} else {
			    render('No defined the details for this endUser')
			    redirect(controller:"endUser", action:"show", id:session.user.id)
			}
		
		} else {
			flash.message = "Sorry, ${params.userName}. Please try again."
			redirect(action:"login")
		}
	}
	
	def homepage() {
		[isHompage:true]
	}

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[endUserInstanceList: EndUser.list(params), endUserInstanceTotal: EndUser.count()]
	}

	def create() {
		if (params.userType == 'Student') {
			[endUserInstance: new Student(params)]
		} else if (params.userType == 'Teacher'){
			[endUserInstance: new Teacher(params)]
		} else if (params.userType == 'School'){
			[endUserInstance: new School(params)]
		}
	}

	def save() {

		def endUserInstance = new EndUser(params)
		if (!endUserInstance.save(flush: true)) {
			render(view: "create", model: [endUserInstance: endUserInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [
			message(code: 'endUser.label', default: 'EndUser'),
			endUserInstance.id
		])
		redirect(action: "show", id: endUserInstance.id)
	}

	def show(Long id) {
		def endUserInstance = EndUser.get(id)
		if (!endUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'endUser.label', default: 'EndUser'),
				id
			])
			redirect(action: "list")
			return
		}

		[endUserInstance: endUserInstance]
	}

	def edit(Long id) {
		def endUserInstance = EndUser.get(id)
		if (!endUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'endUser.label', default: 'EndUser'),
				id
			])
			redirect(action: "list")
			return
		}

		[endUserInstance: endUserInstance]
	}

	def update(Long id, Long version) {
		def endUserInstance = EndUser.get(id)
		if (!endUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'endUser.label', default: 'EndUser'),
				id
			])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (endUserInstance.version > version) {
				endUserInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[
							message(code: 'endUser.label', default: 'EndUser')] as Object[],
						"Another user has updated this EndUser while you were editing")
				render(view: "edit", model: [endUserInstance: endUserInstance])
				return
			}
		}

		endUserInstance.properties = params

		if (!endUserInstance.save(flush: true)) {
			render(view: "edit", model: [endUserInstance: endUserInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [
			message(code: 'endUser.label', default: 'EndUser'),
			endUserInstance.id
		])
		redirect(action: "show", id: endUserInstance.id)
	}

	def delete(Long id) {
		def endUserInstance = EndUser.get(id)
		if (!endUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'endUser.label', default: 'EndUser'),
				id
			])
			redirect(action: "list")
			return
		}

		try {
			endUserInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [
				message(code: 'endUser.label', default: 'EndUser'),
				id
			])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [
				message(code: 'endUser.label', default: 'EndUser'),
				id
			])
			redirect(action: "show", id: id)
		}
	}
}
