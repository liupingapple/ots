
<%@ page import="ots.WeiXinReply" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'weiXinReply.label', default: 'WeiXinReply')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-weiXinReply" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-weiXinReply" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list weiXinReply">
			
				<g:if test="${weiXinReplyInstance?.keyWords}">
				<li class="fieldcontain">
					<span id="keyWords-label" class="property-label"><g:message code="weiXinReply.keyWords.label" default="Key Words" /></span>
					
						<span class="property-value" aria-labelledby="keyWords-label"><g:fieldValue bean="${weiXinReplyInstance}" field="keyWords"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${weiXinReplyInstance?.replyContent}">
				<li class="fieldcontain">
					<span id="replyContent-label" class="property-label"><g:message code="weiXinReply.replyContent.label" default="Reply Content" /></span>
					
						<span class="property-value" aria-labelledby="replyContent-label"><g:fieldValue bean="${weiXinReplyInstance}" field="replyContent"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${weiXinReplyInstance?.id}" />
					<g:link class="edit" action="edit" id="${weiXinReplyInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
