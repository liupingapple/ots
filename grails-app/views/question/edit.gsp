<%@page import="ots.CONSTANT"%>
<%@ page import="ots.Question" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'question.label', default: 'Question')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-question" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="edit-question" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${questionInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${questionInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${questionInstance?.id}" />
				<g:hiddenField name="version" value="${questionInstance?.version}" />
				<fieldset class="form">
					<g:if test="${CONSTANT.YDLJ_Types.contains(questionInstance?.type)}">
						<g:render template="formYDLJ"/>
					</g:if>
					<g:else>
						<g:if test="${questionInstance?.parentQuestion}">
														
							<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'parentQuestion', 'error')} ">
								<label for="parentQuestion">
									<g:message code="question.parentQuestion.label" default="ParentQuestion" />
									<span class="required-indicator">*</span>
								</label>
								<g:textField readonly="true" name="parentQuestionId" value="${parentQuestionId}"/>
								<g:textField readonly="true" size="50" name="parentQuestionType" value="${ots.Question.get(parentQuestionId)?.type}"/>	
							</div>
							
							<ckeditor:resources />
							<ckeditor:config var="toolbar_Mytoolbar">
											[]
							</ckeditor:config>
							<div class="fieldcontain">
								
								<ckeditor:editor toolbar="Mytoolbar" name="descriptionJustToShow" height="200px" width="100%" skin='office2003' readOnly="true" toolbarStartupExpanded="false">
									${ots.Question.get(parentQuestionId)?.description}
								</ckeditor:editor>	    
							</div>
							
						</g:if>
					
						<g:render template="form"/>
						
					</g:else>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
