disabled. show the line chart in _kcp_qs_col.gsp together
<%--<div id="kcpLines"></div>

<gvisualization:lineCoreChart dynamicLoading="${true}" elementId="kcpLines" width="${1000}" height="${340}"
 hAxis="${new Expando([title: '练习序号', viewWindowMode: 'pretty'])}"
	vAxis="${new Expando([title: '掌握度', viewWindowMode: 'pretty'])}" 
	 title="作业知识点掌握趋势" curveType="function"
	legendTextStyle="${new Expando([fontSize: 9])}" columns="${qs_col}" data="${qs_data}" />
	
	
	
<table class="table table-hover table-condensed table-bordered table-striped">
	<tr>
		<th><span class="property-label">序号</span></th>
		<th><span class="property-label">练习</span></th>
		<th><span class="property-label">练习类型</span></th>
		<th><span class="property-label">答对题数</span></th>
		<th><span class="property-label">题目总数</span></th>
		<th><span class="property-label">出题范围</span></th>
		<th><span class="property-label">分数</span></th>
		<th><span class="property-label">练习日期</span>
		<div class="glyphicon glyphicon-arrow-down" /></th>
	</tr>
	<g:each in="${qs_quizs}" status="i" var="q">
		<tr>
			<td>
				${i+1}
			</td>
			<td>
				${q.name}
			</td>
			<td>
				${q.type}
			</td>
			<td>
				${q.correctNum}
			</td>
			<td>
				${q.totalQuestionNum}
			</td>
			<td>
				${q.assignment}
			</td>
			<td>
				${q.score}
			</td>
			<td><g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT" date="${q.answeredDate}" /></td>
		</tr>
	</g:each>
</table>--%>