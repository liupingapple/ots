<%@ page import="ots.AdminUser"%>
<%@ page import="ots.Teacher"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'teacher.label', default: 'Teacher')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
	<div class="container bs-docs-container" role="main">

		<div class="col-md-3">
			&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<div class="panel panel-default">
					<div class="panel-heading">
						<g:message code="default.show.label" args="[entityName]" />
					</div>
					<div class="panel-body">
						<ul class="nav">
							<li><g:if test="${session.user instanceof AdminUser }">
									<g:link class="btn btn-default" action="list">
										<g:message code="default.list.label" args="[entityName]" />
									</g:link>
									&nbsp;
									<g:link class="btn btn-default" action="create">
										<g:message code="default.new.label" args="[entityName]" />
									</g:link>
									&nbsp;
								</g:if> <g:link class="btn btn-default" onclick="history.back();return false;">
										返回
								</g:link></li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class="col-md-9">
			&nbsp;
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>

			<div class="panel panel-default">
				<div class="panel-heading">
					<g:message code="default.show.label" args="[entityName]" />
				</div>
				<div class="panel-body">

					<g:if test="${teacherInstance?.userName  && session.user instanceof ots.Teacher}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.userName.label" default="User Name" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="userName" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${teacherInstance?.password  && session.user instanceof ots.AdminUser}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.password.label" default="Password" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="password" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${teacherInstance?.fullName}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.fullName.label" default="Full Name" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="fullName" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${teacherInstance?.email}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.email.label" default="Email" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="email" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${teacherInstance?.telephone1}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.telephone1.label" default="Telephone1" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="telephone1" />
							</dd>

						</dl>
					</g:if>

					<g:if test="${teacherInstance?.entryTime}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.entryTime.label" default="加盟日期" />
							</dt>
							<dd>
								<g:formatDate type="date" timeZone="Asia/Shanghai" style="SHORT"
									date="${teacherInstance?.entryTime}" />
							</dd>

						</dl>
					</g:if>

					<g:if test="${session.user instanceof AdminUser && teacherInstance?.telephone2}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.telephone2.label" default="Telephone2" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="telephone2" />
							</dd>

						</dl>
					</g:if>

					<g:if test="${teacherInstance?.sex}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.sex.label" default="Sex" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="sex" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${teacherInstance?.degree}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.degree.label" default="Degree" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="degree" />
								</span>
						</dl>
					</g:if>

					<g:if test="${session.user instanceof AdminUser && teacherInstance?.briefComment}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="teacher.briefComment.label" default="Brief Comment" />
							</dt>
							<dd>
								<g:fieldValue bean="${teacherInstance}" field="briefComment" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${session.user instanceof ots.Teacher}">
						<dl class="dl-horizontal">
							<dt>
								<label for="toShowKnowledgeGraph"> <g:message code="teacher.toShowKnowledgeGraph.label" default="Show Knowledge Graph" />
					
								</label>
							</dt>
							<dd>
								<g:checkBox name="toShowKnowledgeGraph" value="${teacherInstance?.toShowKnowledgeGraph}" disabled="true" />
							</dd>
						</dl>
					</g:if>
					
				</div>
				<div class="panel-footer">
					<g:form>
						<g:hiddenField name="id" value="${teacherInstance?.id}" />
						<g:if test="${session.user instanceof ots.Teacher || session.user instanceof ots.AdminUser}">
							<g:link class="btn btn-primary" action="edit" id="${teacherInstance?.id}">
								<g:message code="default.button.edit.label" default="Edit" />
							</g:link>
						</g:if>
						<g:if test="${session.user instanceof AdminUser }">
							<g:actionSubmit class="btn btn-warning" action="delete"
								value="${message(code: 'default.button.delete.label', default: 'Delete')}"
								onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</g:if>
					</g:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
