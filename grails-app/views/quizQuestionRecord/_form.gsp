<%@ page import="ots.QuizQuestionRecord" %>

<div class="fieldcontain ${hasErrors(bean: quizQuestionRecordInstance, field: 'briefComment', 'error')} ">
	<label for="briefComment">
		<g:message code="quizQuestionRecord.briefComment.label" default="Brief Comment" />
		
	</label>
	<g:textArea name="briefComment" cols="40" rows="5" maxlength="1000" value="${quizQuestionRecordInstance?.briefComment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: quizQuestionRecordInstance, field: 'question', 'error')} required">
	<label for="question">
		<g:message code="quizQuestionRecord.question.label" default="Question" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="question" name="question.id" from="${ots.Question.list()}" optionKey="id" required="" value="${quizQuestionRecordInstance?.question?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: quizQuestionRecordInstance, field: 'quiz', 'error')} required">
	<label for="quiz">
		<g:message code="quizQuestionRecord.quiz.label" default="Quiz" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="quiz" name="quiz.id" from="${ots.Quiz.list()}" optionKey="id" required="" value="${quizQuestionRecordInstance?.quiz?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: quizQuestionRecordInstance, field: 'stuAnswers', 'error')} ">
	<label for="stuAnswers">
		<g:message code="quizQuestionRecord.stuAnswers.label" default="Stu Answers" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${quizQuestionRecordInstance?.stuAnswers?}" var="s">
    <li><g:link controller="stuAnswer" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="stuAnswer" action="create" params="['quizQuestionRecord.id': quizQuestionRecordInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'stuAnswer.label', default: 'StuAnswer')])}</g:link>
</li>
</ul>

</div>

