
<%@ page import="ots.BatchOperation" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'batchOperation.label', default: 'BatchOperation')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-batchOperation" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-batchOperation" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list batchOperation">
			
				<g:if test="${batchOperationInstance?.batchOperation}">
				<li class="fieldcontain">
					<span id="batchOperation-label" class="property-label"><g:message code="batchOperation.batchOperation.label" default="Batch Operation" /></span>
					
						<span class="property-value" aria-labelledby="batchOperation-label"><g:fieldValue bean="${batchOperationInstance}" field="batchOperation"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchOperationInstance?.startIndex}">
				<li class="fieldcontain">
					<span id="startIndex-label" class="property-label"><g:message code="batchOperation.startIndex.label" default="Start Index" /></span>
					
						<span class="property-value" aria-labelledby="startIndex-label"><g:fieldValue bean="${batchOperationInstance}" field="startIndex"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchOperationInstance?.endIndex}">
				<li class="fieldcontain">
					<span id="endIndex-label" class="property-label"><g:message code="batchOperation.endIndex.label" default="End Index" /></span>
					
						<span class="property-value" aria-labelledby="endIndex-label"><g:fieldValue bean="${batchOperationInstance}" field="endIndex"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchOperationInstance?.columnSelector}">
				<li class="fieldcontain">
					<span id="columnSelector-label" class="property-label"><g:message code="batchOperation.columnSelector.label" default="Column Selector" /></span>
					
						<span class="property-value" aria-labelledby="columnSelector-label"><g:fieldValue bean="${batchOperationInstance}" field="columnSelector"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchOperationInstance?.startNode}">
				<li class="fieldcontain">
					<span id="startNode-label" class="property-label"><g:message code="batchOperation.startNode.label" default="Start Node" /></span>
					
						<span class="property-value" aria-labelledby="startNode-label"><g:fieldValue bean="${batchOperationInstance}" field="startNode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchOperationInstance?.traverseDepth}">
				<li class="fieldcontain">
					<span id="traverseDepth-label" class="property-label"><g:message code="batchOperation.traverseDepth.label" default="Traverse Depth" /></span>
					
						<span class="property-value" aria-labelledby="traverseDepth-label"><g:fieldValue bean="${batchOperationInstance}" field="traverseDepth"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchOperationInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="batchOperation.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${batchOperationInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>

				<g:if test="${batchOperationInstance?.batchResult}">
				<li class="fieldcontain">
					<span id="batchResult-label" class="property-label"><g:message code="batchOperation.batchResult.label" default="Result" /></span>
					
						<span class="property-value" aria-labelledby="batchResult-label"><g:fieldValue bean="${batchOperationInstance}" field="batchResult"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${batchOperationInstance?.id}" />
					<g:link class="edit" action="edit" id="${batchOperationInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
