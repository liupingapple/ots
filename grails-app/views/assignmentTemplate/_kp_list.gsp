<%@ page import="ots.AssignmentTemplate"%>

<div class="fieldcontain ${hasErrors(bean: assignmentTemplateInstance, field: 'knowledgePoints', 'error')} ">
	<label for="knowledgePoints"> <g:message code="assignmentTemplate.knowledgePoints.label"
			default="Knowledge Points" />

	</label>

	<g:select id="knowledgePointSelection" name="availableKPs" from="${kpList}" multiple="multiple"
		optionKey="id" size="8" value="${assignmentTemplateInstance?.knowledgePoints*.id}" class="many-to-many" />
</div>
