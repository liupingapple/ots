<%@ page import="ots.AdminUser" %>



<div class="fieldcontain ${hasErrors(bean: adminUserInstance, field: 'userName', 'error')} required">
	<label for="userName">
		<g:message code="adminUser.userName.label" default="User Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="userName" required="" value="${adminUserInstance?.userName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminUserInstance, field: 'fullName', 'error')} ">
	<label for="fullName">
		<g:message code="adminUser.fullName.label" default="Full Name" />
		
	</label>
	<g:textField name="fullName" value="${adminUserInstance?.fullName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminUserInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="adminUser.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="password" name="password" required="" value="${adminUserInstance?.password}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminUserInstance, field: 'email', 'error')} ">
	<label for="email">
		<g:message code="adminUser.email.label" default="Email" />
		
	</label>
	<g:field type="email" name="email" value="${adminUserInstance?.email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminUserInstance, field: 'role', 'error')} ">
	<label for="role">
		<g:message code="adminUser.role.label" default="Role" />
		
	</label>
	<g:select name="role" from="${adminUserInstance.constraints.role.inList}" value="${adminUserInstance?.role}" valueMessagePrefix="adminUser.role" noSelection="['': '']"/>
</div>

<%--
<div class="fieldcontain ${hasErrors(bean: adminUserInstance, field: 'question', 'error')} ">
	<label for="question">
		<g:message code="adminUser.question.label" default="Question" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${adminUserInstance?.question?}" var="q">
    <li><g:link controller="question" action="show" id="${q.id}">${q?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="question" action="create" params="['adminUser.id': adminUserInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'question.label', default: 'Question')])}</g:link>
</li>
</ul>
 --%>
 
</div>

