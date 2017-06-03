<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="学生历次练习汇总" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div class="container bs-docs-container" role="main">
	&nbsp;
		<g:form name="queryForm" role="form">
		
		<div class="panel panel-warning">
		  <div class="panel-heading">错题集</div>
		  <div class="panel-body">
		    <div class="input-group" style="width:100%">
			
			<label>题目</label>：<g:textField style="width:85pt" name="searchQuestion" value="${params.searchQuestion }"/>
			&nbsp;&nbsp;<label>知识点(chosenFor)</label>：<g:textField style="width:85pt" name="searchChosenFor" value="${params.searchChosenFor }"/>	
			&nbsp;&nbsp;<label>最少学生数</label>：<g:textField size="2" name="minStuNum" value="${params.minStuNum }"/>
			
			<!-- date picker depends on the Javascript in application.js -->
			<label>&nbsp;&nbsp;练习日期</label>：
			<g:textField class="form-control" style="width:85pt" name="searchDate1" value="${params.searchDate1 }" id="dpd1" data-date-format="yyyy-mm-dd"/>	--
			<g:textField class="form-control" style="width:85pt" name="searchDate2" value="${params.searchDate2 }" id="dpd2" data-date-format="yyyy-mm-dd"/>
			
			&nbsp;&nbsp;<g:actionSubmit class="btn btn-primary" action="report4XRecord" value="查询"/>
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
					<th><span class="property-label">学生数</span><div class="glyphicon glyphicon-arrow-up"/></th>
					<th><div data-toggle="tooltip" data-placement="right" title="学生姓名 - 练习编号。。如有多个错题，则按练习时间降序排列"><span class="property-label">错题详情?</span></div></th>
					<th><span class="property-label">知识点</span></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${failedQuestionStuMap}" status="row" var="e">
					<tr class="${(row % 2) == 0 ? 'even' : 'odd'}">
												
						<td>
							<g:cvtq question="${e.key }"/>
						</td>
						
						<td>
							${e.value.size() }
						</td>
						
						<td>
							<g:each in="${e.value }" status="stuSeq" var="stu">	
							<%--<g:set var="stuFR" value="${stu.failedRecords.findAll{ elem -> elem.id == a.quiz.id} }"></g:set>	 --%>
								<g:set var="stuFR" value="${stu.failedRecords.sort{a,b -> b.quiz.id - a.quiz.id} }"></g:set>							
								<g:each in="${stuFR}" var="incorrectRecord" status="i">
									<g:if test="${incorrectRecord.question == e.key}">
										<!-- if recorrectedRecords used in the future, should check if the question is in recorrectedRecords or not -->
										<a data-toggle="modal" data-target="#incorrectQustionDetail${i}" href="">
											<div data-toggle="tooltip" data-placement="right" title="${incorrectRecord.quiz.name} (${ incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd')} )">
												${stu.fullName} - ${incorrectRecord.quiz.id } <br>
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
								
							</g:each>
						</td>	
						
						<td>
							<g:each in="${e.key.knowledgePoints }" status="kpSseq" var="kp">
								${kp }<br>
							</g:each>
						</td>				
					</tr>
				</g:each>
			</tbody>
		</table>
			  	
		<%-- 有projects的查询，resultsTotalCount不正确，故取消了分页
		<div class="pagination">
			<g:paginate total="${resultsTotalCount}" action="report4allStuQuiz" params="${params }"/>
		</div>--%>
	
	</div>

</body>
</html>