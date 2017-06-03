<%@page import="ots.WeiXinUtil"%>
<%@page import="ots.CONSTANT"%>
<%@ page import="ots.Assignment"%>

<script type="text/javascript">
	$(function(){  
		　　$("#questionType").change(function(){  
				if ($(this).val().contains("阅读")) {
		　　　　		$("#numberOfQuestionsPerPage").val(1);
		　　　　		$("#questionLimit").val(1);		　　　　		
				} else {
		　　　　		$("#numberOfQuestionsPerPage").val(10);
		　　　　		$("#questionLimit").val(10);	
				} 
		　　　}) 
		})  
</script>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.templateFilter.label" default="Filter" />
	<dt>
	<dd>
		<g:textField name="templateFilter" value="" onchange="${remoteFunction(controller:'assignment', action:'filterTemplate',update:'assignmentTemplateSelection', params:'\'nameFilter=\' + this.value' )}"/>
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.name.label" default="Name" />
		<span class="required-indicator">*</span>
	<dt>
	<dd>
		<g:textField autofocus="true" name="name" required="" size="50" value="${assignmentInstance?.name}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.questionType.label" default="QuestionType" />
		<span class="required-indicator">*</span>
	<dt>
	<dd>
		<g:select name="questionType" required="" from="${CONSTANT.QUESTION_TYPEs}" 
		  value="${assignmentInstance?.questionType}" valueMessagePrefix="assignment.questionType" noSelection="['': '']"/>
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.comment.label" default="Comment" />
	</dt>
	<dd>
		<g:textArea name="comment" cols="50" rows="3" maxlength="200" value="${assignmentInstance?.comment}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.assignedBy.label" default="Assigned By" />

	</dt>
	<dd>
		<g:textField name="assignedBy" readonly="true"
			value="${assignmentInstance?.assignedBy == null? session.user.userName : assignmentInstance?.assignedBy}" />
	</dd>

</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.dueDate.label" default="Due Date" />

	</dt>
	<dd>
		<g:datePicker name="dueDate" precision="day" value="${assignmentInstance?.dueDate}" default="none"
			noSelection="['': '']" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.timeLimit.label" default="Time Limit" />
		<span class="required-indicator">*</span>
	</dt>
	<dd>
		<g:field name="timeLimit" type="number" value="${assignmentInstance?.timeLimit}" required="" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.numberOfQuestionsPerPage.label" default="Questions per Page" />
		<span class="required-indicator">*</span>
	</dt>
	<dd>
		<g:field name="numberOfQuestionsPerPage" type="number"
			value="${assignmentInstance?.numberOfQuestionsPerPage}" required="" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.questionLimit.label" default="Question Limit" />
		<span class="required-indicator">*</span>
	</dt>
	<dd>
		<g:field name="questionLimit" type="number" value="${assignmentInstance?.questionLimit}" required="" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.difficultyBar.label" default="Difficulty Bar" />
		<span class="required-indicator">*</span>
	</dt>
	<dd>
		<g:field name="difficultyBar" type="number" value="${assignmentInstance?.difficultyBar}" required="" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.targetCorrectRate.label" default="Target Correct Rate" />
		<span class="required-indicator">*</span>
	</dt>
	<dd>
		<g:field name="targetCorrectRate" type="number"
			value="${(assignmentInstance?.targetCorrectRate*100)?.intValue()}" required="" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.qrCode.label" default="二维码key" />
	</dt>
	<dd>
		<g:textField name="qrCode" value="${assignmentInstance?.qrCode}"/>
		(range: 80001-90000)
	</dd>
</dl>

<%--  Don't use qrCodeNewQuizURL, automatically generate the URL for new quiz, see WeiXinController._scanCode4Assignment()
<dl class="dl-horizontal">
	<dt>
		<g:message code="assignment.qrCodeNewQuizURL.label" default="二维码URL" />
	</dt>
	<dd>
		<g:textField name="qrCodeNewQuizURL" value="${assignmentInstance?.qrCodeNewQuizURL}" size="80"/>
	</dd>
</dl>--%>

<g:render template="assignmentTemplate_list"/>