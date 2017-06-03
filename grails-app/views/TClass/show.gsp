
<%@ page import="ots.TClass" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'TClass.label', default: 'TClass')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-TClass" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-TClass" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list TClass">
			
				<g:if test="${TClassInstance?.monitor}">
				<li class="fieldcontain">
					<span id="monitor-label" class="property-label"><g:message code="TClass.monitor.label" default="Monitor" /></span>
					
						<span class="property-value" aria-labelledby="monitor-label"><g:link controller="student" action="show" id="${TClassInstance?.monitor?.id}">${TClassInstance?.monitor?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.inChargeTeacher}">
				<li class="fieldcontain">
					<span id="inChargeTeacher-label" class="property-label"><g:message code="TClass.inChargeTeacher.label" default="In Charge Teacher" /></span>
					
						<span class="property-value" aria-labelledby="inChargeTeacher-label"><g:link controller="teacher" action="show" id="${TClassInstance?.inChargeTeacher?.id}">${TClassInstance?.inChargeTeacher?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.briefComment}">
				<li class="fieldcontain">
					<span id="briefComment-label" class="property-label"><g:message code="TClass.briefComment.label" default="Brief Comment" /></span>
					
						<span class="property-value" aria-labelledby="briefComment-label"><g:fieldValue bean="${TClassInstance}" field="briefComment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.className}">
				<li class="fieldcontain">
					<span id="className-label" class="property-label"><g:message code="TClass.className.label" default="Class Name" /></span>
					
						<span class="property-value" aria-labelledby="className-label"><g:fieldValue bean="${TClassInstance}" field="className"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.lessons}">
				<li class="fieldcontain">
					<span id="lessons-label" class="property-label"><g:message code="TClass.lessons.label" default="Lessons" /></span>
					
						<g:each in="${TClassInstance.lessons}" var="l">
						<span class="property-value" aria-labelledby="lessons-label"><g:link controller="lesson" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.schedule}">
				<li class="fieldcontain">
					<span id="schedule-label" class="property-label"><g:message code="TClass.schedule.label" default="Schedule" /></span>
					
						<span class="property-value" aria-labelledby="schedule-label"><g:fieldValue bean="${TClassInstance}" field="schedule"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.studentCount}">
				<li class="fieldcontain">
					<span id="studentCount-label" class="property-label"><g:message code="TClass.studentCount.label" default="Student Count" /></span>
					
						<span class="property-value" aria-labelledby="studentCount-label"><g:fieldValue bean="${TClassInstance}" field="studentCount"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.students}">
				<li class="fieldcontain">
					<span id="students-label" class="property-label"><g:message code="TClass.students.label" default="Students" /></span>
					
						<g:each in="${TClassInstance.students}" var="s">
						<span class="property-value" aria-labelledby="students-label"><g:link controller="student" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.teachingPlan}">
				<li class="fieldcontain">
					<span id="teachingPlan-label" class="property-label"><g:message code="TClass.teachingPlan.label" default="Teaching Plan" /></span>
					
						<span class="property-value" aria-labelledby="teachingPlan-label"><g:fieldValue bean="${TClassInstance}" field="teachingPlan"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.totalErrorRate}">
				<li class="fieldcontain">
					<span id="totalErrorRate-label" class="property-label"><g:message code="TClass.totalErrorRate.label" default="Total Error Rate" /></span>
					
						<span class="property-value" aria-labelledby="totalErrorRate-label"><g:fieldValue bean="${TClassInstance}" field="totalErrorRate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${TClassInstance?.totalQuestionsPracticed}">
				<li class="fieldcontain">
					<span id="totalQuestionsPracticed-label" class="property-label"><g:message code="TClass.totalQuestionsPracticed.label" default="Total Questions Practiced" /></span>
					
						<span class="property-value" aria-labelledby="totalQuestionsPracticed-label"><g:fieldValue bean="${TClassInstance}" field="totalQuestionsPracticed"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${TClassInstance?.id}" />
					<g:link class="edit" action="edit" id="${TClassInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
