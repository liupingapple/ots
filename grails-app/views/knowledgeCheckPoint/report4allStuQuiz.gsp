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
		  <div class="panel-heading">学生历次练习知识点成绩汇总报表</div>
		  <div class="panel-body">
		    <div class="input-group" style="width:100%">
			
			<label>学生</label>：(<a herf="#" onClick="select_all_stu()">ALL</a>) <g:select class="form-control" style="width:100pt" name="searchStudents" from="${teacher.students.sort{a, b -> a.id - b.id}}" multiple="multiple" optionKey="id"
			optionValue="fullName" size="5" value="${selectedStudents*.id}" class="many-to-many form-control" />
			
			<label>&nbsp;&nbsp;作业范围</label>：(<a herf="#" onClick="select_all_assi()">ALL</a>)<g:select class="form-control" style="width:180pt" name="searchAssignments" from="${ots.Assignment.findAllByAssignedBy(teacher.userName).sort{a, b -> a.id - b.id}}" multiple="multiple" optionKey="id"
			optionValue="name" size="5" value="${selectedAssignments*.id }" class="many-to-many form-control" />
				 
			<!-- date picker depends on the Javascript in application.js -->
			<label>&nbsp;&nbsp;练习日期</label>：
			<g:textField class="form-control" style="width:85pt" name="searchDate1" value="${params.searchDate1 }" id="dpd1" data-date-format="yyyy-mm-dd"/>	--
			<g:textField class="form-control" style="width:85pt" name="searchDate2" value="${params.searchDate2 }" id="dpd2" data-date-format="yyyy-mm-dd"/>		
			            	
			&nbsp;&nbsp;<g:actionSubmit class="btn btn-primary" action="report4allStuQuiz" value="查询"/>
			
			maxKPShow:<g:textField size="2" name="maxRecord" value="${params.maxRecord }"/>
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
					<th><span class="property-label">知识点</span></th>
					<th><span class="property-label">平均正确率</span><div class="glyphicon glyphicon-arrow-up"/></th>
					<th><span class="property-label">最低正确率</span></th>
					<th><span class="property-label">最高正确率</span></th>
					<th><span class="property-label">学生数</span><div class="glyphicon glyphicon-arrow-down"/></th>
					<th style="width:10%"><span class="property-label">试题数</span></th>
					<th style="width:12%"><span class="property-label">正确率详情</span></th>
					<th style="width:12%"><div data-toggle="tooltip" data-placement="right" title="学生姓名 - 练习编号。如有多个错题，则按练习时间降序排列"><span class="property-label">错题集?</span></div></th>	
				</tr>
			</thead>
			<tbody>
				<g:each in="${results}" status="row" var="item">
					<tr class="${(row % 2) == 0 ? 'even' : 'odd'}">
												
						<td>
							${item[0] }
						</td>
						
						<td>
							${item[1] }
						</td>
												
						<td>
							${item[2] }
						</td>
						
						<td>
							${item[3] }
						</td>
												
						<td>
							${item[4] }
						</td>
						
						<td>
							${qnumDetails.get(item[0].id)  }
						</td>
						
						<td>
							${fDetails.get(item[0].id) }
						</td>

						<td>
							<g:each in="${selectedStudents }" var="stu">								
								<div>
									<g:set var="stuFR" value="${stu.failedRecords.sort{a,b -> b.quiz.id - a.quiz.id} }"></g:set>
									<g:each in="${stuFR}" var="incorrectRecord" status="i">
											<g:if test="${incorrectRecord.question.knowledgePoints*.id.contains(item[0].id) && incorrectRecord.quiz.student.id == stu.id}">
												<!-- if recorrectedRecords used in the future, should check if the question is in recorrectedRecords or not -->
												<a data-toggle="modal" data-target="#incorrectQustionDetail${incorrectRecord.id}" href="">
													<div data-toggle="tooltip" data-placement="right" title="${incorrectRecord.quiz.name} (${incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd')  })">
														${stu.fullName} - ${incorrectRecord.quiz.id }
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

	<script type="text/javascript">
		function select_all_stu(){		  
			 var obj = document.getElementById('searchStudents');
			     
			     for(var i=0;i<obj.options.length;i++)
			     {
			    	 obj.options[i].selected = true;
			     }	
		}

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