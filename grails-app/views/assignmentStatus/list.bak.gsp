
<%@ page import="ots.AssignmentStatus" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'assignmentStatus.label', default: '作业')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-assignmentStatus" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}">首页</a></li>
			    <li class="controller"><g:link controller="quiz" action="create">创建自主练习</g:link></li>
			</ul>
		</div>
		<div id="list-assignmentStatus" class="content scaffold-list" role="main">
			<h1>作业列表</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="assignmentStatus.assignment.label" default="练习范围" /></th>
											
						<g:sortableColumn property="status" title="状态" />
					
						<th><g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="薄弱点" /></th>
					
						<g:sortableColumn property="finishedQuestions" title="完成题量 " />
					
						<g:sortableColumn property="correctQuestions" title="答对" />

						<g:sortableColumn property="masterRate" title="掌握度" />
					
						<g:sortableColumn property="coverageRate" title="覆盖率" />

						<g:sortableColumn property="comment" title="讲评 " />
										
					</tr>
				</thead>
				<tbody>
				<g:each in="${assignmentStatusInstanceList}" status="i" var="assignmentStatusInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${assignmentStatusInstance.id}">${fieldValue(bean: assignmentStatusInstance, field: "assignment")}</g:link></td>
					
						<td>${fieldValue(bean: assignmentStatusInstance, field: "status")}</td>
					
						<td>${fieldValue(bean: assignmentStatusInstance, field: "toBeFocusedKnowledge")}</td>
					
						<td>${fieldValue(bean: assignmentStatusInstance, field: "finishedQuestions")}/${assignmentStatusInstance.assignment.questionLimit}</td>
					
						<td>${fieldValue(bean: assignmentStatusInstance, field: "correctQuestions")}</td>

						<td>${fieldValue(bean: assignmentStatusInstance, field: "masterRate")}%</td>
					
						<td>${fieldValue(bean: assignmentStatusInstance, field: "coverageRate")}%</td>

						<td>${fieldValue(bean: assignmentStatusInstance, field: "comment")}</td>
										
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${assignmentStatusInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
