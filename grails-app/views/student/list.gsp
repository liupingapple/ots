
<%@ page import="ots.Student" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-student" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<g:if test="${session.user instanceof ots.AdminUser && session.user.isAdmin()}">
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
					  
					<%--
					  <li><g:link class="create" action="export"><g:message code="迁移数据" args="[entityName]" /></g:link></li>
					  <li><g:link class="create" action="importx"><g:message code="导入学生数据" args="[entityName]" /></g:link></li>
					--%>
					
					<div class="search-bar">
						<g:form>
							Filter by:
							<g:select name="searchCategory"
								from="${['姓名', '教师', '联系电话', '家长联系信息', '账户有效期<', '账户有效期=', '账户有效期>', '注册日期<', '注册日期=', '注册日期>', '年级', '性别', '出生日期<', '出生日期 (mm/dd/yy)=', '出生日期>']}"
								keys="${['fullName', 'teacher', 'telephone1', 'parentLink', 'loginDatelt', 'loginDate', 'loginDategt', 'dateCreatedlt', 'dateCreated', 'dateCreatedgt', 'term', 'sex', 'birthDatelt', 'birthDate', 'birthDategt']}"
								noSelection="['': '']"
								value="${session.student_searchCategory}"/>
							<input type="search" name="searchable" value="${session.student_searchable}"/>
							<g:actionSubmit action="list" value="Search"/>
						</g:form>
					</div>
				</g:if>				
			</ul>
		</div>
		<div id="list-student" class="content scaffold-list" role="main">
		    <g:if test="${session.user instanceof ots.AdminUser && session.user.isAdmin()}">
			  <h1><g:message code="default.list.label" args="[entityName]" /></h1>
			</g:if>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="fullName" title="姓名" />
											
						<g:sortableColumn property="teacher" title="教师" />
					
						<g:sortableColumn property="telephone1" title="联系电话" />
					
						<g:sortableColumn property="parentLink" title="家长联系信息" />
					
						<g:sortableColumn property="loginDate" title="账户有效期" />
					
						<g:sortableColumn property="dateCreated" title="注册日期" />
					
						<g:sortableColumn property="term" title="年级" />
					
						<g:sortableColumn property="sex" title="性别" />
					
						<g:sortableColumn property="birthDate" title="出生日期" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${studentInstanceList}" status="i" var="studentInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${studentInstance.id}">${fieldValue(bean: studentInstance, field: "fullName")}</g:link></td>
						
						<td>${fieldValue(bean: studentInstance, field: "teacher")}</td>
					
						<td>${fieldValue(bean: studentInstance, field: "telephone1")}</td>
					
						<td>${fieldValue(bean: studentInstance, field: "parentLink")}</td>

						<td><g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd" date="${studentInstance?.loginDate}"/></td>
					
						<td><g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd" date="${studentInstance?.dateCreated}"/></td>
					
						<td>${fieldValue(bean: studentInstance, field: "term")}</td>

						<td>${fieldValue(bean: studentInstance, field: "sex")}</td>

						<td><g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd" date="${studentInstance?.birthDate}"/></td>
						
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${studentInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
