
<%@ page import="ots.ChapterPiece" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'chapterPiece.label', default: 'ChapterPiece')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-chapterPiece" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-chapterPiece" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'chapterPiece.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="content" title="${message(code: 'chapterPiece.content.label', default: 'Content')}" />
					
						<th><g:message code="chapterPiece.chapter.label" default="Chapter" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${chapterPieceInstanceList}" status="i" var="chapterPieceInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${chapterPieceInstance.id}">${fieldValue(bean: chapterPieceInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: chapterPieceInstance, field: "content")}</td>
					
						<td>${fieldValue(bean: chapterPieceInstance, field: "chapter")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${chapterPieceInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
