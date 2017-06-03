
<%@ page import="ots.Chapter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'chapter.label', default: 'Chapter')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-chapter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-chapter" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'chapter.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="qrCode" title="${message(code: 'chapter.qrCode.label', default: 'Qr Code')}" />
					
						<g:sortableColumn property="bookName" title="${message(code: 'chapter.bookName.label', default: 'Book Name')}" />
					
						<g:sortableColumn property="assignment" title="${message(code: 'chapter.assignment.label', default: 'Assignment')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${chapterInstanceList}" status="i" var="chapterInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${chapterInstance.id}">${fieldValue(bean: chapterInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: chapterInstance, field: "qrCode")}</td>
					
						<td>${fieldValue(bean: chapterInstance, field: "bookName")}</td>
					
						<td>${fieldValue(bean: chapterInstance, field: "assignment")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${chapterInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
