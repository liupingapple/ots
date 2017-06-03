<%@page import="ots.Question"%>
<%@page import="ots.AssignmentStatus"%>
<%@ page import="ots.QuizQuestionRecord" %>
<%@ page import="ots.Quiz" %>
<%@ page import="ots.WeiXinUtil" %>
<%-- <%@ page import="org.grails.plugins.google.visualization.data.Cell; org.grails.plugins.google.visualization.util.DateUtil" %>  --%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="bmain">
		<%-- <gvisualization:apiImport/> --%>
		<g:set var="entityName" value="${message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord')}" />
		<title><g:if test="${WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_KCDB }">课程导报英语测试题</g:if></title>
	</head>
	
	<body>

		<div class="container bs-docs-container">
			<div class="row">
				<div class="col-md-3">
					<p>
					<div class="panel panel-info">
					  <div class="panel-heading">
					    <h3 class="panel-title">
					    	<g:if test="${WeiXinUtil.WeiXinName != WeiXinUtil.WeiXinName_KCDB }">
								${quiz }
							</g:if>
							<g:else>
								<%
									String assiNameShow = quiz?.assignment?.name
									if (assiNameShow.startsWith("sg")) {
										String regex = "sg[\\d]*.[\\d]*";
										java.util.regex.Pattern pat = java.util.regex.Pattern.compile(regex);
										java.util.regex.Matcher matcher = pat.matcher(assiNameShow);
										if (matcher.find()) {
										  assiNameShow = assiNameShow.substring(matcher.end());
										}
									}
								 %>
								${assiNameShow }
							</g:else>
					    </h3>
					  </div>
					  <div class="panel-body">
					    <p>题目总数: <span class="label label-info">${quiz.totalQuestionNum}</span>
						<p>答对题数: <span class="label label-success">${quiz.correctNum}</span>
						<p>答错题数: <span class="label label-danger">${quiz.totalQuestionNum - quiz.correctNum - quiz.notAnsweredNum}</span>
						<p>未答题数: <span class="label label-warning">${quiz.notAnsweredNum}</span>
					  </div>
					</div>
					
					<g:if test="${WeiXinUtil.WeiXinName != WeiXinUtil.WeiXinName_KCDB && !quiz.assignment.name.endsWith('_FOR_WX')}">
						<div class="alert alert-warning" role="alert"><strong>高手云提示</strong>:
						<g:if test="${quiz.assignment.questionType?.contains('阅读') }">
							<p/>${quiz.student.fullName}童鞋，经过艰苦奋战，<br>亲的阅读水平达到: <strong>${quiz.student.masterLevel}</strong> <br>当前最薄弱知识点为：<em>${aStatus?.toBeFocusedKnowledge?.name}</em>。 加油吧亲!
						</g:if>
						<g:else>
							<p/>${quiz.student.fullName}童鞋，您的作业: <strong>${quiz.assignment}</strong> 通过训练已经完成了${aStatus?.coverageRate}%，已经掌握${aStatus?.masterRate}, 诊断出来的当前最薄弱知识点为：<em>${aStatus?.toBeFocusedKnowledge?.name}</em>。					
						</g:else> <!-- 还未处理 quiz?.latestCoverage -->
						</div>
					</g:if>
					
					<%--<div class="panel panel-success">
					  <div class="panel-heading">
					    <h3 class="panel-title">高手云提示</h3>
					  </div>
					  <div class="panel-body">
					  	
					    <p>题目总数: <span class="label label-info">${quiz.totalQuestionNum}</span>
						<p>答对题数: <span class="label label-success">${quiz.correctNum}</span>
						<p>答错题数: <span class="label label-danger">${quiz.totalQuestionNum - quiz.correctNum - quiz.notAnsweredNum}</span>
						<p>未答题数: <span class="label label-warning">${quiz.notAnsweredNum}</span>
					  </div>
					</div>
											
					--%><g:form name="questionReportForm">
					<ul class="nav">
						<li>
							<div class="panel-body">
								<div class="btn-group btn-group-md">									
									<g:if test="${quiz.assignment.name.endsWith('_FOR_WX')}">
										<!-- 关闭当前窗口，返回到微信公众平台 , window.close()无响应, 用微信左上角X关闭即可-->
										<%--<a class="btn btn-primary"  onClick="window.close();">返回</a> --%>
										<g:link class="btn btn-primary" controller="weiXin" action="practice" params="${[openId:openId, kpList:kpList, sourceq:sourceq] }">继续练习</g:link>
									</g:if>
									<g:elseif test="${session.user instanceof ots.Student }">
										<g:link class="btn btn-primary" controller="quiz" action="continuePractice" params="${[stuId: session.user.id, aStatusId: AssignmentStatus.findByStudentAndAssignment(ots.Quiz.get(quiz_id).student, ots.Quiz.get(quiz_id).assignment)?.id] }">继续</g:link>											
										<g:hiddenField name="quiz_id" value="${quiz_id}" />
										<g:hiddenField name="quiz_action" value="${quiz_action}" />
										<g:actionSubmit class="btn btn-primary" action="backToList" value="${quiz_action}"/>
									</g:elseif>
									<g:else>		
										<a class="btn btn-primary" HREF="#" onClick="history.back();return false;">返回</a>
									</g:else>
						  		</div>
							</div>
						</li>
					</ul>
					</g:form>
				</div>
	
				<div class="col-md-9" role="main">
						<p>
						
						<g:if test="${flash.message}">
							<div class="message" role="status">
								${flash.message}
							</div>
						</g:if>
						
						<g:each in="${qrMap}" var="qrEntry">
							<!-- parent question 如果是阅读理解题，则为阅读理解题目题干 ，如果非阅读理解题 qrEntry?.key=0-->
							<g:if test="${qrEntry?.key }">
								<g:set var="parentQ" value="${Question.get(qrEntry.key)}"></g:set>
								<table class="table table-bordered">
									<tbody>
										<tr class="success">
											<td><g:cvtq question="${parentQ}" />&nbsp;&nbsp;备注：该篇文章难度系数: ${parentQ.difficultyLevel }</td>
										</tr>
									</tbody>
								</table>
							</g:if>
							<!-- 子题目，如果非阅读理解题，题目本身 -->
							<table class="table table-hover">
								<tbody>
									<g:each in="${qrEntry.value}" status="i" var="quizQuestionRecordInstance">
										<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
											<td>
												${i+1}
											</td>
											<td><g:cvtq quizQuestionRecord="${quizQuestionRecordInstance}" showChecks='true' showKP='true'/></td>

										</tr>
									</g:each>
								</tbody>
							</table>
						</g:each>
																	
					</div>
				</div>
			</div>
	</body>
</html>
