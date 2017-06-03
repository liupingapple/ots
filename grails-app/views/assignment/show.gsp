<%@page import="ots.WeiXinUtil"%>
<%@page import="ots.AdminUser"%>
<%@ page import="ots.Assignment" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="bmain">
	<g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}" />
	<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
	<div id="assignmentList" class="container bs-docs-container" role="main">
		
		<div class="col-md-3">
			&nbsp;&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<div class="panel panel-warning">
					<div class="panel-body">
						<ul class="nav">
							<li>
								<g:link class="btn btn-default" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>&nbsp;
							</li>
						</ul>
						
						<ul class="nav">
							<li>
								<g:link class="btn btn-default" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link>&nbsp;
							</li>
						</ul>
						
						<ul class="nav">
							<li>
								<g:if test="${session.user instanceof ots.Teacher }">
								<g:link class="btn btn-default" action="assignToStu" params="['fromController':'assignment', 'asmtId':"${assignmentInstance.id }", 'teacherId':"${session.user.id}"]">分配给学生</g:link>&nbsp;
							</g:if>
							</li>
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

					<g:if test="${assignmentInstance?.name}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.name.label" default="Name" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="name" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${assignmentInstance?.subject}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.subject.label" default="Subject" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="subject" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${assignmentInstance?.totalAvailableQuestions}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.totalAvailableQuestions.label" default="totalAvailableQuestions" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="totalAvailableQuestions" />
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${assignmentInstance?.totalKnowledgePoints}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.totalKnowledgePoints.label" default="totalKnowledgePoints" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="totalKnowledgePoints" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.comment}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.comment.label" default="Comment" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="comment" />
							</dd>

						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.assignedBy}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.assignedBy.label" default="Assigned By" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="assignedBy" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.dueDate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.dueDate.label" default="Due Date" />
							</dt>
							<dd>
								<g:formatDate date="${assignmentInstance?.dueDate}" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.timeLimit}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.timeLimit.label" default="Time Limit" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="timeLimit" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.numberOfQuestionsPerPage}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.numberOfQuestionsPerPage.label" default="Questions per Page" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="numberOfQuestionsPerPage" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.questionLimit}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.questionLimit.label" default="Question Limit" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="questionLimit" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.difficultyBar}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.difficultyBar.label" default="Difficulty Bar" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="difficultyBar" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.questionType}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.questionType.label" default="Question Type" />
							</dt>
							<dd>
								<g:fieldValue bean="${assignmentInstance}" field="questionType" />
							</dd>
						</dl>
					</g:if>

					<g:if test="${assignmentInstance?.targetCorrectRate}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.targetCorrectRate.label" default="Target Correct Rate" />
							</dt>
							<dd>
								${(assignmentInstance.targetCorrectRate*100).intValue()}
							</dd>
						</dl>
					</g:if>
					
					<g:if test="${assignmentInstance?.qrCode}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.qrCode.label" default="二维码key" />
							</dt>
							<dd>
								${assignmentInstance.qrCode}
							<br>(可通过链接生成二维码:${WeiXinUtil.WEIXIN_SRV_URL}/createQRCode/${assignmentInstance.qrCode})
							</dd>
						</dl>
					</g:if>
					
					<%--<g:if test="${assignmentInstance?.qrCodeNewQuizURL}">
						<dl class="dl-horizontal">
							<dt>
								<g:message code="assignment.qrCodeNewQuizURL.label" default="二维码URL" />
							</dt>
							<dd>
								${assignmentInstance.qrCodeNewQuizURL}
							</dd>
						</dl>
					</g:if>--%>

					<g:if test="${assignmentInstance?.templates}">
						<dl class="dl-horizontal">
							<dt>
								<g:message
										code="assignment.templates.label" default="Assignment Templates" /></dt>
								<g:each in="${assignmentInstance.templates}" var="k">
									<dd><g:link
											controller="assignmentTemplate" action="show" id="${k.id}">
											${k?.encodeAsHTML()}
										</g:link></dd>
								</g:each>
						</dl>
					</g:if>

				</div>
			</div>

			<div class="panel-footer">
				<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${assignmentInstance?.id}" />
						<g:link class="btn btn-primary" action="edit" id="${assignmentInstance?.id}">
							<g:message code="default.button.edit.label" default="Edit" />
						</g:link>
						<g:actionSubmit class="btn btn-warning" action="delete"
							value="${message(code: 'default.button.delete.label', default: 'Delete')}"
							onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</div>

		</div>
	</div>
</body>
</html>
