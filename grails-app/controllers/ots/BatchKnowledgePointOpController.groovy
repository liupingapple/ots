package ots

import java.util.regex.Pattern

import org.springframework.dao.DataIntegrityViolationException

class BatchKnowledgePointOpController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def beforeInterceptor = [action:this.&checkUser]
	
	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'adminUser',action:'login')
			return false
		} else if (!session.user.admin) {
			flash.message = "Sorry, but you need admistrator privilege for Batch Knowledge Import/Export."
			redirect(controller:'question', action:'list')
			return false
		}
	}
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [batchKnowledgePointOpInstanceList: BatchKnowledgePointOp.list(params), batchKnowledgePointOpInstanceTotal: BatchKnowledgePointOp.count()]
    }

    def create() {
        [batchKnowledgePointOpInstance: new BatchKnowledgePointOp(params)]
    }

	def executeBatch() {
		def batchKnowledgePointOpInstance = new BatchKnowledgePointOp(params)
		if (!batchKnowledgePointOpInstance.save(flush: true)) {
			flash.message = "Sorry. Batch ${batchKnowledgePointOpInstance.operation} Failed!"
			render(view: "create", model: [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance])
			return
		}

		if (batchKnowledgePointOpInstance.operation == "Import node content") {

			def kpList = batchKnowledgePointOpInstance.batchContent.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def nodeIndex = 1
			def lineIndex = 0
			def failedOnes = "Failed:"
			def skippedLines = "Skipped:@@"
			KnowledgePoint lastKp = null
			
			for (kp in kpList) {
				lineIndex++
				if (kp.contains("代码\t名称\t内容") || !(kp.contains("[") && kp.contains("]"))) {
					skippedLines += " ${lineIndex}: ${kp}@@"
					if (lastKp) {
						lastKp.content += "\n" + kp
						lastKp.save(flush: true)
					}
					continue
				}
				def fields = kp.split("\t")
				KnowledgePoint newKp = new KnowledgePoint()
				//newKp.name = fields[0].trim()
				newKp.name = fields[0].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', '')
				if (newKp.name != fields[1].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', '')) {
					println "Warning: " + kp
				}
				if (fields.length > 2) {
					newKp.content = fields[2]?.trim().replaceAll("^\"|\"\$", "")
				}
				
				if (!newKp.save()) {
					failedNumber++
					failedOnes += " ${nodeIndex}(${fields[0]})"
				} else {
					passedNumber++
					lastKp = newKp
				}
				
				nodeIndex++
			}
			
			flash.message = "Batch Import of node content finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
			// println skippedLines
			
		} else if (batchKnowledgePointOpInstance.operation == "Import parent relationship") {
			def kppList = batchKnowledgePointOpInstance.batchContent.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def nodeIndex = 1
			def lineIndex = 0
			def failedOnes = "Failed:"
			def skippedLines = "Skipped:@@"
			
			for (kp in kppList) {
				lineIndex++
				if (kp.contains("下游点\t上游点") || !(kp.contains("[") && kp.contains("]"))) {
					skippedLines += " ${lineIndex}: ${kp}@@"
					continue
				}
				def fields = kp.split("\t")
				KnowledgePoint childKp = KnowledgePoint.findByName(fields[0].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				KnowledgePoint parentKp = KnowledgePoint.findByName(fields[1].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))

				if (childKp && parentKp) {
					childKp.addToParentPoints(parentKp)
					parentKp.addToChildPoints(childKp)

					if (!childKp.save() || !parentKp.save()) {
						failedNumber++
						failedOnes += " ${nodeIndex}(${kp})"
					} else {
						passedNumber++
					}
				} else {
					failedNumber++
					failedOnes += " ${nodeIndex}(${kp})"
				}
				
				nodeIndex++
			}
			
			flash.message = "Batch Import of parent relationship finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
			// println skippedLines
		} else if (batchKnowledgePointOpInstance.operation == "Import child relationship") {
			def kppList = batchKnowledgePointOpInstance.batchContent.split("\n")
			def failedNumber = 0
			def passedNumber = 0
			def nodeIndex = 1
			def lineIndex = 0
			def failedOnes = "Failed:"
			def skippedLines = "Skipped:@@"
			
			for (kp in kppList) {
				lineIndex++
				if (kp.contains("上游点\t下游点") || !(kp.contains("[") && kp.contains("]"))) {
					skippedLines += " ${lineIndex}: ${kp}@@"
					continue
				}
				def fields = kp.split("\t")
				KnowledgePoint parentKp = KnowledgePoint.findByName(fields[0].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				KnowledgePoint childKp = KnowledgePoint.findByName(fields[1].trim().replaceAll("^\"|\"\$", "").replace('[', '').replace(']', ''))
				
				if (childKp && parentKp) {
					childKp.addToParentPoints(parentKp)
					parentKp.addToChildPoints(childKp)

					if (!childKp.save() || !parentKp.save()) {
						failedNumber++
						failedOnes += " ${nodeIndex}(${kp})"
					} else {
						passedNumber++
					}
				} else {
					failedNumber++
					failedOnes += " ${nodeIndex}(${kp})"
				}
				
				nodeIndex++
			}
			
			flash.message = "Batch Import of parent relationship finished. Total: Pass = ${passedNumber}; Fail = ${failedNumber}. ${failedOnes}"
			// println skippedLines
		} else if (batchKnowledgePointOpInstance.operation == "Export node content") {
			def kbIndex = 0
			def exportCount = 0
			def c = KnowledgePoint.createCriteria()
			def results = c.list {
				maxResults(10000)
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "代码\t名称\t内容\n"
			Iterator<KnowledgePoint> kbIt =  results.iterator();
			while(kbIt.hasNext()){
				KnowledgePoint kb = (KnowledgePoint)kbIt.next();
				kbIndex++;
				if (kbIndex < batchKnowledgePointOpInstance.startIndex) {
					continue
				} else if (kbIndex > batchKnowledgePointOpInstance.endIndex) {
					break
				}
				
				exportCount++;
				sb << "[${kb.name}]\t[${kb.name}]\t${kb.content}\n"
			}
			
			batchKnowledgePointOpInstance.batchContent = sb.toString()
			flash.message = "Great News. Batch ${batchKnowledgePointOpInstance.operation} Succeed! Total = ${exportCount}"
		} else if (batchKnowledgePointOpInstance.operation == "Export parent relationship") {
			def kbIndex = 0
			def exportCount = 0
			def c = KnowledgePoint.createCriteria()
			def results = c.list {
				maxResults(10000)
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "下游点\t上游点\n"
			Iterator<KnowledgePoint> kbIt =  results.iterator();
			while(kbIt.hasNext()){
				KnowledgePoint kb = (KnowledgePoint)kbIt.next();
				kbIndex++;
				if (kbIndex < batchKnowledgePointOpInstance.startIndex) {
					continue
				} else if (kbIndex > batchKnowledgePointOpInstance.endIndex) {
					break
				}
				
				for (kbParent in kb.parentPoints) {
					exportCount++;
					sb << "[${kb.name}]\t[${kbParent.name}]\n"
				}
			}
			
			batchKnowledgePointOpInstance.batchContent = sb.toString()
			flash.message = "Great News. Batch ${batchKnowledgePointOpInstance.operation} Succeed! Total = ${exportCount}"
		} else if (batchKnowledgePointOpInstance.operation == "Export child relationship") {
			def kbIndex = 0
			def exportCount = 0
			def c = KnowledgePoint.createCriteria()
			def results = c.list {
				maxResults(10000)
				order("id", "desc")
			}
	
			StringBuilder sb = new StringBuilder()
			sb << "上游点\t下游点\n"
			Iterator<KnowledgePoint> kbIt =  results.iterator();
			while(kbIt.hasNext()){
				KnowledgePoint kb = (KnowledgePoint)kbIt.next();
				kbIndex++;
				if (kbIndex < batchKnowledgePointOpInstance.startIndex) {
					continue
				} else if (kbIndex > batchKnowledgePointOpInstance.endIndex) {
					break
				}
				
				for (kbChild in kb.childPoints) {
					exportCount++;
					sb << "[${kb.name}]\t[${kbChild.name}]\n"
				}
			}
			
			batchKnowledgePointOpInstance.batchContent = sb.toString()
			flash.message = "Great News. Batch ${batchKnowledgePointOpInstance.operation} Succeed! Total = ${exportCount}"
		}
		render(view: "create", model: [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance])
		//println flash.message
	}

    def save() {
        def batchKnowledgePointOpInstance = new BatchKnowledgePointOp(params)
        if (!batchKnowledgePointOpInstance.save(flush: true)) {
            render(view: "create", model: [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), batchKnowledgePointOpInstance.id])
        redirect(action: "show", id: batchKnowledgePointOpInstance.id)
    }

    def show(Long id) {
        def batchKnowledgePointOpInstance = BatchKnowledgePointOp.get(id)
        if (!batchKnowledgePointOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), id])
            redirect(action: "list")
            return
        }

        [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance]
    }

    def edit(Long id) {
        def batchKnowledgePointOpInstance = BatchKnowledgePointOp.get(id)
        if (!batchKnowledgePointOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), id])
            redirect(action: "list")
            return
        }

        [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance]
    }

    def update(Long id, Long version) {
        def batchKnowledgePointOpInstance = BatchKnowledgePointOp.get(id)
        if (!batchKnowledgePointOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (batchKnowledgePointOpInstance.version > version) {
                batchKnowledgePointOpInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp')] as Object[],
                          "Another user has updated this BatchKnowledgePointOp while you were editing")
                render(view: "edit", model: [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance])
                return
            }
        }

        batchKnowledgePointOpInstance.properties = params

        if (!batchKnowledgePointOpInstance.save(flush: true)) {
            render(view: "edit", model: [batchKnowledgePointOpInstance: batchKnowledgePointOpInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), batchKnowledgePointOpInstance.id])
        redirect(action: "show", id: batchKnowledgePointOpInstance.id)
    }

    def delete(Long id) {
        def batchKnowledgePointOpInstance = BatchKnowledgePointOp.get(id)
        if (!batchKnowledgePointOpInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), id])
            redirect(action: "list")
            return
        }

        try {
            batchKnowledgePointOpInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp'), id])
            redirect(action: "show", id: id)
        }
    }
}
