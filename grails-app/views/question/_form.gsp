<%@page import="ots.CONSTANT"%>
<%@ page import="ots.Question" %>
<%@page import="ots.WeiXinUtil"%>

<script type="text/javascript">
    var childCount = ${questionInstance?.answers.size()} + 0;
    var indexArray = ['A','B','C','D','E','F','G','H'];
    
    function addChild() {
        var htmlId = "answer" + childCount;
        var deleteIcon = "${resource(dir:'images/skin', file:'database_delete.png')}";
        var templateHtml = "<div id='" + htmlId + "' name='" + htmlId + "'>\n";
        if ($('#type').val() == "单选") {
        	templateHtml += "<input type='checkbox' id='answers[" + childCount + "].correct' name='answers[" + childCount + "].correct'/>\n";
        } else {
        	templateHtml += (childCount+1)+"<input type='hidden' id='answers[" + childCount + "].correct' name='answers[" + childCount + "].correct' value='true'/>\n";
        }
        templateHtml += "<input type='text' id='answers[" + childCount + "].content' name='answers[" + childCount + "].content' size='60'/>\n";
        templateHtml += "<span onClick='$(\"#" + htmlId + "\").remove();'><img src='" + deleteIcon + "' /></span>\n";
        templateHtml += "</div>\n";
        $("#childList").append(templateHtml);
        $("#answers\\[" + childCount + "\\]\\.content").focus();
        childCount++;
    }

    $(function() {
    	$("#type").change(function(){
    		if ($('#type').val() == "阅读填空") {
    			$("#description").height(400); //.css("background-color","#FFFFCC")
    		} else {
    			$("#description").height(60);
        	}
    	});
    })
</script>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'type', 'error')} ">
	<label for="type">
		<g:message code="question.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<!-- don't use 阅读填空 here - parentQuestionId?[CONSTANT.RADIO_QUESTION, CONSTANT.FBLANK_QUESTION] : [CONSTANT.RADIO_QUESTION, CONSTANT.FBLANK_QUESTION, CONSTANT.YDTK_QUESTION] -->
	<g:select name="type" required="" from="${[CONSTANT.RADIO_QUESTION, CONSTANT.FBLANK_QUESTION]}" value="${childQuestionType?childQuestionType:questionInstance?.type}" valueMessagePrefix="question.type" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'description', 'error')} required">
	<label for="description">
		<g:message code="question.description.label" default="Description" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea autofocus="true" name="description" cols="90" rows="3" maxlength="8000" required="" value="${questionInstance?.description}"/>    
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'answers', 'error')} ">
	<label for="answers">
		<g:message code="question.answers.label" default="Answers" />
	</label>

	<ul class="one-to-many">
	<div id="childList">
    	<g:each var="answer" in="${questionInstance?.answers?}" status="i">
        	<div id="answer${i}">
    			<g:hiddenField name='answers[${i}].id' value='${answer.id}'/>
    			<g:if test="${questionInstance?.type == '单选'}">
    				<g:checkBox name='answers[${i}].correct' value='${answer.correct}'/>
    				<input type="hidden" name='answers[${i}]._correct' />
    			</g:if>
    			<g:else>
    				<g:hiddenField name='answers[${i}].correct' value='${answer.correct}'/>
    			</g:else>
    			<g:textField name='answers[${i}].content' value='${answer.content}' size='60'/>
    			<input type="hidden" name='answers[${i}].deleted' id='answers[${i}].deleted' value='false'/>
    			<span onClick="$('#answers\\[${i}\\]\\.deleted').val('true'); $('#answer${i}').hide()"><img src="${resource(dir:'images/skin', file:'database_delete.png')}" /></span>
			</div>
    	</g:each>
	</div>
	<input type="button" value="Add answer" onclick="addChild();" />
	</ul>
</div>

<g:if test="${!questionInstance?.parentQuestion && !parentQuestionId}">
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
</g:if>
	
<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'qrCode', 'error')} ">
	<label for="qrCode">
		<g:message code="question.qrCode.label" default="二维码Key" />		
	</label>
	<g:textField name="qrCode" value="${questionInstance?.qrCode}"/>
	(range: 1-60000)
</div>

<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'qrCodeVideoURL', 'error')} ">
	<label for="qrCodeVideoURL">
		<g:message code="question.qrCodeVideoURL.label" default="二维码讲解视频ID" />		
	</label>
	<g:textField name="qrCodeVideoURL" value="${questionInstance?.qrCodeVideoURL}" size="100" />
</div>
	
<div class="fieldcontain ${hasErrors(bean: questionInstance, field: 'analysis', 'error')} ">
	<label for="analysis">
		<g:message code="question.analysis.label" default="Analysis" />
		
	</label>
	<g:textArea name="analysis" cols="60" rows="3" maxlength="1000" value="${questionInstance?.analysis}"/>
</div>
