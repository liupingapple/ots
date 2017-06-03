package ots

import java.sql.ResultSet;
import java.text.SimpleDateFormat

import javassist.bytecode.stackmap.BasicBlock.Catch;
import grails.gorm.DetachedCriteria
import groovy.sql.DataSet
import groovy.sql.Sql
import org.springframework.dao.DataIntegrityViolationException

import antlr.Utils;

class StudentController extends EndUserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def beforeInterceptor = [action:this.&checkUser, except: ['create','save']]

	def checkUser() {
		if (!session.user || session.user instanceof AdminUser && session.user.role != "admin") {
			flash.message = "Sorry, you are not authorized to access this page!"
			redirect(controller:'endUser',action:'login')
			return false
		}
	}
	
    def index() {        
		if (session.user.role == "admin"){
			redirect(action: "list", params: params)
		} else {
			redirect(controller:'endUser',action:'login')
		}
    }
	
//	def hpage() {
//		session.hpage_x = params.hpage_x
//		session.course = ''
//		[hpage_x:params.hpage_x]
//	}
	
	def pcourse_xunlian(int max) {
		session.paction = 'xunlian'
		
		if (params.course) {
			session.course = params.course
		}
		
		if (!params.pick_item) {
			params.pick_item = 'not_begin'
		}
		
		params.max = Math.min(max ?: 10, 100)
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
		def aList = new ArrayList()
						
		def c = AssignmentStatus.createCriteria()
		def results = c.list(max: params.max, offset: params.offset, sort: params.sort, order: params.order) {
			and {
				eq("student", session.user)
				
				assignment {
					eq("subject", session.course)
				}
				
				if (params.pick_item == 'not_begin') {
					eq("status", CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN)
				} else if (params.pick_item == 'in_progress') {
					eq("status", CONSTANT.ASSIGNMENT_STATUS_INPROGRESS)
				} else if (params.pick_item == 'finished') {
					eq("status", CONSTANT.ASSIGNMENT_STATUS_DONE)
				}
				
				if (params.searchValue && params.searchCategory) {
				toBeFocusedKnowledge {
					ilike("name", "%${params.searchValue}%")
				}
				
				ilike("${params.searchCategory}", "%${params.searchValue}%")		
				}	
			}
		}
		
		// results = results.grep{it.subject == session.course}
		
				
		[course:session.course, pick_item:params.pick_item, aList: results, aTotal: results.size()]
	}
	
	def pcourse_chakan(Long id)
	{
		session.paction = 'chakan'
		
		def studentInstance = Student.get(id);
		
		if (params.course) {
			session.course = params.course
		}
		
		if (!params.pick_item) {
			params.pick_item = 'wdzy'
		}
		
		println "params: ${params}"
		
		def retval = []
		/*** 学习进度 与 练习状况   ***/
		if (params.pick_item == 'xxjd' || params.pick_item == 'lxzk') {
			def lineCol = [] // quiz sequence list
			def lineData = [:]  //
			
			def key_masterLevel = '阅读理解掌握水平'
			def key_masterRate = '掌握度'
			def key_latestCoverage = '覆盖率'
			
			def key_score = '练习成绩'
			
			if (session.course == CONSTANT.COURSE_ENG2) {
				lineData.put(key_masterLevel, [])
			}
			else {				
				lineData.put(key_masterRate, [])
				lineData.put(key_latestCoverage, []) // to get this value from quiz.latestCoverage			
			}
			
			def colData = [:]  //
			colData.put(key_score, [])
			
			def c = Quiz.createCriteria()
			def results = c.list(max:12, sort: 'answeredDate', order: 'desc'){
				eq('student', studentInstance)
				isNotNull('answeredDate')
				
				assignment {
					eq("subject", session.course)
				}
			}
			
			def quizList = results.asList() //Quiz.findAllByStudentAndAnsweredDateIsNotNull(studentInstance, [max:12, sort: 'answeredDate', order: 'desc'])
			
			quizList.eachWithIndex { quiz, i ->
				lineCol << i+1
				
				if (quiz.assignment.questionType?.contains("阅读")) {
					lineData[key_masterLevel] << (quiz.masterLevel < 0.0 ? null : quiz.masterLevel)
				}
				else {
					lineData[key_masterRate] << (quiz.masterRate < 0.0 ? null : quiz.masterRate)
					lineData[key_latestCoverage] << (quiz.latestCoverage < 0.0 ? null : quiz.latestCoverage) // to get this value from quiz.latestCoverage
				}
								
				colData[key_score] << quiz.score
			}

			retval = [lineCol:lineCol, lineData:lineData, colData:colData, quizList:quizList]
		}
		/*** 我的作业   ***/
		else if (params.pick_item == 'wdzy') 
		{
			if (!params.aStatusStatus) {
				params.aStatusStatus = CONSTANT.ASSIGNMENT_STATUS_INPROGRESS
			}
			def aStatusList	= studentInstance.aStatus.findAll { elem ->
				elem.status == params.aStatusStatus && elem.assignment.subject == session.course
			}
			
			retval = [aStatusStatus: params.aStatusStatus, aList : aStatusList, aListSize:aStatusList.size()]
		}
		/*** 错题集   ***/
		else if (params.pick_item == 'ctj') 
		{
			def fDate1 = new Date()-365, fDate2 = new Date()+1
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (params.searchDate1) {
				fDate1 = formatter.parse(params.searchDate1 + " 00:00:00")
			}
			if (params.searchDate2) {
				fDate2 = formatter.parse(params.searchDate2 + " 23:59:59")
			}
									
			def results = StuAnswer.withCriteria() {
				projections {
					quizQuestionRecord {						
						groupProperty('question', 'question')
						if (params.searchQuestion) {
							question {
								like('description', "%${params.searchQuestion}%")
							}
						}
						if (params.searchChosenFor) {
							chosenFor {
								like('name', "%${params.searchChosenFor}%")
							}
						}
						
						quiz {
							assignment {
								eq("subject", session.course)
							}
						}
					}
				}
	
				eq('correct', false)
				
				quizQuestionRecord {
					quiz {
						between('answeredDate', fDate1, fDate2)
						eq('student', studentInstance)
					}
				}	
			}
			
			retval = [xQuestions:results]
		}
		
		/*** 薄弱知识点   ***/
		else if (params.pick_item == 'brzsd') {	
			if (!params.maxRecord) {
				params.maxRecord = 10
			}
			
			def selectedAssignments = []
			def fDate1 = new Date()-365, fDate2 = new Date()+1
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (params.searchDate1) {
				fDate1 = formatter.parse(params.searchDate1 + " 00:00:00")
			}
			if (params.searchDate2) {
				fDate2 = formatter.parse(params.searchDate2 + " 23:59:59")
			}
			
			def latestAllSelectedStuKCPsMap = [:]
			def latestAllSelectedStuKCPs = []
			
			if (!params.searchAssignments) {
				selectedAssignments = studentInstance.aStatus*.assignment.findAll{ a-> a.subject == session.course}.asList()
			} else {
				def assi = []
				assi << params.searchAssignments
				assi = assi.flatten()
				assi.each {
					def a = Assignment.get(it)
					selectedAssignments << a
				}
			}
			
			// get the latest Quiz by fDate2 and assignment, 
			selectedAssignments.each { a->
				
				def quiz = Quiz.findByStudentAndStatusAndAssignmentAndAnsweredDateBetween(studentInstance, CONSTANT.QUIZ_STATUS_SUBMITTED, a, fDate1, fDate2, [sort:'answeredDate', order:'desc'])
						
				println "quiz: ${quiz}"
				// get the latest KCPs with questions answered of this student
				if (quiz && quiz.assignment.subject == session.course) {
					def stuLatestKCPs = KnowledgeCheckPoint.findAllByQuizAndTotalSumGreaterThan(quiz, 0)
					// latestAllSelectedStuKCPs += stuLatestKCPs
					
					stuLatestKCPs.each { kcp1->
						if (latestAllSelectedStuKCPsMap.containsKey(kcp1.knowledge)) {
							if (latestAllSelectedStuKCPsMap[kcp1.knowledge].quiz.answeredDate < kcp1.quiz.answeredDate ) {
								latestAllSelectedStuKCPsMap.put(kcp1.knowledge, kcp1)
							}
						} else {
							latestAllSelectedStuKCPsMap.put(kcp1.knowledge, kcp1)
						}
					}
				}
			}
			
			latestAllSelectedStuKCPs += latestAllSelectedStuKCPsMap.values()
			
							
			def c = KnowledgeCheckPoint.createCriteria()
			
			def results = c.list(max: params.maxRecord, offset: params.offset) {
						
				order("recentCorrectRate", "asc")
				
				if (latestAllSelectedStuKCPs.size() > 0) {
					'in'('id', latestAllSelectedStuKCPs*.id)
				} else {
					'in' ('id', -1L) // id will not be -1, this condition is to be always false
				}
				
				gt('recentCorrectRate', new BigDecimal(-1))
				eq('student', studentInstance)
			}
			
			println "results: ${results}"
			
			def qnumDetails = [:]
						
			latestAllSelectedStuKCPs.each { r ->
								
				// 学生题目数
				if (qnumDetails.get(r.knowledge.id)) {
					def existingVal = qnumDetails.get(r.knowledge.id)
					existingVal.put(r.student.fullName, r.totalSum)
				} else {
					def qnumStuVal = [:]
					qnumStuVal.put(r.student.fullName, r.totalSum)
					qnumDetails.put(r.knowledge.id, qnumStuVal)
				}
			}
			
			println "qnumDetails: ${qnumDetails}"
			
			retval = [selectedAssignments:selectedAssignments, results:results,
			qnumDetails:qnumDetails, maxRecord: params.maxRecord]
		}
		
		/*** 查看辅导情况   ***/
		else if (params.pick_item == 'kcfdqk')
		{
			// no need to do something here
		}
				
		[studentInstance: studentInstance, course:params.course, pick_item:params.pick_item] + retval
	}
	
	def pcourse_goutong()
	{
		session.paction = 'goutong'
		
		if (params.course) {
			session.course = params.course
		}
		
		
		println "params: ${params}"
		[course:params.course]
	}
	
    def list(Integer max) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        params.max = Math.min(max ?: 10, 100)
		if (session.user.role == "admin") {
			
			session.student_searchCategory = params.searchCategory
			session.student_searchable = params.searchable
			if (params.sort || params.order) {
				session.student_sort = params.sort
				session.student_order = params.order
			}
	
			def c = Student.createCriteria()
			def results = c.list(max: params.max, offset: params.offset, sort: params.sort, order: params.order) {
				and {
					if(params.searchable && params.searchCategory) {
						if (params.searchCategory == "loginDatelt") {
							def fDate = formatter.parse(params.searchable + " 00:00:00")
							lt("loginDate", fDate)
						} else if (params.searchCategory == "loginDate") {
							def fDate1 = formatter.parse(params.searchable + " 00:00:00")
							def fDate2 = formatter.parse(params.searchable + " 23:59:59")
							lt("loginDate", fDate2)
							gt("loginDate", fDate1)
						} else if (params.searchCategory == "loginDategt") {
							def fDate = formatter.parse(params.searchable + " 23:59:59")
							gt("loginDate", fDate)
						} else if (params.searchCategory == "dateCreatedlt") {
							def fDate = formatter.parse(params.searchable + " 00:00:00")
							lt("dateCreated", fDate)
						} else if (params.searchCategory == "dateCreated") {
							def fDate1 = formatter.parse(params.searchable + " 00:00:00")
							def fDate2 = formatter.parse(params.searchable + " 23:59:59")
							lt("dateCreated", fDate2)
							gt("dateCreated", fDate1)
						} else if (params.searchCategory == "dateCreatedgt") {
							def fDate = formatter.parse(params.searchable + " 23:59:59")
							gt("dateCreated", fDate)
						} else if (params.searchCategory == "birthDatelt") {
							def fDate = formatter.parse(params.searchable + " 00:00:00")
							lt("birthDate", fDate)
						} else if (params.searchCategory == "birthDate") {
							def fDate1 = formatter.parse(params.searchable + " 00:00:00")
							def fDate2 = formatter.parse(params.searchable + " 23:59:59")
							lt("birthDate", fDate2)
							gt("birthDate", fDate1)
						} else if (params.searchCategory == "birthDategt") {
							def fDate = formatter.parse(params.searchable + " 23:59:59")
							gt("birthDate", fDate)
						} else if (params.searchCategory == "teacher") {
							def fUser = Teacher.findByUserName(params.searchable.trim())
							if (fUser) { 
								eq(params.searchCategory, fUser)
							} else {
								isNull(params.searchCategory)
							}
						} else {
							ilike("${params.searchCategory}", "${params.searchable}%")
						}
					 }
				}
			}
	
			if (!(params.searchCategory || params.searchable)) {
				params.searchCategory = "fullName"
				params.searchable = "%"
			}
			
			results = results.unique{it}
	
			[searchCategory:params.searchCategory, searchKeyword: params.searchable, studentInstanceList: results, studentInstanceTotal: results.totalCount]
		} else {
			redirect(controller:'endUser',action:'login')
		}
    }

	// register
    def create() {
        [studentInstance: new Student(params)]
    }

    def save() {
        def studentInstance = new Student(params)
		if (studentInstance.targetCorrectRate > 1 || studentInstance.evaluationPower > 1) {
			studentInstance.targetCorrectRate = studentInstance.targetCorrectRate/100
			studentInstance.evaluationPower = studentInstance.evaluationPower/100
		}
		
		if (studentInstance.parents) {
			EndUser p = EndUser.findByUserNameAndRole(studentInstance.parents, 'parents')
			if (!p) {
				studentInstance.parents = null
			}
		}
		
        if (!studentInstance.save(flush: true)) {
            render(view: "create", model: [studentInstance: studentInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'student.label', default: 'Student'), studentInstance.id])
        redirect(action: "show", id: studentInstance.id)
    }
	
	def quizList(Long id) {
		redirect(controller: "quiz", action: "list")
	}

    def show(Long id) {
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
            return
        }
        [studentInstance: studentInstance]
    }
	
	def studyStatus(Long id)
	{
		def studentInstance = Student.get(id)
		def maxKPToShow = studentInstance.maxKPToShow
		def assignmentStatusInstanceList = studentInstance.aStatus
		def assignmentStatusInstanceTotal = assignmentStatusInstanceList.size()
		
		params.max = Math.min(params.max ?: 10, 100)
		def c = Quiz.createCriteria()
		def results = c.list(max: params.max, offset: params.offset, sort: 'answeredDate', order: 'desc') { eq("student", studentInstance)	}
		// sync up AssignmentStatusController.list
		def finishedQuestionsTotal = 0
		def correctQuestionsTotal = 0
		def ongoingTotal = 0
		def notBeginTotal = 0
		def finishedTotal = 0
		def focusKPSet = [] as HashSet
		assignmentStatusInstanceList.each{
			finishedQuestionsTotal += it.finishedQuestions
			correctQuestionsTotal += it.correctQuestions
			if (it.status == CONSTANT.ASSIGNMENT_STATUS_INPROGRESS) {
				ongoingTotal++
			} else if (it.status == CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN) {
				notBeginTotal++
			} else if (it.status == CONSTANT.ASSIGNMENT_STATUS_DONE) {
				finishedTotal++
			}
			focusKPSet << it.toBeFocusedKnowledge
		}
		
		[studentInstance: studentInstance, assignmentStatusInstanceList:assignmentStatusInstanceList, 
			assignmentStatusInstanceTotal:assignmentStatusInstanceTotal, 
			quizInstanceList: results, quizInstanceTotal: results.totalCount, 
			finishedQuestionsTotal:finishedQuestionsTotal,
			correctQuestionsTotal:correctQuestionsTotal,
			ongoingTotal:ongoingTotal,notBeginTotal:notBeginTotal,
			finishedTotal:finishedTotal,focusKPSet:focusKPSet] + params
	}
	
	def show_weak_kcp(Long id) {
		def studentInstance = Student.load(id)
		def overallColData = [:]

		HashMap<KnowledgePoint, BigDecimal> overallKPRateMap = new HashMap()

		long t0 = System.currentTimeMillis()
		
		def aStatusList	= studentInstance.aStatus.findAll { aStatus->
			aStatus.assignment.subject == session.course
		}
		
		aStatusList.each { assignStatus->
			assignStatus.coveredKPs.each { kp->
				// find the latest familyRecentCorrectRate of this KP
				if (!overallKPRateMap.containsKey(kp)) {
					def kcp = KnowledgeCheckPoint.findByKnowledgeAndStudentAndFamilyRecentCorrectRateGreaterThan(kp, studentInstance, 0, [sort:'quiz.answeredDate', order:'desc'] )
					if (kcp) {
						overallKPRateMap.put(kp, kcp?.familyRecentCorrectRate)
					}
				}
			}
		}
		overallKPRateMap.sort({java.util.Map.Entry a, java.util.Map.Entry b ->
			a.value.compareTo(b.value)
		}).each { key, val ->
			overallColData.put(key.name.encodeAsHTML(), [val*100])
		}
		// pick up <maxKPToShow> records
		int recNum = Math.min(overallColData.size(), studentInstance.maxKPToShow)
		overallColData = overallColData.take(recNum)

		render(template:'weak_kcp', model:["overallColData":overallColData])
	}
	
	def instructed(Long id) {
		Student stu = Student.load(id)
		
		def ratings = ["", "&#9733;".decodeHTML(), "&#9733;".multiply(2).decodeHTML(), "&#9733;".multiply(3).decodeHTML(),
			"&#9733;".multiply(4).decodeHTML(), "&#9733;".multiply(5).decodeHTML()]
		[studentInstance:stu, ratings:ratings, chakanFlag:params.chakanFlag]
	}
	
	def instructedSave(Long id) {
		Student stu = Student.get(id)
		
		if (stu.briefComment && (params.instructed_evaluating_rate || params.instructed_evaluating_text)) {
			if (!params.instructed_evaluating_text) {
				params.instructed_evaluating_text = ""
			}
			
			def idx = stu.briefComment.indexOf(Util.token1)
			def recentRecord = stu.briefComment.substring(0, idx) //stu.briefComment?.split(Util.token1, 2)[0]
			def ritems = recentRecord.split(Util.token2)
			def updRecord = recentRecord
			def toBeUpdated = false
			if (ritems.size() > 4) {
				toBeUpdated = true
				updRecord = recentRecord.substring(0, recentRecord.indexOf(Util.token2+ritems[4]+Util.token2))
			}
			
			updRecord = updRecord + Util.token2 + params.instructed_evaluating_rate + Util.token2 + params.instructed_evaluating_text
			
			stu.briefComment = updRecord + stu.briefComment.substring(idx)
			stu.save()
			
			if (toBeUpdated) {
				flash.message = "最近一次辅导(${ritems[0]} ${ritems[1]}/${ritems[2]})的评价已经更新： 您原来的评价是${ritems[4]}星, 新评价是${params.instructed_evaluating_rate}星. ${params.instructed_evaluating_text}"
			} else {
				flash.message = "最近一次辅导(${ritems[0]} ${ritems[1]}/${ritems[2]})的评价 已经保存。评价内容是：${params.instructed_evaluating_rate}星. ${params.instructed_evaluating_text}"
			}
		} else {
			flash.message = "评价失败"
		}
		
		redirect(action:'instructed', id: stu.id)
	}
	
	def msg(Long id) {
		Student stu = Student.load(id)
		
		[studentInstance:stu, chakanFlag:params.chakanFlag]
	}
	
	def msgSave(Long id) {
		Student stu = Student.load(id)
				
		if (params.stuToTeacherMsg) {
			
			def msgList = Util.parseLeaveMsg(stu)	
			
			StringBuffer sb = new StringBuffer()
			if (msgList[0]) {
				sb.append(msgList[0]).append(Util.token2)
			}
			if (msgList[1]) {
				sb.append(msgList[1])
			}
			
			def content = params.stuToTeacherMsg
			
			if (content.length() > 127) {
				flash.message = "您输入的文本太长了"
				content = content.substring(0, 127)
			}
			
			sb.append(Util.token1)
				.append(new Date().format("yyyy-MM-dd HH:mm:ss")).append(Util.token2).append(content)
				
			stu.telephone2 = sb.toString()
			stu.save()
			flash.message = "留言发送成功"
		} else {
			flash.message = "留言失败"
		}

		redirect(action:'msg', id:stu.id)
	}
	
    def edit(Long id) {
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list", params: [sort:session.student_sort, order:session.student_order, searchCategory:session.student_searchCategory, searchable:session.student_searchable])
            return
        }

        [studentInstance: studentInstance]
    }

    def update(Long id, Long version) {
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (studentInstance.version > version) {
                studentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'student.label', default: 'Student')] as Object[],
                          "Another user has updated this Student while you were editing")
                render(view: "edit", model: [studentInstance: studentInstance])
                return
            }
        }

        studentInstance.properties = params
		if (studentInstance.targetCorrectRate > 1 || studentInstance.evaluationPower > 1) {
			studentInstance.targetCorrectRate = studentInstance.targetCorrectRate/100
			studentInstance.evaluationPower = studentInstance.evaluationPower/100
		}
		
		if (studentInstance.parents) {
			EndUser p = EndUser.findByUserNameAndRole(studentInstance.parents, 'parents')
			if (!p) {
				studentInstance.parents = null
			}
		}

        if (!studentInstance.save(flush: true)) {
            render(view: "edit", model: [studentInstance: studentInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'student.label', default: 'Student'), studentInstance.id])
        redirect(action: "show", id: studentInstance.id)
    }

    def delete(Long id) {
        def studentInstance = Student.get(id)
        if (!studentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list")
            return
        }

        try {
            studentInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "list", params: [sort:session.student_sort, order:session.student_order, searchCategory:session.student_searchCategory, searchable:session.student_searchable])
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'student.label', default: 'Student'), id])
            redirect(action: "show", id: id)
        }
    }
	
	def export()
	{
		params.max = 100
		[studentInstanceList: Student.list(params), studentInstanceTotal: Student.count()]
	}
		
	def exportdata()
	{
		println "*** FIND data from current APP, INSERT data into remote DB ***"
		
		if (params.stu4export) {
			def stu4export = []
			stu4export << params.stu4export
			stu4export = stu4export.flatten()

			println "students to export ${stu4export}"

			if (params.init_basic_data) {
				exportBasicData()
			} else {
				println "use the exist exporting tables"
			}

			stu4export.each {
				Student stu = Student.load(it)
				// exportdataSave(stu)
			}

			flash.message = "导出成功！学生id：${stu4export}"
		} else {
			flash.message = "请选择需要导出的学生！"
		}

		redirect(action: "export")
	}
	
	private void exportBasicData()
	{
		Sql sql = Util.getExportSql()
														
		// KPs
		def knowledge_point = sql.dataSet("knowledge_point")
		KnowledgePoint.all.each {
			knowledge_point.add(
            id:it.id, version:it.version, associated_question:it.associatedQuestion, content:it.content, degree:it.degree, family_size:it.familySize, 
            master_ratio:it.masterRatio, name:it.name, total_question:it.totalQuestion)			
		}
		
		println "knowledge_point export to "+sql
		
		def adminId = 0
		sql.eachRow("select id from admin_user where user_name='su'") {
			adminId = it.id
		}
		
		// Questions
		def question = sql.dataSet("question")
		// KPs/Questions Map
		def question_knowledge_points = sql.dataSet("question_knowledge_points")
		Question.all.each { q ->
			question.add(id:q.id, version:q.version, analysis:q.analysis, description:q.description, 
				error_rate:q.errorRate, input_by_id:adminId,input_date:q.inputDate, 
				reviewed_by:q.reviewedBy, source:q.source, term:q.term, type:q.type)
			
			q.knowledgePoints.each { k->
				question_knowledge_points.add(knowledge_point_id:k.id, question_id:q.id)
			}
		}
		question.commit()
		question_knowledge_points.commit()
		
		// child KPs
		def child_point = sql.dataSet("child_point")
		KnowledgePoint.all.each { p ->
			p.childPoints.each {	c->			
				child_point.add( 
				knowledge_id:p.id, child_id:c.id)
			}
		}
		
		// parent KPs
		def parent_point = sql.dataSet("parent_point")
		KnowledgePoint.all.each { c ->
			c.parentPoints.each {	p->
				parent_point.add(
				knowledge_id:c.id, child_id:p.id)
			}
		}
		
		println "question and question_knowledge_points export to "+sql
		
		sql.close()
	}
		
	private void exportdataSave(Student stu)
	{
		groovy.sql.Sql sql = Util.getExportSql()
		
//		try{
//			
//		}catch(any) {
//			println any.message
//		}
						
		def endUserExp = sql.dataSet("end_user")
		
		def existsFlag = false
		sql.eachRow("select * from end_user where id=${stu.teacher.id}") {
			existsFlag = true
		}
		
		println "existsFlag: ${existsFlag} for stu.teacher.id: ${stu.teacher.id}"
				
		if (!existsFlag) {
			endUserExp.add([id:stu.teacher.id, version:stu.teacher.version, active:stu.teacher.active, brief_comment:stu.teacher.briefComment,
				date_created:stu.teacher.dateCreated, email:stu.teacher.email, full_name:stu.teacher.fullName,
				last_access_date:stu.teacher.lastAccessDate, login_date:stu.teacher.loginDate, password:stu.teacher.password,
				telephone1:stu.teacher.telephone1, telephone2:stu.teacher.telephone2,
				user_name:stu.teacher.userName])
		}
		
		existsFlag = false
		sql.eachRow("select * from end_user where id=${stu.id}") {
			existsFlag = true
		}
		
		if (!existsFlag) {
			endUserExp.add([id:stu.id, version:stu.version, active:stu.active, brief_comment:stu.briefComment,
				date_created:stu.dateCreated, email:stu.email, full_name:stu.fullName,
				last_access_date:stu.lastAccessDate, login_date:stu.loginDate, password:stu.password,
				telephone1:stu.telephone1, telephone2:stu.telephone2,
				user_name:stu.userName])
		}
		
		endUserExp.commit()

		def teacherExp = new DataSet(sql, 'teacher')
		
		existsFlag = false
		sql.eachRow("select * from teacher where id=${stu.teacher.id}") {
			existsFlag = true
		}
		
		if (!existsFlag) {
			teacherExp.add([id:stu.teacher.id, degree:stu.teacher.degree, sex: stu.teacher.sex, status:stu.teacher.status,
				identification_num:stu.teacher.identificationNum, to_show_knowledge_graph:stu.teacher.toShowKnowledgeGraph])

			teacherExp.commit()			
		}
		
		def studentExp = new DataSet(sql, 'student')
		
		existsFlag = false
		sql.eachRow("select * from student where id=${stu.id}") {
			existsFlag = true
		}
		
		if (!existsFlag) {
			def studentVal = [id:stu.id, birth_date:stu.birthDate, evaluation_power:stu.evaluationPower, focus_power:stu.focusPower,
				home_addr:stu.homeAddr, home_city:stu.homeCity,
				home_province:stu.homeProvince, identification_num:stu.identificationNum, level:stu.level,
				max_associated_questions_to_evaluate:stu.maxAssociatedQuestionsToEvaluate,
				maxkpto_show:stu.maxKPToShow, max_total_questions_to_evaluate:stu.maxTotalQuestionsToEvaluate,
				min_associated_questions_to_evaluate:stu.minAssociatedQuestionsToEvaluate,
				min_total_questions_to_evaluate:stu.minTotalQuestionsToEvaluate,
				number_of_questions_per_page:stu.numberOfQuestionsPerPage,
				parent_link:stu.parentLink, parents_id:stu.parents?.id, repeat_power:stu.repeatPower,
				school_id:stu.school?.id, sex:stu.sex, target_correct_rate:stu.targetCorrectRate,
				tclass_id:stu.tclass?.id, teacher_id:stu.teacher?.id, term:stu.term, to_show_knowledge_graph:stu.toShowKnowledgeGraph]
			
			studentExp.add(studentVal)
			studentExp.commit()
		}
		
		sql.close()
	}
	
	def importx()
	{			
		[stuImpList:params.stuImpList]
	}
	
//	def upload() {
//		println "uploading file ..."
//		def f = request.getFile("myFile");
//		if(!f.empty){
//			String fileName=f.getOriginalFilename();
//			println(new Date().toString()+"--name:"+fileName+";size:"+(f.size/1024)+"kb;type:"+f.contentType);
//			String filePath="web-app/imp/";
//			
//			f.transferTo(new File(filePath+fileName));
//			
//			flash.message = "文件上传成功: ${filePath+fileName}"
//		}else {
//			flash.message = "文件上传失败"
//		}
//		
//		groovy.sql.Sql sql = Util.getImpSql()
//		
//		def stuImpList = []
//		sql.eachRow("select end_user.id, user_name from end_user, student where end_user.id=student.id")
//		{
//			stuImpList << it.user_name
//		}
//		sql.close()
//		
//		redirect(action: "importx", params:[stuImpList:stuImpList])
//	}
	
	def importdata()
	{
		println "*** SELECT data from remote DB, SAVE data into current APP ***"
		
		groovy.sql.Sql sql = Util.getImportSql()
				
		def stuImpList = []
		def stuIdImpList = []
		sql.eachRow("select end_user.id, user_name from end_user, student where end_user.id=student.id")
		{
			stuImpList << it.user_name
			stuIdImpList << it.id
		}
		sql.close()
						
		stuIdImpList.each {			
			importdataSave(it)
		}
		
		flash.message = "导入成功"
		redirect(action: "importx", params:[stuImpList:stuImpList])
	}
	
	private void importdataSave(stuId)
	{
		println "importdataSave: ${stuId}"
		
		groovy.sql.Sql sql = Util.getImpSql()
		
		// end user
		def endUserImp = sql.dataSet("end_user")
		
		endUserImp.each {
			if (!EndUser.get(stuId)) {				
				//TODO: new EndUser instance with endUserImp elements and save
			}
		}
		
		
		// TODO: teacher
		
		// TODO: student
		
		// TODO: assignment_status
		
		// TODO: assignment
		
		// TODO: quiz
		
		// TODO: quiz_question_record
		
		// TODO: stu_answer
		
		sql.close()
	}
}
