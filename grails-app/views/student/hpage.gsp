<!DOCTYPE html>
<%@page import="ots.CONSTANT"%>
<%@page import="ots.Quiz"%>
<%@page import="ots.Student"%>
<%@page import="ots.AssignmentStatus"%>
<html>
<head>
<meta name="layout" content="bmain">
<title>学生首页</title>
</head>
<body>
	<div class="container bs-docs-container">
		<p>
		<div class="row demo-tiles">
			<table class="table table-bordered login_content">
				<g:if test="${!hpage_x || hpage_x == '训练' }">
				<tr>
					<td>
						<div class="col-xs-3">
							<div class="text-center">
								<p>&nbsp;</p>
								<g:img dir="images/icons/png/" file="Map.png" class="tile-image big-illustration" alt="智能练习" />
								<h1>训练</h1>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="compas.svg" class="tile-image big-illustration"
									alt="英语词汇与语法" />
								<h3 class="tile-title">英语词汇与语法<small><br>基于知识点的练习</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_xunlian" id="${session.user.id }" params="${[course:CONSTANT.COURSE_ENG1] }">进入</g:link>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="loop.svg" class="tile-image" alt="Infinity-Loop" />
								<h3 class="tile-title">英语专项提高<small><br>基于难度系数的练习</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_xunlian" id="${session.user.id }" params="${[course:CONSTANT.COURSE_ENG2] }">进入(敬请期待)</g:link>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="pencils.svg" class="tile-image" alt="Pensils" />
								<h3 class="tile-title">文言文<small><br>&nbsp;</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_xunlian" id="${session.user.id }" params="${[course:CONSTANT.COURSE_WYW] }">进入</g:link>
							</div>
						</div>

					</td>
				</tr>
				</g:if>
				<!--  -->
				
				<g:if test="${hpage_x == '查看' }">
				<tr>
					<td>
						<div class="col-xs-3">
							<div class="text-center">
								<p>&nbsp;</p>
								<g:img dir="images/icons/png/" file="Clipboard.png" class="tile-image big-illustration" alt="知识与能力评估" />
								<h1>查看</h1>
							</div>
						</div>
						
						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="book.svg" class="tile-image big-illustration"/>
								<h3 class="tile-title">英语词汇与语法<small><br>基于知识点的练习</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_chakan" id="${session.user.id }" params="${[course:CONSTANT.COURSE_ENG1] }">进入</g:link>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="paper-bag.svg" class="tile-image" alt="Infinity-Loop" />
								<h3 class="tile-title">英语专项提高<small><br>基于难度系数的练习</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_chakan" id="${session.user.id }" params="${[course:CONSTANT.COURSE_ENG2] }">进入(敬请期待……)</g:link>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="retina.svg" class="tile-image"/>
								<h3 class="tile-title">文言文<small><br>&nbsp;</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_chakan" id="${session.user.id }" params="${[course:CONSTANT.COURSE_WYW] }">进入</g:link>
							</div>
						</div>

					</td>
				</tr>
				</g:if>
				
				<g:if test="${hpage_x == '沟通' }">
				<tr>
					<td>
						<div class="col-xs-3">
							<div class="text-center">
								<p>&nbsp;</p>
								<g:img dir="images/icons/png/" file="Chat.png" class="tile-image big-illustration" alt="我的私人老师" />
								<h1>沟通</h1>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/png/" file="Gift-Box.png" class="tile-image big-illustration"
									alt="Compas" />
								<h3 class="tile-title">英语词汇与语法<small><br>基于知识点的练习</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_goutong" id="${session.user.id }" params="${[course:CONSTANT.COURSE_ENG1] }">进入</g:link>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/png/" file="Mail.png" class="tile-image" alt="Infinity-Loop" />
								<h3 class="tile-title">英语专项提高<small><br>基于难度系数的练习</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_goutong" id="${session.user.id }" params="${[course:CONSTANT.COURSE_ENG2] }">进入</g:link>
							</div>
						</div>

						<div class="col-xs-3">
							<div class="tile">
								<g:img dir="images/icons/svg/" file="retina.svg" class="tile-image"/>
								<h3 class="tile-title">文言文<small><br>&nbsp;</small></h3>
								<g:link class="btn btn-primary btn-large btn-block" action="pcourse_goutong" id="${session.user.id }" params="${[course:CONSTANT.COURSE_WYW] }">进入</g:link>
							</div>
						</div>

					</td>
				</tr>
				</g:if>
								
			</table>
		</div>
	</div>
	<!-- /tiles -->
</body>
</html>