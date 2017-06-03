<%@ page import="ots.TClass"%>

<g:hiddenField name="tclassId" value="${TClassInstance?.id }" />
<div class="form-group">
	<label>班级名称</label> <input type="text" class="form-control" name="className"
		value="${TClassInstance?.className}">
</div>
<div class="form-group">
	<label>教学计划</label> <input type="text" class="form-control" name="teachingPlan"
		value="${TClassInstance?.teachingPlan}">
</div>

<label>班级学生</label>
<center>
	<div class="form-group">
		<div class="row">
			<div class="col-md-1">&nbsp;</div>
			<div class="col-md-4">
				<g:select name="available_students" from="${availableStudents}" multiple="multiple" optionKey="id"
					optionValue="fullName" size="20" value="${TClassInstance?.students*.id}" class="many-to-many form-control" />
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
				<g:select name="selected_students" from="${selectedStudents}" multiple="multiple"
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
	　　　　　　　　　　　alert("请选择要添加到班级的学生！");  
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
	　　　　　　　　　　　　　　　alert("请选择要移除班级的学生！");  
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