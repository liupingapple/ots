<zing:chart type="bar" width="900" height="450" container="qsCol" data="${overallColData}"
	xLabels="${['掌握度']}" effect="1" />
<p>
<table class="table table-bordered table-hover">
	<tr>
		<th>知识点</th>
		<th>掌握度</th>
	</tr>
	<g:each in="${overallColData }" status="i" var="kpval">
		<tr>
			<td>
				${kpval.key }
			</td>
			<td>
				${kpval.value[0] }
			</td>
		</tr>
	</g:each>
</table>