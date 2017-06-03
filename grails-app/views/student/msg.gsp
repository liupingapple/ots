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
	<g:form action="msgSave" id="${studentInstance.id }">
		<div class="container bs-docs-container" role="main">

			<div class="col-md-3">
				&nbsp;
				<div class="bs-sidebar hidden-print" role="complementary">
					<div class="panel panel-default">
						<div class="panel-heading">给老师留言</div>
						<div class="panel-body text-center">
							<g:submitButton class="btn btn-primary"
								name="${message(code: 'default.button.save.label', default: 'Save')}"
								onclick="return confirm('${message(code: 'default.button.save.confirm.message', default: 'Are you sure?')}');" />
							<g:if test="${chakanFlag != 'true' }">
								<g:link class="btn btn-default" controller="assignmentStatus" action="list">返回</g:link>
							</g:if>
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
					<div class="panel-body">
						<div class="input-group input-group-sm">
							<g:set var="parsedMsg" value="${ots.Util.parseLeaveMsg(studentInstance)}"></g:set>
							<h5>老师给我的留言: <small>${parsedMsg[0] }</small></h4>
							<g:textArea class="form-control" readonly="true" name="teacherToStuMsg" rows="2" cols="150"
								value="${parsedMsg[1]}"></g:textArea>
							<hr/>
							<h5>我给老师的留言: <small>${parsedMsg[2] }</small></h4>
							<g:textArea class="form-control" name="stuToTeacherMsg" rows="2" cols="150" value="${parsedMsg[3]}"></g:textArea>
						</div>
					</div>
				</div>
			</div>
		</div>
	</g:form>
</body>

</html>