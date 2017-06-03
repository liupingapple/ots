<%@page import="ots.EndUser"%>
<%@page import="ots.CONSTANT"%>
<%@ page import="ots.Quiz" %>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'assignmentStatus.label', default: '作业')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>

	<div id="show-quiz" class="container bs-docs-container" role="main">

		<div class="col-md-3">
			&nbsp;&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<ul class="nav">
					<li>
						<div class="panel panel-warning">
							<div class="panel-body">
								<div class="btn-group btn-group-md">
									<g:if test="${session.user instanceof ots.Student}">
										<g:link class="btn btn-primary" action="continuePractice" params="${[stuId: session.user.id, aStatusId: assignmentStatus.id] }">
											<g:if test="${quizInstanceList}">
												继续练习
											</g:if>
											<g:else>
												开始练习
											</g:else>
										</g:link>
										<a class="btn btn-primary" href="${createLink(uri: '/assignmentStatus')}">作业列表</a>
									</g:if>
									<g:else>
										<button class="btn btn-primary" type="button" onClick="history.back();return false;">返回</button>
									</g:else>
								</div>
							</div>

						</div>
					</li>
				</ul>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							作业  ${session.assignment} 的练习情况
						</h3>
					</div>
					<div class="panel-body">
						<%--<h5>作业要求:</h5>
						<h6>${assignmentInstance?.comment}</h6>
						<h5>练习范围:</h5>						
						<ul>
						<g:each in="${assignmentInstance?.templates}" var="k">
							<li>
								${k?.templateName?.encodeAsHTML()}
							</li>
						</g:each>
						</ul>
						<hr/>
						--%>
						<g:if test="${quizInstanceTotal > 0}">
							<table>
								<tr><td>练习总数：</td><td>${quizInstanceTotal}</td></tr>
								<tr><td>掌握度：</td><td>${assignmentStatus?.masterRate}</td></tr>
								<%--<tr><td>覆盖率：</td><td>${assignmentStatus?.coverageRate}%</td></tr>
								--%><tr><td><g:if test="${assignmentStatus.assignment.questionType?.contains('阅读')}">题干</g:if><g:else>题目</g:else>总数：</td><td>${assignmentStatus?.assignment?.totalAvailableQuestions}</td></tr>
								<tr><td>完成<g:if test="${assignmentStatus.assignment.questionType?.contains('阅读')}">子</g:if>题目数(次)：</td><td>${assignmentStatus?.finishedQuestions}</td></tr>
								<tr><td>答对<g:if test="${assignmentStatus.assignment.questionType?.contains('阅读')}">子</g:if>题目数(次)：</td><td>${assignmentStatus?.correctQuestions}</td></tr>
								<tr><td>作业定义知识点数：</td><td>${assignmentStatus?.assignment?.totalKnowledgePoints}</td></tr>
								<tr><td>覆盖知识点个数：</td><td>${assignmentStatus?.coveredKnowledgPoints}</td></tr>
							</table>
							<hr/>
							<h5>当前最薄弱知识点：</h5>
							<h6>
								<font color="red">${assignmentStatus?.toBeFocusedKnowledge?.name}</font>
							</h6>
						</g:if>
					</div>
				</div>
				
			</div>
		</div>
		
		<div class="col-md-9">
			<h3>作业${session.assignment }的练习列表</h3>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table table-hover table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<g:sortableColumn property="name" title="${message(code: 'quiz.name.label', default: '名称')}" />
						<g:sortableColumn property="status" title="${message(code: 'quiz.status.label', default: '状态')}" />
						<g:sortableColumn property="score" title="${message(code: 'quiz.score.label', default: '成绩')}" />
						<g:sortableColumn property="timeTaken" title="${message(code: 'quiz.timeTaken.label', default: '耗时')}" />
						<g:sortableColumn property="toBeFocusedKnowledge" title="${message(code: 'quiz.toBeFocusedKnowledge.label', default: '需重点练习')}" />
					    <g:sortableColumn property="answeredDate" title="${message(code: 'quiz.answeredDate.label', default: '完成日期')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${quizInstanceList}" status="i" var="quizInstance">
					<tr>
					
						<td><g:link action="show" id="${quizInstance.id}" params="${[stuId:quizInstance.student.id] }">${fieldValue(bean: quizInstance, field: "name")}</g:link></td>
						<td>${fieldValue(bean: quizInstance, field: "status")}</td>
						<td>${fieldValue(bean: quizInstance, field: "score")}</td>
						<td>${fieldValue(bean: quizInstance, field: "timeTaken")}</td>
						<td>${fieldValue(bean: quizInstance, field: "toBeFocusedKnowledge")}</td>
						<td><g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd HH:mm" date="${quizInstance?.answeredDate}" /></td>
						<!-- we use the normal time format in China, if we want to change back, pls recall type="datetime" and style="SHORT" -->
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate params="${['stuId':params.stuId] }" total="${quizInstanceTotal}" />
			</div>
		</div>
		
	</div>
</body>
</html>