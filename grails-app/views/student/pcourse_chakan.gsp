<!DOCTYPE html>
<%@page import="ots.CONSTANT"%>
<%@page import="ots.Quiz"%>
<%@page import="ots.Teacher"%>
<html>
<head>
<meta name="layout" content="bmain">
<title>课程</title>
<zing:include />
</head>

<body>
	<div class="container bs-docs-container">
		<div class="row">
		<g:form name="topForm" action="pcourse_chakan" id="${session.user?.id }">
			<g:hiddenField name="pick_item" value="${pick_item }"/>
			<div class="container-nav">
				<span class="item"></span>
				<a href="#"><span id="xxjd"><i></i>学习进度<b></b></span></a>
				<a href="#"><span id="lxzk"><i></i>练习状况<b></b></span></a>
				<a href="#"><span id="zsdzwt"><i></i>知识点掌握图<b></b></span></a>
				<a href="#"><span id="wdzy"><i></i>我的作业<b></b></span></a>
				<a href="#"><span id="ctj"><i></i>错题集<b></b></span></a>
				<a href="#"><span id="brzsd"><i></i>薄弱知识点<b></b></span></a>
				<a href="#"><span id="kcfdqk"><i></i>课程辅导情况<b></b></span></a>
				<span class="item item2"></span>
			</div>
		</g:form>
		</div>
		<p>
		<div class="row">
		
		<!-- 学习进度 与 练习状况 -->
		<g:if test="${pick_item == 'xxjd' || pick_item == 'lxzk'}">		
			<div class="row">
				<div class="col-md-8">
					<g:if test="${pick_item == 'xxjd'}">
						<zing:chart type="line" width="830" height="450" 
    					container="line_chart" data="${lineData}" xLabels="${lineCol}" effect="4"/>
    				</g:if>
    				<g:else>
    					<zing:chart type="bar" width="830" height="450" 
    					container="bar_chart" data="${colData}" xLabels="${lineCol}" effect="4"/>
    				</g:else>
    			</div>
    			<p>
    			<div class="col-md-4">
    				<table class="table table-hover table-condensed table-bordered table-striped">
    					<tr>
    						<td>序号</td>
    						<td>练习名称</td>
    						<td>完成时间</td>
    					</tr>
    					<g:each in="${quizList }" status="i" var="quiz">
    						<tr>
	    						<td>${i+1 }</td>
	    						<td>${quiz.name }</td>
	    						<td>${quiz.answeredDate }</td>
    						</tr>
    					</g:each>
    				</table>
    			</div>
    		</div>
		</g:if>
		
		<!-- 知识点掌握图 -->
		<g:if test="${pick_item == 'zsdzwt' }">
			<g:include action="studyStatus" id="${studentInstance.id }" params="${[tabActive:'wlist'] }"/>
		</g:if>
		
		<!-- 我的作业 -->
		<g:if test="${pick_item == 'wdzy' }">
			<div class="btn-group">		
			  <g:link class="btn btn-primary" action="pcourse_chakan" id="${studentInstance.id }" params="${[pick_item:'wdzy', aStatusStatus:CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN] }"><g:if test="${aStatusStatus == CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN}"><span class="glyphicon glyphicon-pushpin"> </span></g:if>未完成作业</g:link>
			  <g:link class="btn btn-warning" action="pcourse_chakan" id="${studentInstance.id }" params="${[pick_item:'wdzy', aStatusStatus:CONSTANT.ASSIGNMENT_STATUS_INPROGRESS] }"><g:if test="${aStatusStatus == CONSTANT.ASSIGNMENT_STATUS_INPROGRESS}"><span class="glyphicon glyphicon-pushpin"> </span></g:if>正在进行中作业</g:link>
			  <g:link class="btn btn-success" action="pcourse_chakan" id="${studentInstance.id }" params="${[pick_item:'wdzy', aStatusStatus:CONSTANT.ASSIGNMENT_STATUS_DONE] }"><g:if test="${aStatusStatus == CONSTANT.ASSIGNMENT_STATUS_DONE}"><span class="glyphicon glyphicon-pushpin"> </span></g:if>已经完成的作业</g:link>
			</div>
			<table class="table table-hover table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<th class="text-center"><g:message code="assignmentStatus.assignment.label" default="练习范围" /></th>

						<th class="text-center"><g:message code="assignmentStatus.status.label" default="状态"  /></th>

						<th class="text-center"><g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="薄弱点" /></th>

						<th class="text-center"><g:message code="assignmentStatus.finishedQuestions.label" default="完成题量 " /></th>

						<th class="text-center"><g:message code="assignmentStatus.correctQuestions.label" default="答对" /></th>

						<th class="text-center"><g:message code="assignmentStatus.masterRate.label" default="掌握度" /></th>

						<th class="text-center"><g:message code="assignmentStatus.coverageRate.label" default="覆盖率" /></th>
						
						<th class="text-center">最近练习时间</th>

					</tr>
				</thead>
				<tbody>
					<g:each in="${aList}" status="i" var="assignmentStatusInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td>
							
								<g:if test="${aStatusStatus == CONSTANT.ASSIGNMENT_STATUS_NOTBEGIN}">
									${fieldValue(bean: assignmentStatusInstance, field: "assignment")}
								</g:if>
								<g:else>
									<g:link controller="assignmentStatus" action="show" id="${assignmentStatusInstance.id}">
										${fieldValue(bean: assignmentStatusInstance, field: "assignment")}
									</g:link>
								</g:else>
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "status")}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "toBeFocusedKnowledge")}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "finishedQuestions")}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "correctQuestions")}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "masterRate")}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "coverageRate")}
							</td>

							<td>
								<g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd HH:mm" date="${Quiz.findByStudentAndAssignmentAndAnsweredDateIsNotNull(studentInstance, assignmentStatusInstance.assignment, [sort:'answeredDate', order:'desc'])?.answeredDate }" />
							</td>

						</tr>
					</g:each>
				</tbody>
			</table>
			
			<div class="pagination">
				<g:paginate total="${aListSize}" action="pcourse_chakan" id="${studentInstance.id }"  params="${[pick_item:'wdzy', aStatusStatus:aStatusStatus] }"/>
			</div>
		</g:if>
		
		<!-- 错题集 -->
		<g:if test="${pick_item == 'ctj' }">
			<g:form name="ctjQueryForm" role="form" id="${studentInstance.id }">
			<g:hiddenField name="pick_item" value="${pick_item }"/>
			<div class="panel panel-warning">
			  <div class="panel-heading">查询条件</div>
			  <div class="panel-body">
			    <div class="input-group" style="width:100%">
				
				<label>题目</label>：<g:textField style="width:85pt" name="searchQuestion" value="${params.searchQuestion }"/>
				&nbsp;&nbsp;<label>知识点(chosenFor)</label>：<g:textField style="width:85pt" name="searchChosenFor" value="${params.searchChosenFor }"/>	
				
				<!-- date picker depends on the Javascript in application.js -->
				<label>&nbsp;&nbsp;练习日期</label>：
				<g:textField class="form-control" style="width:85pt" name="searchDate1" value="${params.searchDate1 }" id="dpd1" data-date-format="yyyy-mm-dd"/>	--
				<g:textField class="form-control" style="width:85pt" name="searchDate2" value="${params.searchDate2 }" id="dpd2" data-date-format="yyyy-mm-dd"/>
				
				&nbsp;&nbsp;<g:actionSubmit class="btn btn-primary" action="pcourse_chakan" value="查询"/>
				<%--maxRecord:<g:textField size="2" name="maxRecord" value="${params.maxRecord }"/>--%>
				
				</div>
			  </div>
			</div>			
			</g:form>
			<p>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>
			<table class="table table-hover table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<th style="width:50%"><span class="property-label">题目</span></th>
						<th><span class="property-label">错题详情</span></th>
						<th><span class="property-label">知识点</span></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${xQuestions}" status="row" var="e">
						<tr class="${(row % 2) == 0 ? 'even' : 'odd'}">
													
							<td>
								<g:cvtq question="${e}"/>
							</td>
							
							<td>
								<g:set var="stuFR" value="${studentInstance.failedRecords.findAll{ elem -> elem.question == e} }"></g:set>	
								<g:each in="${stuFR}" var="incorrectRecord" status="i">
										<!-- if recorrectedRecords used in the future, should check if the question is in recorrectedRecords or not -->
										<a data-toggle="modal" data-target="#incorrectQustionDetail${incorrectRecord.id}" href="">
											<div data-toggle="tooltip" data-placement="right" title="${incorrectRecord.quiz.name} (${ incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd')} )">
												${incorrectRecord.quiz.name } <br>
											</div>
										</a>
										<g:form name="A_dummyForm">
											<!-- Modal -->
											<div class="modal fade" id="incorrectQustionDetail${incorrectRecord.id}" tabindex="-1" role="dialog"
												aria-labelledby="myModalLabel" aria-hidden="true">
												<div class="modal-dialog">
													<div class="modal-content">
														<div class="modal-header">
															<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
															<h3 class="modal-title" id="myModalLabel">
																${incorrectRecord.quiz.name }
																的错题
															</h3>
															完成时间：${incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd HH:mm:ss') }
														</div>
														<div class="modal-body">
															<g:if test="${incorrectRecord.question?.parentQuestion}">
																${incorrectRecord.question?.parentQuestion.description}
																---------------------------------------- 这里是华丽的分割线 ----------------------------------------
																<br>
															</g:if>
															<g:cvtq question="${incorrectRecord.question }" quizQuestionRecord="${incorrectRecord }"
																showChecks="true" showKP="true" />
														</div>
														<div class="modal-footer">
															<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
														</div>
													</div>
													<!-- /.modal-content -->
												</div>
												<!-- /.modal-dialog -->
											</div>
											<!-- /.modal -->
										</g:form>
								</g:each>
									
							</td>	
							
							<td>
								<g:each in="${e.knowledgePoints }" status="kpSseq" var="kp">
									${kp }<br>
								</g:each>
							</td>				
						</tr>
					</g:each>
				</tbody>
			</table>		
			
		</g:if>
		
		<!-- 薄弱知识点-->
		<g:if test="${pick_item == 'brzsd' }">
			<g:form name="brzsdQueryForm" role="form" id="${studentInstance.id }">
			<g:hiddenField name="pick_item" value="${pick_item }"/>
			
			<div class="panel panel-warning">
			  <div class="panel-heading">查询条件</div>
			  <div class="panel-body">
			    <div class="input-group" style="width:100%">
				
				<label>&nbsp;&nbsp;作业范围</label>：(<a herf="#" onClick="select_all_assi()">ALL</a>)<g:select class="form-control" style="width:180pt" name="searchAssignments" from="${studentInstance.aStatus*.assignment.grep{elem-> elem.subject == session.course}}" multiple="multiple" optionKey="id"
				optionValue="name" size="5" value="${selectedAssignments*.id }" class="many-to-many form-control" />
					 
				<!-- date picker depends on the Javascript in application.js -->
				<label>&nbsp;&nbsp;练习日期</label>：
				<g:textField class="form-control" style="width:85pt" name="searchDate1" value="${params.searchDate1 }" id="dpd1" data-date-format="yyyy-mm-dd"/>	--
				<g:textField class="form-control" style="width:85pt" name="searchDate2" value="${params.searchDate2 }" id="dpd2" data-date-format="yyyy-mm-dd"/>		
				            	
				&nbsp;&nbsp;<g:actionSubmit class="btn btn-primary" action="pcourse_chakan" value="查询"/>
				
				maxKPShow:<g:textField size="3" name="maxRecord" value="${maxRecord }"/>
				</div>
			  </div>
			</div>			
			</g:form>
			<p>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>
			<table class="table table-hover table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<th style="width:35%"><span class="property-label">知识点</span></th>
						<th><span class="property-label">正确率</span><div class="glyphicon glyphicon-arrow-up"/></th>
						<th><span class="property-label">掌握度</span></th>
						<th><span class="property-label">练习数</span></th>
						<th><span class="property-label">错题集</span></th>	
					</tr>
				</thead>
				<tbody>
					<g:each in="${results}" status="row" var="item">
						<tr class="${(row % 2) == 0 ? 'even' : 'odd'}">
													
							<td>
								${item.knowledge }
							</td>
							
							<td>
								${item.recentCorrectRate }
							</td>
													
							<td>
								${item.familyRecentCorrectRate == -1 ? "" : item.familyRecentCorrectRate}
							</td>
																						
							<td>
								${qnumDetails.get(item.knowledge.id).values()  }
							</td>
							
							<td>
								<div>
									<g:set var="stuFR" value="${studentInstance.failedRecords.sort{a,b -> b.quiz.id - a.quiz.id} }"></g:set>
									<g:each in="${stuFR}" var="incorrectRecord" status="i">
											<g:if test="${incorrectRecord.question.knowledgePoints*.id.contains(item.knowledge.id) && incorrectRecord.quiz.student.id == studentInstance.id}">
												<!-- if recorrectedRecords used in the future, should check if the question is in recorrectedRecords or not -->
												<a data-toggle="modal" data-target="#incorrectQustionDetail${i}" href="">
													<div data-toggle="tooltip" data-placement="right" title="${incorrectRecord.quiz.name} (${incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd')  })">
														${incorrectRecord.quiz.name }
													</div>
												</a>
												<g:form name="A_dummyForm">
													<!-- Modal -->
													<div class="modal fade" id="incorrectQustionDetail${i}" tabindex="-1" role="dialog"
														aria-labelledby="myModalLabel" aria-hidden="true">
														<div class="modal-dialog">
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
																	<h3 class="modal-title" id="myModalLabel">
																		${incorrectRecord.quiz.name }
																		的错题
																	</h3>
																	完成时间：${incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd HH:mm:ss') }
																</div>
																<div class="modal-body">
																	<g:if test="${incorrectRecord.question?.parentQuestion}">
																		${incorrectRecord.question?.parentQuestion.description}
																		---------------------------------------- 这里是华丽的分割线 ----------------------------------------
																		<br>
																	</g:if>
																	<g:cvtq question="${incorrectRecord.question }" quizQuestionRecord="${incorrectRecord }"
																		showChecks="true" showKP="true" />
																</div>
																<div class="modal-footer">
																	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
																</div>
															</div>
															<!-- /.modal-content -->
														</div>
														<!-- /.modal-dialog -->
													</div>
													<!-- /.modal -->
												</g:form>
	
											</g:if>
									</g:each>
								</div>
							</td>
						</tr>
					</g:each>
				</tbody>
			</table>
			
		</g:if>
		
		<!-- 课程辅导情况-->
		<g:if test="${pick_item == 'kcfdqk' }">
			<g:include action="instructed" id="${studentInstance.id }" params="${[chakanFlag:'true'] }" />
		</g:if>
		</div>
	</div>
	<!-- for pick_item == 'brzsd' 薄弱知识点 -->
	<script type="text/javascript">
		function select_all_assi(){		  
			 var obj = document.getElementById('searchAssignments');
			     
			     for(var i=0;i<obj.options.length;i++)
			     {
			    	 obj.options[i].selected = true;
			     }	
		}
	</script>
</body>

</html>