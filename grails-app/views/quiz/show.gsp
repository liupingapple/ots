<%@page import="java.lang.invoke.ConstantCallSite"%>
<%@page import="ots.CONSTANT"%>
<%@page import="ots.Quiz"%>

<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'quiz.label', default: 'Quiz')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
<zing:include />
</head>

<body>    
	<div id="show-quiz" class="container bs-docs-container" role="main">
		<div class="col-md-3">
			<g:form name="QRForm" controller="QuizQuestionRecord" action="answering">
				&nbsp;&nbsp;
				<div class="bs-sidebar hidden-print" role="complementary">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">
								<g:fieldValue bean="${quizInstance}" field="name" />
							</h3>
						</div>
						<div class="panel-body">
							<table>
								<tr>
									<td>所属作业：</td>
									<td><g:fieldValue bean="${quizInstance}" field="assignment" /></td>
								</tr>
								<tr>
									<td>练习状态：</td>
									<td><g:fieldValue bean="${quizInstance}" field="status" /></td>
								</tr>
								<g:if test="${quizInstance.status == CONSTANT.QUIZ_STATUS_SUBMITTED}">
									<tr>
										<td>答题时间：</td>
										<td><g:fieldValue bean="${quizInstance}" field="timeTaken" />秒</td>
									</tr>
									<tr>
										<td>完成日期：</td>
										<td><g:formatDate timeZone="Asia/Shanghai" format="MM-dd HH:mm"
												date="${quizInstance?.answeredDate}" /></td>
									</tr>
									<tr>
										<td>练习题数：</td>
										<td><g:fieldValue bean="${quizInstance}" field="totalQuestionNum" /></td>
									</tr>
									<tr>
										<td>答对题数：</td>
										<td><g:fieldValue bean="${quizInstance}" field="correctNum" /></td>
									</tr>
									<tr>
										<td>答错题数：</td>
										<td>
											${quizInstance.totalQuestionNum - quizInstance.correctNum}
										</td>
									</tr>
									<tr>
										<td>练习成绩：</td>
										<td><g:fieldValue bean="${quizInstance}" field="score" /></td>
									</tr>
								</g:if>
							</table>
							<hr />
							<h5>需重点练习:</h5>
							<h6>
								<font color="red"> ${quizInstance?.toBeFocusedKnowledge?.name}
								</font>
							</h6>
						</div>
					</div>

					<ul class="nav">
						<li>
							<div class="panel panel-warning">
								<div class="panel-body">
									<div class="btn-group btn-group-md">
										<g:if test="${session.user instanceof ots.Student }">
											<g:if test="${quizInstance?.status == CONSTANT.QUIZ_STATUS_NOTBEGIN}">
												<button class="btn btn-primary" type="button" onClick="QRForm.submit();">开始答题</button>
											</g:if>
											<g:if test="${quizInstance?.status == CONSTANT.QUIZ_STATUS_INPROGRESS}">
												<button class="btn btn-primary" type="button" onClick="QRForm.submit();">继续答题</button>
											</g:if>
											<g:if test="${quizInstance?.status == CONSTANT.QUIZ_STATUS_SUBMITTED}">
												<%--<button class="btn btn-primary" type="button" onClick="QRForm.submit();">再做一遍</button>
											--%>
												<g:link class="btn btn-primary" controller="quizQuestionRecord" action="answered"
													params="['quiz_id':"${quizInstance.id}"]">答题记录</g:link>
											</g:if>

											<g:link class="btn btn-primary" action="list" params="${params +['stuId':quizInstance.student.id]}">返回</g:link>
										</g:if>
										<g:else>
											<g:link class="btn btn-primary" controller="quizQuestionRecord" action="answered"
												params="${params + ['quiz_id':"${quizInstance.id}"]}">查看结果</g:link>
											<%--<g:link class="btn btn-primary" controller="student" action="studyStatus" id="${quizInstance.student.id }" params="${params }">返回</g:link>
										--%>
											<a class="btn btn-primary" HREF="#" onClick="history.back();return false;">返回</a>
										</g:else>
									</div>
								</div>

							</div>
						</li>
					</ul>
				</div>

				<g:hiddenField name="id" value="${quizInstance?.id}" />
				<g:hiddenField name="quiz_id" value="${quizInstance?.id}" />
			</g:form>
		</div>

		<div class="col-md-9" role="main">
			&nbsp;&nbsp;
			<ul class="nav nav-tabs">
				<li class="active"><a href="#current_kcp" data-toggle="tab">本次练习成绩</a></li>
				<li><a href="#overall_kcp" data-toggle="tab"
					onClick="$( function(){  if(!loading1_done){$('#overall_kcp_form1').submit();}  });">历次练习成绩</a></li>
				<li><a href="#kcp_zwd" data-toggle="tab"
					onClick="$( function(){  if(!loading2_done){$('#kcp_zwd_form1').submit();}  });">作业知识点掌握度</a></li>
				<li><a href="#kcp_qs" data-toggle="tab" 
					onClick="$( function(){  if(!loading3_done){$('#kcp_qs_form1').submit();}  });">作业知识点掌握趋势</a></li>
				<!-- <li><a href="#kcp_org" data-toggle="tab">作业知识点结构</a></li>  -->
			</ul>

			<script type="text/javascript">
				// support loading once
				var loading1_done = false;
				var loading2_done = false;
				var loading3_done = false;
				function hiddenLoading1() {
					$('#loading1').hide();
					loading1_done = true;
				}

				function hiddenLoading2() {
					$('#loading2').hide();
					loading2_done = true;
				}

				function hiddenLoading3() {
					$('#loading3').hide();
					loading3_done = true;
				}
			</script>

			<g:formRemote name="overall_kcp_form1" url="[action:'show_overall_kcp', id:"${quizInstance?.id}"]" update="all_quiz_score_table" onFailure="alert('failed to show overall kcp')"
				onLoaded="hiddenLoading1()">
				<g:hiddenField name="max_kcp_rate" value="95"/>
				<!-- submit by tab-pane click -->
			</g:formRemote>

			<g:formRemote name="kcp_zwd_form1" url="[action:'show_kcp_zwd_col', id:"${quizInstance?.id}"]" update="kcp_zwd_col" onFailure="alert('failed to show chart')"
				onLoaded="hiddenLoading2()">
				<g:hiddenField name="max_kcp_rate" value="95"/>
				<g:hiddenField name="quiz_number" value="5"/>
				<!-- submit by tab-pane click -->
			</g:formRemote>
			
			<div class="tab-content">
				<!-- 这次练习各知识点成绩 -->
				<div id="current_kcp" class="tab-pane fade in active">
					<g:if test="${currentKCPList && quizInstance?.status == CONSTANT.QUIZ_STATUS_SUBMITTED}">
						<g:set var="KPCList4Table" value="${currentKCPList }"></g:set>
						<g:render template="quiz_score_table"></g:render>
					</g:if>
					<g:else>
						<h3>
							<span class="label label-warning">还没有这次练习知识点成绩信息, 这次练习你提交了吗？ </span>
						</h3>
					</g:else>					
				</div>

				<!-- 历次练习知识点成绩 -->
				<div id="overall_kcp" class="tab-pane fade">
					<div id="loading1"><br>Loading ...</div>
					<br/>
					<g:formRemote name="overall_kcp_form2" url="[action:'show_overall_kcp', id:"${quizInstance?.id}"]" update="all_quiz_score_table" value="查看图表" onFailure="alert('failed to show chart')">
						知识点正确率小于等于：<g:select name="max_kcp_rate" from="${['60', '70', '80', '85', '90', '95', '100']}" value="${'95'}"  onChange="jQuery('#overall_kcp_form2').submit();" />
					</g:formRemote>

					<div id="all_quiz_score_table"></div>
				</div>

				<div id="kcp_zwd" class="tab-pane fade">
					<div id="loading2"><br>Loading ...</div>
					<br/>
					<g:formRemote name="kcp_zwd_form2" url="[action:'show_kcp_zwd_col', id:"${quizInstance?.id}"]" update="kcp_zwd_col" value="查看图表" onFailure="alert('failed to show chart')">
						知识点正确率小于等于：<g:select name="max_kcp_rate" from="${['60', '70', '80', '85', '90', '95', '100']}" value="${'95'}"  onChange="jQuery('#kcp_zwd_form2').submit();" />
						&nbsp;&nbsp; 显示练习数目：<g:select name="quiz_number" from="${['2', '3', '4', '5', '6', '7', '8', '9', '10', '15', '20', '50', '100']}" value="${'5'}"  onChange="jQuery('#kcp_zwd_form2').submit();" />
					</g:formRemote>

					<div id="kcp_zwd_col"></div>
				</div>

				<div id="kcp_qs" class="tab-pane fade">
					<div id="loading3"><br>Loading ...</div>
					<br />
					<g:formRemote name="kcp_qs_form1" url="[action:'show_kcp_qs_col', id:"${quizInstance?.id}"]" update="kcp_qs_col" onFailure="alert('failed to show chart')"
						onLoaded="hiddenLoading3()">
						知识点组合：<g:select name="qs_selection" from="${kcpQSMap.keySet()}" value="${kcpQSMap.keySet().asList()[0]}" onChange="jQuery('#kcp_qs_form1').submit();" />
						&nbsp;<g:submitToRemote class="btn btn-sm btn-default" url="[action:'show_kcp_qs_kplist', id:"${quizInstance?.id}"]" update="kcp_qs_modal" value="选择知识点" onFailure="alert('failed to show chart')"></g:submitToRemote>
						&nbsp;&nbsp; 显示练习数目：<g:select name="quiz_number" from="${['3', '4', '5', '6', '7', '8', '9', '10', '15', '20', '50', '100']}" value="${quiz_number==null?5:quiz_number}"  onChange="jQuery('#kcp_qs_form1').submit();" />
					</g:formRemote>
											
					<div id="kcp_qs_modal"></div>
					
					<div id="kcp_qs_col"></div>										
										
				</div>

				<%-- <div id="kcp_org" class="tab-pane fade">
						<g:if test="${currentKCPList && quizInstance?.status == CONSTANT.QUIZ_STATUS_SUBMITTED}">
							<g:if test="${orgData}">
								<gvisualization:orgChart elementId="kcpOrg" allowCollapse="${true}" allowHtml="${true}" columns="${orgColumns}"
									data="${orgData}" />
								<div id="kcpOrg"></div>
							</g:if>
							<g:else>
								<h4><span class="label label-warning">知识点数量太多,无法正常显示</span></h4>
							</g:else>
						</g:if>
						<g:else>
							<h4><span class="label label-warning">请先完成题目</span></h4>
						</g:else>
					</div>  --%>

			</div>
		</div>
	</div>
</body>
</html>
