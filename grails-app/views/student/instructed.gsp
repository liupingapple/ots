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
	<g:form action="instructedSave" id="${studentInstance.id }">
		<div class="container bs-docs-container" role="main">

			<g:if test="${chakanFlag != 'true'}">
			<div class="col-md-3">
				&nbsp;
				<div class="bs-sidebar hidden-print" role="complementary">
					<div class="panel panel-default">
						<div class="panel-heading">
							我的辅导记录
						</div>
						<div class="panel-body text-center">
							<g:submitButton class="btn btn-primary" name="${message(code: 'default.button.save.label', default: '保存')}"
							 onclick="return confirm('${message(code: 'default.button.save.confirm.message', default: 'Are you sure?')}');"/>
							<g:link class="btn btn-default" controller="assignmentStatus" action="list">返回</g:link>
						</div>
					</div>
				</div>
			</div>
			</g:if>

			<div class="col-md-9">
				&nbsp;
				<g:if test="${flash.message}">
					<div class="message" role="status">
						${flash.message}
					</div>
				</g:if>

				<div class="panel panel-default">
					<div class="panel-body">
						<label>最近一次辅导评价</label>
						<div class="input-group input-group-sm">
							<span class="input-group-addon">评分</span>
							<g:select name="instructed_evaluating_rate" class="form-control" from="${ratings}" keys="${0..5}" value="${ots.Util.getLastRecord(ots.Student.get(session.user?.id), 5)}"/>
							<span class="input-group-addon">评价详情</span>
							<input name="instructed_evaluating_text" type="text" class="form-control" placeholder="" value="${ots.Util.getLastRecord(ots.Student.get(session.user?.id), 6)}" size="60">
						</div>
						<br/>
						<label>辅导记录</label>
						<g:textArea class="form-control" readonly="true" name="records" rows="18" cols="128">${ots.Util.parseInstructedRecords(studentInstance, true) }</g:textArea>
						<div class="help-block">温馨提示: 您的评分是对老师教学效果的重要反馈,只有校长可以查阅,老师本人无法看到。</div>
					</div>
				</div>
			</div>
		</div>
	</g:form>
</body>

</html>