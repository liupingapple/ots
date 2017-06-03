<%@page import="java.lang.invoke.ConstantCallSite"%>
<%@page import="ots.CONSTANT"%>
<%@page import="ots.Quiz"%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta name="layout" content="bmain">
<title>高手云</title>
<%-- <gvisualization:apiImport /> --%>
<zing:include />
</head>

<body>
	<div class="container bs-docs-container ">

		<script type="text/javascript">
			// support loading once
			var loading_done = false;
			function hiddenLoading() {
				$('#loading').hide();
				loading_done = true;
			}
	
	
			$(function(){
				if(!loading_done){$('#weak_kcp_form').submit();}
			}) 
		</script>
	
		<g:if test="${tabActive == 'alist' }">
				<%--also works for g:include, but chart will not work
				  <g:include controller="assignmentStatus" action="list" params="${[stuId:studentInstance.id] }"/> 
				 --%>
				<g:render contextPath="/assignmentStatus" template="inc_list"> </g:render>	
		</g:if>
		
		<g:if test="${tabActive == 'qlist' }"><p/>
			<h3>
				&nbsp;&nbsp;&nbsp;${studentInstance.fullName } 最近的练习
			</h3>
			<table class="table table-hover table-condensed table-bordered table-striped">
				<tr>
					<th>练习名称</th>
					<th>练习范围（作业 - 知识点）</th>
					<th>状态</th>
					<th>成绩</th>
					<th>耗时</th>
					<th>完成时间<div class="glyphicon glyphicon-arrow-down"/></th>
				</tr>
				<g:each in="${quizInstanceList }" status="i" var="q">
					<tr>
						<td><g:link controller="quiz" action="show" id="${q.id }">
								${q.name }
							</g:link></td>
						<td>
							<div data-toggle="tooltip" data-placement="right"
								title="${q.assignment.templates*.knowledgePoints }">
								<g:link controller="assignmentStatus" action="show" id="${ots.AssignmentStatus.findByStudentAndAssignment(q.student, q.assignment)?.id}">${q.assignment}</g:link> focus: ${q?.toBeFocusedKnowledge} 
							</div> <!-- use q.assignment.templates*.knowledgePoints[0] instead ?-->
						</td>
						<td>
							${q.status}
						</td>
						<td>
							${q.score.intValue()}
						</td>
						<td>
							${q.timeTaken}
						</td>
						<td><g:formatDate format="yyyy-MM-dd HH:mm" timeZone="Asia/Shanghai" 
								date="${q.answeredDate}" /></td>
					</tr>
				</g:each>
			</table>
			 <%--<div class="pagination"><g:paginate total="${quizInstanceTotal}" id="${session.user.id }"/></div>
		--%></g:if>
		
		<g:if test="${tabActive == 'wlist' }">
			 	<div id="loading"><br>&nbsp;&nbsp;正在加载中，请稍等片刻 ...</div>
			 
				 <g:formRemote name="weak_kcp_form" url="[action:'show_weak_kcp', id:"${studentInstance?.id}"]" update="weak_kcp" onFailure="alert('failed to show chart')"
					onLoaded="hiddenLoading()">						
				</g:formRemote>
										 
				<div id="weak_kcp"></div>						
		</g:if>
					
	</div>
</body>
</html>
