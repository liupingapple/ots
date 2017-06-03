<%@ page import="ots.Assignment"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div id="assignmentList" class="container bs-docs-container" role="main">
	
		<div class="col-md-3">
			&nbsp;&nbsp;
			<div class="bs-sidebar hidden-print" role="complementary">
				<div class="panel panel-warning">
					<div class="panel-body">
						<ul class="nav">
							<li>
								<g:link class="btn btn-default" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class="col-md-9">

			<h3>
				${session?.user.fullName }
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
						<g:sortableColumn property="name" title="${message(code: 'assignment.name.label', default: 'Name')}" />
						<g:sortableColumn property="assignedBy"
							title="${message(code: 'assignment.assignedBy.label', default: 'Assigned By')}" />
						<g:sortableColumn property="dueDate"
							title="${message(code: 'assignment.dueDate.label', default: 'Due Date')}" />
						<g:sortableColumn property="timeLimit"
							title="${message(code: 'assignment.timeLimit.label', default: 'Time Limit')}" />
						<g:sortableColumn property="questionLimit"
							title="${message(code: 'assignment.questionLimit.label', default: 'Question Limit')}" />
					</tr>
				</thead>
				<tbody>
					<g:each in="${assignmentInstanceList}" status="i" var="assignmentInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${assignmentInstance.id}">
									${fieldValue(bean: assignmentInstance, field: "name")}
								</g:link></td>

							<td>
								${fieldValue(bean: assignmentInstance, field: "assignedBy")}
							</td>

							<td><g:formatDate date="${assignmentInstance.dueDate}" /></td>

							<td>
								${fieldValue(bean: assignmentInstance, field: "timeLimit")}
							</td>

							<td>
								${fieldValue(bean: assignmentInstance, field: "questionLimit")}
							</td>

						</tr>
					</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${assignmentInstanceTotal}" />
			</div>
		</div>
	</div>
</body>
</html>
