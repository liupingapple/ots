import ots.AdminUser
import ots.Lesson
import ots.EndUser
import ots.KnowledgePoint;
import ots.School
import ots.Teacher
import ots.Student
import ots.WeiXinUtil;

import java.text.SimpleDateFormat

class BootStrap {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	def init = { servletContext ->
		new	AdminUser(userName: "su", email: "steven.ding@gmail.com", password: "pw", fullName: "Steve Ding", role: "admin").save()
		new	AdminUser(userName: "ch", email: "steven.ding@gmail.com", password: "ch", fullName: "checker", role: "checker").save()
		new	AdminUser(userName: "in", email: "steven.ding@gmail.com", password: "in", fullName: "inputer", role: "inputer").save()
		new	AdminUser(userName: "ln", email: "steven.ding@gmail.com", password: "ln", fullName: "linker", role: "linker").save()
				
		new	AdminUser(userName: "adminwx", email: "steven.ding@gmail.com", password: "adminwx", fullName: "AdminUserForWeiXin", role: "admin").save()
		
		Teacher te = new	Teacher(birthDate: sdf.parse("21/12/1998"), userName: "te", email: "steven.ding@gmail.com", password: "te", fullName: "Teacher_A", role: "teacher").save()
		Teacher te1 = new	Teacher(birthDate: sdf.parse("21/12/1998"), userName: "te1", email: "steven.ding@gmail.com", password: "te1", fullName: "Teacher_B", role: "teacher").save()
		
		Teacher tewx = new	Teacher(birthDate: sdf.parse("21/12/1998"), userName: "tewx", email: "steven.ding@gmail.com", password: "tewx", fullName: "TeacherForWeiXin", role: "teacher").save()
		
		// don't use save(failOnError: true) in case break starting
		if (!Student.findByUserName("st") && !new Student(birthDate: sdf.parse("21/12/1998"), loginDate: sdf.parse("21/12/2018"), userName: "st", email: "steven.ding@gmail.com", password: "st", fullName: "student", role: "student", teacher:te).save())
		{
			println "WARNING: in BootStrap. init student st creation failed!"
		}
		
		new	Student(birthDate: sdf.parse("21/12/1998"), loginDate: sdf.parse("21/12/2018"), userName: "st1", email: "steven.ding@gmail.com", password: "st1", fullName: "student1", role: "student", teacher:te).save()
		new	Student(birthDate: sdf.parse("21/12/1998"), loginDate: sdf.parse("21/12/2018"), userName: "st2", email: "steven.ding@gmail.com", password: "st2", fullName: "student2", role: "student", teacher:te).save()
		new	Student(birthDate: sdf.parse("21/12/1998"), loginDate: sdf.parse("21/12/2018"), userName: "st3", email: "steven.ding@gmail.com", password: "st3", fullName: "student3", role: "student", teacher:te1).save()
		new	Student(birthDate: sdf.parse("21/12/1998"), loginDate: sdf.parse("21/12/2018"), userName: "st4", email: "steven.ding@gmail.com", password: "st4", fullName: "student4", role: "student", teacher:te1).save()
		
		/*Thread weeklyReport = new Thread(){
				@Override
				public void run() {
					// weekly report
					println "Weekly Report for weak knowledge points started ..."
					while (true) {
						Calendar c = new GregorianCalendar()
						int day = c.get(Calendar.DAY_OF_WEEK)
						if (day == Calendar.FRIDAY && c.get(Calendar.HOUR_OF_DAY) == 6) {
						    println "weakKPReport go ..."
							WeiXinUtil.weakKPReport()							
							Thread.sleep(10*60*1000)
						} 
						
						// 50 minutes
						Thread.sleep(50*60*1000)
					}

				}
			};
		
		weeklyReport.setDaemon(true)
		weeklyReport.start();*/
	}
	
	def destroy = {
	}
}
