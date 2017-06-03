<%@page import="ots.AdminUser"%>
<%@ page import="ots.Teacher"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'teacher.label', default: 'Teacher')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
	<div class="container bs-docs-container" role="main">
		<div class="col-md-3">
			&nbsp;&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<div class="panel panel-warning">
					<div class="panel-body">
						<ul class="nav">
							<li><g:if test="${session.user instanceof AdminUser }">
									<g:link class="btn btn-default" action="list">
										<g:message code="default.list.label" args="[entityName]" />
									</g:link> 
								&nbsp;
								<g:link class="btn btn-default" action="create">
										<g:message code="default.new.label" args="[entityName]" />
									</g:link>
									&nbsp;
								</g:if>
								<g:link class="btn btn-default" onclick="history.back();return false;">
										返回
								</g:link>
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
			<g:hasErrors bean="${teacherInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${teacherInstance}" var="error">
						<li
							<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
								error="${error}" /></li>
					</g:eachError>
				</ul>
			</g:hasErrors>

			<g:form method="post">
				<g:hiddenField name="id" value="${teacherInstance?.id}" />
				<g:hiddenField name="version" value="${teacherInstance?.version}" />

				<div class="panel panel-default">
					<div class="panel-heading">
						<g:message code="default.edit.label" args="[entityName]" />
					</div>
					<div class="panel-body">
						<g:render template="form" />
					</div>

					<div class="panel-footer">
						<g:actionSubmit class="btn btn-primary" action="update"
							value="${message(code: 'default.button.update.label', default: 'Update')}" />
						<g:if test="${session.user instanceof AdminUser }">
							<g:actionSubmit class="btn btn-warning" action="delete"
								value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate=""
								onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</g:if>
						<g:actionSubmit class="btn btn-default"
							value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" formnovalidate=""
							onclick="history.back();return false;" />
					</div>
				</div>
			</g:form>
		</div>
	</div>
</body>
</html>
