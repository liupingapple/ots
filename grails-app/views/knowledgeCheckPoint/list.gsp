
<%@ page import="ots.KnowledgeCheckPoint" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-knowledgeCheckPoint" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-knowledgeCheckPoint" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="knowledgeCheckPoint.knowledge.label" default="Knowledge" /></th>
					
						<th><g:message code="knowledgeCheckPoint.quiz.label" default="Quiz" /></th>
					
						<g:sortableColumn property="totalQuestions" title="${message(code: 'knowledgeCheckPoint.totalQuestions.label', default: 'TQ#')}" />
					
						<g:sortableColumn property="correctQuestions" title="${message(code: 'knowledgeCheckPoint.correctQuestions.label', default: 'CQ#')}" />
					
						<g:sortableColumn property="recentTotal" title="${message(code: 'knowledgeCheckPoint.recentTotal.label', default: 'RT#')}" />

						<g:sortableColumn property="recentCorrectRate" title="${message(code: 'knowledgeCheckPoint.recentCorrectRate.label', default: 'RCR')}" />

						<g:sortableColumn property="familyTotal" title="FTQ#" />
					
						<g:sortableColumn property="familyCorrect" title="FCQ#" />
					
						<g:sortableColumn property="familyRecentTotal" title="FRT#" />

						<g:sortableColumn property="familyRecentCorrectRate" title="FRCR" />

						<g:sortableColumn property="student" title="FRT#" />

						<g:sortableColumn property="dateCreated" title="Date" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${knowledgeCheckPointInstanceList}" status="i" var="knowledgeCheckPointInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${knowledgeCheckPointInstance.id}">${fieldValue(bean: knowledgeCheckPointInstance, field: "knowledge")}</g:link></td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "quiz")}</td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "totalQuestions")}</td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "correctQuestions")}</td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "recentTotal")}</td>
						
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "recentCorrectRate")}</td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "familyTotal")}</td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "familyCorrect")}</td>
					
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "familyRecentTotal")}</td>
						
						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "familyRecentCorrectRate")}</td>

						<td>${fieldValue(bean: knowledgeCheckPointInstance, field: "student")}</td>
						
						<td><g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT" date="${knowledgeCheckPointInstance.dateCreated}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${knowledgeCheckPointInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
