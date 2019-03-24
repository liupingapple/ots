<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<%@page import="ots.WeiXinUtil"%>
<%@page import="ots.CONSTANT"%>
<%@page import="ots.Student"%>
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<title><g:layoutTitle default="高手云" /></title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- 不再使用BuildConfig.groovy 中安装的JQuery插件，因为版本太低，直接引入文件 -->
	<%--<NOUSED_r:require modules="jquery" /> 
	<NOUSED_r:layoutResources />--%>
	
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">	
	<!-- 可选的Bootstrap主题文件（可以有更好的视觉效果，比如渐变色） -->
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-theme.min.css')}" type="text/css">	
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'datepicker.css')}" type="text/css">
	
	<!-- We need to load JQuery firstly, maybe some customer JS will be defined in gsp file -->
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-1.11.3.min.js')}"></script> <!-- jquery.js 必须放在 bootstrap.js前面 -->
	<%--<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.jqprint-0.3.js')}"></script>  
	<!-- jqprint needs jquery-migrate-1.0.0.js because of uncaught typeerror Cannot read property 'opera'-->	--%>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-migrate-1.1.0.js')}"></script> 
	
	<script type="text/javascript" src="${resource(dir: 'js', file: 'bootstrap.min.js')}"></script>
	
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'ots.css')}" type="text/css">

<g:layoutHead />

</head>

<body>
<g:if test="${!( session.weixinOpenId || session.assignment?.name?.endsWith('_FOR_WX') )}">
  <header>
	<!-- Fixed navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<g:if test="${WeiXinUtil.WeiXinName != WeiXinUtil.WeiXinName_KCDB }">
					<g:link class="navbar-brand" controller="endUser" action="homepage">高手云</g:link>
				</g:if>
			</div>

			<div class="navbar-collapse collapse">
				<g:if test="${WeiXinUtil.WeiXinName != WeiXinUtil.WeiXinName_KCDB }">
				<ul class="nav navbar-nav">
					<li class="${session.paction == 'xunlian' ? 'active' : '' }">
						<g:if test="${session?.user instanceof ots.Student && !isHompage}">							
							<g:link controller="student" action="pcourse_xunlian" id="${session?.user?.id }" params="${[course : session.course? session.course: CONSTANT.COURSE_ENG1] }"><span class="glyphicon glyphicon-cloud">/</span>训练</g:link>														
						</g:if>
						<g:elseif test="${session?.user instanceof ots.Teacher }">
							<g:link controller="teacher" action="thome" id="${session?.user?.id }">主页</g:link>
						</g:elseif>	
					</li>
				</ul>
				
				<ul class="nav navbar-nav">
					<li class="${session.paction == 'chakan' ? 'active' : '' }">
						<g:if test="${session?.user instanceof ots.Student && !isHompage }">
							<g:link controller="student" action="pcourse_chakan" id="${session?.user?.id }" params="${[course : session.course? session.course: CONSTANT.COURSE_ENG1] }"><span class="glyphicon glyphicon-thumbs-up">/</span>查看</g:link>							
						</g:if>	
					</li>
				</ul>
				
				<ul class="nav navbar-nav">
					<li class="${session.paction == 'goutong' ? 'active' : '' }">
						<g:if test="${session?.user instanceof ots.Student && !isHompage }">
							<g:link controller="student" action="pcourse_goutong" id="${session?.user?.id }" params="${[course : session.course? session.course: CONSTANT.COURSE_ENG1] }"><span class="glyphicon glyphicon-user">/</span>沟通</g:link>
							<%--<g:link controller="teacher" action="show" id="${session?.user?.teacher?.id }"><span class="glyphicon glyphicon-user">
						--%></g:if>	
					</li>
				</ul>
				
				<g:if test="${session?.user instanceof ots.Student && !isHompage}">
					<ul class="nav navbar-nav">
						<li>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</li>
					</ul>
					
					<g:if test="${session.course}">
						<ul class="nav navbar-nav">
							<li class="active">
								<a data-toggle="modal" data-target="#subject" href="#"><span class="glyphicon glyphicon-book"> </span> ${session.course }</a>								
							</li>
						</ul>
					</g:if>
				</g:if>

				<g:if test="${session?.user != null && !(session?.user instanceof Student)}">
				
				  <ul class="nav navbar-nav">
			        <li class="dropdown">
			          <a href="#" class="dropdown-toggle" data-toggle="dropdown">报表 <span class="caret"></span></a>
			          <ul class="dropdown-menu" role="menu">
			            <li><g:link controller="assignmentStatus" action="list4report" id="${session?.user?.id }">学生作业情况报表</g:link></li>
			            <li><g:link controller="knowledgeCheckPoint" action="report4allStuQuiz" id="${session?.user?.id }">学生历次练习成绩汇总</g:link></li>
			            <li><g:link controller="adminUser" action="report" id="${session?.user?.id }">错题集</g:link></li>
			            <li class="disabled"><a herf="#">教师辅导信息表</a></li>
			            <li class="disabled"><a herf="#">学生训练情况表</a></li>
			            <li class="divider"></li>
			            <li class="disabled"><a herf="#">学生基本信息表</a></li>
			            <li class="disabled"><a herf="#">教师基本信息表</a></li>
			          </ul>
			        </li>
			      </ul>
				
				</g:if>	
				
				<ul class="nav navbar-nav navbar-right">
					<li><g:loginControl /></li>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
					<li><g:preference/></li>
				</ul>
				</g:if>												
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	
	<!-- About Modal -->
	<div class="modal fade" id="subject" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title" id="myModalLabel">
						请选择您要进入的课程
					</h3>
				</div>
				<div class="modal-body">
					<g:link class="btn btn-info btn-large btn-block" controller="student" action="pcourse_${session.paction? session.paction : 'xunlian'}" id="${session.user?.id }" params="${[course:CONSTANT.COURSE_ENG1] }">${CONSTANT.COURSE_ENG1 }</g:link>
					<g:link class="btn btn-info btn-large btn-block" controller="student" action="pcourse_${session.paction? session.paction : 'xunlian'}" id="${session.user?.id }" params="${[course:CONSTANT.COURSE_ENG2] }">${CONSTANT.COURSE_ENG2 }</g:link>
					<g:link class="btn btn-info btn-large btn-block" controller="student" action="pcourse_${session.paction? session.paction : 'xunlian'}" id="${session.user?.id }" params="${[course:CONSTANT.COURSE_WYW] }">${CONSTANT.COURSE_WYW }</g:link>
				</div>
				</div>
				<!-- <div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>  -->
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
		
	<!-- About Modal -->
	<div class="modal fade" id="about" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title" id="myModalLabel">
						高手云在线训练与测评系统
					</h3>
				</div>
				<div class="modal-body">
					版本：<g:message code="about.version"/>
					<br>名称：<g:message code="about.prod" />
					<br>网址：<g:message code="about.website" />
					<br>版权：<g:message code="about.copyright" />
					<br>详细：<g:message code="about.desc" />
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
   </header>
</g:if>	 <!-- end if openid for WeiXin -->	

	<g:layoutBody />	
	
	<g:javascript library="application" />
	<!-- 不再使用BuildConfig.groovy 中安装的JQuery插件，因为版本太低，直接引入文件，参考 Bootstrap中文网：http://v3.bootcss.com/getting-started/ -->
	<%--<NOUSED_r:require module="jquery" />   
	<NOUSED_r:layoutResources />--%>
		
	<!-- The js will be used after ALL page loaded, and we no need to write JS in gsp based on below .js  -->
	<script type="text/javascript" src="${resource(dir: 'js', file: 'bootstrap-datepicker.js')}"></script>
	<r:layoutResources />

<g:if test="${!(WeiXinUtil.WeiXinName == WeiXinUtil.WeiXinName_KCDB || session.weixinOpenId || session.assignment?.name?.endsWith('_FOR_WX') )}">
	<hr class="footer-divider">
	<div class="container">
		<p><a href="http://www.miibeian.gov.cn/">沪ICP备14038534</a> &nbsp;&nbsp;
			&copy; 2013-2014 sHOTs Software Inc. All Rights Reserved. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<g:link controller="adminUser" action="login">管理员</g:link>
			&nbsp;&nbsp;<a data-toggle="modal" data-target="#about" href="#">关于高手云</a>
		</p>
	</div>
</g:if> <!-- end if openid for WeiXin -->	
</body>
</html>
