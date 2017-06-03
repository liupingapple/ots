<%@page import="ots.KnowledgeCheckPoint"%>
<%@page import="ots.StuAnswer"%>
<h4>
	<div class="label label-default">练习 ${KPCList4Table[0]?.quiz.name } 各知识点成绩 (总数: ${KPCList4Table.size()}, 完成时间：<g:formatDate
			type="datetime" timeZone="Asia/Shanghai" style="SHORT" date="${KPCList4Table[0]?.quiz.answeredDate}" />)
	</div>
</h4>
<g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
</g:if>
<table class="table table-hover table-condensed table-bordered table-striped">
	<tr>
		<th><span class="property-label">知识点</span></th>
		<th><span class="property-label">本次题数</span></th>
		<th><span class="property-label">本次答对</span></th>
		<th><span class="property-label">累积题数</span></th>
		<%--<th><span class="property-label">近期答对</span></th>--%>
		<th><span class="property-label">正确率<div class="glyphicon glyphicon-arrow-down"/></span></th>
		<th><span class="property-label">错题编号</span></th>
		<th><span class="property-label">是否强化</span></th>

		<%--<th><span class="property-label">综合题数</span></th>
				<th><span class="property-label">综合答对</span></th>
				<th><span class="property-label">累计题数</span></th>
				<th><span class="property-label">累计答对</span></th>
				<th><span class="property-label">掌握度</span></th>
				<th><span class="property-label">练习日期</span></th>
		--%>
	</tr>
	<g:each in="${KPCList4Table}" var="kcp">
		<tr>
			<td>
				${kcp.knowledge}
			</td>
			<td>
				${kcp.totalQuestions}
			</td>
			<td>
				${kcp.correctQuestions}
			</td>
			<td>
				${kcp.totalSum} <!-- recentTotal -->
			</td>
			<%--<td>
				${kcp.recentCorrect}
			</td>--%>
			<td>
				${kcp.recentCorrectRate < 0?"-":(kcp.recentCorrectRate<0.95?"":"<font color='green'>")} <g:if
					test="${kcp.recentCorrectRate>=0}">
					<g:formatNumber number="${kcp.recentCorrectRate*100}" type="number" maxFractionDigits="0"
						roundingMode="HALF_DOWN" />
				</g:if> ${kcp.recentCorrectRate<0.95?"":"<font color='green'>"}
			</td>
			
			<td><g:each in="${failedRecords}" var="incorrectRecord" status="i">
			
				<g:if test="${incorrectRecord.quiz == kcp.quiz && incorrectRecord.question.knowledgePoints*.id.contains(kcp.knowledge.id) }">
					<a data-toggle="modal" data-target="#A_incorrectQustionDetail${i}" href=""> ${incorrectRecord.question.id }
					</a>
					<g:form name="dummyForm">
					<!-- Modal -->
					<div class="modal fade" id="A_incorrectQustionDetail${i}" tabindex="-1" role="dialog"
						aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
									<h3 class="modal-title" id="myModalLabel">
										${incorrectRecord.quiz.name }
										的错题
									</h3>
									完成时间：${incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd HH:mm:ss') }
								</div>
								<div class="modal-body">
									<g:cvtq question="${incorrectRecord.question }" quizQuestionRecord="${incorrectRecord }"
										showChecks="true" showKP="true" />
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
								</div>
							</div>
							<!-- /.modal-content -->
						</div>
						<!-- /.modal-dialog -->
					</div>
					<!-- /.modal -->
					</g:form>

				</g:if>
			</g:each></td>
			<td>
				<g:if test="${kcp.isFocused}">
					<font color='red'>是</font>
				</g:if>
			</td>
			<%--  
			<td>
				${kcp.familyTotal}
			</td>
			<td>
				${kcp.familyCorrect}
			</td>
			<td>
				${kcp.familyRecentTotal}
			</td>
			<td>
				${kcp.familyRecentCorrect}
			</td>
			<td>
				${kcp.familyRecentCorrectRate < 0?"-":(kcp.familyRecentCorrectRate<0.95?"":"<font color='green'>")}
				<g:if test="${kcp.familyRecentCorrectRate>=0}">
					<g:formatNumber number="${kcp.familyRecentCorrectRate*100}" type="number" maxFractionDigits="0"
						roundingMode="HALF_DOWN" />
				</g:if> ${kcp.familyRecentCorrectRate<0.95?"":"<font color='green'>"}
			</td>
			<td><g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT"
					date="${kcp.dateCreated}" /></td>
			--%>
		</tr>
	</g:each>
</table>