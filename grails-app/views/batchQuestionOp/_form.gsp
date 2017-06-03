<%@ page import="ots.BatchQuestionOp" %>



<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'operation', 'error')} ">
	<label for="operation">
		<g:message code="batchQuestionOp.operation.label" default="Operation" />
		<span class="required-indicator">*</span>
	</label> <!-- don't use: batchQuestionOpInstance.constraints.operation.inList -->
	<g:select name="operation" from="${["Export in Excel format","Import in Excel format","Export Question - Knowledge mapping","Import Question - Knowledge mapping"]}" required="" value="${batchQuestionOpInstance?.operation}" valueMessagePrefix="batchQuestionOp.operation" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="batchQuestionOp.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label> <!-- don't use: batchQuestionOpInstance.constraints.type.inList -->
	<g:select name="type" from="${['单选', '填空', '阅读理解题-transfer']}" required="" value="${batchQuestionOpInstance?.type}" valueMessagePrefix="batchQuestionOp.type"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'extraFields', 'error')} ">
	<label for="extraFields">
		<g:message code="batchQuestionOp.extraFields.label" default="Extra Fields" />
		
	</label>
	<g:textField name="extraFields" value="${batchQuestionOpInstance?.extraFields}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'startIndex', 'error')} ">
	<label for="startIndex">
		<g:message code="batchQuestionOp.startIndex.label" default="Start Index" />
		
	</label>
	<g:field name="startIndex" type="number" value="${batchQuestionOpInstance.startIndex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'endIndex', 'error')} ">
	<label for="endIndex">
		<g:message code="batchQuestionOp.endIndex.label" default="End Index" />
		
	</label>
	<g:field name="endIndex" type="number" value="${batchQuestionOpInstance.endIndex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'source', 'error')} ">
	<label for="source">
		<g:message code="batchQuestionOp.source.label" default="Source" />
		
	</label>
	<g:textField name="source" value="${batchQuestionOpInstance?.source}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'term', 'error')} ">
	<label for="term">
		<g:message code="batchQuestionOp.term.label" default="Term" />
		
	</label>
	<g:textField name="term" value="${batchQuestionOpInstance?.term}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'errorRate', 'error')} required">
	<label for="errorRate">
		<g:message code="batchQuestionOp.errorRate.label" default="Error Rate" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="errorRate" type="number" value="${batchQuestionOpInstance.errorRate}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'inputBy', 'error')} required">
	<label for="inputBy">
		<g:message code="batchQuestionOp.inputBy.label" default="Input By" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="inputBy" name="inputBy.id" from="${ots.AdminUser.list()}" optionKey="id" required="" value="${batchQuestionOpInstance?.inputBy?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'inputDate', 'error')} required">
	<label for="inputDate">
		<g:message code="batchQuestionOp.inputDate.label" default="Input Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="inputDate" precision="day"  value="${batchQuestionOpInstance?.inputDate}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'reviewedBy', 'error')} ">
	<label for="reviewedBy">
		<g:message code="batchQuestionOp.reviewedBy.label" default="Reviewed By" />
		
	</label>
	<g:textField name="reviewedBy" value="${batchQuestionOpInstance?.reviewedBy}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchQuestionOpInstance, field: 'batchContent', 'error')} ">
	<label for="batchContent">
		<g:message code="batchQuestionOp.batchContent.label" default="Batch Content" />
		
	</label>
	<g:textArea name="batchContent" cols="60" rows="10" maxlength="5000000" value="${batchQuestionOpInstance?.batchContent}"/>
</div>
