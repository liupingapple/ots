<%@page import="ots.WeiXinUtil"%>
<%@ page import="ots.KnowledgePoint" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'knowledgePoint.label', default: 'KnowledgePoint')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-knowledgePoint" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<g:unless test="${session.user.role == 'linker'}">
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</g:unless>
				<li><g:link class="view" action="view" id="${knowledgePointInstance?.id}">View Family</g:link></li>
				<li><g:link class="view" action="weixinPracticeLink" id="${knowledgePointInstance?.id}">生成手机端练习链接</g:link></li>
			</ul>
		</div>
		
		<g:if test="${questionInstance}">
		<div id="show-question" class="content scaffold-show" role="main">
			<h1>Linking Question - ${questionInstance.id}</h1>
			<ol class="property-list question">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="question.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${questionInstance}" field="description"/></span>
					
				</li>
				<li class="fieldcontain">
					<span id="answers-label" class="property-label"><g:message code="question.answers.label" default="Answers" /></span>
					
						<g:each in="${questionInstance.answers}" var="a">
							<span class="property-value" aria-labelledby="answers-label">${a?.encodeAsHTML()}</span>
						</g:each>
				</li>
				<li class="fieldcontain">
					<span id="knowledgePoints-label" class="property-label"><g:message code="question.knowledgePoints.label" default="Knowledge Points" /></span>
					
						<g:each in="${questionInstance.knowledgePoints}" var="a">
							<span class="property-value" aria-labelledby="knowledgePoints-label">
								<g:link controller="knowledgePoint" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link>
							</span>
						</g:each>
				</li>
			</ol>
		</div>
		</g:if>

		<g:if test="${linkingKnowledgePointInstance}">
		<div id="show-knowledgePoint" class="content scaffold-show" role="main">
			<g:if test="${enableUnlinkParent || enableUnlinkChild}">
				<h1>Unlinking Knowledge Point - ${linkingKnowledgePointInstance.id}</h1>
			</g:if>
			<g:else>
				<h1>Linking Knowledge Point - ${linkingKnowledgePointInstance.id}</h1>
			</g:else>
			<ol class="property-list knowledgePoint">
			
				<g:if test="${linkingKnowledgePointInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="knowledgePoint.name.label" default="Name" /></span>
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${linkingKnowledgePointInstance}" field="name"/></span>
				</li>
				</g:if>
			
				<g:if test="${linkingKnowledgePointInstance?.content}">
				<li class="fieldcontain">
					<span id="content-label" class="property-label"><g:message code="knowledgePoint.content.label" default="Content" /></span>
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${linkingKnowledgePointInstance}" field="content"/></span>
				</li>
				</g:if>
			
				<g:if test="${linkingKnowledgePointInstance?.degree}">
				<li class="fieldcontain">
					<span id="degree-label" class="property-label"><g:message code="knowledgePoint.degree.label" default="Degree" /></span>
						<span class="property-value" aria-labelledby="degree-label"><g:fieldValue bean="${linkingKnowledgePointInstance}" field="degree"/></span>
				</li>
				</g:if>
				
				<g:if test="${linkingKnowledgePointInstance?.parentPoints || linkingKnowledgePointInstance?.childPoints}">
				<table border="0">
				<tr>
				<th><span id="parentPoints-label" class="property-label"><g:message code="knowledgePoint.parentPoints.label" default="Parent Points" /></span></th>
				<th><span id="childPoints-label" class="property-label"><g:message code="knowledgePoint.childPoints.label" default="Child Points" /></span></th>
				</tr>
				<tr>
				<td>
						<g:each in="${linkingKnowledgePointInstance.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				<td>
						<g:each in="${linkingKnowledgePointInstance.childPoints?.sort { it.name }}" var="c">
						<g:link controller="knowledgePoint" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				</tr></table>
				</g:if>
				
			</ol>
		</div>
		</g:if>

		<div id="show-knowledgePoint" class="content scaffold-show" role="main">
			<g:if test="${linkingKnowledgePointInstance || questionInstance}">
				<h1>To Knowledge Point ${knowledgePointInstance.id}</h1>
			</g:if>
			<g:else>
				<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			</g:else>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list knowledgePoint">
			
				<g:if test="${knowledgePointInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="knowledgePoint.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${knowledgePointInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgePointInstance?.content}">
				<li class="fieldcontain">
					<span id="content-label" class="property-label"><g:message code="knowledgePoint.content.label" default="Content" /></span>
					
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${knowledgePointInstance}" field="content"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgePointInstance?.degree}">
				<li class="fieldcontain">
					<span id="degree-label" class="property-label"><g:message code="knowledgePoint.degree.label" default="Degree" /></span>
					
						<span class="property-value" aria-labelledby="degree-label"><g:fieldValue bean="${knowledgePointInstance}" field="degree"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgePointInstance?.masterRatio}">
				<li class="fieldcontain">
					<span id="masterRatio-label" class="property-label"><g:message code="knowledgePoint.masterRatio.label" default="Master Ratio" /></span>
					
						<span class="property-value" aria-labelledby="masterRatio-label"><g:fieldValue bean="${knowledgePointInstance}" field="masterRatio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgePointInstance?.questions}">
				<li class="fieldcontain">
					<span id="question-label" class="property-label"><g:message code="adminUser.question.label" default="Questions" /></span>
					
						<g:each in="${knowledgePointInstance.questions}" var="q">
						&nbsp;&nbsp;&nbsp;<g:link controller="question" action="show" id="${q.id}">${q?.id}</g:link>
						</g:each>
					
				</li>
				</g:if>
				
				<g:if test="${knowledgePointInstance?.associatedQuestion}">
				<li class="fieldcontain">
					<span id="associatedQuestion-label" class="property-label"><g:message code="knowledgePoint.associatedQuestion.label" default="Associated Question" /></span>
					
						<span class="property-value" aria-labelledby="associatedQuestion-label"><g:fieldValue bean="${knowledgePointInstance}" field="associatedQuestion"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${knowledgePointInstance?.totalQuestion}">
				<li class="fieldcontain">
					<span id="totalQuestion-label" class="property-label"><g:message code="knowledgePoint.totalQuestion.label" default="Total Question" /></span>
					
						<span class="property-value" aria-labelledby="totalQuestion-label"><g:fieldValue bean="${knowledgePointInstance}" field="totalQuestion"/></span>
					
				</li>
				</g:if>

				<g:if test="${knowledgePointInstance?.familySize}">
				<li class="fieldcontain">
					<span id="familySize-label" class="property-label"><g:message code="knowledgePoint.familySize.label" default="Family Size" /></span>
					
						<span class="property-value" aria-labelledby="familySize-label"><g:fieldValue bean="${knowledgePointInstance}" field="familySize"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${knowledgePointInstance?.qrCode}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="knowledgePoint.qrCode.label" default="二维码Key" /></span>					
					<span class="property-value" aria-labelledby="type-label">${knowledgePointInstance?.qrCode}</span>
					(可通过链接生成二维码:${WeiXinUtil.WEIXIN_SRV_URL}/createQRCode/${knowledgePointInstance?.qrCode})		
				</li>
				</g:if>
				
				<g:if test="${knowledgePointInstance?.qrCodeVideoURL}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="knowledgePoint.qrCodeVideoURL.label" default="二维码讲解视频URL" /></span>					
					<span class="property-value" aria-labelledby="type-label">${knowledgePointInstance?.qrCodeVideoURL}</span>			
				</li>
				</g:if>

				<g:if test="${knowledgePointInstance?.parentPoints || knowledgePointInstance?.childPoints}">
				<table border="0">
				<tr>
				<th><span id="parentPoints-label" class="property-label"><g:message code="knowledgePoint.parentPoints.label" default="Parent Points" /></span></th>
				<th><span id="childPoints-label" class="property-label"><g:message code="knowledgePoint.childPoints.label" default="Child Points" /></span></th>
				</tr>
				<tr>
				<td>
						<g:each in="${knowledgePointInstance.parentPoints?.sort { it.name }}" var="p">
						<g:link controller="knowledgePoint" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				<td>
						<g:each in="${knowledgePointInstance.childPoints?.sort { it.name }}" var="c">
						<g:link controller="knowledgePoint" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link><br/>
						</g:each>
				</td>
				</tr></table>
				</g:if>
				
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${knowledgePointInstance?.id}" />
					<g:if test="${questionInstance}">
						<g:if test="${enableUnlink}">
							<g:link class="edit" action="finishUnlinkQuestion" id="${knowledgePointInstance?.id}"><g:message code="default.button.finishUnlink.label" default="Finish Unlink" /></g:link>
						</g:if>
						<g:else>
							<g:link class="edit" action="finishLinkQuestion" id="${knowledgePointInstance?.id}"><g:message code="default.button.finishLink.label" default="Finish Link" /></g:link>
						</g:else>
					</g:if>
					<g:if test="${session.user.role == 'linker' || session.user.role == 'checker'}">
						<g:link class="back" action="backToQuestion" id="${knowledgePointInstance?.id}"><g:message code="default.button.back.label" default="Back to Question" /></g:link>
					</g:if>
					<g:else>
						<g:if test="${linkingKnowledgePointInstance}">
							<g:if test="${enableUnlinkParent}">
								<g:link class="edit" action="unlinkedAsParent" id="${knowledgePointInstance?.id}"><g:message code="default.button.unlinkedAsParent.label" default="Unlinked as Child - Parent" /></g:link>
							</g:if>
							<g:else>
								<g:if test="${enableUnlinkChild}">
									<g:link class="edit" action="unlinkedAsChild" id="${knowledgePointInstance?.id}"><g:message code="default.button.unlinkedAsChild.label" default="Unlinked as Parent - Child" /></g:link>
								</g:if>
								<g:else>
									<g:link class="edit" action="linkedAsParent" id="${knowledgePointInstance?.id}"><g:message code="default.button.linkedAsParent.label" default="Linked as Child - Parent" /></g:link>
									<g:link class="edit" action="linkedAsChild" id="${knowledgePointInstance?.id}"><g:message code="default.button.linkedAsChild.label" default="Linked as Parent - Child" /></g:link>
								</g:else>
							</g:else>
							<g:link class="edit" action="cancelLink" id="${knowledgePointInstance?.id}"><g:message code="default.button.cancelLinking.label" default="Cancel" /></g:link>
						</g:if>
						<g:else>
							<g:unless test="${questionInstance}">
								<g:link class="edit" action="edit" id="${knowledgePointInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
								<g:link class="edit" action="beginLink" id="${knowledgePointInstance?.id}"><g:message code="default.button.linking.label" default="Linking/Unlinking Nodes" /></g:link>
								<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
							</g:unless>
						</g:else>
						<g:if test="${questionInstance}">
							<g:link class="back" action="backToQuestion" id="${knowledgePointInstance?.id}"><g:message code="default.button.back.label" default="Back to Question" /></g:link>
						</g:if>
					</g:else>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
