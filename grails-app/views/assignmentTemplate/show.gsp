
<%@ page import="ots.AssignmentTemplate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-assignmentTemplate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-assignmentTemplate" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list assignmentTemplate">
			
				<g:if test="${assignmentTemplateInstance?.templateName}">
				<li class="fieldcontain">
					<span id="templateName-label" class="property-label"><g:message code="assignmentTemplate.templateName.label" default="Template Name" /></span>
					
						<span class="property-value" aria-labelledby="templateName-label"><g:fieldValue bean="${assignmentTemplateInstance}" field="templateName"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${assignmentTemplateInstance?.subject}">
				<li class="fieldcontain">
					<span id="subject-label" class="property-label"><g:message code="assignmentTemplate.subject.label" default="课程" /></span>
					
						<span class="property-value" aria-labelledby="subject-label"><g:fieldValue bean="${assignmentTemplateInstance}" field="subject"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${assignmentTemplateInstance?.school}">
				<li class="fieldcontain">
					<span id="school-label" class="property-label"><g:message code="assignmentTemplate.subject.label" default="机构" /></span>
					
						<span class="property-value" aria-labelledby="school-label"><g:fieldValue bean="${assignmentTemplateInstance}" field="school.name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentTemplateInstance?.createdBy}">
				<li class="fieldcontain">
					<span id="createdBy-label" class="property-label"><g:message code="assignmentTemplate.createdBy.label" default="Created By" /></span>
					
						<span class="property-value" aria-labelledby="createdBy-label"><g:fieldValue bean="${assignmentTemplateInstance}" field="createdBy"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentTemplateInstance?.knowledgePoints}">
				<li class="fieldcontain">
					<span id="knowledgePoints-label" class="property-label"><g:message code="assignmentTemplate.knowledgePoints.label" default="Knowledge Points" /></span>
					
						<g:each in="${assignmentTemplateInstance.knowledgePoints}" var="k">
						<span class="property-value" aria-labelledby="knowledgePoints-label"><g:link controller="knowledgePoint" action="show" id="${k.id}">${k?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentTemplateInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="assignmentTemplate.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${assignmentTemplateInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${assignmentTemplateInstance?.id}" />
					<g:link class="edit" action="edit" id="${assignmentTemplateInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
