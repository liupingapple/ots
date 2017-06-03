<%@ page import="ots.Quiz" %>
<%@ page import="ots.Student" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'quiz.label', default: 'Quiz')}" />
		<title>Student Quiz List</title>
	</head>
	<body>
		<a href="#list-student" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<g:if test="${session.user instanceof ots.AdminUser && session.user.isAdmin()}">
				  <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</g:if>
			</ul>
		</div>
		<div id="list-student" class="content scaffold-list" role="main">
		    <g:if test="${session.user instanceof ots.AdminUser && session.user.isAdmin()}">
			  <h1><g:message code="default.list.label" args="[entityName]" /></h1>
			</g:if>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="QuizName" title="${message(code: 'quiz.name.label', default: 'Quiz Name')}" />
					
						<g:sortableColumn property="DateCreated" title="${message(code: 'quiz.dateCreated.label', default: 'Password')}" />
					
						<g:sortableColumn property="fullName" title="${message(code: 'student.fullName.label', default: 'Full Name')}" />
					
						<g:sortableColumn property="email" title="${message(code: 'student.email.label', default: 'Email')}" />
					
						<g:sortableColumn property="telephone1" title="${message(code: 'student.telephone1.label', default: 'Telephone1')}" />
					
						<g:sortableColumn property="telephone2" title="${message(code: 'student.telephone2.label', default: 'Telephone2')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${studentInstanceList}" status="i" var="studentInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${studentInstance.id}">${fieldValue(bean: studentInstance, field: "userName")}</g:link></td>
					
						<td>${fieldValue(bean: studentInstance, field: "password")}</td>
					
						<td>${fieldValue(bean: studentInstance, field: "fullName")}</td>
					
						<td>${fieldValue(bean: studentInstance, field: "email")}</td>
					
						<td>${fieldValue(bean: studentInstance, field: "telephone1")}</td>
					
						<td>${fieldValue(bean: studentInstance, field: "telephone2")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				 <g:paginate total="${quizTotal}" /> 
			</div>
		</div>
	</body>
</html>
