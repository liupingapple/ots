<%@ page import="ots.EndUser"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'endUser.label', default: 'End User')}" />
<title><g:message code="default.login.label" args="End User" />- End User</title>
</head>
<body>
      <div class="login_content">
			<div class="container">
			              
			 <g:hasErrors bean="${endUserInstance}">
				<ul class="alert alert-danger">
					<g:eachError bean="${endUserInstance}" var="error">
						<li
							<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
							<g:message error="${error}" />
						</li>
					</g:eachError>
				</ul>
			</g:hasErrors>
					
			<div class="row">
				<div class="span6 offset3">
					<h4 class="widget-header">
						<i class="icon-lock"></i> 欢迎光临高手云在线训练与测评系统
					</h4>
					<g:if test="${flash.message}">
						<div class="message" role="status">
							${flash.message}
						</div>
					</g:if>
					<div class="widget-body">
						<div class="center-align">
							<g:form class="form-horizontal form-signin-signup" action="authenticate" method="post">
								<input type="text" name="userName" id="userName" placeholder="userName">
								<input type="password" name="password" id="password" placeholder="Password">
								
								<div class="remember-me">
									<%--<div class="pull-left">
										<label class="checkbox"> <input type="checkbox"> Remember me
										</label>
									</div>
									--%>
									<div class="pull-right">
										<a href="#" onClick="alert('Please ask administrator to reset your password')">Forgot password?</a>
									</div>
									<div class="clearfix"></div>
								</div>
								<input type="submit" value="Signin" class="btn btn-primary btn-large">
							</g:form>
							
							<%--<h4>
								<i class="icon-question-sign"></i> Don't have an account?
							</h4>

							<h4>
								<!-- Split button -->
								<div class="btn-group">
									<button type="button" class="btn btn-danger">Register</button>
									<button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
										<span class="caret"></span> <span class="sr-only">Toggle Dropdown</span>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li><g:link controller="student" action="create">Student</g:link></li>
										<li><g:link controller="teacher" action="create">Teach</g:link></li>
										<li><g:link controller="parent" action="create">Parent</g:link></li>
									</ul>
								</div>

							</h4>

							<h4>
								<i class="icon-thumbs-up"></i> Sign in with third party account
							</h4>
							<ul class="signin-with-list">
								<li><a class="btn-twitter"> <i class="icon-twitter icon-small"></i> Signin with QQ
								</a></li>
								<li><a class="btn-google"> <i class="icon-google-plus icon-small"></i> Signin with Google
								</a></li>								
							</ul>	
						    
						    <br>
							<g:form controller="AdminUser" action="login">
								<input type="submit" value="Goto AdminUser login (for test only)" class="btn bottom-space btn-mini">
							</g:form>
							--%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- End: MAIN CONTENT -->
</body>
</html>
