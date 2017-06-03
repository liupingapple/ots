<!-- Modal -->
<!-- removed class="modal fade"  -->
<g:formRemote name="kcp_qs_form3" url="[action:'show_kcp_qs_col', id:"${quizInstance?.id}"]" update="kcp_qs_col" onFailure="alert('failed to show chart')">
<g:hiddenField name="qs_selection" value="${qs_selection }"/>
<div id="KPSelector" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<!-- replace data-dismiss="modal" with onClick -->
				<button type="button" class="close" onClick="jQuery('#KPSelector').modal('toggle');jQuery('#KPSelector').modal('toggle');" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">选择知识点 - ${qs_selection }</h4>
			</div>
			<div class="modal-body text-center">	
				<div class="alert alert-success">知识点已按正确率从低到高排序</div>	
				<span>
					<g:select name="selectedKPs" required="" from="${KPs}" multiple="multiple"
					optionKey="id" size="12" value="${KPs*.id}" class="many-to-many" />  <%-- if select all, use: value="${KPs*.id}"  --%>
				</span>	
			</div>
			<g:hiddenField name="quiz_number" value="${quiz_number}"/> <!-- value init in kcp_qs_form1 -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onClick="jQuery('#kcp_qs_form3').submit();jQuery('#KPSelector').modal('toggle');jQuery('#KPSelector').modal('toggle');">确定</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
</g:formRemote>

