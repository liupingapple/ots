package ots

import org.springframework.dao.DataIntegrityViolationException

class KnowledgePointController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def beforeInterceptor = [action:this.&checkUser]
	
	def checkUser() {
		if (!session.user) {
			// i.e. user not logged in
			redirect(controller:'adminUser',action:'login')
			return false
		} else if (!(session.user instanceof AdminUser)) {
			flash.message = "Sorry, you are not Admin User, please try to log in with an End User account"
			redirect(controller:"student", action:"quizList", id:session.user.id)
			return false
		} else if (session.user.role == "inputer") {
			redirect(controller:'question', action:'list')
			return false
		}
	}
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		
		def c = KnowledgePoint.createCriteria()
		def results = c.list(max: params.max, offset: params.offset, sort: params.sort, order: params.order) {
			and {
				if(params.searchable && params.searchCategory) {
					if (params.searchCategory == "idlt") {
						lt("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "id") {
						eq("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "idgt") {
						gt("id", Long.parseLong(params.searchable))
					} else if (params.searchCategory == "masterRatiolt" || params.searchCategory == "associatedQuestionlt" || params.searchCategory == "totalQuestionlt") {
						lt(params.searchCategory.replaceAll("lt\$", ""), Integer.parseInt(params.searchable))
					} else if (params.searchCategory == "masterRatio" || params.searchCategory == "associatedQuestion" || params.searchCategory == "totalQuestion") {
						eq(params.searchCategory, Integer.parseInt(params.searchable))
					} else if (params.searchCategory == "masterRatiogt" || params.searchCategory == "associatedQuestiongt" || params.searchCategory == "totalQuestiongt") {
						gt(params.searchCategory.replaceAll("gt\$", ""), Integer.parseInt(params.searchable))
					} else if (params.searchCategory == "parentPoints") {
						parentPoints {
							ilike("name", "${params.searchable}%")
						}
					} else if (params.searchCategory == "childPoints") {
						childPoints {
							ilike("name", "${params.searchable}%")
						}
					} else {
						ilike("${params.searchCategory}", "${params.searchable}%")
					}
				 } else {
					if(params.searchCategory) {
						if (params.searchCategory == "parentPoints" || params.searchCategory == "childPoints") {
							isEmpty(params.searchCategory)
						} else {
							or {
								isNull(params.searchCategory)
								eq("${params.searchCategory}", "")
							}
						}
					}
				}
			}
		}

		if (!(params.searchCategory || params.searchable)) {
			params.searchCategory = "name"
			params.searchable = "%"
		}
		
		results = results.unique{it}
		
		if (session["linkingQuestionID"]) {
        	[questionInstance: Question.get(session["linkingQuestionID"]), searchCategory:params.searchCategory, searchKeyword: params.searchable, knowledgePointInstanceList: results, knowledgePointInstanceTotal: results.totalCount]
		} else if (session["linkingKnowledgePointID"]) {
			[linkingKnowledgePointInstance: KnowledgePoint.get(session["linkingKnowledgePointID"]), searchCategory:params.searchCategory, searchKeyword: params.searchable, knowledgePointInstanceList: results, knowledgePointInstanceTotal: results.totalCount]
		} else {
			[searchCategory:params.searchCategory, searchKeyword: params.searchable, knowledgePointInstanceList: results, knowledgePointInstanceTotal: results.totalCount]
		}
    }

    def create() {
        [knowledgePointInstance: new KnowledgePoint(params)]
    }
	
	def view(Long id) {
		if (!id && params.root) {
			id = Long.parseLong(params.root, 10)
		}

		def c = KnowledgePoint.createCriteria()
		def knowledgePointList1
		def knowledgePoint1 = KnowledgePoint.get(params.kp1)
		def knowledgePointList2
		def knowledgePoint2
		def knowledgePointList3
		def knowledgePoint3
		def knowledgePointList4
		def knowledgePoint4
		def knowledgePointList5
		def knowledgePoint5
		
		if (!id) {
			knowledgePointList1 = c.list() {
				parentPoints {
					ilike("name", "*")
				}
				// isEmpty("parentPoints")
			}
		} else {
			knowledgePointList1 = c.list() {
				eq("id", id)
			}
		}

		knowledgePointList2 = knowledgePoint1?.childPoints?.sort { it.name }
		if (params.kp1Updated == "false") {
			knowledgePoint2 = KnowledgePoint.get(params.kp2)
			knowledgePointList3 = knowledgePoint2?.childPoints?.sort { it.name }
			if (params.kp2Updated == "false") {
				knowledgePoint3 = KnowledgePoint.get(params.kp3)
				knowledgePointList4 = knowledgePoint3?.childPoints?.sort { it.name }
				if (params.kp3Updated == "false") {
					knowledgePoint4 = KnowledgePoint.get(params.kp4)
					knowledgePointList5 = knowledgePoint4?.childPoints?.sort { it.name }
					if (params.kp4Updated == "false") {
						knowledgePoint5 = KnowledgePoint.get(params.kp5)
					}
				}
			}
		}

		[knowledgePointList1: knowledgePointList1, knowledgePoint1:knowledgePoint1, 
		 knowledgePointList2: knowledgePointList2, knowledgePoint2:knowledgePoint2,
		 knowledgePointList3: knowledgePointList3, knowledgePoint3:knowledgePoint3,
		 knowledgePointList4: knowledgePointList4, knowledgePoint4:knowledgePoint4,
		 knowledgePointList5: knowledgePointList5, knowledgePoint5:knowledgePoint5,
		 rootID: id]
	}
	
    def save() {
        def knowledgePointInstance = new KnowledgePoint(params)

		if (!knowledgePointInstance.save(flush: true)) {
			render(view: "create", model: [knowledgePointInstance: knowledgePointInstance])
			return
		}
		
		/*
		Iterator<KnowledgePoint> kpIt =  knowledgePointInstance.parentPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
 			kp.addToChildPoints(knowledgePointInstance)
			kp.save()
		}
		
		kpIt =  knowledgePointInstance.childPoints?.iterator();
		def quesNum = 0
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			kp.addToParentPoints(knowledgePointInstance)
			quesNum += kp.totalQuestion
			kp.save()
		}
		knowledgePointInstance.totalQuestion = quesNum
		knowledgePointInstance.save()
		*/
		
        flash.message = message(code: 'default.created.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), knowledgePointInstance.id])
        redirect(action: "show", id: knowledgePointInstance.id)
    }

    def show(Long id) {
        def knowledgePointInstance = KnowledgePoint.get(id)
        if (!knowledgePointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
            redirect(action: "list")
            return
        }

		if (session["linkingQuestionID"]) {
			def questionInstance = Question.get(session["linkingQuestionID"])
			if (questionInstance.knowledgePoints.contains(knowledgePointInstance)) {
				[enableUnlink: true, questionInstance: questionInstance, knowledgePointInstance: knowledgePointInstance]
			} else {
				[questionInstance: questionInstance, knowledgePointInstance: knowledgePointInstance]
			}
		} else if (session["linkingKnowledgePointID"]) {
			def linkingKnowledgePointInstance = KnowledgePoint.get(session["linkingKnowledgePointID"])
			if (linkingKnowledgePointInstance.parentPoints.contains(knowledgePointInstance)) {
				[enableUnlinkParent: true, linkingKnowledgePointInstance: linkingKnowledgePointInstance, knowledgePointInstance: knowledgePointInstance]
			} else if (linkingKnowledgePointInstance.childPoints.contains(knowledgePointInstance) ) {
				[enableUnlinkChild: true, linkingKnowledgePointInstance: linkingKnowledgePointInstance, knowledgePointInstance: knowledgePointInstance]
			} else {
				[linkingKnowledgePointInstance: linkingKnowledgePointInstance, knowledgePointInstance: knowledgePointInstance]
			}
		} else {
			[knowledgePointInstance: knowledgePointInstance]
		}

        
    }

	def beginLink(Long id) {
		session["linkingKnowledgePointID"] = id
		redirect(action: "list")
	}

	def cancelLink(Long id) {
		session["linkingKnowledgePointID"] = null
		redirect(action: "list")
	}

	def linkedAsParent(Long id) {
		def linkingKnowledgePointInstance = KnowledgePoint.get(session["linkingKnowledgePointID"])
		def knowledgePointInstance = KnowledgePoint.get(id)
		linkingKnowledgePointInstance.addToParentPoints(knowledgePointInstance)
		knowledgePointInstance.addToChildPoints(linkingKnowledgePointInstance)
		knowledgePointInstance.totalQuestion += linkingKnowledgePointInstance.totalQuestion
		linkingKnowledgePointInstance.save()
		knowledgePointInstance.save()
		session["linkingKnowledgePointID"] = null
		redirect(action: "show", id: linkingKnowledgePointInstance.id)
	}

	def unlinkedAsParent(Long id) {
		def linkingKnowledgePointInstance = KnowledgePoint.get(session["linkingKnowledgePointID"])
		def knowledgePointInstance = KnowledgePoint.get(id)
		linkingKnowledgePointInstance.removeFromParentPoints(knowledgePointInstance)
		knowledgePointInstance.removeFromChildPoints(linkingKnowledgePointInstance)
		knowledgePointInstance.totalQuestion -= linkingKnowledgePointInstance.totalQuestion
		linkingKnowledgePointInstance.save()
		knowledgePointInstance.save()
		session["linkingKnowledgePointID"] = null
		redirect(action: "show", id: linkingKnowledgePointInstance.id)
	}

	def linkedAsChild(Long id) {
		def linkingKnowledgePointInstance = KnowledgePoint.get(session["linkingKnowledgePointID"])
		def knowledgePointInstance = KnowledgePoint.get(id)
		linkingKnowledgePointInstance.addToChildPoints(knowledgePointInstance)
		knowledgePointInstance.addToParentPoints(linkingKnowledgePointInstance)
		linkingKnowledgePointInstance.totalQuestion += knowledgePointInstance.totalQuestion 
		linkingKnowledgePointInstance.save()
		knowledgePointInstance.save()
		session["linkingKnowledgePointID"] = null
		redirect(action: "show", id: linkingKnowledgePointInstance.id)
	}

	def unlinkedAsChild(Long id) {
		def linkingKnowledgePointInstance = KnowledgePoint.get(session["linkingKnowledgePointID"])
		def knowledgePointInstance = KnowledgePoint.get(id)
		linkingKnowledgePointInstance.removeFromChildPoints(knowledgePointInstance)
		knowledgePointInstance.removeFromParentPoints(linkingKnowledgePointInstance)
		linkingKnowledgePointInstance.totalQuestion -= knowledgePointInstance.totalQuestion
		linkingKnowledgePointInstance.save()
		knowledgePointInstance.save()
		session["linkingKnowledgePointID"] = null
		redirect(action: "show", id: linkingKnowledgePointInstance.id)
	}

	def finishLinkQuestion(Long id) {
		def qID = session["linkingQuestionID"]
		def questionInstance = Question.get(qID)
		def knowledgePointInstance = KnowledgePoint.get(id)
		knowledgePointInstance.associatedQuestion++
		knowledgePointInstance.totalQuestion++
		knowledgePointInstance.save()
		Iterator<KnowledgePoint> kpIt =  knowledgePointInstance.parentPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			kp.totalQuestion++
			kp.save()
		}
		questionInstance.addToKnowledgePoints(knowledgePointInstance)
		questionInstance.inputBy = session.user
		questionInstance.save()
		session["linkingQuestionID"] = null
		redirect(controller:"question", action: "show", id:qID)
	}

	def finishUnlinkQuestion(Long id) {
		def qID = session["linkingQuestionID"]
		def questionInstance = Question.get(qID)
		def knowledgePointInstance = KnowledgePoint.get(id)
		knowledgePointInstance.associatedQuestion--
		knowledgePointInstance.totalQuestion--
		knowledgePointInstance.save()
		Iterator<KnowledgePoint> kpIt =  knowledgePointInstance.parentPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			kp.totalQuestion--
			kp.save()
		}
		questionInstance.removeFromKnowledgePoints(knowledgePointInstance)
		questionInstance.save()
		session["linkingQuestionID"] = null
		redirect(controller:"question", action: "show", id:qID)
	}

	def backToQuestion(Long id) { 
		session["linkingQuestionID"] = null
		redirect(controller:"question", action: "show", id: session.showQuestionID)
	}
	
    def edit(Long id) {
        def knowledgePointInstance = KnowledgePoint.get(id)
        if (!knowledgePointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
            redirect(action: "list")
            return
        }
		
		/*
		def toBeUpdatedParents =  [:]
		def toBeUpdatedChildren =  [:]
		
		Iterator<KnowledgePoint> kpIt =  knowledgePointInstance.parentPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			toBeUpdatedParents.put(kp.name, true)
		}
		
		kpIt =  knowledgePointInstance.childPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			toBeUpdatedChildren.put(kp.name, true)
		}
		
		session["toBeUpdatedParents"] = toBeUpdatedParents
		session["toBeUpdatedChildren"] = toBeUpdatedChildren
		*/
		
        [knowledgePointInstance: knowledgePointInstance]
    }

    def update(Long id, Long version) {
        def knowledgePointInstance = KnowledgePoint.get(id)
        if (!knowledgePointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (knowledgePointInstance.version > version) {
                knowledgePointInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'knowledgePoint.label', default: 'KnowledgePoint')] as Object[],
                          "Another user has updated this KnowledgePoint while you were editing")
                render(view: "edit", model: [knowledgePointInstance: knowledgePointInstance])
                return
            }
        }

        knowledgePointInstance.properties = params
		
        if (!knowledgePointInstance.save(flush: true)) {
            render(view: "edit", model: [knowledgePointInstance: knowledgePointInstance])
            return
        }

		/*
		def toBeUpdatedParents = session["toBeUpdatedParents"] 
		def toBeUpdatedChildren = session["toBeUpdatedChildren"]
		
		Iterator<KnowledgePoint> kpIt =  knowledgePointInstance.parentPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			if (toBeUpdatedParents?.containsKey(kp.name)) {
				toBeUpdatedParents[kp.name] = false
			} else {
				kp.addToChildPoints(knowledgePointInstance)
				kp.save()
			}
		}
		
		toBeUpdatedParents.each{ k, v -> 
			if (v) { println k
					KnowledgePoint.findByName(k).removeFromChildPoints(knowledgePointInstance).save() }}
		
		kpIt =  knowledgePointInstance.childPoints?.iterator();
		while(kpIt?.hasNext()){
			KnowledgePoint kp = (KnowledgePoint)kpIt.next();
			if (toBeUpdatedChildren?.containsKey(kp.name)) {
				toBeUpdatedChildren[kp.name] = false
			} else {
				kp.addToParentPoints(knowledgePointInstance)
				kp.save()
			}
		}
		
		toBeUpdatedChildren.each{ k, v -> 
			if (v) { KnowledgePoint.findByName(k).removeFromParentPoints(knowledgePointInstance).save() }}
		*/
		
        flash.message = message(code: 'default.updated.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), knowledgePointInstance.id])
        redirect(action: "show", id: knowledgePointInstance.id)
    }

    def delete(Long id) {
        def knowledgePointInstance = KnowledgePoint.get(id)
        if (!knowledgePointInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
            redirect(action: "list")
            return
        }
		
		if (knowledgePointInstance.parentPoints.empty && knowledgePointInstance.childPoints.empty && knowledgePointInstance.associatedQuestion == 0) {
	        try {
	            knowledgePointInstance.delete(flush: true)
	            flash.message = message(code: 'default.deleted.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
	            redirect(action: "list")
	        }
	        catch (DataIntegrityViolationException e) {
	            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
	            redirect(action: "show", id: id)
	        }
		} else {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'knowledgePoint.label', default: 'KnowledgePoint'), id])
			redirect(action: "show", id: id)
		}
    }
	
	def weixinPracticeLink(Long id) {
		flash.message = WeiXinUtil.WEIXIN_SRV_URL+"/practice?kpList=${id}"
		redirect(action: "show", id: id)
	}
}