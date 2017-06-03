
<%@ page import="ots.Answer" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'answer.label', default: 'Answer')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-answer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-answer" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="serialNum" title="${message(code: 'answer.serialNum.label', default: 'Serial Num')}" />
					
						<g:sortableColumn property="content" title="${message(code: 'answer.content.label', default: 'Content')}" />
					
						<g:sortableColumn property="correct" title="${message(code: 'answer.correct.label', default: 'Correct')}" />
					
						<th><g:message code="answer.question.label" default="Question" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${answerInstanceList}" status="i" var="answerInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${answerInstance.id}">${fieldValue(bean: answerInstance, field: "serialNum")}</g:link></td>
					
						<td>${fieldValue(bean: answerInstance, field: "content")}</td>
					
						<td><g:formatBoolean boolean="${answerInstance.correct}" /></td>
					
						<td>${fieldValue(bean: answerInstance, field: "question")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate controller="answer" action="list" total="${answerInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
