<%@ page import="ots.KnowledgeCheckPoint" %>



<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'knowledge', 'error')} required">
	<label for="knowledge">
		<g:message code="knowledgeCheckPoint.knowledge.label" default="Knowledge" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="knowledge" name="knowledge.id" from="${ots.KnowledgePoint.list()}" optionKey="id" required="" value="${knowledgeCheckPointInstance?.knowledge?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'quiz', 'error')} required">
	<label for="quiz">
		<g:message code="knowledgeCheckPoint.quiz.label" default="Quiz" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="quiz" name="quiz.id" from="${ots.Quiz.list()}" optionKey="id" required="" value="${knowledgeCheckPointInstance?.quiz?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'totalQuestions', 'error')} required">
	<label for="totalQuestions">
		<g:message code="knowledgeCheckPoint.totalQuestions.label" default="Total Questions" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="totalQuestions" type="number" value="${knowledgeCheckPointInstance.totalQuestions}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'correctQuestions', 'error')} required">
	<label for="correctQuestions">
		<g:message code="knowledgeCheckPoint.correctQuestions.label" default="Correct Questions" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="correctQuestions" type="number" value="${knowledgeCheckPointInstance.correctQuestions}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'recentCorrectRate', 'error')} ">
	<label for="recentCorrectRate">
		<g:message code="knowledgeCheckPoint.recentCorrectRate.label" default="Recent Correct Rate" />
		
	</label>
	<g:field name="recentCorrectRate" type="number" value="${fieldValue(bean: knowledgeCheckPointInstance, field: 'recentCorrectRate')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'recentTotal', 'error')} required">
	<label for="recentTotal">
		<g:message code="knowledgeCheckPoint.recentTotal.label" default="Recent Total" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="recentTotal" type="number" value="${knowledgeCheckPointInstance.recentTotal}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'recentCorrect', 'error')} ">
	<label for="recentCorrect">
		<g:message code="knowledgeCheckPoint.recentCorrect.label" default="Recent Correct" />
		
	</label>
	<g:field name="recentCorrect" type="number" value="${fieldValue(bean: knowledgeCheckPointInstance, field: 'recentCorrect')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'totalTimeTaken', 'error')} required">
	<label for="totalTimeTaken">
		<g:message code="knowledgeCheckPoint.totalTimeTaken.label" default="Total Time Taken" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="totalTimeTaken" type="number" value="${knowledgeCheckPointInstance.totalTimeTaken}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'familyTotal', 'error')} required">
	<label for="familyTotal">
		<g:message code="knowledgeCheckPoint.familyTotal.label" default="Family Total" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="familyTotal" type="number" value="${knowledgeCheckPointInstance.familyTotal}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'familyCorrect', 'error')} required">
	<label for="familyCorrect">
		<g:message code="knowledgeCheckPoint.familyCorrect.label" default="Family Correct" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="familyCorrect" type="number" value="${knowledgeCheckPointInstance.familyCorrect}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'familyRecentTotal', 'error')} required">
	<label for="familyRecentTotal">
		<g:message code="knowledgeCheckPoint.familyRecentTotal.label" default="Family Recent Total" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="familyRecentTotal" type="number" value="${knowledgeCheckPointInstance.familyRecentTotal}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'familyRecentCorrect', 'error')} required">
	<label for="familyRecentCorrect">
		<g:message code="knowledgeCheckPoint.familyRecentCorrect.label" default="Family Recent Correct" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="familyRecentCorrect" type="number" value="${knowledgeCheckPointInstance.familyRecentCorrect}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'familyRecentCorrectRate', 'error')} ">
	<label for="familyRecentCorrectRate">
		<g:message code="knowledgeCheckPoint.familyRecentCorrectRate.label" default="Family Recent Correct Rate" />
		
	</label>
	<g:field name="familyRecentCorrectRate" type="number" value="${fieldValue(bean: knowledgeCheckPointInstance, field: 'familyRecentCorrectRate')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'dateCreated', 'error')} required">
	<label for="dateCreated">
		<g:message code="knowledgeCheckPoint."dateCreated".label" default="Check Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name=""dateCreated"" precision="day"  value="${knowledgeCheckPointInstance?."dateCreated"}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgeCheckPointInstance, field: 'student', 'error')} required">
	<label for="student">
		<g:message code="knowledgeCheckPoint.student.label" default="Student" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="student" name="student.id" from="${ots.Student.list()}" optionKey="id" required="" value="${knowledgeCheckPointInstance?.student?.id}" class="many-to-one"/>
</div>

