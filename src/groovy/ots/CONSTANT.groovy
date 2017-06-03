package ots

class CONSTANT {
	final static String FEMALE = '女'
	final static String MALE = '男'
	final static String RADIO_QUESTION = '单选'
	final static String FBLANK_QUESTION = '填空'
	final static String YDLJ_QUESTION = '阅读理解'
	final static String YDLJ_DANXUAN = '阅读理解 - 单选题' // Child Question 是单选题
	final static String YDLJ_TIANKONG = '阅读理解 - 填空题' // Child Question 是填空题
	final static String YDLJ_WANXING_DANX = '阅读完型填空 - 单选题' // Parent Question 是“大的”填空题；Child Question 是单选题
	final static String YDLJ_SZMTK = '阅读理解 - 首字母填空题' // Parent Question 是“大的”填空题；Child Question 是首字母填空题
	final static String YDLJ_DUOXTK = '阅读理解 - 多选填空题' // Parent Question 是“大的”填空题（包含多选项）；Child Question 是填空题
	final static ArrayList QUESTION_TYPEs = [
		RADIO_QUESTION,
		FBLANK_QUESTION,
		
		YDLJ_QUESTION,
		
		YDLJ_DANXUAN,
		YDLJ_TIANKONG,
		YDLJ_WANXING_DANX,
		YDLJ_SZMTK,
		YDLJ_DUOXTK
	]
	final static ArrayList YDLJ_Types = [		
		YDLJ_QUESTION,
		
		YDLJ_DANXUAN,
		YDLJ_TIANKONG,
		YDLJ_WANXING_DANX,
		YDLJ_SZMTK,
		YDLJ_DUOXTK
	]
		
	final static String COURSE_ENG1 = '英语词汇与语法'
	final static String COURSE_ENG2 = '英语阅读理解'
	final static String COURSE_WYW = '文言文'
	final static String COURSE_YW = '语文'
	final static ArrayList COURSE_LIST = [
		COURSE_ENG1, COURSE_ENG2, COURSE_WYW, COURSE_YW
	]

	final static String QUIZ_TYPE_INCLASS = '课堂练习'
	final static String QUIZ_TYPE_HOMEWORK = '家庭作业'
	final static String QUIZ_TYPE_EXAM = '考试'
	final static String QUIZ_TYPE_SELFPRAC  = '自我练习' // self practice
	final static ArrayList QUIZ_TYPEs = [
		QUIZ_TYPE_INCLASS,
		QUIZ_TYPE_HOMEWORK,
		QUIZ_TYPE_EXAM,
		QUIZ_TYPE_SELFPRAC
	]

	final static ASSIGNMENT_STATUS_NOTBEGIN = '未开始'
	final static ASSIGNMENT_STATUS_INPROGRESS = '进行中'
	final static ASSIGNMENT_STATUS_DONE = '已完成'
	final static ASSIGNMENT_STATUS_LIST = [ASSIGNMENT_STATUS_NOTBEGIN, ASSIGNMENT_STATUS_INPROGRESS, ASSIGNMENT_STATUS_DONE]

	final static QUIZ_STATUS_NOTBEGIN = '还未开始'
	final static QUIZ_STATUS_INPROGRESS = '进行中'
	final static QUIZ_STATUS_SUBMITTED = '已提交'
	final static QUIZ_STATUS_REVIEWED = '已阅卷'
	final static ArrayList QUIZ_STATUSes = [
		QUIZ_STATUS_NOTBEGIN,
		QUIZ_STATUS_INPROGRESS,
		QUIZ_STATUS_SUBMITTED,
		QUIZ_STATUS_REVIEWED
	]
	
	final static int Practice_History_Limit = 5600

	final static ArrayList PROVINCEs = [
		'安徽',
		'北京',
		'重庆',
		'福建',
		'甘肃',
		'广东',
		'广西',
		'贵州',
		'海南',
		'河北',
		'黑龙江',
		'河南',
		'香港',
		'湖北',
		'湖南',
		'江苏',
		'江西',
		'吉林',
		'辽宁',
		'澳门',
		'内蒙古',
		'宁夏',
		'青海',
		'山东',
		'上海',
		'山西',
		'陕西',
		'四川',
		'天津',
		'台湾',
		'新疆',
		'西藏',
		'云南',
		'浙江'
	]

	final static ArrayList TERMs = [
		'预备',
		'初一',
		'初二',
		'初三','高一','高二','高三','其他'] // for student
	final static ArrayList TEACHER_TYPEs = ['全职', '兼职', '特聘', '培训生']
	
	final static ArrayList TEACHER_STATUSes = ["working", "vacation", "quit"]

	final static ArrayList DEGREEs = [
		'大专及大专以下',
		'本科',
		'硕士研究生',
		'博士研究生'] // for teacher
	final static ArrayList ACCOUNT_LEVELs = [
		'初级用户',
		'中级用户',
		'高级用户'] // for student and school account
	final static ArrayList BATCH_OPERATIONS = ["Load Sample Data",
		"Load Sample Knowledge Node Content",
		"Load Sample Knowledge Parent Relationship",
		"Load Sample Questions (单选)",
		"Load Sample Questions (填空)",
		"Load Sample Question Knowledge Mapping (单选)",
		"Load Sample Question Knowledge Mapping (填空)",
		"Clean up Questions",
		"Clean up Questions with no mapped Knowledge Points",
		"Clean up Knowledge Points",
		"Clean up Knowledge Points with no associated Questions",
		"Clean up QuizQuestionRecord",
		"Clean up Quiz",
		"Clean up Knowledge Point Checkpoints",
		"Clean up mappings between Question and Knowledge Points",
		"Clean up parent/child relationships among Knowledge Points",
		"Update total Questions for Knowledge Points",
		"Generate Assignment Templates",
		"Export Assignment Templates",
		"Import Assignment Templates",
		"Export Assignments",
		"Import Assignments",
		"Export AdminUser",
		"Import AdminUser",
		"Resort Quiz Names",
		"Formalize Knowledge Points Names",
		"Export Knowledge Points Statistics",
		"Import Knowledge Points Statistics",
		"Export the K Values",
		"Regenerate Question ID",
		"Export Question Parent/Child Relationship",
		"Import Question Parent/Child Relationship",
		"Remove Teacher Accounts"]
	
}
