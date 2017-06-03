
<%@ page import="ots.ChapterPiece" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'chapterPiece.label', default: 'ChapterPiece')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-chapterPiece" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create" params="['chapter.id': chapterPieceInstance?.chapter?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-chapterPiece" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list chapterPiece">
			
				<g:if test="${chapterPieceInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="chapterPiece.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${chapterPieceInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${chapterPieceInstance?.content}">
				<li class="fieldcontain">
					<span id="content-label" class="property-label"><g:message code="chapterPiece.content.label" default="Content" /></span>
					
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${chapterPieceInstance}" field="content"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${chapterPieceInstance?.chapter}">
				<li class="fieldcontain">
					<span id="chapter-label" class="property-label"><g:message code="chapterPiece.chapter.label" default="Chapter" /></span>
					
						<span class="property-value" aria-labelledby="chapter-label"><g:link controller="chapter" action="show" id="${chapterPieceInstance?.chapter?.id}">${chapterPieceInstance?.chapter?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${chapterPieceInstance?.id}" />
					<g:link class="edit" action="edit" id="${chapterPieceInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
