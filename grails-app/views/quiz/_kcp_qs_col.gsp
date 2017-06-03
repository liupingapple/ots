<i class="icon-time"></i> <small>练习与时间范围：${qs_quizs.first().name }@${qs_quizs.first()?.answeredDate?.format('yyyy-MM-dd hh:mm:ss') } &nbsp;<i class="icon-arrow-right"></i> &nbsp;${qs_quizs.last() }@${qs_quizs.last()?.answeredDate?.format('yyyy-MM-dd hh:mm:ss') }</small>

<zing:chart type="bar" width="1000" height="450" 
    container="qsCol" data="${qsData}" xLabels="${qsXlabel}"  effect="1"  /> 

<zing:chart type="line" width="1000" height="450" 
    container="qsLine" data="${qsData}" xLabels="${qsXlabel}" effect="4" />

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
</table>