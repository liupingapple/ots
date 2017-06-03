<%@page import="ots.KnowledgeCheckPoint"%>
<%@page import="ots.StuAnswer"%>

<h4>
	<div class="label label-default">各知识点历次练习成绩 (总数: ${overallKCPList?.size()})
	</div>
</h4>
<g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
</g:if>
<table class="table table-hover table-condensed table-bordered table-striped">
	<tr>
		<th><span class="property-label">知识点</span></th>
		<th><span class="property-label">累积题数<div data-toggle='tooltip' data-placement='right' title="[本节点题数/含子节点题数]" class="glyphicon glyphicon-hand-up"></div></span></th>
		<th><span class="property-label">正确率<div class="glyphicon glyphicon-arrow-down"/></span></th>
		<th><div data-toggle="tooltip" data-placement="right" title="题目编号 | 练习编号。如有多个错题，则按练习时间降序排列"><span class="property-label">错题编号?</span></div></th>
		<th><span class="property-label">是否强化</span></th>
	</tr>
	<g:each in="${overallKCPList}" var="kcp">
		<tr>
			<td>
				${kcp.knowledge}
			</td>
			<td>
				${kcp.totalSum}/${kcp.familyTotalSum }
			</td>
			<td>
				${kcp.recentCorrectRate < 0?"-":(kcp.recentCorrectRate<0.95?"":"<font color='green'>")} <g:if
					test="${kcp.recentCorrectRate>=0}">
					<g:formatNumber number="${kcp.recentCorrectRate*100}" type="number" maxFractionDigits="0"
						roundingMode="HALF_DOWN" />
				</g:if> ${kcp.recentCorrectRate<0.95?"":"<font color='green'>"}
			</td>

			<g:set var="stuFR" value="${failedRecords.sort{a,b -> b.quiz.id - a.quiz.id} }"></g:set>
			<td><g:each in="${stuFR}" var="incorrectRecord" status="i">
					<g:if test="${incorrectRecord.question.knowledgePoints*.id.contains(kcp.knowledge.id) && incorrectRecord.quiz.id <= kcp.quiz.id}"> 
					<!-- if recorrectedRecords used in the future, should check if the question is in recorrectedRecords or not -->
						<a data-toggle="modal" data-target="#incorrectQustionDetail${i}" href=""> <div data-toggle="tooltip" data-placement="right" title="${incorrectRecord.quiz.name} (${incorrectRecord.quiz.answeredDate.format('yyyy-MM-dd') })">${incorrectRecord.question.id } | ${incorrectRecord.quiz.id }</div>
						</a>
						<g:form name="A_dummyForm">
						<!-- Modal -->
						<div class="modal fade" id="incorrectQustionDetail${i}" tabindex="-1" role="dialog"
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