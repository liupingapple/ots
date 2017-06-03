<%@ page import="ots.ChapterPiece" %>

<div class="fieldcontain ${hasErrors(bean: chapterPieceInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="chapterPiece.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${chapterPieceInstance?.name}" size="40"/>
</div>

<div class="fieldcontain ${hasErrors(bean: chapterPieceInstance, field: 'content', 'error')} ">
	<label for="content">
		<g:message code="chapterPiece.content.label" default="Content" />
		
	</label>
	<g:textArea name="content" value="${chapterPieceInstance?.content}"/>
	(支持<a href="http://www.w3school.com.cn/html/html_formatting.asp" target="_blank">HTML标签</a>)
</div>

<div class="fieldcontain ${hasErrors(bean: chapterPieceInstance, field: 'chapter', 'error')} required">
	<label for="chapter">
		<g:message code="chapterPiece.chapter.label" default="Chapter" />
		<span class="required-indicator">*</span>
	</label>	
	<g:hiddenField name="chapter.id" value="${chapterPieceInstance?.chapter?.id}"/>
	<!-- we pass chapter.id from views/chapter/form.gsp, not use below selector in case value changed abnormally -->
	<g:select id="chapter" name="chapter.id" disabled="true" from="${ots.Chapter.list()}" optionKey="id" required="" value="${chapterPieceInstance?.chapter?.id}" class="many-to-one"/>
</div>

<g:if test="${chapterPieceInstance}">
	<g:link controller="chapter" action="show" id="${chapterPieceInstance.chapter.id }"> 回到该章节 </g:link>
</g:if>
