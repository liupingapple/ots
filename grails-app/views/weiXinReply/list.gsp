
<%@ page import="ots.WeiXinReply" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'weiXinReply.label', default: 'WeiXinReply')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-weiXinReply" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-weiXinReply" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="keyWords" title="${message(code: 'weiXinReply.keyWords.label', default: 'Key Words')}" />
					
						<g:sortableColumn property="replyContent" title="${message(code: 'weiXinReply.replyContent.label', default: 'Reply Content')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${weiXinReplyInstanceList}" status="i" var="weiXinReplyInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${weiXinReplyInstance.id}">${fieldValue(bean: weiXinReplyInstance, field: "keyWords")}</g:link></td>
					
						<td>${fieldValue(bean: weiXinReplyInstance, field: "replyContent")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${weiXinReplyInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
