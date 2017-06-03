<%@ page import="ots.Assignment"%>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.templates.label" default="Templates" />
		<span class="required-indicator">*</span>
	</dt>
	<dd>
		<g:select id="assignmentTemplateSelection" name="templates" required="" from="${assignmentTemplateList}" multiple="multiple"
			optionKey="id" size="10" value="${assignmentInstance?.templates*.id}" class="many-to-many" />
	</dd>
</dl>
