<%@ page import="ots.Quiz" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'quiz.label', default: 'Quiz')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-quiz" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}">首页</a></li>
			</ul>
		</div>
		<div id="create-quiz" class="content scaffold-create" role="main">
			<h1>创建自主练习</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${quizInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${quizInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form>
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit action="focusGenerate" value="智能选题"/>
					<g:actionSubmit action="randomGenerate" value="随机出题"/>
					<a href="${createLink(uri: '/assignmentStatus')}">取消</a>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
