<%@page import="ots.AssignmentStatus"%>
<%@page import="ots.Student"%>

<g:hiddenField name="teacherId" value="${teacherInstance?.id }" />
<g:hiddenField name="asmtId" value="${asmt?.id }" />
<% 
	Set<Student> existing_students = AssignmentStatus.findAllByAssignment(asmt)*.student
	println existing_students
	Set<Student> available_students = teacherInstance.students - existing_students
	println available_students
%>
<center>
	<div class="form-group">
		<div class="row">
			<div class="col-md-1">&nbsp;</div>
			<div class="col-md-4">
				<g:select name="available_students" from="${available_students}" multiple="multiple" optionKey="id"
					optionValue="fullName" size="20" value="${available_students*.id}" class="many-to-many form-control" />
			</div>
			<div class="col-md-2">
				<button id="add" class="btn btn-default btn-lg" type="button">
					<span class="glyphicon glyphicon-circle-arrow-right"></span>
				</button>
				<p></p>
				<p></p>

				<button id="delete" class="btn btn-default btn-lg" type="button">
					<span class="glyphicon glyphicon-circle-arrow-left"></span>
				</button>
			</div>
			<div class="col-md-4">
				<g:select name="selected_students" from="${existing_students}" multiple="multiple"
					optionKey="selected_students" size="20" class="many-to-many form-control" />
			</div>
			<div class="col-md-1">&nbsp;</div>
		</div>
	</div>
</center>
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