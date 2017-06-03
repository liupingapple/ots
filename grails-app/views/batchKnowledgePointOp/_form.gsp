<%@ page import="ots.BatchKnowledgePointOp" %>



<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'operation', 'error')} ">
	<label for="operation">
		<g:message code="batchKnowledgePointOp.operation.label" default="Operation" />
		
	</label>
	<g:select name="operation" from="${batchKnowledgePointOpInstance.constraints.operation.inList}" value="${batchKnowledgePointOpInstance?.operation}" valueMessagePrefix="batchKnowledgePointOp.operation" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'extraFields', 'error')} ">
	<label for="extraFields">
		<g:message code="batchKnowledgePointOp.extraFields.label" default="Extra Fields" />
		
	</label>
	<g:textField name="extraFields" value="${batchKnowledgePointOpInstance?.extraFields}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'startNode', 'error')} ">
	<label for="startNode">
		<g:message code="batchKnowledgePointOp.startNode.label" default="Start Node" />
		
	</label>
	<g:textField name="startNode" value="${batchKnowledgePointOpInstance?.startNode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'traverseDepth', 'error')} ">
	<label for="traverseDepth">
		<g:message code="batchKnowledgePointOp.traverseDepth.label" default="Traverse Depth" />
		
	</label>
	<g:field name="traverseDepth" type="number" value="${batchKnowledgePointOpInstance.traverseDepth}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'startIndex', 'error')} ">
	<label for="startIndex">
		<g:message code="batchKnowledgePointOp.startIndex.label" default="Start Index" />
		
	</label>
	<g:field name="startIndex" type="number" value="${batchKnowledgePointOpInstance.startIndex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'endIndex', 'error')} ">
	<label for="endIndex">
		<g:message code="batchKnowledgePointOp.endIndex.label" default="End Index" />
		
	</label>
	<g:field name="endIndex" type="number" value="${batchKnowledgePointOpInstance.endIndex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: batchKnowledgePointOpInstance, field: 'batchContent', 'error')} ">
	<label for="batchContent">
		<g:message code="batchKnowledgePointOp.batchContent.label" default="Batch Content" />
		
	</label>
	<g:textArea name="batchContent" cols="60" rows="10" maxlength="5000000" value="${batchKnowledgePointOpInstance?.batchContent}"/>
</div>
