
<%@ page import="ots.AssignmentStatus" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'assignmentStatus.label', default: 'AssignmentStatus')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-assignmentStatus" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-assignmentStatus" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list assignmentStatus">
			
				<g:if test="${assignmentStatusInstance?.assignment}">
				<li class="fieldcontain">
					<span id="assignment-label" class="property-label"><g:message code="assignmentStatus.assignment.label" default="Assignment" /></span>
					
						<span class="property-value" aria-labelledby="assignment-label"><g:link controller="assignment" action="show" id="${assignmentStatusInstance?.assignment?.id}">${assignmentStatusInstance?.assignment?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.comment}">
				<li class="fieldcontain">
					<span id="comment-label" class="property-label"><g:message code="assignmentStatus.comment.label" default="Comment" /></span>
					
						<span class="property-value" aria-labelledby="comment-label"><g:fieldValue bean="${assignmentStatusInstance}" field="comment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="assignmentStatus.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${assignmentStatusInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.toBeFocusedKnowledge}">
				<li class="fieldcontain">
					<span id="toBeFocusedKnowledge-label" class="property-label"><g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="To Be Focused Knowledge" /></span>
					
						<span class="property-value" aria-labelledby="toBeFocusedKnowledge-label"><g:link controller="knowledgePoint" action="show" id="${assignmentStatusInstance?.toBeFocusedKnowledge?.id}">${assignmentStatusInstance?.toBeFocusedKnowledge?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.availableQuestions}">
				<li class="fieldcontain">
					<span id="availableQuestions-label" class="property-label"><g:message code="assignmentStatus.availableQuestions.label" default="Available Questions" /></span>
					
						<span class="property-value" aria-labelledby="availableQuestions-label"><g:fieldValue bean="${assignmentStatusInstance}" field="availableQuestions"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.correctQuestions}">
				<li class="fieldcontain">
					<span id="correctQuestions-label" class="property-label"><g:message code="assignmentStatus.correctQuestions.label" default="Correct Questions" /></span>
					
						<span class="property-value" aria-labelledby="correctQuestions-label"><g:fieldValue bean="${assignmentStatusInstance}" field="correctQuestions"/></span>
					
				</li>
				</g:if>
			
				<%--<g:if test="${assignmentStatusInstance?.coverageRate}">
				<li class="fieldcontain">
					<span id="coverageRate-label" class="property-label"><g:message code="assignmentStatus.coverageRate.label" default="Coverage Rate" /></span>
					
						<span class="property-value" aria-labelledby="coverageRate-label"><g:fieldValue bean="${assignmentStatusInstance}" field="coverageRate"/></span>
					
				</li>
				</g:if>--%>
			
				<g:if test="${assignmentStatusInstance?.coveredKPs}">
				<li class="fieldcontain">
					<span id="coveredKPs-label" class="property-label"><g:message code="assignmentStatus.coveredKPs.label" default="Covered KP s" /></span>
					
						<g:each in="${assignmentStatusInstance.coveredKPs}" var="c">
						<span class="property-value" aria-labelledby="coveredKPs-label"><g:link controller="knowledgePoint" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.coveredKnowledgPoints}">
				<li class="fieldcontain">
					<span id="coveredKnowledgPoints-label" class="property-label"><g:message code="assignmentStatus.coveredKnowledgPoints.label" default="Covered Knowledg Points" /></span>
					
						<span class="property-value" aria-labelledby="coveredKnowledgPoints-label"><g:fieldValue bean="${assignmentStatusInstance}" field="coveredKnowledgPoints"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.finishedQuestions}">
				<li class="fieldcontain">
					<span id="finishedQuestions-label" class="property-label"><g:message code="assignmentStatus.finishedQuestions.label" default="Finished Questions" /></span>
					
						<span class="property-value" aria-labelledby="finishedQuestions-label"><g:fieldValue bean="${assignmentStatusInstance}" field="finishedQuestions"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.masterRate}">
				<li class="fieldcontain">
					<span id="masterRate-label" class="property-label"><g:message code="assignmentStatus.masterRate.label" default="Master Rate" /></span>
					
						<span class="property-value" aria-labelledby="masterRate-label"><g:fieldValue bean="${assignmentStatusInstance}" field="masterRate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.relativeTargetCorrectRate}">
				<li class="fieldcontain">
					<span id="relativeTargetCorrectRate-label" class="property-label"><g:message code="assignmentStatus.relativeTargetCorrectRate.label" default="Relative Target Correct Rate" /></span>
					
						<span class="property-value" aria-labelledby="relativeTargetCorrectRate-label"><g:fieldValue bean="${assignmentStatusInstance}" field="relativeTargetCorrectRate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.student}">
				<li class="fieldcontain">
					<span id="student-label" class="property-label"><g:message code="assignmentStatus.student.label" default="Student" /></span>
					
						<span class="property-value" aria-labelledby="student-label"><g:link controller="student" action="show" id="${assignmentStatusInstance?.student?.id}">${assignmentStatusInstance?.student?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${assignmentStatusInstance?.totalKnowledgePoints}">
				<li class="fieldcontain">
					<span id="totalKnowledgePoints-label" class="property-label"><g:message code="assignmentStatus.totalKnowledgePoints.label" default="Total Knowledge Points" /></span>
					
						<span class="property-value" aria-labelledby="totalKnowledgePoints-label"><g:fieldValue bean="${assignmentStatusInstance}" field="totalKnowledgePoints"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${assignmentStatusInstance?.id}" />
					<g:link class="edit" action="edit" id="${assignmentStatusInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
