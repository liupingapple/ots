<%@ page import="ots.EndUser" %>



<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'userName', 'error')} required">
	<label for="userName">
		<g:message code="endUser.userName.label" default="User Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="userName" required="" value="${endUserInstance?.userName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="endUser.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="password" name="password" required="" value="${endUserInstance?.password}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'fullName', 'error')} required">
	<label for="fullName">
		<g:message code="endUser.fullName.label" default="Full Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="fullName" required="" value="${endUserInstance?.fullName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'role', 'error')} required">
	<label for="role">
		<g:message code="endUser.role.label" default="role" />
	</label>
	<g:textField disabled="true" name="role" required="" value="${endUserInstance?.role}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'email', 'error')} ">
	<label for="email">
		<g:message code="endUser.email.label" default="Email" />
		
	</label>
	<g:field type="email" name="email" value="${endUserInstance?.email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'telephone1', 'error')} required">
	<label for="telephone1">
		<g:message code="endUser.telephone1.label" default="Telephone1" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="telephone1" type="number" value="${endUserInstance.telephone1}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'telephone2', 'error')} required">
	<label for="telephone2">
		<g:message code="endUser.telephone2.label" default="Telephone2" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="telephone2" type="number" value="${endUserInstance.telephone2}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'lastAccessDate', 'error')} ">
	<label for="lastAccessDate">
		<g:message code="endUser.lastAccessDate.label" default="Last Access Date" />
		
	</label>
	<g:datePicker name="lastAccessDate" precision="day"  value="${endUserInstance?.lastAccessDate}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: endUserInstance, field: 'active', 'error')} ">
	<label for="active">
		<g:message code="endUser.active.label" default="Active" />
		
	</label>
	<g:checkBox name="active" value="${endUserInstance?.active}" />
</div>


