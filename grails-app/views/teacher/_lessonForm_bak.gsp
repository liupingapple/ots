<%@ page import="ots.Lesson"%>

<g:hiddenField name="lessonId" value="${les?.id }" />
<div class="form-group">
	<label>课程名称</label> <input type="text" class="form-control" name="className"
		value="${les?.name}">
</div>
<div class="form-group">
	<label>开始日期</label>
	<g:datePicker name="beginDate" precision="day" value="${les?.beginDate}" default="${new Date() }"
		noSelection="['': '']" />
	&nbsp;&nbsp;
	<label>结束日期</label>
	<g:datePicker name="endDate" precision="day"
		value="${les?.beginDate}" default="${new Date() }"
		noSelection="['': '']" />
</div>
<div class="form-group">
	<label>上课地点</label> <input type="text" class="form-control" name="classroom"
		value="${les?.classroom}">
</div>
<div class="form-group">
	<label>Assignment</label> <input type="text" class="form-control" name="classroom"
		value="${les?.classroom}"> TBD
</div>
<div class="form-group">
	<label>讲义</label> <input type="text" class="form-control" name="lectureNote"
		value="${les?.lectureNote}">
</div>

<div class="form-group">
	<label>班级</label>
	[TBD]
</div>


<%--<center>
	<div class="form-group">
		<div class="row">
			<div class="col-md-5">
				<label>学生</label>
				<g:select name="selected_students_lesson" from="${les?.students*.id}" multiple="multiple"
					optionKey="selected_students_lesson" size="20" class="many-to-many form-control" />
			</div>
			<div class="col-md-7">
				<label>知识点</label>
				<g:select name="selected_kps_lesson"
					from="${ots.KnowledgePoint.list(max: 100, sort: 'totalQuestion', order: 'desc')}" multiple="multiple"
					optionKey="id" size="15" value="${les?.knowledgePoints*.id}" class="many-to-many form-control" />
			</div>
		</div>
	</div>
</center>
<script type="text/javascript"> 
//提交按扭获取左右的options所有值传给后台处理
function sel4lesson(){		  
	var obj = document.getElementById('selected_students_lesson');
     
    for(var i=0;i<obj.options.length;i++)
    {
    	obj.options[i].selected = true;
    }

    var obj2 = document.getElementById('selected_kps_lesson');
    
    for(var i=0;i<obj2.options.length;i++)
    {
    	obj2.options[i].selected = true;
    }	
}
</script>--%>