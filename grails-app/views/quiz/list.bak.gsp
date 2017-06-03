<%@ page import="ots.Quiz" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'quiz.label', default: 'Quiz')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>		
		<a href="#list-quiz" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="continuePractice">
					<g:if test="${quizInstanceList}">
						继续练习
					</g:if>
					<g:else>
						开始练习
					</g:else>
				</g:link></li>
			    <li><a href="${createLink(uri: '/assignmentStatus')}">作业列表</a></li>
			 </ul>
		</div>
		<div id="list-quiz" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="练习" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="name" title="${message(code: 'quiz.name.label', default: '名称')}" />
						<g:sortableColumn property="type" title="${message(code: 'quiz.assignment.label', default: '练习范围')}" />
						<g:sortableColumn property="type" title="布置人" />
						<g:sortableColumn property="status" title="${message(code: 'quiz.status.label', default: '状态')}" />
						<g:sortableColumn property="score" title="${message(code: 'quiz.score.label', default: '成绩')}" />
						<g:sortableColumn property="timeTaken" title="${message(code: 'quiz.timeTaken.label', default: '耗时')}" />
					    <g:sortableColumn property="answeredDate" title="${message(code: 'quiz.answeredDate.label', default: '完成日期')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${quizInstanceList}" status="i" var="quizInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${quizInstance.id}">${fieldValue(bean: quizInstance, field: "name")}</g:link></td>
						<td>${fieldValue(bean: quizInstance, field: "assignment")}</td>
						<td>${fieldValue(bean: quizInstance.assignment, field: "assignedBy")}</td>
						
						<td>${fieldValue(bean: quizInstance, field: "status")}</td>
						<td>${fieldValue(bean: quizInstance, field: "score")}</td>
						<td>${fieldValue(bean: quizInstance, field: "timeTaken")}</td>
						<td><g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT" date="${quizInstance?.answeredDate}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${quizInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
