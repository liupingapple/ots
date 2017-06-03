<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>

</head>

<body>
	<div class="container bs-docs-container">
		<p>
		<div class="panel-group" id="accordion">
		
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseOne"> <span
							class="glyphicon glyphicon-user">&nbsp;我的班级</span>
						</a>
					</h4>
				</div>

				<div id="collapseOne" class="panel-collapse collapse in">
					<div class="panel-body">
						<table class="table table-hover">
							<tbody>
								<tr>
									<th><span class="property-label">班级名称</span></th>
									<th><span class="property-label">学生数</span></th>
									<th><span class="property-label">课程数</span></th>
									<th><span class="property-label">完成练习数</span></th>
									<th><span class="property-label">错误率</span></th>
									<th><span class="property-label">教学计划(模板)</span></th>
								</tr>
								<g:each in="${tclassList }" status="i" var="tclass">
									<g:hiddenField name="tclassid" value="${tclass.id }" />
									<tr>
										<td>
											<a data-toggle="modal" data-target="#myModal" href="#">${tclass.className }</a>
										</td>
										<td>
											${tclass.students.size() }
										</td>
										<td>
											${tclass.lessons.size() }
										</td>
										<td>
											${tclass.totalQuestionsPracticed }
										</td>
										<td>
											${tclass.totalErrorRate }
										</td>
										<td>
											${tclass.teachingPlan }
										</td>
									</tr>
								</g:each>
							</tbody>
						</table>

						<!-- Button trigger modal -->
						<button class="btn btn-primary" data-toggle="modal" data-target="#myModal">新建班级</button>

						<!-- Modal -->
						<g:form name="saveTClassForm" action="saveTClass">
							<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
								aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
											<h4 class="modal-title" id="myModalLabel">班级</h4>
										</div>
										<div class="modal-body">
											<g:render template="tclassForm"></g:render>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
											<button type="button" class="btn btn-default" onClick="sel();saveTClassForm.submit();">保存</button>
										</div>
									</div>
									<!-- /.modal-content -->
								</div>
								<!-- /.modal-dialog -->
							</div>
							<!-- /.modal -->
						</g:form>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo"> <span
							class="glyphicon glyphicon-book">&nbsp;我的课程</span>
						</a>
					</h4>
				</div>
				<div id="collapseTwo" class="panel-collapse collapse">
					<div class="panel-body">
						<table class="table table-hover">
							<tbody>
								<tr>
									<th><span class="glyphicon glyphicon-book"></span></th>
									<th><span class="property-label">课程ID</span></th>
									<th><span class="property-label">课程名称</span></th>
									<th><span class="property-label">班级数</span></th>
									<th><span class="property-label">Assignment</span></th>
									<th><span class="property-label">开始日期</span></th>
									<th><span class="property-label">结束日期</span></th>
								</tr>
								<g:each in="${lessonList }" status="i" var="les">
									<g:hiddenField name="lessonid${i}" value="${les.id }" />
									<tr>
										<th>
											${les.id }
										</th>
										<th>
											${les.name }
										</th>
										<th>
											${les.teachingPlan }
										</th>
										<th>
											${les.teachingPlan }
										</th>
										<th>
											${les.teachingPlan }
										</th>
										<th>
											${les.teachingPlan }
										</th>
										<th>
											${les.teachingPlan }
										</th>
									</tr>
								</g:each>
							</tbody>
						</table>

						<!-- Button trigger modal -->
						<button class="btn btn-primary" data-toggle="modal" data-target="#myModal_lesson">新建课程</button>

						<!-- Modal -->
						<g:form name="saveLessonForm" action="saveLesson">
							<div class="modal fade" id="myModal_lesson" tabindex="-1" role="dialog"
								aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
											<h4 class="modal-title" id="myModalLabel">课程</h4>
										</div>
										<div class="modal-body">
											<g:render template="lessonForm"></g:render>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
											<button type="button" class="btn btn-default" onClick="saveLessonForm.submit();">保存</button>
										</div>
									</div>
									<!-- /.modal-content -->
								</div>
								<!-- /.modal-dialog -->
							</div>
							<!-- /.modal -->
						</g:form>
					</div>
				</div>
			</div>


			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseThree"> <span
							class="glyphicon glyphicon-file">&nbsp;我的Assignment</span>
						</a>
					</h4>
				</div>
				<div id="collapseThree" class="panel-collapse collapse">
					<div class="panel-body">
						<g:link class="btn" controller="assignment" action="list">管理我的Assignment</g:link>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseFour"> <span
							class="glyphicon glyphicon-asterisk">&nbsp;我的Assignment模板</span>
						</a>
					</h4>
				</div>
				<div id="collapseFour" class="panel-collapse collapse">
					<div class="panel-body">
						<g:link class="btn" controller="assignmentTemplate" action="list">管理我的Assignment模板</g:link>
					</div>
				</div>
			</div>

			<!-- End of panel -->

		</div>
	</div>
</body>
</html>