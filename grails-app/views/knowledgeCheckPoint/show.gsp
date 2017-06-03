
<%@ page import="ots.KnowledgeCheckPoint" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'knowledgeCheckPoint.label', default: 'KnowledgeCheckPoint')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-knowledgeCheckPoint" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-knowledgeCheckPoint" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list knowledgeCheckPoint">
			
				<g:if test="${knowledgeCheckPointInstance?.knowledge}">
				<li class="fieldcontain">
					<span id="knowledge-label" class="property-label"><g:message code="knowledgeCheckPoint.knowledge.label" default="Knowledge" /></span>
					
						<span class="property-value" aria-labelledby="knowledge-label"><g:link controller="knowledgePoint" action="show" id="${knowledgeCheckPointInstance?.knowledge?.id}">${knowledgeCheckPointInstance?.knowledge?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.quiz}">
				<li class="fieldcontain">
					<span id="quiz-label" class="property-label"><g:message code="knowledgeCheckPoint.quiz.label" default="Quiz" /></span>
					
						<span class="property-value" aria-labelledby="quiz-label"><g:link controller="quiz" action="show" id="${knowledgeCheckPointInstance?.quiz?.id}">${knowledgeCheckPointInstance?.quiz?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.totalQuestions}">
				<li class="fieldcontain">
					<span id="totalQuestions-label" class="property-label"><g:message code="knowledgeCheckPoint.totalQuestions.label" default="Total Questions" /></span>
					
						<span class="property-value" aria-labelledby="totalQuestions-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="totalQuestions"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.correctQuestions}">
				<li class="fieldcontain">
					<span id="correctQuestions-label" class="property-label"><g:message code="knowledgeCheckPoint.correctQuestions.label" default="Correct Questions" /></span>
					
						<span class="property-value" aria-labelledby="correctQuestions-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="correctQuestions"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.recentCorrectRate}">
				<li class="fieldcontain">
					<span id="recentCorrectRate-label" class="property-label"><g:message code="knowledgeCheckPoint.recentCorrectRate.label" default="Recent Correct Rate" /></span>
					
						<span class="property-value" aria-labelledby="recentCorrectRate-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="recentCorrectRate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.recentTotal}">
				<li class="fieldcontain">
					<span id="recentTotal-label" class="property-label"><g:message code="knowledgeCheckPoint.recentTotal.label" default="Recent Total" /></span>
					
						<span class="property-value" aria-labelledby="recentTotal-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="recentTotal"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.recentCorrect}">
				<li class="fieldcontain">
					<span id="recentCorrect-label" class="property-label"><g:message code="knowledgeCheckPoint.recentCorrect.label" default="Recent Correct" /></span>
					
						<span class="property-value" aria-labelledby="recentCorrect-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="recentCorrect"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.totalSum}">
				<li class="fieldcontain">
					<span id="totalSum-label" class="property-label"><g:message code="knowledgeCheckPoint.totalSum.label" default="Total Sum" /></span>
					
						<span class="property-value" aria-labelledby="totalSum-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="totalSum"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.familyTotal}">
				<li class="fieldcontain">
					<span id="familyTotal-label" class="property-label"><g:message code="knowledgeCheckPoint.familyTotal.label" default="Family Total" /></span>
					
						<span class="property-value" aria-labelledby="familyTotal-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="familyTotal"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.familyCorrect}">
				<li class="fieldcontain">
					<span id="familyCorrect-label" class="property-label"><g:message code="knowledgeCheckPoint.familyCorrect.label" default="Family Correct" /></span>
					
						<span class="property-value" aria-labelledby="familyCorrect-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="familyCorrect"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.familyRecentTotal}">
				<li class="fieldcontain">
					<span id="familyRecentTotal-label" class="property-label"><g:message code="knowledgeCheckPoint.familyRecentTotal.label" default="Family Recent Total" /></span>
					
						<span class="property-value" aria-labelledby="familyRecentTotal-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="familyRecentTotal"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.familyRecentCorrect}">
				<li class="fieldcontain">
					<span id="familyRecentCorrect-label" class="property-label"><g:message code="knowledgeCheckPoint.familyRecentCorrect.label" default="Family Recent Correct" /></span>
					
						<span class="property-value" aria-labelledby="familyRecentCorrect-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="familyRecentCorrect"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.familyRecentCorrectRate}">
				<li class="fieldcontain">
					<span id="familyRecentCorrectRate-label" class="property-label"><g:message code="knowledgeCheckPoint.familyRecentCorrectRate.label" default="Family Recent Correct Rate" /></span>
					
						<span class="property-value" aria-labelledby="familyRecentCorrectRate-label"><g:fieldValue bean="${knowledgeCheckPointInstance}" field="familyRecentCorrectRate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?."dateCreated"}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="knowledgeCheckPoint.dateCreated.label" default="Check Date" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${knowledgeCheckPointInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgeCheckPointInstance?.student}">
				<li class="fieldcontain">
					<span id="student-label" class="property-label"><g:message code="knowledgeCheckPoint.student.label" default="Student" /></span>
					
						<span class="property-value" aria-labelledby="student-label"><g:link controller="student" action="show" id="${knowledgeCheckPointInstance?.student?.id}">${knowledgeCheckPointInstance?.student?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>

				<g:if test="${knowledgeCheckPointInstance?.records}">
				<li class="fieldcontain">
					<span id="question-label" class="property-label"><g:message code="knowledgeCheckPoint.questions.label" default="Questions" /></span>
					
						<g:each in="${knowledgeCheckPointInstance.records}" var="a">
						<span class="property-value" aria-labelledby="questions-label">
							<g:if test="${flash.readOnly}">
								${a?.encodeAsHTML()}
							</g:if>
							<g:else>
								<g:link controller="quizQuestionRecord" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link>
							</g:else>
						</span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${knowledgeCheckPointInstance?.id}" />
					<g:link class="edit" action="edit" id="${knowledgeCheckPointInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
