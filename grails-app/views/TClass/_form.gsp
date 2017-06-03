<%@ page import="ots.TClass" %>



<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'monitor', 'error')} ">
	<label for="monitor">
		<g:message code="TClass.monitor.label" default="Monitor" />
		
	</label>
	<g:select id="monitor" name="monitor.id" from="${ots.Student.list()}" optionKey="id" value="${TClassInstance?.monitor?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'inChargeTeacher', 'error')} ">
	<label for="inChargeTeacher">
		<g:message code="TClass.inChargeTeacher.label" default="In Charge Teacher" />
		
	</label>
	<g:select id="inChargeTeacher" name="inChargeTeacher.id" from="${ots.Teacher.list()}" optionKey="id" value="${TClassInstance?.inChargeTeacher?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'briefComment', 'error')} ">
	<label for="briefComment">
		<g:message code="TClass.briefComment.label" default="Brief Comment" />
		
	</label>
	<g:textArea name="briefComment" cols="40" rows="5" maxlength="2000" value="${TClassInstance?.briefComment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'className', 'error')} ">
	<label for="className">
		<g:message code="TClass.className.label" default="Class Name" />
		
	</label>
	<g:textField name="className" value="${TClassInstance?.className}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'lessons', 'error')} ">
	<label for="lessons">
		<g:message code="TClass.lessons.label" default="Lessons" />
		
	</label>
	<g:select name="lessons" from="${ots.Lesson.list()}" multiple="multiple" optionKey="id" size="5" value="${TClassInstance?.lessons*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'schedule', 'error')} ">
	<label for="schedule">
		<g:message code="TClass.schedule.label" default="Schedule" />
		
	</label>
	<g:textField name="schedule" value="${TClassInstance?.schedule}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'studentCount', 'error')} required">
	<label for="studentCount">
		<g:message code="TClass.studentCount.label" default="Student Count" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="studentCount" type="number" value="${TClassInstance.studentCount}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'students', 'error')} ">
	<label for="students">
		<g:message code="TClass.students.label" default="Students" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${TClassInstance?.students?}" var="s">
    <li><g:link controller="student" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="student" action="create" params="['TClass.id': TClassInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'student.label', default: 'Student')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'teachingPlan', 'error')} ">
	<label for="teachingPlan">
		<g:message code="TClass.teachingPlan.label" default="Teaching Plan" />
		
	</label>
	<g:textField name="teachingPlan" value="${TClassInstance?.teachingPlan}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'totalErrorRate', 'error')} required">
	<label for="totalErrorRate">
		<g:message code="TClass.totalErrorRate.label" default="Total Error Rate" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="totalErrorRate" type="number" value="${TClassInstance.totalErrorRate}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: TClassInstance, field: 'totalQuestionsPracticed', 'error')} required">
	<label for="totalQuestionsPracticed">
		<g:message code="TClass.totalQuestionsPracticed.label" default="Total Questions Practiced" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="totalQuestionsPracticed" value="${fieldValue(bean: TClassInstance, field: 'totalQuestionsPracticed')}" required=""/>
</div>

