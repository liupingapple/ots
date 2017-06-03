<%@ page import="ots.BatchOperation" %>
<%@ page import="ots.CONSTANT"%>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'operation', 'error')} ">
	<label for="batchOperation">
		<g:message code="batchOperation.batchOperation.label" default="Batch Operation" />
		
	</label>
	<%-- <g:select name="batchOperation" from="${batchOperationInstance.constraints.batchOperation.inList}" value="${batchOperationInstance?.batchOperation}" valueMessagePrefix="batchOperation.batchOperation" noSelection="['': '']"/> --%>
	<g:select name="batchOperation" from="${CONSTANT.BATCH_OPERATIONS}" value="${batchOperationInstance?.batchOperation}" valueMessagePrefix="batchOperation.batchOperation" noSelection="['': '']"/>	
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'startIndex', 'error')} ">
	<label for="startIndex">
		<g:message code="batchOperation.startIndex.label" default="Start Index" />
		
	</label>
	<g:field name="startIndex" type="number" value="${batchOperationInstance.startIndex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'endIndex', 'error')} ">
	<label for="endIndex">
		<g:message code="batchOperation.endIndex.label" default="End Index" />
		
	</label>
	<g:field name="endIndex" type="number" value="${batchOperationInstance.endIndex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'columnSelector', 'error')} ">
	<label for="columnSelector">
		<g:message code="batchOperation.columnSelector.label" default="Column Selector" />
		
	</label>
	<g:textField name="columnSelector" value="${batchOperationInstance?.columnSelector}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'startNode', 'error')} ">
	<label for="startNode">
		<g:message code="batchOperation.startNode.label" default="Start Node" />
		
	</label>
	<g:textField name="startNode" value="${batchOperationInstance?.startNode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'traverseDepth', 'error')} ">
	<label for="traverseDepth">
		<g:message code="batchOperation.traverseDepth.label" default="Traverse Depth" />
		
	</label>
	<g:field name="traverseDepth" type="number" value="${batchOperationInstance.traverseDepth}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'batchInput', 'error')} ">
	<label for="batchInput">
		<g:message code="batchOperation.batchInput.label" default="Batch Input" />
		
	</label>
	<g:textArea name="batchInput" cols="60" rows="10" maxlength="5000000" value="${batchOperationInstance?.batchInput}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'batchOutput', 'error')} ">
	<label for="batchOutput">
		<g:message code="batchOperation.batchOutput.label" default="Batch Output" />
		
	</label>
	<g:textArea name="batchOutput" cols="60" rows="10" maxlength="5000000" value="${batchOperationInstance?.batchOutput}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchOperationInstance, field: 'batchResult', 'error')} ">
	<label for="batchResult">
		<g:message code="batchOperation.batchResult.label" default="Result" />
		
	</label>
	<g:textArea name="batchResult" cols="60" rows="10" maxlength="5000000" value="${batchOperationInstance?.batchResult}"/>
</div>
