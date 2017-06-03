<%@ page import="ots.Assignment" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="bmain">
		<g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="assignmentList" class="container bs-docs-container" role="main">
			<div class="col-md-3">
				&nbsp;&nbsp;
				<div class="bs-sidebar hidden-print" role="complementary">
					<div class="panel panel-warning">
						<div class="panel-body">
							<ul class="nav">
								<li>
									<g:link class="btn btn-default" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>&nbsp;
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-md-9">
				&nbsp;
				<g:if test="${flash.message}">
					<div class="message" role="status">
						${flash.message}
					</div>
				</g:if>
				
				<g:hasErrors bean="${assignmentInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${assignmentInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
				</g:hasErrors>
				<g:form action="save" >
						<div class="panel panel-default">
							<div class="panel-heading">
								<g:message code="default.create.label" args="[entityName]" />
							</div>
							<div class="panel-body">
								<g:render template="form"/>
							</div>
						</div>
						
					<div class="panel-footer">
						<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
						<g:actionSubmit class="btn btn-default" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" formnovalidate="" onclick="history.back();return false;" />
					</div>
				</g:form>			
			</div>
		</div>
		
	</body>
</html>
