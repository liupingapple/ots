<%@page import="ots.CONSTANT"%>
<%@ page import="ots.Question" %>

<ckeditor:resources />
<ckeditor:config var="toolbar_Mytoolbar">
				[
				
				['Source', 'Preview','Styles','Format','Font','FontSize','TextColor','BGColor','Maximize','Image'],
				
				['Bold','Italic','Underline','Strike','-','Subscript','Superscript','-','SpellChecker', 'Scayt'],
				
				['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
				
				['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','Link','Unlink','Anchor','Smiley','SpecialChar'],
				
				]
</ckeditor:config>

<ckeditor:config var="toolbar_Imagetoolbar">
			[
			
			['Source', 'Preview','Image'],
			
			]
</ckeditor:config>

<script type="text/javascript">
<!--
$(function() {
	$("#type").change(function(){
		if ($('#type').val() == "${CONSTANT.YDLJ_QUESTION}") {
			$("#type_desc").text("阅读理解题大类，如果该阅读理解题需要细分为小类，请选择"); //.css("background-color","#FFFFCC")
		} else if ($('#type').val() == "${CONSTANT.YDLJ_DANXUAN}") {
			$("#type_desc").text("Child Question 是单选题");
    	} else if ($('#type').val() == "${CONSTANT.YDLJ_TIANKONG}") {
			$("#type_desc").text("Child Question 是填空题");
    	} else if ($('#type').val() == "${CONSTANT.YDLJ_WANXING_DANX}") {
			$("#type_desc").text("Parent Question 是“大的”填空题；Child Question 是单选题");
    	} else if ($('#type').val() == "${CONSTANT.YDLJ_SZMTK}") {
			$("#type_desc").text("Parent Question 是“大的”填空题；Child Question 是首字母填空题");
    	} else if ($('#type').val() == "${CONSTANT.YDLJ_DUOXTK}") {
			$("#type_desc").text("Parent Question 是“大的”填空题（包含多选项）；Child Question 是填空题");
    	}
	});
})
//-->
</script>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'type', 'error')} ">
	<label for="type">
		<g:message code="question.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="type" required="" from="${CONSTANT.YDLJ_Types}" value="${questionInstance?.type}" valueMessagePrefix="question.type" />
	&nbsp;<label style="width: 50%; text-align: left" id="type_desc">阅读理解题大类，如果该阅读理解题需要细分为小类，请选择</label>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'source', 'error')} ">
	<label for="source">
		<g:message code="question.source.label" default="Source" />
		
	</label>
	<g:textField name="source" value="${questionInstance?.source}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'term', 'error')} ">
	<label for="term">
		<g:message code="question.term.label" default="Term" />
		
	</label>
	<g:textField name="term" value="${questionInstance?.term}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'difficultyLevel', 'error')} ">
	<label for="difficultyLevel">
		<g:message code="question.difficultyLevel.label" default="Difficulty Level" />
		
	</label>
	<g:textField name="difficultyLevel" value="${questionInstance?.difficultyLevel}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'analysis', 'error')} ">
	<label for="analysis">
		<g:message code="question.analysis.label" default="Analysis" />			
	</label>
	<g:textArea name="analysis" cols="60" rows="3" maxlength="1000" value="${questionInstance?.analysis}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'instructions', 'error')} ">
	<label for="instructions">
		<g:message code="question.instructions.label" default="Instructions" />			
	</label>
	<g:textField name="instructions" value="${questionInstance?.instructions}" size="108"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'qrCode', 'error')} ">
	<label for="qrCode">
		<g:message code="question.qrCode.label" default="二维码Key" />
		
	</label>
	<g:textField name="qrCode" value="${questionInstance?.qrCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'qrCodeVideoURL', 'error')} ">
	<label for="qrCodeVideoURL">
		<g:message code="question.qrCodeVideoURL.label" default="二维码讲解视频ID" />
		
	</label>
	<g:textField name="qrCodeVideoURL" value="${questionInstance?.qrCodeVideoURL}" size="100" />
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'description', 'error')} required">
	<label for="description">
		<g:message code="question.description.label" default="Description" />
		<span class="required-indicator">*</span>
	</label>
	
	<ckeditor:editor toolbar="Mytoolbar" name="description" height="300px" width="100%" skin='office2003'>
		${questionInstance?.description}
	</ckeditor:editor>	    
</div>

<g:if test="${questionInstance?.id}">
	<ol class="property-list question">	
	<g:each in="${ questionInstance?.childQuestions  }" var="childQuestion" status="seq">
		<li class="fieldcontain">
		<span id="childQuestions-label" class="property-label">Child question ${seq+1 }:</span>
		<span class="property-value"><g:link class="save" action="show" id="${childQuestion?.id}">${childQuestion?.id }</g:link> - ${childQuestion?.description }
		</span>
		</li>
	</g:each>
	<li class="fieldcontain"><span id="childQuestions-label" class="property-label"></span>
		<span class="property-value"><g:link class="save" action="create" params="${[parentQuestionId:questionInstance?.id, toCreateQuestionType:'XZTK'] }">Add child question</g:link>
		</span>
	</li>
	</ol>
</g:if>