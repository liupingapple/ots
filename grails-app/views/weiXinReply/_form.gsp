<%@ page import="ots.WeiXinReply" %>



<div class="fieldcontain ${hasErrors(bean: weiXinReplyInstance, field: 'keyWords', 'error')} required">
	<label for="keyWords">
		<g:message code="weiXinReply.keyWords.label" default="Key Words" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="keyWords" maxlength="100" required="" value="${weiXinReplyInstance?.keyWords}"/>
	(用户关注公众号需要的回复，请输入关键词：subscribe)
</div>

<div class="fieldcontain ${hasErrors(bean: weiXinReplyInstance, field: 'replyContent', 'error')} required">
	<label for="replyContent">
		<g:message code="weiXinReply.replyContent.label" default="Reply Content" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="replyContent" cols="40" rows="5" maxlength="2000" required="" value="${weiXinReplyInstance?.replyContent}"/>
</div>

