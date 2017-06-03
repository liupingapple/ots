
<%@ page import="ots.KnowledgePoint" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'knowledgePoint.label', default: 'KnowledgePoint')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-knowledgePoint" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<g:unless test="${session.user.role == 'linker'}">
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</g:unless>
				<li><g:link class="view" action="view">View</g:link></li>
				
				<div class="search-bar">
					<g:form>
						Filter by:
						<g:select name="searchCategory"
							from="${['ID <', 'ID =', 'ID >', 'Name', 'Content', 'Master Ratio <', 'Master Ratio =', 'Master Ratio >', 'Associated Questions <', 'Associated Questions =', 'Associated Questions >', 'Total Questions <', 'Total Questions =', 'Total Questions >', 'Parents', 'Children']}"
							keys="${['idlt', 'id', 'idgt', 'name', 'content', 'masterRatiolt', 'masterRatio', 'masterRatiogt', 'associatedQuestionlt', 'associatedQuestion', 'associatedQuestiongt', 'totalQuestionlt', 'totalQuestion', 'totalQuestiongt', 'parentPoints', 'childPoints']}"
							noSelection="['': '']"
							value="${searchCategory}"/>
						<input type="search" name="searchable" value="${searchKeyword}"/>
						<g:actionSubmit action="list" value="Search"/>
					</g:form>
				</div>				
			</ul>
		</div>
		
		<g:if test="${questionInstance}">
		<div id="show-question" class="content scaffold-show" role="main">
			<h1>Linking/Unlinking Question - ${questionInstance.id}</h1>
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
			<h1>Linking/Unlinking Knowledge Point - ${linkingKnowledgePointInstance.id}</h1>
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
		
		<div id="list-knowledgePoint" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="id" title="${message(code: 'knowledgePoint.id.label', default: 'ID')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						
						<g:sortableColumn property="name" title="${message(code: 'knowledgePoint.name.label', default: 'Name')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
					
						<g:sortableColumn property="content" title="${message(code: 'knowledgePoint.content.label', default: 'Content')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
					
						<g:sortableColumn property="associatedQuestion" title="${message(code: 'knowledgePoint.associatedQuestion.label', default: 'A.Q#')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
					
						<g:sortableColumn property="totalQuestion" title="${message(code: 'knowledgePoint.totalQuestion.label', default: 'T.Q#')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>

						<th>${message(code: 'knowledgePoint.parentPoints.label', default: 'Parents')}</th>
						
						<th>${message(code: 'knowledgePoint.childPoints.label', default: 'Children')}</th>

						<g:sortableColumn property="familySize" title="${message(code: 'knowledgePoint.familySize.label', default: 'Size')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
																
					</tr>
				</thead>
				<tbody>
				<g:each in="${knowledgePointInstanceList}" status="i" var="knowledgePointInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td>${fieldValue(bean: knowledgePointInstance, field: "id")}</td>
					
						<td><g:link action="show" id="${knowledgePointInstance.id}">${fieldValue(bean: knowledgePointInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: knowledgePointInstance, field: "content")}</td>
					
						<td>${fieldValue(bean: knowledgePointInstance, field: "associatedQuestion")}</td>
					
						<td>${fieldValue(bean: knowledgePointInstance, field: "totalQuestion")}</td>

						<td>${fieldValue(bean: knowledgePointInstance, field: "parentPoints")}</td>
						
						<td>${fieldValue(bean: knowledgePointInstance, field: "childPoints")}</td>

						<td>${fieldValue(bean: knowledgePointInstance, field: "familySize")}</td>
											
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${knowledgePointInstanceTotal}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
			</div>
		</div>
	</body>
</html>
