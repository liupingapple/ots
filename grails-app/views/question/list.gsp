
<%@ page import="ots.Question" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'question.label', default: 'Question')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-question" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<g:unless test="${session.user.role == 'linker'}">
					<li><g:link class="create" action="create" params="${[toCreateQuestionType:'XZTK'] }">新建单选/填空填空题</g:link></li>
					<li><g:link class="create" action="create" params="${[toCreateQuestionType:'YDLJ'] }">新建阅读理解题</g:link></li>
				</g:unless>
				
				<li><div class="search-bar">
					<g:form>
						Filter by:
						<g:select name="searchCategory"
							from="${['ID <', 'ID =', 'ID >', 'Type', 'Description', 'qrCode <', 'qrCode =', 'qrCode >', 'Knowledge Points', 'Term', 'Analysis', 'Input By', 'Reviewed By', 'Input Date <', 'Input Date (mm/dd/yy)=', 'Input Date >']}"
							keys="${['idlt', 'id', 'idgt', 'type', 'description', 'qrCode_lt', 'qrCode', 'qrCode_gt', 'knowledgePoints', 'term', 'analysis', 'inputBy', 'reviewedBy', 'inputDatelt', 'inputDate', 'inputDategt']}"
							noSelection="['': '']"
							value="${session.question_searchCategory}"/>
						<input type="search" name="searchable" value="${session.question_searchable}"/>
						<g:actionSubmit action="list" value="Search"/>
					</g:form>
				</div></li>	
			</ul>
		</div>
		
		<div id="list-question" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>			
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>

						<g:sortableColumn property="id" defaultOrder="desc" title="${message(code: 'question.id.label', default: 'ID')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>

						<g:sortableColumn property="qID" defaultOrder="desc" title="${message(code: 'question.qid.label', default: 'QID')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
																						
						<g:sortableColumn property="description" title="${message(code: 'question.description.label', default: 'Description')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
					
						<g:sortableColumn property="type" title="${message(code: 'question.type.label', default: 'Type')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>

						<g:sortableColumn property="qrCode" title="${message(code: 'question.qrCode.label', default: 'qrCode')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>

						<g:unless test="${session.user.role == 'linker' || session.user.role == 'checker'}">
							<g:sortableColumn property="term" title="${message(code: 'question.term.label', default: 'Term')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						</g:unless>

						<th>${message(code: 'question.knowledgePoints.label', default: 'K.P.')}</th>

						<g:sortableColumn property="analysis" title="${message(code: 'question.analysis.label', default: 'Analysis')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						
						<g:if test="${session.user.role == 'linker'}">					
							<g:sortableColumn property="inputBy" title="${message(code: 'question.linkedBy.label', default: 'Linked By')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						</g:if>
						<g:else>
							<g:sortableColumn property="inputBy" title="${message(code: 'question.inputBy.label', default: 'Input By')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						</g:else>
					
						<g:sortableColumn property="reviewedBy" title="${message(code: 'question.reviewedBy.label', default: 'Reviewed')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>

						<g:if test="${session.user.role == 'linker'}">
							<g:sortableColumn property="inputDate" title="${message(code: 'question.linkedDate.label', default: 'Linked Date')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						</g:if>
						<g:else>
							<g:sortableColumn property="inputDate" title="${message(code: 'question.inputDate.label', default: 'Input Date')}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
						</g:else>
						
					</tr>
				</thead>
				<tbody>
				<g:each in="${questionInstanceList}" status="i" var="questionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${questionInstance.id}">${fieldValue(bean: questionInstance, field: "id")}</g:link></td>
					
						<td>${questionInstance?.qID}</td>
					
						<td>${questionInstance?.description}</td>
						
						<td>${fieldValue(bean: questionInstance, field: "type")}</td>

						<td>${fieldValue(bean: questionInstance, field: "qrCode")}</td>
					
						<g:unless test="${session.user.role == 'linker' || session.user.role == 'checker'}">
							<td>${fieldValue(bean: questionInstance, field: "term")}</td>
						</g:unless>

						<td>${fieldValue(bean: questionInstance, field: "knowledgePoints")}</td>
												
						<td>${fieldValue(bean: questionInstance, field: "analysis")}</td>
											
						<td>${fieldValue(bean: questionInstance, field: "inputBy")}</td>
						
						<td>${fieldValue(bean: questionInstance, field: "reviewedBy")}</td>
					
						<td><g:formatDate type="datetime" timeZone="Asia/Shanghai" style="SHORT" date="${questionInstance.inputDate}" /></td>
											
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate controller="question" action="list" total="${questionInstanceTotal}" params="[searchCategory:searchCategory,searchable:searchKeyword]"/>
			</div>
		</div>
	</body>
</html>
