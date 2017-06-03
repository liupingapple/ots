<%@ page import="ots.Student" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	<g:form method="post" >
		<a href="#list-student" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		
		<fieldset class="buttons">
			<g:actionSubmit class="list" action="list" value="返回"/>
			<g:actionSubmit class="save" action="exportdata" value="迁移学生数据" />
		</fieldset>
		
		<div id="list-student" class="content scaffold-list" role="main">
		    <g:if test="${session.user instanceof ots.AdminUser && session.user.isAdmin()}">
			  <h1><g:message code="default.list.label" args="[entityName]" />&nbsp;&nbsp;&nbsp;<g:checkBox name="init_basic_data" checked="true" />初始化基础数据<p></h1>			  
			</g:if>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>			
			<g:each in="${studentInstanceList}" status="i" var="studentInstance">	
				&nbsp;&nbsp;&nbsp;<g:checkBox name="stu4export" value="${studentInstance.id }" checked="false"/>		
				<g:link action="show" id="${studentInstance.id}">${fieldValue(bean: studentInstance, field: "userName")}</g:link>					
				${fieldValue(bean: studentInstance, field: "level")}&nbsp;&nbsp;&nbsp;		
			</g:each>
			<br><br>
			<div class="pagination">
				<g:paginate total="${studentInstanceTotal}" />
			</div>
		</div>
	</g:form>
	</body>
</html>
