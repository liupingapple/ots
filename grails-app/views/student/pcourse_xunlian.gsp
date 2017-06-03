<!DOCTYPE html>
<%@page import="ots.Quiz"%>
<%@page import="ots.Teacher"%>
<html>
<head>
<meta name="layout" content="bmain">
<title>课程</title>
</head>

<body>
	<div class="container bs-docs-container">		
		<g:form name="topForm" action="pcourse_xunlian">
			<g:hiddenField name="pick_item" id="pick_item" value="${pick_item }"/>
			<div class="container-nav">
				<span class="item"></span>
				<a href="#"><span id="not_begin"><i></i>未开始作业<b></b></span></a>
				<a href="#"><span id="in_progress"><i></i>正在进行中的作业<b></b></span></a>
				<a href="#"><span id="finished"><i></i>已经完成的作业<b></b></span></a>
				<span class="item item2"></span>
			</div>
		</g:form>
		<p>
		
		<table class="table table-hover table-condensed table-bordered table-striped">
			<thead>
				<g:if test="${pick_item == 'not_begin' }">
					<tr>					
						<g:sortableColumn property="assignment" title="${message(code: 'assignmentStatus.assignment.label', default: '作业名称')}" params="${[pick_item:pick_item] }"/>
						
						<th class="text-center">题目题数</th>
						<th class="text-center">知识点数</th>
		
						<g:sortableColumn property="dateCreated" title="${message(code: 'assignmentStatus.dateCreated.label', default: '作业布置时间')}" params="${[pick_item:pick_item] }"/>
										
						<th class="text-center"></th>
					</tr>
				</g:if>
				<g:else>
					<tr>					
						<g:sortableColumn property="assignment" title="${message(code: 'assignmentStatus.assignment.label', default: '作业名称')}" params="${[pick_item:pick_item] }"/>
				
						<th class="text-center"><g:message code="assignmentStatus.toBeFocusedKnowledge.label" default="薄弱点" /></th>
		
						<g:sortableColumn property="finishedQuestions" title="${message(code: 'assignmentStatus.finishedQuestions.label', default: '完成题数')}" params="${[pick_item:pick_item] }"/>
		
						<g:sortableColumn property="correctQuestions" title="${message(code: 'assignmentStatus.correctQuestions.label', default: '答对')}" params="${[pick_item:pick_item] }"/>
		
						<g:sortableColumn property="masterRate" title="${message(code: 'assignmentStatus.masterRate.label', default: '掌握度')}" params="${[pick_item:pick_item] }"/>
		
						<g:sortableColumn property="coverageRate" title="${message(code: 'assignmentStatus.coverageRate.label', default: '覆盖率')}" params="${[pick_item:pick_item] }"/>
						
						<th class="text-center">最近练习时间</th>
						<th class="text-center"></th>
					</tr>
				</g:else>
			</thead>
			<tbody>
				<g:each in="${aList}" status="i" var="assignmentStatusInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<g:if test="${pick_item == 'not_begin' }">
							<td><g:link controller="assignmentStatus" action="show" id="${assignmentStatusInstance.id}">
									${fieldValue(bean: assignmentStatusInstance, field: "assignment")}
								</g:link></td>
		
							<%
								def kpNum = 0;
								def questionNum = 0;
								assignmentStatusInstance.assignment.templates.each { template->
									template.knowledgePoints.each{ kp->
										kpNum++
										questionNum += kp.totalQuestion
									}
								}
							%>
							<td>
								${questionNum}
							</td>
							
							<td>
								${kpNum}
							</td>
							
							<td>
								${fieldValue(bean: assignmentStatusInstance, field: "dateCreated")}
							</td>
							
							<td><g:link class="btn btn-xs btn-success" controller="quiz" action="continuePractice" params="${[stuId: session.user.id, aStatusId:assignmentStatusInstance.id] }">开始练习</g:link></td>
						</g:if>
						<g:else>
							<td><g:link controller="assignmentStatus" action="show" id="${assignmentStatusInstance.id}">
									${fieldValue(bean: assignmentStatusInstance, field: "assignment")}
								</g:link></td>
		
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
							
							<td><g:link class="btn btn-xs btn-success" controller="quiz" action="continuePractice" params="${[stuId: session.user.id, aStatusId:assignmentStatusInstance.id] }">继续练习</g:link></td>
						</g:else>
					</tr>
				</g:each>
			</tbody>
		</table>
			  	
		<div class="pagination">
			<g:paginate total="${aTotal}" action="pcourse_xunlian" params="${[pick_item:pick_item] }"/>
		</div>
		
	</div>
</body>

</html>