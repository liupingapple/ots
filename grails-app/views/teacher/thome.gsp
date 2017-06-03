<!DOCTYPE html>
<%@page import="javax.rmi.CORBA.Util"%>
<%@page import="ots.Util"%>
<%@page import="ots.Assignment"%>
<%@page import="ots.CONSTANT"%>
<%@page import="ots.Quiz"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="layout" content="bmain">
<title>Teacher Home page</title>
</head>

<body>
	<script type="text/javascript">
	
		function addAssignment(stuId){
			var avai = "#available_assi"+stuId; 
			var sel = "#selected_assi"+stuId; 
			
			if($(avai+" option:selected").length>0) {  
	　　　　　　　　　　　$(avai+" option:selected").each(function(){  
	　　　　　　　　　　　　　　$(sel).append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option"); 
	　　　　　　　　　　　　　　$(this).remove();　  
	　　　　　　　　　　　})  
	　　　　　　　}  
	　　　　　　　else {  
	　　　　　　　　　　　alert("请选择要添加的作业！");  
	　　　　　　　}  
		}

		function delAssignment(stuId){
			var avai = "#available_assi"+stuId; 
			var sel = "#selected_assi"+stuId; 
			  
			if($(sel+" option:selected").length>0) 　{
				//if (!confirm("如果学生已经做了该作业的练习，您的操作将会删除学生已经做了该作业的数据(AssignmentStatus 数据)，确定这么做吗？") ) {
				//	return
				//} 
				$(sel+" option:selected").each(function(){  
					$(avai).append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option"); 
					$(this).remove();　  
				})  
			}  
			else 　{  
				alert("请选择要移除的作业！");  
			}
		}
				
		//提交按扭获取左右的options所有值传给后台处理
		function sel_submit(stuId){
			var avai = "#available_assi"+stuId; 
			var sel = "#selected_assi"+stuId; 
			
			$(sel+" option").each(
				function(){
					$(this).attr('selected', true); // 或者 $(this).attr('selected',true); 
				}
			)
			
			$(avai+" option").each(
				function(){
					$(this).attr('selected', true);
				}
			)
			    
			$("#assignForm"+stuId).submit();
		}
		
	</script>
	
	<div class="container bs-docs-container">
		<div class="panel-group" id="accordion">
			<!-- panel group -->
			
			<%--<div class="panel panel-default">
			
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseMsg"> <span
							class="glyphicon glyphicon-envelope">&nbsp;我的消息（new/total）</span>
						</a>
					</h4>
				</div>
				<div id="collapseMsg" class="panel-collapse collapse">
					<div class="panel-body">
						<table class="table table-hover">
							<tbody>
								<tr>
									<th><span class="property-label">序号</span></th>
									<th><span class="property-label">来自</span></th>
									<th><span class="property-label">内容</span></th>
									<th><span class="property-label">日期</span></th>
									<th><span class="property-label">状态</span></th>
								</tr>
								<g:each
									in="${msgList?.sort({ots.Message a, ots.Message b -> a.replyDate.compareToIgnoreCase(b.replyDate) })}"
									status="i" var="msg">
									<tr>
										<td><a data-toggle="modal" data-target="#myMSGModal${msg.id }">${i }</a></td>
										<td>
											${i+1 }
										</td>
										<td>
										</td>
										<td>
										</td>
										<td>
										</td>
										<td></td>
									</tr>

									<!-- Modal -->
									<div class="modal fade" id="myMSGModal${msg.id }" tabindex="-1" role="dialog"
										aria-labelledby="myModalLabel" aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													<h4 class="modal-title" id="myModalLabel">
														来自xxx的消息
													</h4>
												</div>
												<div class="modal-body">
													x
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
													<!-- <button type="button" class="btn btn-default" onClick="sel();saveStuForm.submit();">保存</button>  -->
												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->

								</g:each>
							</tbody>
						</table>
					</div>
				</div>
			</div>

			--%>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseStu"> <span
							class="glyphicon glyphicon-user">&nbsp;我的学生</span>
						</a>
					</h4>
				</div>

				<div id="collapseStu" class="panel-collapse collapse in">  <!-- show student list by default -->
					<div class="panel-body">
						<table class="table table-hover">
							<tbody>
								<tr>
									<th><span class="property-label">&nbsp;</span></th>
									<th><span class="property-label">学生姓名</span></th>
									<th><span class="property-label">作业数</span></th>
									<th><span class="property-label">最近一次做题时间</span></th>
									<th><span class="property-label">最近一次做的练习</span></th>
									<th><span class="property-label">最新留言</span></th>
									<th><span class="property-label">辅导记录</span></th>
								</tr>
								<g:each
									in="${teacherInstance?.students?.sort({ots.Student a, ots.Student b -> a.userName.compareToIgnoreCase(b.userName) })}"
									status="i" var="stu">
									<tr>
										<td class="text-center">
												<g:link class="btn btn-primary btn-xs" controller="student" action="studyStatus" id="${stu.id }">
													学习情况
												</g:link>
										</td>
										<td>
											<g:link controller="student" id="${stu.id }" action="show">${stu.fullName }</g:link>
										</td>
										<td>
											<a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#myAStatusModal${stu.id }">${stu.aStatus.size()}</a>
										</td>
											<% def quiz = Quiz.findByStudentAndAnsweredDateIsNotNull(stu, [sort: 'answeredDate', order: 'desc']) %>
										<td>			 
											<g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd HH:mm"  date="${quiz?.answeredDate }" />
										</td>
										<td>
											<g:link controller="quiz" action="show" id="${quiz?.id }">${quiz}</g:link>
										</td>
										<td>
											<g:set var="parsedMsg" value="${Util.parseLeaveMsg(stu)}"></g:set>
											<a class="btn ${parsedMsg[0]<parsedMsg[2]?'btn-warning':'btn-primary'} btn-xs" data-toggle="modal" data-target="#myMsg${stu.id }">											
												<g:if test="${stu.telephone2?.length() > 10}">
													${stu.telephone2.substring(5, 10)}
												</g:if>
												<g:else>
													留言
												</g:else>												
											</a>
										</td>
										<td>
											<a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#myInstructed${stu.id }">											
												<g:if test="${stu.briefComment?.length() > 10}">
													${stu.briefComment.substring(5, 10)}
												</g:if>
												<g:else>
													新建
												</g:else>												
											</a>
										</td>
									</tr>
									
									<!-- 点击作业数 - 作业分配 Modal -->									
									<g:form name="assignForm${stu.id }" action="assiSave">
										<g:hiddenField name="teacherId" value="${teacherInstance.id }" />
										<g:hiddenField name="stuId" value="${stu.id }" />
									<div class="modal fade" id="myAStatusModal${stu.id }" tabindex="-1" role="dialog"
										aria-labelledby="myModalLabel" aria-hidden="true">
										
										<div class="modal-dialog" style="width: 60%">
											<div class="modal-content">
											
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													<h4 class="modal-title" id="myModalLabel">
														${stu.fullName }的作业情况
													</h4>
												</div>
												
												<div class="modal-body">
														<div class="row">
															<div class="col-md-1">&nbsp;</div>
															<div class="col-md-4"><h4 class="glyphicon glyphicon-user">&nbsp;未分配作业</h4>
																<select name="available_assi" multiple="multiple" size="15" class="many-to-many form-control" id="available_assi${stu.id }">
																	<g:each in="${ots.Assignment.where{assignedBy == teacherInstance.userName}.list() - stu?.aStatus*.assignment }" var="asmt">
																		<option value="${asmt.id }">${asmt.name } - [${Quiz.countByStudentAndAssignment(stu, asmt)}]</option>
																	</g:each>
																</select>																	
															</div>
															
															<div class="col-md-1">
																<br/><br/><br/><br/><br/>
																<button id="add" class="btn btn-default btn-lg" type="button" onClick="addAssignment(${stu.id});">
																	<span class="glyphicon glyphicon-circle-arrow-right"></span>
																</button>
																<br><br>
																<button id="delete" class="btn btn-default btn-lg" type="button" onClick="delAssignment(${stu.id});">
																	<span class="glyphicon glyphicon-circle-arrow-left"></span>
																</button>
															</div>
															
															<div class="col-md-5">
																<h4 class="glyphicon glyphicon-user">&nbsp;已分配作业[练习数]</h4>																
																<select name="selected_assi" multiple="multiple" size="15" class="many-to-many form-control" id="selected_assi${stu.id }">
																	<g:each in="${stu?.aStatus*.assignment}" var="asmt">
																		<g:set var="quizNum" value="${Quiz.countByStudentAndAssignment(stu, asmt) }"></g:set>
																		<g:if test="${quizNum > 0 }">
																			<option disabled="disabled" value="${asmt.id }" >${asmt.name } - [${quizNum}]</option>
																		</g:if>
																		<g:else>
																			<option value="${asmt.id }" >${asmt.name } - [${quizNum }]</option>
																		</g:else>
																	</g:each>
																</select>
															</div>
															<div class="col-md-1">&nbsp;</div>
														</div>
													
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
													<button type="button" class="btn btn-primary" onClick="sel_submit(${stu.id });">保存</button>
												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->
									</g:form>
									
									<!-- 最近留言 - Modal -->									
									<g:form name="MsgForm${stu.id }" action="leaveMsg">
										<g:hiddenField name="teacherId" value="${teacherInstance.id }" />
										<g:hiddenField name="stuId" value="${stu.id }" />
									<div class="modal fade" id="myMsg${stu.id }" tabindex="-1" role="dialog"
										aria-labelledby="myModalLabel" aria-hidden="true">
										
										<div class="modal-dialog" style="width: 40%">
											<div class="modal-content">
											
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													<h4 class="modal-title" id="myModalLabel">
														有关${stu.fullName }的留言
													</h4>
												</div>
																								
												<div class="modal-body">
													<g:set var="parsedMsg" value="${Util.parseLeaveMsg(stu)}"></g:set>
													<label>给学生的留言: ${parsedMsg[0] }</label>
													<g:textArea class="form-control" name="teacherToStuMsg" rows="2" cols="80" value="${parsedMsg[1]}"></g:textArea>
													<br>
													<label>来自学生的留言: ${parsedMsg[2] }</label>
													<g:textArea class="form-control" readonly="true" name="stuToTeacherMsg" rows="2" cols="80" value="${parsedMsg[3]}"></g:textArea>
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
													<g:submitButton class="btn btn-primary" name="保存"/>
												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->
									</g:form>
																		 
									<!-- 辅导记录 - Modal -->									
									<g:form name="InstructedForm${stu.id }" action="instructed">
										<g:hiddenField name="teacherId" value="${teacherInstance.id }" />
										<g:hiddenField name="stuId" value="${stu.id }" />
									<div class="modal fade" id="myInstructed${stu.id }" tabindex="-1" role="dialog"
										aria-labelledby="myModalLabel" aria-hidden="true">
										
										<div class="modal-dialog" style="width: 50%">
											<div class="modal-content">
											
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													<h4 class="modal-title" id="myModalLabel">
														${stu.fullName }的辅导情况
													</h4>
												</div>
												
												<div class="modal-body">
													<label>新增辅导记录</label>
													<div class="input-group input-group-sm">														
														<span class="input-group-addon">辅导日期</span>
														<input name="instructed_date" type="text" class="form-control" placeholder="年-月-日  " size="10" value="${new Date().format('yyyy-MM-dd', TimeZone.getTimeZone('Asia/Shanghai'))}">	
														<span class="input-group-addon">开始时间</span>
												        <input name="instructed_begin" type="text" class="form-control" placeholder="20:00" value="20:00" size="5">	
												        <span class="input-group-addon">结束时间</span>
												        <input name="instructed_end" type="text" class="form-control" placeholder="20:20" value="20:20"  size="5">
												    </div>	
												    <div class="input-group input-group-sm">
												        <span class="input-group-addon">涵盖知识点</span>
													  	<input name="instructed_kps" type="text" class="form-control" size="60">
													</div>
													<br><label>历史辅导记录</label><br>
													<g:textArea class="form-control" readonly="true"  name="records" rows="8" cols="80">${Util.parseInstructedRecords(stu, false) }</g:textArea>
																			    
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
													<g:submitButton class="btn btn-primary" name="保存"/>
												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->
									</g:form>
									
								</g:each>
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseAss"> <span
							class="glyphicon glyphicon-file">&nbsp;我的作业列表</span>
						</a>
					</h4>
				</div>
				<div id="collapseAss" class="panel-collapse collapse">
					<div class="panel-body">
						<table class="table table-hover">  <!--  table-striped table-bordered  -->
							<tr>
								<th>名称</th>
								<th>作业要求</th>
								<!-- <th>正在做练习学生数</th> -->
								<th>完成日期</th>
								<th>时间要求</th>
								<th>题数要求</th>
								<th>掌握度要求</th>
								<th>每次题数</th>
								<th>模板数</th>
							</tr>
							<g:each in="${asmtList}" status="i" var="asmt">
								<tr>
									<td>
									   <g:link controller="assignment" action="assignToStu" params="['fromController':'teacher', 'asmtId':"${asmt.id }", 'teacherId':"${teacherInstance.id}"]">
									      ${asmt?.name }
									   </g:link>
									</td>
									<td>${asmt?.comment }</td>
									<%-- <td>${asmt?.students.size() }</td> --%>
									<td><g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd" date="${asmt?.dueDate}" /></td>
									<td>${asmt?.timeLimit }</td>
									<td>${asmt?.questionLimit }</td>
									<td>${(asmt?.targetCorrectRate*100).intValue()}</td>
									<td>${asmt?.numberOfQuestionsPerPage}</td>
									<td>${asmt?.templates?.size()}</td>
								</tr>
								
							</g:each>
						</table>
						
						<g:link controller="assignment" action="list">管理我的更多作业</g:link>
					</div>
				</div>
			</div>
		</div>
		<!-- End of panel -->

	</div>

</body>
</html>