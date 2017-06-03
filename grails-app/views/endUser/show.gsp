
<%@ page import="ots.EndUser" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'endUser.label', default: 'EndUser')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-endUser" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-endUser" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list endUser">
			
				<g:if test="${endUserInstance?.userName}">
				<li class="fieldcontain">
					<span id="userName-label" class="property-label"><g:message code="endUser.userName.label" default="User Name" /></span>
					
						<span class="property-value" aria-labelledby="userName-label"><g:fieldValue bean="${endUserInstance}" field="userName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="endUser.password.label" default="Password" /></span>
					
						<span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${endUserInstance}" field="password"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.fullName}">
				<li class="fieldcontain">
					<span id="fullName-label" class="property-label"><g:message code="endUser.fullName.label" default="Full Name" /></span>
					
						<span class="property-value" aria-labelledby="fullName-label"><g:fieldValue bean="${endUserInstance}" field="fullName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.email}">
				<li class="fieldcontain">
					<span id="email-label" class="property-label"><g:message code="endUser.email.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${endUserInstance}" field="email"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.telephone1}">
				<li class="fieldcontain">
					<span id="telephone1-label" class="property-label"><g:message code="endUser.telephone1.label" default="Telephone1" /></span>
					
						<span class="property-value" aria-labelledby="telephone1-label"><g:fieldValue bean="${endUserInstance}" field="telephone1"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.telephone2}">
				<li class="fieldcontain">
					<span id="telephone2-label" class="property-label"><g:message code="endUser.telephone2.label" default="Telephone2" /></span>
					
						<span class="property-value" aria-labelledby="telephone2-label"><g:fieldValue bean="${endUserInstance}" field="telephone2"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.lastAccessDate}">
				<li class="fieldcontain">
					<span id="lastAccessDate-label" class="property-label"><g:message code="endUser.lastAccessDate.label" default="Last Access Date" /></span>
					
						<span class="property-value" aria-labelledby="lastAccessDate-label"><g:formatDate date="${endUserInstance?.lastAccessDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="endUser.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${endUserInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.active}">
				<li class="fieldcontain">
					<span id="active-label" class="property-label"><g:message code="endUser.active.label" default="Active" /></span>
					
						<span class="property-value" aria-labelledby="active-label"><g:formatBoolean boolean="${endUserInstance?.active}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${endUserInstance?.briefComment}">
				<li class="fieldcontain">
					<span id="briefComment-label" class="property-label"><g:message code="endUser.briefComment.label" default="briefComment" /></span>
					
						<span class="property-value" aria-labelledby="briefComment-label"><g:fieldValue bean="${endUserInstance}" field="briefComment"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${endUserInstance?.id}" />
					<g:link class="edit" action="edit" id="${endUserInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
