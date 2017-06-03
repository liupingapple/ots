
<%@ page import="ots.BatchKnowledgePointOp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'batchKnowledgePointOp.label', default: 'BatchKnowledgePointOp')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-batchKnowledgePointOp" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-batchKnowledgePointOp" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list batchKnowledgePointOp">
			
				<g:if test="${batchKnowledgePointOpInstance?.operation}">
				<li class="fieldcontain">
					<span id="operation-label" class="property-label"><g:message code="batchKnowledgePointOp.operation.label" default="Operation" /></span>
					
						<span class="property-value" aria-labelledby="operation-label"><g:fieldValue bean="${batchKnowledgePointOpInstance}" field="operation"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchKnowledgePointOpInstance?.extraFields}">
				<li class="fieldcontain">
					<span id="extraFields-label" class="property-label"><g:message code="batchKnowledgePointOp.extraFields.label" default="Extra Fields" /></span>
					
						<span class="property-value" aria-labelledby="extraFields-label"><g:fieldValue bean="${batchKnowledgePointOpInstance}" field="extraFields"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchKnowledgePointOpInstance?.startNode}">
				<li class="fieldcontain">
					<span id="startNode-label" class="property-label"><g:message code="batchKnowledgePointOp.startNode.label" default="Start Node" /></span>
					
						<span class="property-value" aria-labelledby="startNode-label"><g:fieldValue bean="${batchKnowledgePointOpInstance}" field="startNode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchKnowledgePointOpInstance?.traverseDepth}">
				<li class="fieldcontain">
					<span id="traverseDepth-label" class="property-label"><g:message code="batchKnowledgePointOp.traverseDepth.label" default="Traverse Depth" /></span>
					
						<span class="property-value" aria-labelledby="traverseDepth-label"><g:fieldValue bean="${batchKnowledgePointOpInstance}" field="traverseDepth"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchKnowledgePointOpInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="batchKnowledgePointOp.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${batchKnowledgePointOpInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${batchKnowledgePointOpInstance?.id}" />
					<g:link class="edit" action="edit" id="${batchKnowledgePointOpInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
