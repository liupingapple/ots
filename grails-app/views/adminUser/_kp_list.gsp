<%@page import="ots.KnowledgePoint"%>
<div class="fieldcontain">	
	<g:select id="knowledgePointSelection" name="selected_kps" from="${kpList}" multiple="multiple"
		optionKey="id" size="8" value="" class="many-to-many" />
</div>
