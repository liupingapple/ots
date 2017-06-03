<%@page import="ots.School"%>
<%@page import="ots.CONSTANT"%>
<%@ page import="ots.AssignmentTemplate"%>

<div class="fieldcontain ${hasErrors(bean: assignmentTemplateInstance, field: 'knowledgePoints', 'error')} ">
	<label for="knowledgePoints"> <g:message code="assignmentTemplate.knowledgePointsFilter.label"
			default="Knowledge Points Filter" />

	</label>
	<g:textField name="kpFilter" value=""
		onchange="${remoteFunction(controller:'assignmentTemplate', action:'filterKnowledge',update:'knowledgePointSelection', params:'\'nameFilter=\' + this.value' )}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: assignmentTemplateInstance, field: 'templateName', 'error')} required">
	<label for="templateName"> <g:message code="assignmentTemplate.templateName.label"
			default="Template Name" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="templateName" required="" value="${assignmentTemplateInstance?.templateName}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: assignmentTemplateInstance, field: 'subject', 'error')} required">
	<label for="subject"> <g:message code="assignmentTemplate.subject.label"
			default="课程" /> <span class="required-indicator">*</span>
	</label>
	<g:select name="subject" required="" from="${CONSTANT.COURSE_LIST}" value="${assignmentTemplateInstance?.subject}" valueMessagePrefix="assignmentTemplate.subject" noSelection="['': '']"/>
</div>

<div
	class="fieldcontain ${hasErrors(bean: assignmentTemplateInstance, field: 'school', 'error')}">
	<label for="school"> <g:message code="assignmentTemplate.school.label"
			default="机构" />
	</label>
	<g:select name="school.id" from="${School.list()}" optionKey="id" optionValue="name" value="${assignmentTemplateInstance?.school?.id}" valueMessagePrefix="assignmentTemplate.subject" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: assignmentTemplateInstance, field: 'createdBy', 'error')} ">
	<label for="createdBy"> <g:message code="assignmentTemplate.createdBy.label" default="Created By" />

	</label>
	<g:textField name="createdBy" value="${assignmentTemplateInstance?.createdBy}" />
</div>

<div class="row">
	<div class="col-md-5">
		<g:render template="kp_list" />
	</div>

	<div class="col-md-2 fieldcontain">
		<label> Operations </label>
		 &nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="add">Add</a> &nbsp;&nbsp;&nbsp;&nbsp; <a href="#" id="delete">Delete</a>
	</div>
	
	<div class="col-md-5 fieldcontain">
		<label> Selected knowledgePoints </label>
		<g:select style="width:260pt" id="selected_kps" name="knowledgePoints" from="${assignmentTemplateInstance?.knowledgePoints}" multiple="multiple" optionKey="id"
			optionValue="name" size="6" value="${assignmentTemplateInstance?.knowledgePoints*.id}" class="many-to-many form-control" />
	</div>
</div>

<script type="text/javascript"> 

	$(function(){  
	　　$("#add").click(function(){  
	　　　　　　　if($("#knowledgePointSelection option:selected").length>0)  
	　　　　　　　{  
	　　　　　　　　　　　$("#knowledgePointSelection option:selected").each(function(){  
	　　　　　　　　　　　　　　$("#selected_kps").append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option"); 
	　　　　　　　　　　　}) 
	       	
	　　　　　　　}  
	　　　　　　　else 
	　　　　　　　{  
	　　　　　　　　　　　alert("没有选中任何知识点，请选择知识点！");  
	　　　　　　　} 

	　　　}) 
	})  
	
	$(function(){  
	　　　　　　$("#delete").click(function(){  
	　　　　　　　　　　　if($("#selected_kps option:selected").length>0)  
	　　　　　　　　　　　{						
	　　　　　　　　　　　　　　　$("#selected_kps option:selected").each(function(){ 
	　　　　　　　　　　　　　　　　　　　$(this).remove();　  
	　　　　　　　　　　　　　　　})  
	　　　　　　　　　　　}  
	　　　　　　　　　　　else 
	　　　　　　　　　　　{  
	　　　　　　　　　　　　　　　alert("请选择要移除知识点！");  
	　　　　　　　　　　　}

	　　　　　}) 
	})
	
	//提交前选中所有selected_kps的可选项
	function sel(){		  
		 var obj = document.getElementById('selected_kps');
		     
		     for(var i=0;i<obj.options.length;i++)
		     {
		    	 obj.options[i].selected = true;
		     }	
	}
		
</script>