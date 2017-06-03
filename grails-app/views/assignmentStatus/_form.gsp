<%@ page import="ots.AssignmentStatus" %>



<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'assignment', 'error')} required">
	<label for="assignment">
		<g:message code="assignmentStatus.assignment.label" default="Assignment" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="assignment" name="assignment.id" from="${ots.Assignment.list()}" optionKey="id" required="" value="${assignmentStatusInstance?.assignment?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'comment', 'error')} ">
	<label for="comment">
		<g:message code="assignmentStatus.comment.label" default="Comment" />
		
	</label>
	<g:textField name="comment" value="${assignmentStatusInstance?.comment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="assignmentStatus.status.label" default="Status" />
		
	</label>
	<g:textField name="status" value="${assignmentStatusInstance?.status}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'toBeFocusedKnowledge', 'error')} ">
	<label for="toBeFocusedKnowledge">
		<g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="To Be Focused Knowledge" />
		
	</label>
	<g:select id="toBeFocusedKnowledge" name="toBeFocusedKnowledge.id" from="${ots.KnowledgePoint.list()}" optionKey="id" value="${assignmentStatusInstance?.toBeFocusedKnowledge?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'availableQuestions', 'error')} required">
	<label for="availableQuestions">
		<g:message code="assignmentStatus.availableQuestions.label" default="Available Questions" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="availableQuestions" type="number" value="${assignmentStatusInstance.availableQuestions}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'correctQuestions', 'error')} required">
	<label for="correctQuestions">
		<g:message code="assignmentStatus.correctQuestions.label" default="Correct Questions" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="correctQuestions" type="number" value="${assignmentStatusInstance.correctQuestions}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'coverageRate', 'error')} required">
	<label for="coverageRate">
		<g:message code="assignmentStatus.coverageRate.label" default="Coverage Rate" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="coverageRate" value="${fieldValue(bean: assignmentStatusInstance, field: 'coverageRate')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'coveredKPs', 'error')} ">
	<label for="coveredKPs">
		<g:message code="assignmentStatus.coveredKPs.label" default="Covered KP s" />
		
	</label>
	<g:select name="coveredKPs" from="${ots.KnowledgePoint.list()}" multiple="multiple" optionKey="id" size="5" value="${assignmentStatusInstance?.coveredKPs*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'coveredKnowledgPoints', 'error')} required">
	<label for="coveredKnowledgPoints">
		<g:message code="assignmentStatus.coveredKnowledgPoints.label" default="Covered Knowledg Points" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="coveredKnowledgPoints" type="number" value="${assignmentStatusInstance.coveredKnowledgPoints}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'finishedQuestions', 'error')} required">
	<label for="finishedQuestions">
		<g:message code="assignmentStatus.finishedQuestions.label" default="Finished Questions" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="finishedQuestions" type="number" value="${assignmentStatusInstance.finishedQuestions}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'masterRate', 'error')} required">
	<label for="masterRate">
		<g:message code="assignmentStatus.masterRate.label" default="Master Rate" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="masterRate" value="${fieldValue(bean: assignmentStatusInstance, field: 'masterRate')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'relativeTargetCorrectRate', 'error')} required">
	<label for="relativeTargetCorrectRate">
		<g:message code="assignmentStatus.relativeTargetCorrectRate.label" default="Relative Target Correct Rate" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="relativeTargetCorrectRate" value="${fieldValue(bean: assignmentStatusInstance, field: 'relativeTargetCorrectRate')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'student', 'error')} required">
	<label for="student">
		<g:message code="assignmentStatus.student.label" default="Student" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="student" name="student.id" from="${ots.Student.list()}" optionKey="id" required="" value="${assignmentStatusInstance?.student?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentStatusInstance, field: 'totalKnowledgePoints', 'error')} required">
	<label for="totalKnowledgePoints">
		<g:message code="assignmentStatus.totalKnowledgePoints.label" default="Total Knowledge Points" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="totalKnowledgePoints" type="number" value="${assignmentStatusInstance.totalKnowledgePoints}" required=""/>
</div>

