package ots

import org.springframework.dao.DataIntegrityViolationException

class AssignmentTemplateController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [assignmentTemplateInstanceList: AssignmentTemplate.list(params), assignmentTemplateInstanceTotal: AssignmentTemplate.count()]
    }

    def create() {
		AssignmentTemplate newTemplate = new AssignmentTemplate(params)
		newTemplate.createdBy = session.user.userName
		def c = KnowledgePoint.createCriteria()
		def kpList = c.list(sort: 'totalQuestion', order: 'desc') {
			gt("totalQuestion", 0)
		}
        [assignmentTemplateInstance: newTemplate, kpList: kpList]
    }

    def save() {
        def assignmentTemplateInstance = new AssignmentTemplate(params)		
        if (!assignmentTemplateInstance.save(flush: true)) {
            render(view: "create", model: [assignmentTemplateInstance: assignmentTemplateInstance])
            return
        }
		
        flash.message = message(code: 'default.created.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), assignmentTemplateInstance.id])
        redirect(action: "show", id: assignmentTemplateInstance.id)
    }

    def show(Long id) {
        def assignmentTemplateInstance = AssignmentTemplate.get(id)
        if (!assignmentTemplateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), id])
            redirect(action: "list")
            return
        }

        [assignmentTemplateInstance: assignmentTemplateInstance]
    }

	def filterKnowledge = {
		def c = KnowledgePoint.createCriteria()
		def kpList = c.list(sort: 'totalQuestion', order: 'desc') {
			and {
				ilike("name", "%${params.nameFilter}%")
				gt("totalQuestion", 0)
			}
		}
	    render(template: 'kp_list', model:  [kpList: kpList])
	}
	
    def edit(Long id) {
        def assignmentTemplateInstance = AssignmentTemplate.get(id)
        if (!assignmentTemplateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), id])
            redirect(action: "list")
            return
        }

		def c = KnowledgePoint.createCriteria()
		def kpList = c.list(sort: 'totalQuestion', order: 'desc') {
			gt("totalQuestion", 2)
		}
		[assignmentTemplateInstance: assignmentTemplateInstance, kpList: kpList]
    }

    def update(Long id, Long version) {
        def assignmentTemplateInstance = AssignmentTemplate.get(id)
        if (!assignmentTemplateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (assignmentTemplateInstance.version > version) {
                assignmentTemplateInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate')] as Object[],
                          "Another user has updated this AssignmentTemplate while you were editing")
                render(view: "edit", model: [assignmentTemplateInstance: assignmentTemplateInstance])
                return
            }
        }

        assignmentTemplateInstance.properties = params

        if (!assignmentTemplateInstance.save(flush: true)) {
            render(view: "edit", model: [assignmentTemplateInstance: assignmentTemplateInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), assignmentTemplateInstance.id])
        redirect(action: "show", id: assignmentTemplateInstance.id)
    }
	
	/* Will get error "java.lang.StackOverflowError" if too many child KPs
	 *  No need to refreshChildKPs for template, when creating new quiz, the child KPs will be refreshed
	 *  
	 * private void refreshChildKPs(knowledgePoints, kpSet)
	{
		knowledgePoints.each { p->
			kpSet << p
			p.childPoints.each { c->				
				refreshChildKPs(c, kpSet)
			}
		}
	}*/

    def delete(Long id) {
        def assignmentTemplateInstance = AssignmentTemplate.get(id)
        if (!assignmentTemplateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), id])
            redirect(action: "list")
            return
        }

        try {
            assignmentTemplateInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate'), id])
            redirect(action: "show", id: id)
        }
    }
}
