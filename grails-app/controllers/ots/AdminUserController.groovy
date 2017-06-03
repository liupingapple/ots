package ots

import groovy.sql.Sql;

import org.springframework.dao.DataIntegrityViolationException

class AdminUserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def beforeInterceptor = [action:this.&checkUser, except: ['login', 'logout', 'authenticate']]

	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'adminUser',action:'login')
			return false
		} else if (!(session.user instanceof AdminUser)) {
		    flash.message = "Sorry, you are not Admin User, the operation you want to do is not allowed"
			session.user = null
			redirect(controller:'adminUser', action:'login')
			return false
		} else if (!session.user.admin) {
			redirect(controller:'question', action:'list')
			return false
		}
	}

	def login = {}
	
	def logout = {
		flash.message = "Goodbye ${session.user?.fullName}"
		session.user = null
		redirect(controller:'adminUser', action:"login")
	}
	
	def authenticate = {
		def user = AdminUser.findByUserNameAndPassword(params.userName, params.password)
		if(user){
			session.user = user
			flash.message = "Hello ${user.fullName}!"
			if(user.admin){
				redirect(controller:"adminUser", action:"admin")
			} else{
				redirect(controller:"question", action:"list")
			}
		} else {
			flash.message = "Sorry, ${params.userName}. Please try again."
			redirect(action:"login")
		}
	}
	
    def index() {
        redirect(action: "list")
    }
	
	def admin() {		
	}
	
	def report() {
		/*def results = StuAnswer.withCriteria {
			projections {
				quizQuestionRecord {
					groupProperty('chosenFor', 'KP_SELECTED')
					
					chosenFor {
						between ("totalQuestion", 3, 200)
					}
				}
			}
			
			eq('correct', false)
		}
		
		[hasWrongAnsweredQuestKPs:results]*/
		
		[kpList : KnowledgePoint.findAllByTotalQuestionBetween(3, 200)]
	}
	
	def filterKnowledge() {
		def c = KnowledgePoint.createCriteria()
		
		if (!params.nameFilter) {
			params.nameFilter = ""
		}
		
		def kpList = c.list(sort: 'totalQuestion', order: 'desc') {
			and {
				ilike("name", "%${params.nameFilter}%")
				between ("totalQuestion", 3, 200)
			}
		}
	    render(template: 'kp_list', model:  [kpList: kpList])
	}

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [adminUserInstanceList: AdminUser.list(params), adminUserInstanceTotal: AdminUser.count()]
    }

    def create() {
        [adminUserInstance: new AdminUser(params)]
    }

    def save() {
        def adminUserInstance = new AdminUser(params)
        if (!adminUserInstance.save(flush: true)) {
            render(view: "create", model: [adminUserInstance: adminUserInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), adminUserInstance.id])
        redirect(action: "show", id: adminUserInstance.id)
    }

    def show(Long id) {
        def adminUserInstance = AdminUser.get(id)
        if (!adminUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), id])
            redirect(action: "list")
            return
        }

        [adminUserInstance: adminUserInstance]
    }

    def edit(Long id) {
        def adminUserInstance = AdminUser.get(id)
        if (!adminUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), id])
            redirect(action: "list")
            return
        }

        [adminUserInstance: adminUserInstance]
    }

    def update(Long id, Long version) {
        def adminUserInstance = AdminUser.get(id)
        if (!adminUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (adminUserInstance.version > version) {
                adminUserInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'adminUser.label', default: 'AdminUser')] as Object[],
                          "Another user has updated this AdminUser while you were editing")
                render(view: "edit", model: [adminUserInstance: adminUserInstance])
                return
            }
        }

        adminUserInstance.properties = params

        if (!adminUserInstance.save(flush: true)) {
            render(view: "edit", model: [adminUserInstance: adminUserInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), adminUserInstance.id])
        redirect(action: "show", id: adminUserInstance.id)
    }

    def delete(Long id) {
        def adminUserInstance = AdminUser.get(id)
        if (!adminUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), id])
            redirect(action: "list")
            return
        }

        try {
            adminUserInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'adminUser.label', default: 'AdminUser'), id])
            redirect(action: "show", id: id)
        }
    }	
}
