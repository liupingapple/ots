<%@page import="ots.WeiXinUtil"%>
<%@page import="ots.CONSTANT"%>
<%@ page import="ots.Question" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'question.label', default: 'Question')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-question" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/adminUser/admin')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<g:unless test="${session.user.role == 'linker'}">
					<li><g:link class="create" action="create" params="${[toCreateQuestionType:'XZTK'] }">新建单选/填空填空题</g:link></li>
					<li><g:link class="create" action="create" params="${[toCreateQuestionType:'YDLJ'] }">新建阅读理解题</g:link></li>
					<li><g:link class="test" action="test" id="${questionInstance?.id}"><g:message code="Run Test" args="[entityName]" /></g:link></li>
				</g:unless>
			</ul>
		</div>
		<div id="show-question" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list question">				
				<g:if test="${questionInstance?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="question.id.label" default="ID" /></span>					
							<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${questionInstance}" field="id"/></span>					
					</li>
				</g:if>

				<g:if test="${questionInstance?.qID}">
				<li class="fieldcontain">
					<span id="id-label" class="property-label"><g:message code="question.qid.label" default="QID" /></span>
					
						<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${questionInstance}" field="qID"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${questionInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="question.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${questionInstance}" field="type"/></span>
					
				</li>
				</g:if>

				<g:if test="${questionInstance?.instructions}">
				<li class="fieldcontain">
					<span id="instructions-label" class="property-label"><g:message code="question.instructions.label" default="Instructions" /></span>
					
					<span class="property-value" aria-labelledby="instructions-label">						
					    ${fieldValue(bean: questionInstance, field: "instructions")}
					</span>
				</li>
				</g:if>
				
				<g:if test="${questionInstance?.difficultyLevel > -1 || CONSTANT.YDLJ_Types.contains(questionInstance?.type)}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="question.difficultyLevel.label" default="Difficulty Level" /></span>
					
						<span class="property-value" aria-labelledby="difficultyLevel-label"><g:fieldValue bean="${questionInstance}" field="difficultyLevel"/></span>
				</li>
				</g:if>
			
				<g:if test="${questionInstance?.instructions}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="question.instructions.label" default="Instructions" /></span>					
					<span class="property-value" aria-labelledby="type-label">${questionInstance?.instructions}</span>			
				</li>
				</g:if>
				
				<g:if test="${questionInstance?.qrCode}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="question.qrCode.label" default="二维码Key" /></span>					
					<span class="property-value" aria-labelledby="type-label">${questionInstance?.qrCode}</span>
					(可通过链接生成二维码:${WeiXinUtil.WEIXIN_SRV_URL}/createQRCode/${questionInstance?.qrCode})		
				</li>
				</g:if>
				
				<g:if test="${questionInstance?.qrCodeVideoURL}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="question.qrCodeVideoURL.label" default="二维码讲解视频URL" /></span>					
					<span class="property-value" aria-labelledby="type-label">${questionInstance?.qrCodeVideoURL}</span>			
				</li>
				</g:if>
				
				<g:if test="${questionInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="question.description.label" default="Description" /></span>
					
					<span class="property-value" aria-labelledby="description-label">	
						${questionInstance.description }
					</span>
				</li>
				</g:if>
			
				<g:if test="${questionInstance?.answers}">
				<li class="fieldcontain">
					<span id="answers-label" class="property-label"><g:message code="question.answers.label" default="Answers" /></span>
					
						<g:each in="${questionInstance.answers}" var="a">
						<span class="property-value" aria-labelledby="answers-label">
							<g:if test="${flash.readOnly}">
								${a?.encodeAsHTML()}
							</g:if>
							<g:else>
								<g:link controller="answer" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link>
							</g:else>
						</span>
						</g:each>
					
				</li>
				</g:if>
				
				
				<g:if test="${!questionInstance?.parentQuestion}">
						
					<g:if test="${questionInstance?.source}">
					<li class="fieldcontain">
						<span id="source-label" class="property-label"><g:message code="question.source.label" default="Source" /></span>
						
							<span class="property-value" aria-labelledby="source-label"><g:fieldValue bean="${questionInstance}" field="source"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${questionInstance?.term}">
					<li class="fieldcontain">
						<span id="term-label" class="property-label"><g:message code="question.term.label" default="Term" /></span>
						
							<span class="property-value" aria-labelledby="term-label"><g:fieldValue bean="${questionInstance}" field="term"/></span>
						
					</li>
					</g:if>
					
					<g:unless test="${session.user.role == 'linker'}">
					<g:if test="${questionInstance?.errorRate && questionInstance?.errorRate != -1}">
					<li class="fieldcontain">
						<span id="errorRate-label" class="property-label"><g:message code="question.errorRate.label" default="Error Rate" /></span>
						
							<span class="property-value" aria-labelledby="errorRate-label"><g:fieldValue bean="${questionInstance}" field="errorRate"/></span>
						
					</li>
					</g:if>
					</g:unless>
				
				</g:if>
			
				<g:if test="${questionInstance?.analysis}">
				<li class="fieldcontain">
					<span id="analysis-label" class="property-label"><g:message code="question.analysis.label" default="Analysis" /></span>
					
						<span class="property-value" aria-labelledby="analysis-label"><g:fieldValue bean="${questionInstance}" field="analysis"/></span>
					
				</li>
				</g:if>
										
				<%--<g:if test="${questionInstance?.inputBy}">
				<li class="fieldcontain">
					<span id="inputBy-label" class="property-label"><g:message code="question.inputBy.label" default="Input By" /></span>
					
						<span class="property-value" aria-labelledby="inputBy-label">${questionInstance?.inputBy?.encodeAsHTML()}</span>
					
				</li>
				</g:if>

				<g:if test="${questionInstance?.reviewedBy}">
				<li class="fieldcontain">
					<span id="term-label" class="property-label"><g:message code="question.reviewedBy.label" default="Reviewed By" /></span>
					
						<span class="property-value" aria-labelledby="reviewedBy-label"><g:fieldValue bean="${questionInstance}" field="reviewedBy"/></span>
					
				</li>
				</g:if>

				
				<g:if test="${questionInstance?.inputDate}">
				<li class="fieldcontain">
					<span id="inputDate-label" class="property-label"><g:message code="question.inputDate.label" default="Input Date" /></span>
					
						<span class="property-value" aria-labelledby="inputDate-label"><g:formatDate type="datetime" timeZone="Asia/Shanghai" style="MEDIUM" date="${questionInstance?.inputDate}" /></span>
					
				</li>
				</g:if>--%>	
				
				<g:if test="${questionInstance?.parentQuestion}">
					<li class="fieldcontain"><span id="childQuestions-label" class="property-label">---------------</span><span class="property-value">---------------</span></li>
					<li class="fieldcontain">
						<span id="parentQuestion-label" class="property-label"><g:message code="question.parentQuestion.label" default="Parent Question" /></span>					
							<g:link action="show" id="${questionInstance?.parentQuestion?.id }">
								<span class="property-value" aria-labelledby="parentQuestion-label"><g:fieldValue bean="${questionInstance}" field="parentQuestion.id"/>
									- ${questionInstance?.parentQuestion?.type }
								</span>		
							</g:link>							
					</li>
					<li class="fieldcontain"><span id="childQuestions-label" class="property-label"></span>
						<span class="property-value"><g:link class="save" action="create" params="${[parentQuestionId:questionInstance?.parentQuestion?.id, toCreateQuestionType:'XZTK'] }">Add child question</g:link>
						</span>
					</li>
					<li class="fieldcontain"><span id="childQuestions-label" class="property-label">Parent Description</span>
						<span class="property-value">${ questionInstance?.parentQuestion?.description}</span>
					</li>					
				</g:if>
				
				<g:if test="${questionInstance?.childQuestions}">
					<li class="fieldcontain"><span id="childQuestions-label" class="property-label">---------------</span><span class="property-value">---------------</span></li>
					<li class="fieldcontain">
						<span id="childQuestions-label" class="property-label"><g:message code="question.childQuestions.label" default="Child Questions" /></span>
						<g:each in="${questionInstance?.childQuestions }" var="childQuestion">
							<span class="property-value" aria-labelledby="childQuestions-label">
							   <g:link action="show" id="${ childQuestion.id}"> ${childQuestion }</g:link>
							</span>
						</g:each>
					</li>
				</g:if>
				
				<g:if test="${CONSTANT.YDLJ_Types.contains(questionInstance?.type)}">
					<li class="fieldcontain"><span id="childQuestions-label" class="property-label"></span>
						<span class="property-value"><g:link class="save" action="create" params="${[parentQuestionId:questionInstance?.id, toCreateQuestionType:'XZTK'] }">Add child question</g:link>
						</span>
					</li>
				</g:if>
				
				<g:if test="${questionInstance?.knowledgePoints}">
				<li class="fieldcontain">
					<span id="knowledgePoints-label" class="property-label"><g:message code="question.knowledgePoints.label" default="Knowledge Points" /></span>
					
						<g:each in="${questionInstance.knowledgePoints}" var="a">
						<span class="property-value" aria-labelledby="knowledgePoints-label">
							<g:if test="${flash.readOnly}">
								${a?.encodeAsHTML()}
							</g:if>
							<g:else>
								<g:link controller="knowledgePoint" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link>
							</g:else>
						</span>
						</g:each>
					
				</li>
				</g:if>
			
				<li class="fieldcontain">
					<span class="property-label"><g:message code="question.answeredStats.label" default="学生答题统计" /></span>					
					<span class="property-value">									
						<g:remoteLink update="[success: 'message', failure: 'error']"  action="showAnsweredStats" id="${questionInstance?.id}">
							显示学生答题情况信息
						</g:remoteLink>		
					</span>					 
					<div class="property-value" id="message"></div> <%-- <g:remoteLink update contains success:'message' --%>
					<div id="error"></div>   <%-- <g:remoteLink update contains failure:'error' --%>
				</li>
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${questionInstance?.id}" />
					<g:link class="showPrevious" action="showPrevious" id="${questionInstance?.id}"><g:message code="default.button.showPrevious.label" default="Previous" /></g:link>					
					<g:link class="showNext" action="showNext" id="${questionInstance?.id}"><g:message code="default.button.showNext.label" default="Next" /></g:link>
					<g:if test="${session.user.role == 'checker'}">
						<g:link class="reviewedAndNext" action="reviewedAndNext" id="${questionInstance?.id}"><g:message code="default.button.reviewedAndNext.label" default="Check & Next" /></g:link>
						<g:link class="uncheckAndNext" action="uncheckAndNext" id="${questionInstance?.id}"><g:message code="default.button.uncheckAndNext.label" default="Uncheck & Next" /></g:link>
						<g:link class="reviewed" action="reviewed" id="${questionInstance?.id}"><g:message code="default.button.reviewed.label" default="Check" /></g:link>
						<g:link class="uncheck" action="uncheck" id="${questionInstance?.id}"><g:message code="default.button.uncheck.label" default="Uncheck" /></g:link>
					</g:if>
					<g:if test="${session.user.role != 'inputer'}">
						<g:link class="link" action="link" id="${questionInstance?.id}"><g:message code="default.button.link.label" default="Link/Unlink" /></g:link>
					</g:if>
					<g:if test="${flash.readOnly || session.user.role == 'linker' || session.user.role == 'checker'}">
						<g:link class="list" action="list" params="[sort:session.question_sort,order:session.question_order,searchCategory:session.question_searchCategory,searchable:session.question_searchable]">Back to List</g:link>
						<g:if test="${session.user.role == 'checker'}">
							<g:link class="edit" action="edit" id="${questionInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						</g:if>
					</g:if>
					<g:else>
						<g:link class="edit" action="edit" id="${questionInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</g:else>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
