package ots

import org.springframework.dao.DataIntegrityViolationException

class ChapterPieceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [chapterPieceInstanceList: ChapterPiece.list(params), chapterPieceInstanceTotal: ChapterPiece.count()]
    }

    def create() {
        [chapterPieceInstance: new ChapterPiece(params)]
    }

    def save() {
        def chapterPieceInstance = new ChapterPiece(params)
        if (!chapterPieceInstance.save(flush: true)) {
            render(view: "create", model: [chapterPieceInstance: chapterPieceInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), chapterPieceInstance.id])
        redirect(action: "show", id: chapterPieceInstance.id)
    }

    def show(Long id) {
        def chapterPieceInstance = ChapterPiece.get(id)
        if (!chapterPieceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), id])
            redirect(action: "list")
            return
        }

        [chapterPieceInstance: chapterPieceInstance]
    }

    def edit(Long id) {
        def chapterPieceInstance = ChapterPiece.get(id)
        if (!chapterPieceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), id])
            redirect(action: "list")
            return
        }

        [chapterPieceInstance: chapterPieceInstance]
    }

    def update(Long id, Long version) {
        def chapterPieceInstance = ChapterPiece.get(id)
        if (!chapterPieceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (chapterPieceInstance.version > version) {
                chapterPieceInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'chapterPiece.label', default: 'ChapterPiece')] as Object[],
                          "Another user has updated this ChapterPiece while you were editing")
                render(view: "edit", model: [chapterPieceInstance: chapterPieceInstance])
                return
            }
        }

        chapterPieceInstance.properties = params

        if (!chapterPieceInstance.save(flush: true)) {
            render(view: "edit", model: [chapterPieceInstance: chapterPieceInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), chapterPieceInstance.id])
        redirect(action: "show", id: chapterPieceInstance.id)
    }

    def delete(Long id) {
        def chapterPieceInstance = ChapterPiece.get(id)
        if (!chapterPieceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), id])
            redirect(action: "list")
            return
        }

        try {
            chapterPieceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'chapterPiece.label', default: 'ChapterPiece'), id])
            redirect(action: "show", id: id)
        }
    }
}
