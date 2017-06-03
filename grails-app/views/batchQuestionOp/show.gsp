
<%@ page import="ots.BatchQuestionOp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'batchQuestionOp.label', default: 'BatchQuestionOp')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-batchQuestionOp" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-batchQuestionOp" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list batchQuestionOp">
			
				<g:if test="${batchQuestionOpInstance?.operation}">
				<li class="fieldcontain">
					<span id="operation-label" class="property-label"><g:message code="batchQuestionOp.operation.label" default="Operation" /></span>
					
						<span class="property-value" aria-labelledby="operation-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="operation"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="batchQuestionOp.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.extraFields}">
				<li class="fieldcontain">
					<span id="extraFields-label" class="property-label"><g:message code="batchQuestionOp.extraFields.label" default="Extra Fields" /></span>
					
						<span class="property-value" aria-labelledby="extraFields-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="extraFields"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.startIndex}">
				<li class="fieldcontain">
					<span id="startIndex-label" class="property-label"><g:message code="batchQuestionOp.startIndex.label" default="Start Index" /></span>
					
						<span class="property-value" aria-labelledby="startIndex-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="startIndex"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.endIndex}">
				<li class="fieldcontain">
					<span id="endIndex-label" class="property-label"><g:message code="batchQuestionOp.endIndex.label" default="End Index" /></span>
					
						<span class="property-value" aria-labelledby="endIndex-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="endIndex"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.source}">
				<li class="fieldcontain">
					<span id="source-label" class="property-label"><g:message code="batchQuestionOp.source.label" default="Source" /></span>
					
						<span class="property-value" aria-labelledby="source-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="source"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.term}">
				<li class="fieldcontain">
					<span id="term-label" class="property-label"><g:message code="batchQuestionOp.term.label" default="Term" /></span>
					
						<span class="property-value" aria-labelledby="term-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="term"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.errorRate}">
				<li class="fieldcontain">
					<span id="errorRate-label" class="property-label"><g:message code="batchQuestionOp.errorRate.label" default="Error Rate" /></span>
					
						<span class="property-value" aria-labelledby="errorRate-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="errorRate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.inputBy}">
				<li class="fieldcontain">
					<span id="inputBy-label" class="property-label"><g:message code="batchQuestionOp.inputBy.label" default="Input By" /></span>
					
						<span class="property-value" aria-labelledby="inputBy-label"><g:link controller="adminUser" action="show" id="${batchQuestionOpInstance?.inputBy?.id}">${batchQuestionOpInstance?.inputBy?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.inputDate}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="batchQuestionOp.inputDate.label" default="Input Date" /></span>
					
						<span class="property-value" aria-labelledby="inputDate-label"><g:formatDate date="${batchQuestionOpInstance?.inputDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.reviewedBy}">
				<li class="fieldcontain">
					<span id="reviewedBy-label" class="property-label"><g:message code="batchQuestionOp.reviewedBy.label" default="Reviewed By" /></span>
					
						<span class="property-value" aria-labelledby="reviewedBy-label"><g:fieldValue bean="${batchQuestionOpInstance}" field="reviewedBy"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${batchQuestionOpInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="batchQuestionOp.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${batchQuestionOpInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${batchQuestionOpInstance?.id}" />
					<g:link class="edit" action="edit" id="${batchQuestionOpInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
