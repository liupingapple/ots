
<%@ page import="ots.KnowledgePoint" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'knowledgePoint.label', default: 'KnowledgePoint')}" />
		<title><g:message code="default.view.label" args="[entityName]" /></title>
	</head>
	<body>
		<script>
			$(document).ready(function(){
			  $(".kpList").dblclick(function(){
				  $(this).children(".kpUpdated").val("true")
				  $("#Refresh").trigger('click');
			  });
			});
		</script>
		<a href="#show-knowledgePoint" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="view-knowledgePoint" class="content scaffold-show" role="main">
			<h1><g:message code="default.view.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:form action="view" >
			<table border="1">
				<tr>
				<th><span id="childPoints-label" class="property-label"><g:message code="knowledgePoint.childPoints.label" default="Child Points" /></span></th>
				<th><span id="content-label" class="property-label"><g:message code="knowledgePoint.content.label" default="Content" /></span></th>
				<th><span id="question-label" class="property-label"><g:message code="question.id.label" default="Questions" /></span></th>
				<th><span id="familySize-label" class="property-label"><g:message code="familySize.id.label" default="Family#" /></span></th>
				<th><span id="parentPoints-label" class="property-label"><g:message code="knowledgePoint.parentPoints.label" default="Parent Points" /></span></th>
				</tr>
			
				<g:if test="${knowledgePointList1}">
				<tr>
				<td>
				<div class="kpList">
					<g:hiddenField name="kp1Updated" class="kpUpdated" value="false" />
					<g:select name="kp1" from="${knowledgePointList1}" optionKey="id" size="5" value="${knowledgePoint1?.id}" class="one-to-many"/>
				</div>
				</td>
				<td>
						<g:fieldValue bean="${knowledgePoint1}" field="content"/>
				</td>
				<td>		
						<g:each in="${knowledgePoint1?.questions?.sort { it.id }}" var="p">
						<g:link controller="question" action="show" id="${p.id}">${p?.id}</g:link><br/>
						</g:each>
				</td>
				<td>
						<g:fieldValue bean="${knowledgePoint1}" field="familySize"/>
				</td>
				<td>		
						<g:each in="${knowledgePoint1?.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				</tr>
				</g:if>
			
				<g:if test="${knowledgePointList2}">
				<tr>
				<td>
				<div class="kpList">
					<g:hiddenField name="kp2Updated" class="kpUpdated" value="false" />
					<g:select name="kp2" from="${knowledgePointList2}" optionKey="id" size="5" value="${knowledgePoint2?.id}" class="one-to-many"/>
				</div>
				</td>
				<td>
						<span class="property-value"><g:fieldValue bean="${knowledgePoint2}" field="content"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint2?.questions?.sort { it.id }}" var="p">
						<g:link controller="question" action="show" id="${p.id}">${p?.id}</g:link><br/>
						</g:each>
				</td>
				<td>
						<span class="property-value"><g:fieldValue bean="${knowledgePoint2}" field="familySize"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint2?.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				</tr>
				</g:if>

				<g:if test="${knowledgePointList3}">
				<tr>
				<td>
				<div class="kpList">
					<g:hiddenField name="kp3Updated" class="kpUpdated" value="false" />
					<g:select name="kp3" from="${knowledgePointList3}" optionKey="id" size="5" value="${knowledgePoint3?.id}" class="one-to-many"/>
				</div>
				</td>
				<td>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePoint3}" field="content"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint3?.questions?.sort { it.id }}" var="p">
						<g:link controller="question" action="show" id="${p.id}">${p?.id}</g:link><br/>
						</g:each>
				</td>
				<td>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePoint3}" field="familySize"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint3?.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				</tr>
				</g:if>

				<g:if test="${knowledgePointList4}">
				<tr>
				<td>
				<div class="kpList">
					<g:hiddenField name="kp4Updated" class="kpUpdated" value="false" />
					<g:select name="kp4" from="${knowledgePointList4}" optionKey="id" size="5" value="${knowledgePoint4?.id}" class="one-to-many"/>
				</div>
				</td>
				<td>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePoint4}" field="content"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint4?.questions?.sort { it.id }}" var="p">
						<g:link controller="question" action="show" id="${p.id}">${p?.id}</g:link><br/>
						</g:each>
				</td>
				<td>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePoint4}" field="familySize"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint4?.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>

				</td>
				</tr>
				</g:if>

				<g:if test="${knowledgePointList5}">
				<tr>
				<td>
				<div class="kpList">
					<g:hiddenField name="kp5Updated" class="kpUpdated" value="false" />
					<g:select name="kp5" from="${knowledgePointList5}" optionKey="id" size="5" value="${knowledgePoint5?.id}" class="one-to-many"/>
				</div>
				</td>
				<td>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePoint5}" field="content"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint5?.questions?.sort { it.id }}" var="p">
						<g:link controller="question" action="show" id="${p.id}">${p?.id}</g:link><br/>
						</g:each>
				</td>
				<td>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePoint5}" field="familySize"/></span>
				</td>
				<td>		
						<g:each in="${knowledgePoint5?.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				</tr>
				</g:if>
								
				</table>			
				<fieldset class="buttons">
					<g:hiddenField name="root" value="${rootID}" />
					<g:submitButton name="Refresh" class="save" value="Refresh"/>
					<g:if test="${rootID}">
						<g:link action="show" id="${rootID}">Back</g:link>
					</g:if>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
