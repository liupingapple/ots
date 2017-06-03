<%@page import="ots.AssignmentStatus"%>
<%@page import="ots.Assignment"%>
<%@page import="ots.Student"%>

<%--<%@page import="org.grails.plugins.google.visualization.data.Cell; org.grails.plugins.google.visualization.util.DateUtil"%>
--%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="layout" content="bmain">
<title>Teacher</title>
<%--<gvisualization:apiImport />--%>
</head>
<body>
	<div class="container bs-docs-container">
		<div class="well well-sm">
			分配作业：${asmt }
		</div>		
		<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
		</g:if>
		<g:form name="assignToStuForm" action="assignToStuSave">
			<g:hiddenField name="teacherId" value="${teacherId }" />
			<g:hiddenField name="asmtId" value="${asmt.id }" />
			<g:hiddenField name="fromController" value="${fromController }" />
			<div class="row">
				<div class="col-md-2">&nbsp;</div>
				<div class="col-md-3"><center><label class="glyphicon glyphicon-user">&nbsp;未分配学生</label></center>
					<g:select name="available_students" from="${available_students}" multiple="multiple" optionKey="id"
						optionValue="fullName" size="6" value="${available_students*.id}" class="many-to-many form-control" />
				</div>
				<div class="col-md-1">
					<p><br/></p>
					<button id="add" class="btn btn-default btn-lg" type="button">
						<span class="glyphicon glyphicon-circle-arrow-right"></span>
					</button>
					<%--
					<button id="delete" class="btn btn-default btn-lg" type="button">
						<span class="glyphicon glyphicon-circle-arrow-left"></span>
					</button>
					 --%>
				</div>
				<div class="col-md-3"><center><label class="glyphicon glyphicon-user">&nbsp;已分配学生</label></center>
					<g:select name="selected_students" from="${existing_students}" multiple="multiple" optionKey="id"
						optionValue="fullName" size="6" class="many-to-many form-control" />
				</div>
				<div class="col-md-3">
					<button type="button" class="btn btn-primary" onClick="sel();assignToStuForm.submit();">保存</button>
					<g:if test="${ fromController == 'assignment' }">
						<g:link class="btn btn-primary" action="show" id="${asmt.id }">返回</g:link>
					</g:if>
					<g:elseif test="${ fromController == 'teacher' }">
						<g:link class="btn btn-primary" controller="teacher" action="thome" id="${teacherId }">返回</g:link>
					</g:elseif>
				</div>
			</div>
		</g:form>
		<%--
		<g:if test="${toShowKnowledgeGraph}">
		<div class="well well-sm"> 作业${Assignment.get(asmtId) } 包含的知识点</div>
		<gvisualization:orgChart elementId="kcpOrg" allowCollapse="${true}" allowHtml="${true}"
				columns="${orgColumns}" data="${orgData}" />
		<div id="kcpOrg"></div>
		</g:if>
		--%>
		<%--
		<g:if test="${toShowKnowledgeGraph}">
		<div class="well well-sm"> 作业${asmt } 包含的知识点</div>
		
			<g:each in="${asmt.templates*.knowledgePoints }" var="kp" status="i">
				<b>${i+1}:</b> ${kp }<br>
				<g:each in="${kp.childPoints}" var="ckp" status="j">
					|---<b>${i+1}.${j+1}</b> : ${ckp }<br>
				</g:each>
				<br>
			</g:each>
		</g:if>
		--%>
	</div>

	<script type="text/javascript"> 

	$(function(){  
	　　$("#add").click(function(){  
	　　　　　　　if($("#available_students option:selected").length>0)  
	　　　　　　　{  
	　　　　　　　　　　　$("#available_students option:selected").each(function(){  
	　　　　　　　　　　　　　　$("#selected_students").append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option"); 
	　　　　　　　　　　　　　　$(this).remove();　  
	　　　　　　　　　　　})  
	　　　　　　　}  
	　　　　　　　else 
	　　　　　　　{  
	　　　　　　　　　　　alert("请选择要添加到Assignement的学生！");  
	　　　　　　　}  
	　　　})  
	})  
	$(function(){  
	　　　　　　$("#delete").click(function(){  
	　　　　　　　　　　　if($("#selected_students option:selected").length>0)  
	　　　　　　　　　　　{
						if (!confirm("如果学生已经做了该作业的练习，您的操作将会删除学生已经做了该作业的数据(AssignmentStatus 数据)，确定这么做吗？") ) {
							return
						} 
	　　　　　　　　　　　　　　　$("#selected_students option:selected").each(function(){  
	　　　　　　　　　　　　　　　　　　　　　$("#available_students").append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option"); 
	　　　　　　　　　　　　　　　　　　　　　$(this).remove();　  
	　　　　　　　　　　　　　　　})  
	　　　　　　　　　　　}  
	　　　　　　　　　　　else 
	　　　　　　　　　　　{  
	　　　　　　　　　　　　　　　alert("请选择要移除Assignement的学生！");  
	　　　　　　　　　　　}  
	　　　　　})  
	}) 
	
	//提交按扭获取左右的options所有值传给后台处理
	function sel(){		  
	 var obj = document.getElementById('selected_students');
	     
	     for(var i=0;i<obj.options.length;i++)
	     {
	    	 obj.options[i].selected = true;
	     }	
	}
</script>
</body>
</html>