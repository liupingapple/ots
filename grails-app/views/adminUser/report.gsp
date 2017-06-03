<%@ page import="ots.AdminUser" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'adminUser.label', default: 'AdminUser')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-adminUser" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="list" action="admin"><g:message code="default.home.label"/></g:link></li>
			</ul>
		</div>
		
		<div id="show-adminUser" class="content scaffold-show" role="main">
			
			<h1>管理员查看报表</h1>
			
			<ol class="property-list adminUser">
				<li class="fieldcontain">
					<g:link controller="assignmentStatus" action="list4report" id="${session?.user?.id }">学生作业情况报表 </g:link>
				</li>
				
				<li class="fieldcontain">
					<g:link url="#" onclick="togErrQList()">错题集</g:link>
				</li>
								
				<li id="errQList" class="fieldcontain" style="display:none">
					<g:form controller="quizQuestionRecord" action="report4XRecord" id="${session?.user?.id }">
						输入过滤知识点：
						<g:textField name="kpFilter" value=""
						onchange="${remoteFunction(controller:'adminUser', action:'filterKnowledge',update:'knowledgePointSelection', params:'\'nameFilter=\' + this.value' )}" />
						
						<%-- 
						<g:select style="width:310pt" name="selected_kps" from="${hasWrongAnsweredQuestKPs}" multiple="multiple" optionKey="id"
								 size="6" value="${hasWrongAnsweredQuestKPs[0]?.id }" class="many-to-many form-control" />  <!-- removed optionValue="name", because we want to see TQ/AQ -->
						--%>
						
						<g:render template="kp_list"  />
						
						<g:submitButton name="确定"/>
					</g:form>
				</li>
			</ol>
		</div>
		<script type="text/javascript">
			function togErrQList() {
				$("#errQList").toggle();
			}
		</script>
	</body>
</html>
