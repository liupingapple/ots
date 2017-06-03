<%@ page import="ots.Answer" %>



<div class="fieldcontain ${hasErrors(bean: answerInstance, field: 'serialNum', 'error')} ">
	<label for="serialNum">
		<g:message code="answer.serialNum.label" default="Serial Num" />
		
	</label>
	<g:select name="serialNum" from="${answerInstance.constraints.serialNum.inList}" value="${answerInstance?.serialNum}" valueMessagePrefix="answer.serialNum" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: answerInstance, field: 'content', 'error')} required">
	<label for="content">
		<g:message code="answer.content.label" default="Content" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="content" required="" value="${answerInstance?.content}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: answerInstance, field: 'correct', 'error')} ">
	<label for="correct">
		<g:message code="answer.correct.label" default="Correct" />
		
	</label>
	<g:checkBox name="correct" value="${answerInstance?.correct}" />
</div>

<div class="fieldcontain ${hasErrors(bean: answerInstance, field: 'question', 'error')} required">
	<label for="question">
		<g:message code="answer.question.label" default="Question" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="question" name="question.id" from="${ots.Question.list()}" optionKey="id" required="" value="${answerInstance?.question?.id}" class="many-to-one"/>
</div>

