<%@ page import="ots.Student"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<a href="#list-student" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
			default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
			<ul>
				<g:if test="${session.user instanceof ots.AdminUser && session.user.isAdmin()}">
				  <li><g:link class="list" action="list"><g:message code="返回" args="[entityName]" /></g:link></li>
				</g:if>
			</ul>
		</div>

	<div id="list-student" class="content scaffold-list" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<br>
		<g:uploadForm action="upload">  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文件:<input type="file" name="myFile" id="myFile" />
			<g:submitButton class="save" name="submit" value="开始上传" />
			<br><br>
			<h3>&nbsp;&nbsp;&nbsp;需要导入的学生列表</h3>
			<g:each in="${stuImpList }" var="stu" status="i">
				&nbsp;&nbsp;&nbsp;&nbsp;${i+1 }&nbsp;-${stu }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</g:each>
		</g:uploadForm>
		<br>------> 请核对导入学生列表，如果正确无误，请点击下面按钮进行导入<br>
		<g:form>
			<fieldset class="buttons">
				<g:actionSubmit class="save" action="importdata" value="开始导入学生数据" />				
			</fieldset>
		</g:form>
	</div>
</body>
</html>
