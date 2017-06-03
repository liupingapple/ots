<%@page import="ots.Teacher"%>
<%@page import="ots.Student"%>
<%@page import="ots.Quiz"%>
<%@page import="ots.KnowledgePoint"%>
<%@ page import="ots.AssignmentStatus"%>
<%@ page import="ots.Assignment"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'assignmentStatus.label', default: '作业信息统计表')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div class="container bs-docs-container" role="main">
	&nbsp;
		<g:form role="form">
			<div class="input-group" style="width:60%">
				<label>查询:&nbsp;&nbsp;</label>
				<% 
					def fromList = ['薄弱知识点', '状态', '学生账户名', '学生姓名']
					def keys = ['toBeFocusedKnowledge', 'status', 'student', 'studentFullName']
					if (!(session.user instanceof Teacher)) {
						fromList << '老师账户名'
						keys << 'teacher'
					}
				 %>
				<g:select name="searchCategory" class="form-control" style="width:20%"
					from="${fromList}"
					keys="${keys}"
					noSelection="['': '']"
					value="${searchCategory}"/>
					&nbsp;
				<input class="form-control" style="width:20%" type="search" name="searchValue" value="${searchValue}"/>
				&nbsp;
				<g:actionSubmit class="btn btn-primary" action="list4report" value="Search"/>
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
					<th class="text-center"><g:message code="assignmentStatus.student.label" default="学生账户" /></th>
					<th class="text-center"><g:message code="assignmentStatus.studentFullName.label" default="学生姓名" /></th>
					<g:if test="${!(session.user instanceof Teacher) }">
						<th class="text-center"><g:message code="assignmentStatus.student.label" default="老师" /></th>
					</g:if>
					
					<g:sortableColumn property="assignment" title="${message(code: 'assignmentStatus.assignment.label', default: '练习范围')}" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
	
					<g:sortableColumn property="status" title="${message(code: 'assignmentStatus.status.label', default: '状态')}" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
	
					<th class="text-center"><g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="薄弱点" /></th>
	
					<g:sortableColumn property="finishedQuestions" title="${message(code: 'assignmentStatus.finishedQuestions.label', default: '完成题数')}" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
	
					<g:sortableColumn property="correctQuestions" title="${message(code: 'assignmentStatus.correctQuestions.label', default: '答对')}" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
	
					<g:sortableColumn property="masterRate" title="${message(code: 'assignmentStatus.masterRate.label', default: '掌握度')}" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
	
					<g:sortableColumn property="coverageRate" title="${message(code: 'assignmentStatus.coverageRate.label', default: '覆盖率')}" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
					
					<th class="text-center">最近练习时间</th>
	
				</tr>
			</thead>
			<tbody>
				<g:each in="${aList}" status="i" var="assignmentStatusInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td>
							${fieldValue(bean: assignmentStatusInstance, field: "student")}
						</td>
						<td>
							${fieldValue(bean: assignmentStatusInstance, field: "student.fullName")}
						</td>
						
						<g:if test="${!(session.user instanceof Teacher) }">
						<td>
							${fieldValue(bean: assignmentStatusInstance, field: "student.teacher")} / ${fieldValue(bean: assignmentStatusInstance, field: "student.teacher.fullName")}
						</td>
						</g:if>
	
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
							<g:formatDate timeZone="Asia/Shanghai" format="yyyy-MM-dd HH:mm" date="${Quiz.findByStudentAndAssignmentAndAnsweredDateIsNotNull(assignmentStatusInstance.student, assignmentStatusInstance.assignment, [sort:'answeredDate', order:'desc'])?.answeredDate }" />
						</td>
	
					</tr>
				</g:each>
			</tbody>
		</table>
			  	
		<div class="pagination">
			<g:paginate total="${aTotal}" controller="assignmentStatus" action="list4report" params="${[searchCategory:searchCategory, searchValue:searchValue] }"/>
		</div>
	
	</div>	
</body>
</html>