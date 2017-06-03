
<%@ page import="ots.AssignmentTemplate" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'assignmentTemplate.label', default: 'AssignmentTemplate')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-assignmentTemplate" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-assignmentTemplate" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="templateName" title="${message(code: 'assignmentTemplate.templateName.label', default: 'Template Name')}" />
					
						<g:sortableColumn property="createdBy" title="${message(code: 'assignmentTemplate.createdBy.label', default: 'Created By')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'assignmentTemplate.dateCreated.label', default: 'Date Created')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${assignmentTemplateInstanceList}" status="i" var="assignmentTemplateInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${assignmentTemplateInstance.id}">${fieldValue(bean: assignmentTemplateInstance, field: "templateName")}</g:link></td>
					
						<td>${fieldValue(bean: assignmentTemplateInstance, field: "createdBy")}</td>
					
						<td><g:formatDate date="${assignmentTemplateInstance.dateCreated}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${assignmentTemplateInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
