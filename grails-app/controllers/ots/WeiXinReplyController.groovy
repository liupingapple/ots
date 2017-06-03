package ots

import org.springframework.dao.DataIntegrityViolationException

class WeiXinReplyController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [weiXinReplyInstanceList: WeiXinReply.list(params), weiXinReplyInstanceTotal: WeiXinReply.count()]
    }

    def create() {
        [weiXinReplyInstance: new WeiXinReply(params)]
    }

    def save() {
        def weiXinReplyInstance = new WeiXinReply(params)
        if (!weiXinReplyInstance.save(flush: true)) {
            render(view: "create", model: [weiXinReplyInstance: weiXinReplyInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), weiXinReplyInstance.id])
        redirect(action: "show", id: weiXinReplyInstance.id)
    }

    def show(Long id) {
        def weiXinReplyInstance = WeiXinReply.get(id)
        if (!weiXinReplyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), id])
            redirect(action: "list")
            return
        }

        [weiXinReplyInstance: weiXinReplyInstance]
    }

    def edit(Long id) {
        def weiXinReplyInstance = WeiXinReply.get(id)
        if (!weiXinReplyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), id])
            redirect(action: "list")
            return
        }

        [weiXinReplyInstance: weiXinReplyInstance]
    }

    def update(Long id, Long version) {
        def weiXinReplyInstance = WeiXinReply.get(id)
        if (!weiXinReplyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (weiXinReplyInstance.version > version) {
                weiXinReplyInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'weiXinReply.label', default: 'WeiXinReply')] as Object[],
                          "Another user has updated this WeiXinReply while you were editing")
                render(view: "edit", model: [weiXinReplyInstance: weiXinReplyInstance])
                return
            }
        }

        weiXinReplyInstance.properties = params

        if (!weiXinReplyInstance.save(flush: true)) {
            render(view: "edit", model: [weiXinReplyInstance: weiXinReplyInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), weiXinReplyInstance.id])
        redirect(action: "show", id: weiXinReplyInstance.id)
    }

    def delete(Long id) {
        def weiXinReplyInstance = WeiXinReply.get(id)
        if (!weiXinReplyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), id])
            redirect(action: "list")
            return
        }

        try {
            weiXinReplyInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'weiXinReply.label', default: 'WeiXinReply'), id])
            redirect(action: "show", id: id)
        }
    }
}
