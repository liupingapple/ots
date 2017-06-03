
<%@ page import="ots.Question"%>
<%@ page import="ots.CONSTANT"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'question.label', default: 'Question')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>

</head>
<body>
	<a href="#show-question" class="skip" tabindex="-1"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
			<li><g:link class="list" action="list">
					<g:message code="default.list.label" args="[entityName]" />
				</g:link></li>
			<li><g:link class="create" action="create">
					<g:message code="default.new.label" args="[entityName]" />
				</g:link></li>
		</ul>
	</div>
	<div id="show-question" class="content scaffold-show" role="main">
		<h1>
			<g:message code="default.show.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:form name="questionForm" url="[controller: 'Question', action:'judge']">
		<g:hiddenField name="questionInstanceId" value="${questionInstance?.id}" />
		
		<ol class="property-list question">
			<g:if test="${questionInstance?.description}">
				<li class="fieldcontain"><span id="description-label"
					class="property-label"><g:message
							code="question.description.label" default="Description" /></span> 
				   <span class="property-value" aria-labelledby="description-label">	
				   <g:if test="${questionInstance?.type == CONSTANT.FBLANK_QUESTION}">	
				      <g:cvtq question="${questionInstance}" />	
				   </g:if>
				   <g:else>				       
				      ${questionInstance}					   
				      <!-- 选择题，显示备选各答案 -->				      
				         <g:if test="${questionInstance?.answers}">		
							<li class="fieldcontain"><span id="answers-label"
								class="property-label"><g:message code="question.answers.label" default="Answer" /></span> 									
										<g:each in="${questionInstance.answers}" var="a" status="i">
										<span class="property-value" aria-labelledby="answers-label">
										<input type="radio" name="answerByUser" required="" value="${a.id}" />
										<g:link controller="answer" action="show" id="${a.id}">
											${a?.showInQuestion()}
										</g:link>
										</span>
									</g:each>		
							</li>				
			             </g:if>
				   </g:else>
				  </span>
				</li>
			</g:if>

		    

		</ol>
		
		<div class="buttons">
                    <span class="button"><input type="submit" value="提交答案" /></span>
                </div>
		</g:form>
	</div>
</body>
</html>
