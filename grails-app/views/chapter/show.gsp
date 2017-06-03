<%@ page import="ots.Chapter" %>
<%@ page import="ots.AdminUser" %>
<!DOCTYPE html>
<html>

<head>
<meta name="layout" content="bmain">
<title>章节</title>
<style type="text/css">	
	.rowx {
		margin-bottom:3px;
		padding-top:0;
		padding-bottom:0;
		margin-left:0;
		margin-right:0
	}
	
	body {
		padding-top: 0px;
	}
</style>
</head>

<body>
	<div class="container-fluid">
		<div class="row jumbotron rowx">
			<h2 class="text-muted text-center">高手云 - 课程导报</h2>	
			<p class="text-warning text-center">${chapterInstance?.name }</p>
			
			<g:if test="${session?.user instanceof AdminUser }">
				<div class="btn-group btn-group-sm" style="float: right">
					<g:link class="btn btn-xs btn-warning" action="list">所有章节</g:link>
					<g:link class="btn btn-xs btn-warning" controller="chapterPiece" action="create" params="['chapter.id': chapterInstance?.id]">新增章节段落</g:link>
					<g:link class="btn btn-xs btn-warning" action="edit" id="${chapterInstance?.id}">编辑该章节</g:link>
				</div>		
			</g:if>
		</div>
		
		<g:each in="${chapterInstance?.chapterPieces.sort{a, b -> a.id - b.id} }" var="piece">				
			<div class="row panel panel-primary rowx">
			  <div class="panel-heading">
			    <h3 class="panel-title">${piece?.name }
			    	<g:if test="${session?.user instanceof AdminUser }">
						<g:link class="btn btn-xs btn-warning" controller="chapterPiece" action="edit" id="${piece.id}">编辑 </g:link>	
					</g:if>	
			    </h3>
			  </div>
			  <div class="panel-body">
			    ${piece?.content }			    
			  </div>				
			</div>
		</g:each>
		
		<g:if test="${!session?.user instanceof AdminUser }">
			<div class="row panel panel-success rowx">
			  <div class="panel-heading">
			    <h3 class="panel-title">水平测试</h3>
			  </div>
			  <div class="panel-body">
			    <g:link class="btn btn-primary" controller="quizQuestionRecord" action="answering" params="${params4newQuiz }">进入练习</g:link>
			  </div>
			</div>
		</g:if>
	</div>
		
</body>

</html>
