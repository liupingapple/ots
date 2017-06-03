<%@ page import="ots.Quiz" %>

<div class="fieldcontain ${hasErrors(bean: quizInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="quiz.name.label" default="名称" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" size="50" value="${quizInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: quizInstance, field: 'assignment', 'error')} ">
	<label for="assignment">
		<g:message code="quiz.assignment.label" default="选题范围" />
		<span class="required-indicator">*</span>		
	</label>
	<g:select id="assignment" name="assignment.id" required="" from="${asmtList}" optionKey="id" value="${quizInstance?.assignment?.id}" class="many-to-one"/>
</div>

