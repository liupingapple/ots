<%@page import="ots.Student"%>
<%@page import="ots.Quiz"%>
<div id="show-quiz" class="container bs-docs-container" role="main">
this page is replaced with assignmentStatus/_inc_list.gsp
			<h3>
				${studentInstance.fullName }
				的作业列表
			</h3>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>
			<table class="table table-hover table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<th><g:message code="assignmentStatus.assignment.label" default="练习范围" /></th>

						<g:sortableColumn property="status" title="状态" />

						<th><g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="薄弱点" /></th>

						<g:sortableColumn property="finishedQuestions" title="完成题量/要求 " />

						<g:sortableColumn property="correctQuestions" title="答对" />

						<g:sortableColumn property="masterRate" title="掌握度/要求" />

						<g:sortableColumn property="coverageRate" title="覆盖率" />

						<th><g:message code="assignmentStatus.comment" default="讲评 " /></th>
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
								${fieldValue(bean: assignmentStatusInstance, field: "finishedQuestions")}/${assignmentStatusInstance.assignment.questionLimit}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "correctQuestions")}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "masterRate")}/${(assignmentStatusInstance.assignment.targetCorrectRate*100).intValue()}
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "coverageRate")}%
							</td>

							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "comment")}
							</td>

						</tr>
					</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${assignmentStatusInstanceTotal}" />
			</div>
	</div>