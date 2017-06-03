<%@ page import="ots.AdminUser" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'adminUser.label', default: ' ')}" />
		<title><g:message code="default.login.label" args=" " /> - Admin User</title>
	</head>
	<body>
		<a href="#login-adminUser" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li></li>
				<li></li>
			</ul>
		</div>
		<div id="login-adminUser" class="content scaffold-list" role="main">
			<h1><g:message code="default.login.label" args=" " />- Admin User</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${adminUserInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${adminUserInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
            <g:form action="authenticate" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="login"><g:message code="user.login.label" default="User Name" /></label>
                                </td>
                                <td valign="top">
                                    <input type="text" id="userName" name="userName" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="password"><g:message code="user.password.label" default="Password" /></label>
                                </td>
                                <td valign="top" >
                                    <input type="password" id="password" name="password" />
                                </td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input type="submit" value="Login" /></span>
                    <g:link class="button" controller="EndUser" action="login">Goto EndUser login</g:link>
                </div>
            </g:form>
        </div>
	</body>
</html>
