
<%@page import="ots.Student"%>
<%@ page import="ots.QuizQuestionRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-quizQuestionRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<g:if test="${!session.user instanceof Student }">
				  <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				  <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</g:if>
			</ul>
		</div>
		
		<g:form name="questionForm" action="judgeAndRecord">
		
		<div id="show-quizQuestionRecord" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list quizQuestionRecord">
			
				<g:if test="${quizQuestionRecordInstance?.question}">
				<li class="fieldcontain">
					<span id="question-label" class="property-label"><g:message code="quizQuestionRecord.question.label" default="Question" /></span>
					
					<span class="property-value" aria-labelledby="question-label">
					  <g:cvtq question="${quizQuestionRecordInstance?.question}" />
					  <!-- don't link to show question details here, otherwise student may get the answer defined -->
					</span>
				</li>
				</g:if>
			
				<g:if test="${quizQuestionRecordInstance?.quiz}">
				<li class="fieldcontain">
					<span id="quiz-label" class="property-label"><g:message code="quizQuestionRecord.quiz.label" default="Quiz" /></span>
					
						<span class="property-value" aria-labelledby="quiz-label"><g:link controller="quiz" action="show" id="${quizQuestionRecordInstance?.quiz?.id}">${quizQuestionRecordInstance?.quiz?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${quizQuestionRecordInstance?.id}" />
					<g:if test="${!session.user instanceof Student }">
					  <g:link class="edit" action="edit" id="${quizQuestionRecordInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</g:if>
		            <g:hiddenField name="quizQuestionRecordInstanceId" value="${quizQuestionRecordInstance?.id}" />
					<span class="button"><input type="submit" value="提交答案" /></span>
					<g:paginate action="navShow" params="${params}" total="${quizQuestionRecordInstance.quiz.records.size()}" max="1"/>	
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
