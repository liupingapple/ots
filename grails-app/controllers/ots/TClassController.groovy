package ots

import org.springframework.dao.DataIntegrityViolationException

class TClassController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [TClassInstanceList: TClass.list(params), TClassInstanceTotal: TClass.count()]
    }

    def create() {
        [TClassInstance: new TClass(params)]
    }

    def save() {
        def TClassInstance = new TClass(params)
        if (!TClassInstance.save(flush: true)) {
            render(view: "create", model: [TClassInstance: TClassInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'TClass.label', default: 'TClass'), TClassInstance.id])
        redirect(action: "show", id: TClassInstance.id)
    }

    def show(Long id) {
        def TClassInstance = TClass.get(id)
        if (!TClassInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'TClass.label', default: 'TClass'), id])
            redirect(action: "list")
            return
        }

        [TClassInstance: TClassInstance]
    }

    def edit(Long id) {
        def TClassInstance = TClass.get(id)
        if (!TClassInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'TClass.label', default: 'TClass'), id])
            redirect(action: "list")
            return
        }

        [TClassInstance: TClassInstance]
    }

    def update(Long id, Long version) {
        def TClassInstance = TClass.get(id)
        if (!TClassInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'TClass.label', default: 'TClass'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (TClassInstance.version > version) {
                TClassInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'TClass.label', default: 'TClass')] as Object[],
                          "Another user has updated this TClass while you were editing")
                render(view: "edit", model: [TClassInstance: TClassInstance])
                return
            }
        }

        TClassInstance.properties = params

        if (!TClassInstance.save(flush: true)) {
            render(view: "edit", model: [TClassInstance: TClassInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'TClass.label', default: 'TClass'), TClassInstance.id])
        redirect(action: "show", id: TClassInstance.id)
    }

    def delete(Long id) {
        def TClassInstance = TClass.get(id)
        if (!TClassInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'TClass.label', default: 'TClass'), id])
            redirect(action: "list")
            return
        }

        try {
            TClassInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'TClass.label', default: 'TClass'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'TClass.label', default: 'TClass'), id])
            redirect(action: "show", id: id)
        }
    }
}
