package ots

import org.springframework.dao.DataIntegrityViolationException
import java.text.SimpleDateFormat

class TeacherController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def beforeInterceptor = [action:this.&checkUser, except: ['create','save', 'show']]

	def checkUser() {
		if (!session.user || session.user instanceof Student || session.user instanceof AdminUser && session.user.role != "admin") {
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
	
	def thome(Long id) {
		if (!session.user) {
			redirect(controller:'endUser', action:"login")
		}
		
		if (!id) {
			id = session.user?.id
		}
				
		def teacherInstance = Teacher.get(id);
        if (!teacherInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            //redirect(controller:'endUser', action:"login")
            return
        }
		
        [teacherInstance: teacherInstance, asmtList: Assignment.findAllByAssignedBy(teacherInstance.userName, [max:5])]
		
	}
		
	def assiSave() {		
		Student stu = Student.load(params.int('stuId'))
		
		def sel_assi = []
		if (params.selected_assi) {
			sel_assi << params.selected_assi
		}
		sel_assi = sel_assi.flatten()
		
		sel_assi.each { asmtId ->
			def asmt = Assignment.load(Integer.parseInt(asmtId))
			def aStatus = AssignmentStatus.findByAssignmentAndStudent(asmt, stu)			
			if (!aStatus) {
				aStatus = new AssignmentStatus(student:stu, assignment:asmt, status:CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN ).save()
				println "added: ${aStatus}"
			} else {
				// println "already exists aStatus: ${aStatus}"
			}
		}
		
		def ava_assi = []
		if (params.available_assi) {
			ava_assi << params.available_assi
		}
		ava_assi = ava_assi.flatten()
		
		ava_assi.each { asmtId ->
			def asmt = Assignment.load(Integer.parseInt(asmtId))
			if (Quiz.countByStudentAndAssignment(stu, asmt) == 0 ) {
				AssignmentStatus.deleteAll(AssignmentStatus.findAllByAssignmentAndStudent(asmt, stu))
			}	
		}
				
		redirect(action:'thome', id:params.teacherId)
	}
	
	// use telephone2 for msg field at present
	def leaveMsg()
	{
		Student stu = Student.load(params.int('stuId'))

		if (params.teacherToStuMsg) {
			def content = params.teacherToStuMsg

			if (content.length() > 127) {
				flash.message = "您输入的文本太长了"
				content = content.substring(0, 127)
			}

			if (stu.telephone2) {
				int indx = stu.telephone2.indexOf(Util.token1)+Util.token1.length()
				def stuToTeacherMsg = ""
				if (indx > -1) {
					stuToTeacherMsg = stu.telephone2.substring(indx)
				}
				
				stu.telephone2 = new Date().format("yyyy-MM-dd HH:mm:ss") + Util.token2 + content + Util.token1 + stuToTeacherMsg

			} else {
				stu.telephone2 = new Date().format("yyyy-MM-dd HH:mm:ss") + Util.token2 + content + Util.token1
			}
			stu.save()

		}

		redirect(action:'thome', id:params.teacherId)
	}
	
	def instructed() {
		Student stu = Student.load(params.int('stuId'))
		def token1 = Util.token1
		def token2 = Util.token2

		if (params.instructed_date && params.instructed_begin && params.instructed_end && params.instructed_kps) {
			def newLine = params.instructed_date+token2+params.instructed_begin+token2+params.instructed_end+token2+params.instructed_kps
			
			if (stu.briefComment) {
				// 2014-06-22###20:00###21:30###KP1,KP2%%%2014-06-22###20:00###21:30###...
				int lastRecordIndx = stu.briefComment.lastIndexOf(token1+"20")
				if (stu.briefComment.length() + newLine.length() >= 1800) {
					stu.briefComment = stu.briefComment.substring(0, lastRecordIndx > -1 ? lastRecordIndx:0)
				}

				int preRecordIndx = stu.briefComment.indexOf(token1)
					
				def preLine = stu.briefComment.substring(0, preRecordIndx)
				def preLineHead = preLine.substring(0, preLine.lastIndexOf(token2))
				def newLineHead = newLine.substring(0, newLine.lastIndexOf(token2))
					
				if (preLineHead?.equals(newLineHead)) {
					flash.message = "辅导时间与上次输入的一样，上次的辅导内容已经更新为您最近输入的内容"
					stu.briefComment = stu.briefComment.replace(preLine, newLine)
				} else {				
					stu.briefComment = newLine + token1+ stu.briefComment
				}

			} else {
				stu.briefComment = newLine + token1
			}

			stu.save()
		}
		else {
			flash.message = "请填写辅导日期，时间以及涵盖知识点"
		}

		redirect(action:'thome', id:params.teacherId)
	}
			
	def saveTClass() {	
		def TClassInstance
		println "selected_students==${params.selected_students}"
		if (!params.tclassId) {			
			TClassInstance = new TClass(schedule:'NotDefined',inChargeTeacher:session.user,className:params.className, teachingPlan:params.teachingPlan, students:params.selected_students)
		} else {
			TClassInstance = TClass.get(params.int('tclassId'));
			TClassInstance.className = params.className
			TClassInstance.teachingPlan = params.teachingPlan
			TClassInstance.students = params.selected_students
		}
		
		
		if (!TClassInstance.save(flush: true, failOnError: true)) {
			render(view: "thome", model: [TClassInstance: TClassInstance])
			return
		}
				
		flash.message = message(code: 'default.created.message', args: [message(code: 'TClass.label', default: 'TClass'), TClassInstance.id])
		redirect(action: "thome")
	}
	
	def createLesson() {
	
	}

    def list(Integer max) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        params.max = Math.min(max ?: 10, 100)
		if (session.user.role == "admin") {
			session.teacher_searchCategory = params.searchCategory
			session.teacher_searchable = params.searchable
			if (params.sort || params.order) {
				session.teacher_sort = params.sort
				session.teacher_order = params.order
			}
	
			def c = Teacher.createCriteria()
			def results = c.list(max: params.max, offset: params.offset, sort: params.sort, order: params.order) {
				and {
					if(params.searchable && params.searchCategory) {
						if (params.searchCategory == "entryTimelt") {
							def fDate = formatter.parse(params.searchable + " 00:00:00")
							lt("entryTime", fDate)
						} else if (params.searchCategory == "entryTime") {
							def fDate1 = formatter.parse(params.searchable + " 00:00:00")
							def fDate2 = formatter.parse(params.searchable + " 23:59:59")
							lt("entryTime", fDate2)
							gt("entryTime", fDate1)
						} else if (params.searchCategory == "entryTimegt") {
							def fDate = formatter.parse(params.searchable + " 23:59:59")
							gt("entryTime", fDate)
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

			[searchCategory:params.searchCategory, searchKeyword: params.searchable, teacherInstanceList: results, teacherInstanceTotal: results.totalCount]
		} else {
			redirect(controller:'endUser',action:'login')
		}
    }

    def create() {
        [teacherInstance: new Teacher(params)]
    }

    def save() {
        def teacherInstance = new Teacher(params)
        if (!teacherInstance.save(flush: true)) {
            render(view: "create", model: [teacherInstance: teacherInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'teacher.label', default: 'Teacher'), teacherInstance.id])
        redirect(action: "show", id: teacherInstance.id)
    }

    def show(Long id) {
        def teacherInstance = Teacher.get(id)
        if (!teacherInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            redirect(action: "list")
            return
        }

        [teacherInstance: teacherInstance]
    }

    def edit(Long id) {
        def teacherInstance = Teacher.get(id)
        if (!teacherInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            redirect(action: "list", params: [sort:session.teacher_sort, order:session.teacher_order, searchCategory:session.teacher_searchCategory, searchable:session.teacher_searchable])
            return
        }

        [teacherInstance: teacherInstance]
    }

    def update(Long id, Long version) {
        def teacherInstance = Teacher.get(id)
        if (!teacherInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (teacherInstance.version > version) {
                teacherInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'teacher.label', default: 'Teacher')] as Object[],
                          "Another user has updated this Teacher while you were editing")
                render(view: "edit", model: [teacherInstance: teacherInstance])
                return
            }
        }

        teacherInstance.properties = params

        if (!teacherInstance.save(flush: true)) {
            render(view: "edit", model: [teacherInstance: teacherInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'teacher.label', default: 'Teacher'), teacherInstance.id])
        redirect(action: "show", id: teacherInstance.id)
    }

    def delete(Long id) {
        def teacherInstance = Teacher.get(id)
        if (!teacherInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            redirect(action: "list")
            return
        }

        try {
            teacherInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            redirect(action: "list", params: [sort:session.teacher_sort, order:session.teacher_order, searchCategory:session.teacher_searchCategory, searchable:session.teacher_searchable])
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'teacher.label', default: 'Teacher'), id])
            redirect(action: "show", id: id)
        }
    }
}
