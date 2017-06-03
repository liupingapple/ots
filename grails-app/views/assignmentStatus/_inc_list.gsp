<%@page import="ots.Student"%>
<%@page import="ots.Quiz"%>
<%@page import="ots.KnowledgePoint"%>
<%@ page import="ots.AssignmentStatus"%>
<%@ page import="ots.Assignment"%>
<%@ page import="ots.Util"%>

	<div class="container bs-docs-container" role="main">

		<div class="col-md-3">
			&nbsp;&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">${studentInstance?.fullName }的作业统计信息</h3>
					</div>
					<div class="panel-body">
					<table class="bg-info">
						<tr><td>作业总数：</td><td>${assignmentStatusInstanceTotal}</td></tr>
						<tr><td>练习总数：</td><td>${quizInstanceTotal}</td></tr>
						<tr><td>未开始作业数：</td><td>${notBeginTotal}</td></tr>
						<tr><td>进行中作业数：</td><td>${ongoingTotal}</td></tr>
						<tr><td>已完成作业数：</td><td>${finishedTotal}</td></tr>
						<tr><td>完成题目总次数：</td><td>${finishedQuestionsTotal}</td></tr>
						<tr><td>答对题目总次数：</td><td>${correctQuestionsTotal}</td></tr>
					</table>
					<hr/>
					<h5>薄弱知识点：</h5>
						<g:if test="${focusKPSet.size() < 5 }">
							<g:each in="${focusKPSet }" var="focusKP">
								<g:if test="${focusKP}">
								<font color="red">- ${focusKP.name}</font><br/>
								</g:if>
							</g:each>
							</ul>
						</g:if>
						<g:else>
							<h6>
								<a data-toggle="modal" data-target="#myFocusKPModal" href="">点击查看薄弱知识点</a>
							</h6>
							<!-- Modal -->
							<div class="modal fade" id="myFocusKPModal" tabindex="-1" role="dialog"
								aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
											<h4 class="modal-title" id="myModalLabel">需要关注的薄弱知识点</h4>
										</div>
										<div class="modal-body">
											<g:each in="${focusKPSet }" var="focusKP">
												<h5>
													<font color="red">&nbsp;-&nbsp;${focusKP }</font>
												</h5>
											</g:each>
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
						</g:else>
			
						
					<%--
					<g:if test="${session.user instanceof Student }">
						<hr/>
						<h5><a data-toggle="modal" data-target="#myMsg">最新留言</a></h5>
						<!-- 最近留言 - Modal -->									
						<g:form name="MsgForm" controller="assignmentStatus" action="leaveMsgToTeacher">
						<g:hiddenField name="stuId" value="${studentInstance.id }" />
						<div class="modal fade" id="myMsg" tabindex="-1" role="dialog"
							aria-labelledby="myModalLabel" aria-hidden="true">
							
							<div class="modal-dialog" style="width: 40%">
								<div class="modal-content">
								
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										<h4 class="modal-title" id="myModalLabel">
											最新留言
										</h4>
									</div>
									
									<div class="modal-body">
										<g:set var="parsedMsg" value="${Util.parseLeaveMsg(studentInstance)}"></g:set>
										<label>来自老师的留言: ${parsedMsg[0] }</label>
										<g:textArea class="form-control" readonly="true" name="teacherToStuMsg" rows="2" cols="80" value="${parsedMsg[1]}"></g:textArea>
										<br>
										<label>给老师的留言: ${parsedMsg[2] }</label>
										<g:textArea class="form-control" name="stuToTeacherMsg" rows="2" cols="80" value="${parsedMsg[3]}"></g:textArea>
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
						<h5><a data-toggle="modal" data-target="#myInstructed">辅导记录</a></h5>
						<div class="modal fade" id="myInstructed" tabindex="-1" role="dialog"
							aria-labelledby="myModalLabel" aria-hidden="true">
							
							<div class="modal-dialog" style="width: 50%">
								<div class="modal-content">
								
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										<h4 class="modal-title" id="myModalLabel">
											${studentInstance.fullName }的辅导情况
										</h4>
									</div>									
									<div class="modal-body">
										<g:textArea  readonly="true"  name="records" rows="8" cols="115">${Util.parseInstructedRecords(studentInstance) }</g:textArea>
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
										
					</g:if>									
					--%>
					
					</div>
				</div>
			</div>

		</div>

		<div class="col-md-9">
			<br/>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>
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
					<g:each in="${assignmentStatusInstanceList}" status="i" var="assignmentStatusInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link controller="assignmentStatus" action="show" id="${assignmentStatusInstance.id}">
									${fieldValue(bean: assignmentStatusInstance, field: "assignment")}
								</g:link></td>

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
				<g:paginate total="${assignmentStatusInstanceTotal}" controller="assignmentStatus" action="list"  params="${[stuId:studentInstance.id] }"/>
			</div>
			
			<%--<blockquote>
				<p class="bg-primary">Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec
					ullamcorper nulla non metus auctor fringilla. Duis mollis, est non commodo luctus.</p>
				<small> Steve Jobs, CEO Apple </small>
			</blockquote>
			
			<div class="thumbnail">
		      <img data-src="holder.js/300x300" alt="...">
		      <div class="caption">
		        <h3>Thumbnail label</h3>
		        <p>...</p>
		        <p><a href="#" class="btn btn-primary" role="button">Button</a> <a href="#" class="btn btn-default" role="button">Button</a></p>
		      </div>
		    </div>
		--%>
		</div>
	</div>