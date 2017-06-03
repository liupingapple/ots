<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<%-- <meta name="layout" content="main"/>  --%>
<title>Insert title here</title>
</head>
<body>
  <div class="body">
  	<g:if test="flash.message">${flash.message }</g:if><g:else>请输入学生/家长账户名，并关联</g:else>
  	<g:form>
  		<br>Student Account name: <g:textField name="stuName" value="${stuName }"/>
  		<br>Parents Account name: <g:textField name="parentsName" value="${parentsName }"/>
  		<br><g:actionSubmit action="parents_stu_link" value="关联"/>
  	</g:form>
  </div>
</body>
</html>