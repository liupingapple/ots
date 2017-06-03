
<%@ page import="ots.EndUser" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'endUser.label', default: 'EndUser')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-endUser" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-endUser" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="userName" title="${message(code: 'endUser.userName.label', default: 'User Name')}" />
					
						<g:sortableColumn property="password" title="${message(code: 'endUser.password.label', default: 'Password')}" />
					
						<g:sortableColumn property="fullName" title="${message(code: 'endUser.fullName.label', default: 'Full Name')}" />
					
						<g:sortableColumn property="email" title="${message(code: 'endUser.email.label', default: 'Email')}" />
					
						<g:sortableColumn property="telephone1" title="${message(code: 'endUser.telephone1.label', default: 'Telephone1')}" />
					
						<g:sortableColumn property="telephone2" title="${message(code: 'endUser.telephone2.label', default: 'Telephone2')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${endUserInstanceList}" status="i" var="endUserInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${endUserInstance.id}">${fieldValue(bean: endUserInstance, field: "userName")}</g:link></td>
					
						<td>${fieldValue(bean: endUserInstance, field: "password")}</td>
					
						<td>${fieldValue(bean: endUserInstance, field: "fullName")}</td>
					
						<td>${fieldValue(bean: endUserInstance, field: "email")}</td>
					
						<td>${fieldValue(bean: endUserInstance, field: "telephone1")}</td>
					
						<td>${fieldValue(bean: endUserInstance, field: "telephone2")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${endUserInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
