
<%@ page import="ots.BatchQuestionOp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-batchQuestionOp" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="transferYDLJ">迁移阅读理解题</g:link></li>
			</ul>
		</div>
		<div id="list-batchQuestionOp" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="operation" title="${message(code: 'batchQuestionOp.operation.label', default: 'Operation')}" />
					
						<g:sortableColumn property="type" title="${message(code: 'batchQuestionOp.type.label', default: 'Type')}" />
					
						<g:sortableColumn property="extraFields" title="${message(code: 'batchQuestionOp.extraFields.label', default: 'Extra Fields')}" />
					
						<g:sortableColumn property="startIndex" title="${message(code: 'batchQuestionOp.startIndex.label', default: 'Start Index')}" />
					
						<g:sortableColumn property="endIndex" title="${message(code: 'batchQuestionOp.endIndex.label', default: 'End Index')}" />
					
						<g:sortableColumn property="source" title="${message(code: 'batchQuestionOp.source.label', default: 'Source')}" />

						<g:sortableColumn property="dateCreated" title="${message(code: 'batchQuestionOp.dateCreated.label', default: 'Created on')}" />					
					</tr>
				</thead>
				<tbody>
				<g:each in="${batchQuestionOpInstanceList}" status="i" var="batchQuestionOpInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${batchQuestionOpInstance.id}">${fieldValue(bean: batchQuestionOpInstance, field: "operation")}</g:link></td>
					
						<td>${fieldValue(bean: batchQuestionOpInstance, field: "type")}</td>
					
						<td>${fieldValue(bean: batchQuestionOpInstance, field: "extraFields")}</td>
					
						<td>${fieldValue(bean: batchQuestionOpInstance, field: "startIndex")}</td>
					
						<td>${fieldValue(bean: batchQuestionOpInstance, field: "endIndex")}</td>
					
						<td>${fieldValue(bean: batchQuestionOpInstance, field: "source")}</td>
					
						<td>${fieldValue(bean: batchQuestionOpInstance, field: "dateCreated")}</td>
						
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate controller="batchQuestionOp" action="list" total="${batchQuestionOpInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
