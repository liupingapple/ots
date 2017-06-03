
<%@ page import="ots.TClass" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'TClass.label', default: 'TClass')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-TClass" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-TClass" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="TClass.monitor.label" default="Monitor" /></th>
					
						<th><g:message code="TClass.inChargeTeacher.label" default="In Charge Teacher" /></th>
					
						<g:sortableColumn property="briefComment" title="${message(code: 'TClass.briefComment.label', default: 'Brief Comment')}" />
					
						<g:sortableColumn property="className" title="${message(code: 'TClass.className.label', default: 'Class Name')}" />
					
						<g:sortableColumn property="schedule" title="${message(code: 'TClass.schedule.label', default: 'Schedule')}" />
					
						<g:sortableColumn property="studentCount" title="${message(code: 'TClass.studentCount.label', default: 'Student Count')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${TClassInstanceList}" status="i" var="TClassInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${TClassInstance.id}">${fieldValue(bean: TClassInstance, field: "monitor")}</g:link></td>
					
						<td>${fieldValue(bean: TClassInstance, field: "inChargeTeacher")}</td>
					
						<td>${fieldValue(bean: TClassInstance, field: "briefComment")}</td>
					
						<td>${fieldValue(bean: TClassInstance, field: "className")}</td>
					
						<td>${fieldValue(bean: TClassInstance, field: "schedule")}</td>
					
						<td>${fieldValue(bean: TClassInstance, field: "studentCount")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${TClassInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
