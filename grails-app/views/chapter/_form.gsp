<%@ page import="ots.Chapter" %>

<div class="fieldcontain ${hasErrors(bean: chapterInstance, field: 'name', 'error')}">
	<label for="name">
		<g:message code="chapter.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${chapterInstance?.name}" size="40"/>
</div>

<div class="fieldcontain ${hasErrors(bean: chapterInstance, field: 'bookName', 'error')} ">
	<label for="bookName">
		<g:message code="chapter.bookName.label" default="Book Name" />
		
	</label>
	<g:textField name="bookName" value="${chapterInstance?.bookName}" size="40"/>
</div>

<div class="fieldcontain ${hasErrors(bean: chapterInstance, field: 'qrCode', 'error')} ">
	<label for="qrCode">
		<g:message code="chapter.qrCode.label" default="Qr Code" />		
	</label>
	<g:textField name="qrCode" value="${chapterInstance?.qrCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: chapterInstance, field: 'assignment', 'error')} ">
	<label for="assignment">
		<g:message code="chapter.assignment.label" default="Assignment" />		
	</label>
	<g:select name="assignment" from="${ots.Assignment.list()}" multiple="multiple" optionKey="id" size="15" value="${chapterInstance?.assignment*.id}" class="many-to-many"/>
	<g:each in="${chapterInstance.assignment}" var="a">
		<li style="text-align: center">${a}</li>
	</g:each>
</div>

<div class="fieldcontain ${hasErrors(bean: chapterInstance, field: 'chapterPieces', 'error')} ">
	<label for="chapterPieces">
		<g:message code="chapter.chapterPieces.label" default="chapterPieces" />		
	</label>
	<g:each in="${chapterInstance?.chapterPieces}" var="piece">
		<g:link controller="chapterPiece" action="edit" id="${piece?.id}">${piece?.name}</g:link>		
	</g:each>
	<p>
	<label>&nbsp;</label>
	<g:link controller="chapterPiece" action="create" params="['chapter.id': chapterInstance?.id]">Add Chapter Piece(新增章节段落)</g:link>
</div>

<div class="fieldcontain ${hasErrors(bean: chapterInstance, field: 'comment', 'error')} ">
	<label for="comment">
		<g:message code="chapter.comment.label" default="Comment" />		
	</label>
	<g:textField name="comment" value="${chapterInstance?.comment}" size="100"/>
</div>
