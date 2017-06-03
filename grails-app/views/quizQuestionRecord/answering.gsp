<!DOCTYPE html>
<%@page import="ots.Question"%>
<%@page import="ots.WeiXinUtil"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'quizQuestionRecord.label', default: 'QuizQuestionRecord')}" />
<title><g:if test="${WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_KCDB }">课程导报英语测试题</g:if></title>

</head>

<body>
	<div class="container bs-docs-container">
	  <g:form id="questionAnswerForm" name="questionAnswerForm" method="POST">
		<div class="row">
			
			<div class="col-md-9" role="main">
			
				<g:if test="${flash.message}">
					<div class="message" role="status">
						${flash.message}
					</div>
				</g:if>

					<div class="bs-docs-section">
						<div>
							<!-- no class="page-header" here-->
							
							<%-- No need the assiNameShow
							<h3 id="answer_question">
								<g:if test="${WeiXinUtil.WeiXinName != WeiXinUtil.WeiXinName_KCDB }">
									${quiz }
								</g:if>
								<g:else>
									<%
										String assiNameShow = quiz?.assignment?.name
										if (assiNameShow.startsWith("sg")) {
											String regex = "sg[\\d]*.[\\d]*";
											java.util.regex.Pattern pat = java.util.regex.Pattern.compile(regex);
											java.util.regex.Matcher matcher = pat.matcher(assiNameShow);
											if (matcher.find()) {
											  assiNameShow = assiNameShow.substring(matcher.end());
											}
										}
									 %>
									${assiNameShow }
								</g:else>
							</h3>
						--%></div>

						<g:hiddenField name="quiz_id" value="${params.quiz_id}" />
						<g:hiddenField name="max" value="${params.max}" />
						<g:hiddenField name="offset" value="${params.offset}" />
						
						<g:hiddenField name="openId" value="${openId}" />
						<g:hiddenField name="kpList" value="${kpList}" />
						<g:hiddenField name="sourceq" value="${sourceq}" />

						<g:each in="${qrMap}" var="qrEntry">
							<!-- parent question 如果是阅读理解题，则为阅读理解题目题干 ，如果非阅读理解题 qrEntry?.key=0-->
							<g:if test="${qrEntry?.key }">
								<g:set var="parentQ" value="${Question.get(qrEntry.key)}"></g:set>
								<table class="table table-bordered">
									<tbody>
										<tr class="success">
											<td><g:cvtq question="${parentQ}" />&nbsp;&nbsp;备注：该篇文章难度系数: ${parentQ.difficultyLevel }</td>
										</tr>
									</tbody>
								</table>
							</g:if>
							<!-- 子题目，如果非阅读理解题，题目本身 -->
							<table class="table table-hover">
								<tbody>
									<g:each in="${qrEntry.value}" status="i" var="quizQuestionRecordInstance">										
										<g:hiddenField name="qqr" value="${quizQuestionRecordInstance.id}" />
										<!-- qqr will be a list, value quizQuestionRecordInstance.id will be added into it at each loop -->
										<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
											<td>
												${i+1}
											</td>
											<td><g:cvtq quizQuestionRecord="${quizQuestionRecordInstance}" /></td>
										</tr>
									</g:each>
								</tbody>
							</table>
						</g:each>

						<%-- disable g:paginate now
					    <g:paginate total="${quizQuestionRecordInstanceTotal}" params="['quiz_id':"${params.quiz_id}",'timeCounter':"${params.timeCounter }"]" />
					     --%>
					</div>
				</div>
			
			<div class="col-md-3">
				<div class="bs-sidebar hidden-print" role="complementary">
					<ul class="nav">&nbsp;</ul>
					
					<ul class="nav">
					  <li>
							<div class="panel panel-warning">
								<div class="panel-heading"> 答题用时(秒):
								      <input type="text" class="text-danger" style="border:0px; background-color:transparent;"  size="3" readonly="readonly" id="timeCounter" name="timeCounter" value="${params.timeCounter}">
								      <%--<button class="btn btn-sm btn-warning glyphicon glyphicon-pause" type="button" onClick="pause()" id="pauseBtn"><div class="glyphicon glyphicon-play"></div></button>--%>
								</div>
								<div class="panel-body text-center">
								  <div class="btn-group btn-group-md">
								  	<%--<g:actionSubmit class="btn btn-primary" action='saveRecords' value="保存"/>--%>
								  	<g:actionSubmit class="btn btn-primary" action='answered' value="交卷"/>
								  	<%--<g:actionSubmit class="btn btn-primary" action='resetAnswer' value="重做"/>--%>
								  	</div>
								</div>
								
								<g:if test="${session.user.userName.startsWith("st")}"> 
									<div class="panel-body">
									  <div class="btn-group btn-group-md">
									  <!-- please note, if use below buttons, the params openId, kpList and sourceq will not pass to action -->
										<g:link class="btn btn-warning" action="autoFillIn_4_Test" params="['quiz_id':"${params.quiz_id}"]">80% Correct</g:link>
										<g:link class="btn btn-warning" action="autoFillIn_4_Test" params="['all_correct':'true','quiz_id':"${params.quiz_id}"]">100% Correct</g:link>								  
									  </div>
									</div>
								</g:if>
																
							</div>
						</li>
						
						<li>
							<g:if test="${selectQuestionsMethod}">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title">
											<!-- 出题备注 -->
										</h3>
									</div>
									<div class="panel-body">
										${selectQuestionsMethod }
									</div>
								</div>
							</g:if>
						</li>
					</ul>
					
				</div>
				
			</div>
		</div> 
       </g:form>
	</div>
	
	<script type="text/javascript">
	$(function() {
		$('form').submit(function(){
			$('input[type=submit]', this).val("处理中...").attr('disabled', 'disabled');
		});
	});

	// 计时器
	var c = 0
	var t
	var p = 0
	function timedCount() {
		document.getElementById('timeCounter').value = c
		c = c + 1
		isCounting = 1
		if (p % 2 == 0) {
			t = setTimeout("timedCount()", 1000)
			// setTimeout("document.getElementById('questionAnswerForm').submit()",3000); // no auto sbmit
		}
	}

	function pause() {
		p = p + 1
		timedCount()
	}

	function stopCount() {
		c = 0;
		setTimeout("document.getElementById('timeCounter').value=0", 0);
		clearTimeout(t);
		timedCount();
	}

	// 刷新滚动条位置不变
	/*window.onbeforeunload = function() {
		var scrollPos;
		if (typeof window.pageYOffset != 'undefined') {
			scrollPos = window.pageYOffset;
		} else if (typeof document.compatMode != 'undefined'
				&& document.compatMode != 'BackCompat') {
			scrollPos = document.documentElement.scrollTop;
		} else if (typeof document.body != 'undefined') {
			scrollPos = document.body.scrollTop;
		}
		document.cookie = "scrollTop=" + scrollPos; // 存储滚动条位置到cookies中

	}*/
	
	window.onload = function() {
		// if (document.cookie.match(/scrollTop=([^;]+)(;|$)/) != null) {
		// 	var arr = document.cookie.match(/scrollTop=([^;]+)(;|$)/); // cookies中不为空，则读取滚动条位置
		// 	document.documentElement.scrollTop = parseInt(arr[1]);
		// 	document.body.scrollTop = parseInt(arr[1]);
		// }
		
		if (document.getElementById('timeCounter').value != null
				&& document.getElementById('timeCounter').value != '') {
			c = parseInt(document.getElementById('timeCounter').value);
			p = 0;
		}
		timedCount();
	}
	
	</script>
</body>
</html>