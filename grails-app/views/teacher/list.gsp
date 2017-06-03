
<%@ page import="ots.Teacher" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'teacher.label', default: 'Teacher')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-teacher" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<div class="search-bar">
					<g:form>
						Filter by:
						<g:select name="searchCategory"
							from="${['登录名', '姓名', '电子邮件', '联系电话', '加盟日期<', '加盟日期=', '加盟日期>']}"
							keys="${['userName', 'fullName', 'email', 'telephone1', 'entryTimelt', 'entryTime', 'entryTimegt']}"
							noSelection="['': '']"
							value="${session.teacher_searchCategory}"/>
						<input type="search" name="searchable" value="${session.teacher_searchable}"/>
						<g:actionSubmit action="list" value="Search"/>
					</g:form>
				</div>
			</ul>
		</div>
		<div id="list-teacher" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="userName" title="${message(code: 'teacher.userName.label', default: 'User Name')}" />
					
						<g:sortableColumn property="fullName" title="${message(code: 'teacher.fullName.label', default: 'Full Name')}" />
					
						<g:sortableColumn property="email" title="${message(code: 'teacher.email.label', default: 'Email')}" />
					
						<g:sortableColumn property="telephone1" title="${message(code: 'teacher.telephone1.label', default: 'Telephone1')}" />
					
						<g:sortableColumn property="sex" title="性别" />

						<g:sortableColumn property="degree" title="学历" />
						
						<g:sortableColumn property="entryTime" title="加盟日期" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${teacherInstanceList}" status="i" var="teacherInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${teacherInstance.id}">${fieldValue(bean: teacherInstance, field: "userName")}</g:link></td>
					
						<td>${fieldValue(bean: teacherInstance, field: "fullName")}</td>
					
						<td>${fieldValue(bean: teacherInstance, field: "email")}</td>
					
						<td>${fieldValue(bean: teacherInstance, field: "telephone1")}</td>
					
						<td>${fieldValue(bean: teacherInstance, field: "sex")}</td>
						
						<td>${fieldValue(bean: teacherInstance, field: "degree")}</td>
						
						<td><g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd" date="${teacherInstance?.entryTime}"/></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${teacherInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
