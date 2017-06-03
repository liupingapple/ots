
<%@ page import="ots.QuizQuestionRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-quizQuestionRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-quizQuestionRecord" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="quizQuestionRecord.question.label" default="Question" /></th>
					
						<th><g:message code="quizQuestionRecord.quiz.label" default="Quiz" /></th>

						<g:sortableColumn property="briefComment" title="${message(code: 'quizQuestionRecord.briefComment.label', default: 'Brief Comment')}" />					
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${quizQuestionRecordInstanceList}" status="i" var="quizQuestionRecordInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${quizQuestionRecordInstance.id}">${fieldValue(bean: quizQuestionRecordInstance, field: "question")}</g:link></td>
					
						<td>${fieldValue(bean: quizQuestionRecordInstance, field: "quiz")}</td>

						<td>${fieldValue(bean: quizQuestionRecordInstance, field: "briefComment")}</td>
										
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${quizQuestionRecordInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
