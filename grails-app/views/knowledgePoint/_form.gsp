<%@page import="ots.WeiXinUtil"%>
<%@ page import="ots.KnowledgePoint" %>

<div class="fieldcontain ${hasErrors(bean: knowledgePointInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="knowledgePoint.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField autofocus="true" name="name" required="" value="${knowledgePointInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgePointInstance, field: 'qrCode', 'error')} ">
	<label for="qrCode">
		<g:message code="knowledgePoint.qrCode.label" default="二维码key" /> 
	</label>
	<g:textField autofocus="true" name="qrCode" value="${knowledgePointInstance?.qrCode}"/>
	(range: 60001-80000)
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgePointInstance, field: 'qrCodeVideoURL', 'error')} ">
	<label for="qrCodeVideoURL">
		<g:message code="knowledgePoint.qrCodeVideoURL.label" default="二维码视频URL" />
	</label>
	<g:textField autofocus="true" name="qrCodeVideoURL" value="${knowledgePointInstance?.qrCodeVideoURL}" size="80"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgePointInstance, field: 'content', 'error')} ">
	<label for="content">
		<g:message code="knowledgePoint.content.label" default="Content" />		
	</label>
	<g:textArea name="content" cols="60" rows="3" maxlength="1000" value="${knowledgePointInstance?.content}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: knowledgePointInstance, field: 'degree', 'error')} required">
	<label for="degree">
		<g:message code="knowledgePoint.degree.label" default="Degree" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="degree" from="${0..10}" class="range" required="" value="${fieldValue(bean: knowledgePointInstance, field: 'degree')}"/>
</div>
