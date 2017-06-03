
<%@page import="ots.AdminUser"%>
<%@ page import="ots.Student"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>

	<div class="container bs-docs-container" role="main">

		<div class="col-md-3">
			&nbsp;&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<div class="panel panel-warning">
					<div class="panel-body">
						<g:if test="${ session.user instanceof ots.AdminUser}">
							<ul class="nav">
								<li><g:link class="btn btn-default" action="create">
										<g:message code="default.new.label" args="[entityName]" />
									</g:link>&nbsp;</li>
							</ul>
							
							<ul class="nav">
								<li><g:link class="btn btn-default" action="list">
										<g:message code="default.list.label" args="[entityName]" />
									</g:link></li>
							</ul>
						</g:if>
						
						
							<ul class="nav">
								<li>
									<img src="http://img1.3lian.com/gif/more/11/2012/03/c76278a5cec275712f1af7d6d565a64e.gif" alt="学生靓照" class="img-rounded">
								</li>
							</ul>
							
					</div>
					
					<div class="panel-footer">			
						<g:form>
							<fieldset class="buttons">
								<g:hiddenField name="id" value="${studentInstance?.id}" />
								<g:link class="btn btn-warning" action="edit" id="${studentInstance?.id}">
									<g:message code="default.button.edit.label" default="Edit" />
								</g:link>
								<g:if test="${session?.user instanceof ots.AdminUser}">
									<g:actionSubmit class="btn btn-danger" action="delete"
										value="${message(code: 'default.button.delete.label', default: 'Delete')}"
										onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
								</g:if>
								<g:if test="${session?.user instanceof ots.Student }">
									<g:link class="btn btn-primary" controller="assignmentStatus" action="list">返回</g:link>
								</g:if>
								<g:elseif test="${session?.user instanceof ots.Teacher }">
									<g:link class="btn btn-primary" controller="teacher" action="thome">返回</g:link>
								</g:elseif>
							</fieldset>
						</g:form>
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
					<g:if test="${studentInstance?.userName}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.userName.label" default="登录名" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="userName" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.password && session.user instanceof ots.AdminUser}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.password.label" default="登录密码" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="password" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.fullName}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.fullName.label" default="全名" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="fullName" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.email}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.email.label" default="Email" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="email" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.teacher}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.teacher.label" default="教师" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="teacher" /></dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.telephone1}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.telephone1.label" default="联系电话" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="telephone1" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${session.user instanceof AdminUser && studentInstance?.telephone2}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.telephone2.label" default="Telephone2" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="telephone2" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.parentLink}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.parentLink.label" default="Telephone2" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="parentLink" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.lastAccessDate && session.user instanceof ots.AdminUser}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.lastAccessDate.label" default="Last Access Date" />
							</dt>

							<dd>
								<g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT"
									date="${studentInstance?.lastAccessDate}" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${studentInstance?.expiredDate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.expiredDate.label" default="账户有效期至" />
							</dt>

							<dd>
								<g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT"
									date="${studentInstance?.expiredDate}" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.dateCreated}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.dateCreated.label" default="注册日期" />
							</dt>

							<dd>
								<g:formatDate type="date" timeZone="Asia/Shanghai" style="SHORT"
									date="${studentInstance?.dateCreated}" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${studentInstance?.lastAccessDate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.lastAccessDate.label" default="上次访问系统日期" />
							</dt>

							<dd>
								<g:formatDate type="date" timeZone="Asia/Shanghai" style="SHORT"
									date="${studentInstance?.lastAccessDate}" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${studentInstance?.loginDate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.loginDate.label" default="此次登录时间" />
							</dt>

							<dd>
								<g:formatDate type="time" timeZone="Asia/Shanghai" date="${studentInstance?.loginDate}" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.active && session.user instanceof ots.AdminUser}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.active.label" default="激活状态" />
							</dt>

							<dd>
								<g:formatBoolean boolean="${studentInstance?.active}" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${session.user instanceof AdminUser && studentInstance?.briefComment}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.briefComment.label" default="briefComment" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="briefComment" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.term}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.term.label" default="Term" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="term" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.sex}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.sex.label" default="性别" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="sex" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.birthDate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.birthDate.label" default="出生日期" />
							</dt>

							<dd>
								<g:formatDate type="date" timeZone="Asia/Shanghai" style="SHORT"
									date="${studentInstance?.birthDate}" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.identificationNum}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.identificationNum.label" default="身份证号" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="identificationNum" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.homeProvince}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.homeProvince.label" default="Home Province" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="homeProvince" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.homeCity}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.homeCity.label" default="Home City" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="homeCity" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.homeAddr}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.homeAddr.label" default="Home Addr" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="homeAddr" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.school}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.school.label" default="所在学校" />
							</dt>

							<dd>
								<g:link controller="school" action="show" id="${studentInstance?.school?.id}">
									${studentInstance?.school?.name}
								</g:link>
							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.level && session.user instanceof ots.AdminUser}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.level.label" default="Level" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="level" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${studentInstance?.masterLevel}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.masterLevel.label" default="掌握水平(阅读理解)" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="masterLevel" />

							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.numberOfQuestionsPerPage}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.numberOfQuestionsPerPage.label" default="每页练习题量" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="numberOfQuestionsPerPage" />

							</dd>
						</dl>
					</g:if>

					<g:if test="${studentInstance?.targetCorrectRate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.targetCorrectRate.label" default="目标成绩" />
							</dt>

							<dd>
								${(studentInstance.targetCorrectRate*100).intValue()}

							</dd>
						</dl>

					</g:if>
					<g:if test="${studentInstance?.minAssociatedQuestionsToEvaluate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.minAssociatedQuestionsToEvaluate.label" default="最少评估关联题量" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="minAssociatedQuestionsToEvaluate" />

							</dd>
						</dl>

					</g:if>
					<g:if test="${studentInstance?.maxAssociatedQuestionsToEvaluate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.maxAssociatedQuestionsToEvaluate.label" default="最多评估关联题量" />
							</dt>

							<dd><g:fieldValue bean="${studentInstance}" field="maxAssociatedQuestionsToEvaluate" />

							</dd>
						</dl>

					</g:if>
					<g:if test="${studentInstance?.minTotalQuestionsToEvaluate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.minTotalQuestionsToEvaluate.label" default="最少评估家族题量" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="minTotalQuestionsToEvaluate" />

							</dd>
						</dl>

					</g:if>
					<g:if test="${studentInstance?.maxTotalQuestionsToEvaluate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.maxTotalQuestionsToEvaluate.label" default="最多评估家族题量" />
							</dt>

							<dd>
								<g:fieldValue bean="${studentInstance}" field="maxTotalQuestionsToEvaluate" />

							</dd>
						</dl>

					</g:if>
					<g:if test="${studentInstance?.evaluationPower}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.evaluationPower.label" default="历史成绩权重" />
							</dt>

							<dd>
								${(studentInstance.evaluationPower*100).intValue()}

							</dd>
						</dl>
					</g:if>
					
					<g:if test="${studentInstance?.maxKPToShow}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="student.maxKPToShow.label" default="薄弱点显示个数" />
							</dt>

							<dd>
								${studentInstance.maxKPToShow}

							</dd>
						</dl>
					</g:if>

					<dl class="dl-horizontal">
						<dt>
							<label for="toShowKnowledgeGraph"> <g:message code="student.toShowKnowledgeGraph.label" default="不限出题范围" />
				
							</label>
						</dt>
						<dd>
							<g:checkBox name="toShowKnowledgeGraph" value="${studentInstance?.toShowKnowledgeGraph}" disabled="true" />
						</dd>
					</dl>
						
				</div>

			</div>

		</div>

	</div>
		
</body>
</html>
