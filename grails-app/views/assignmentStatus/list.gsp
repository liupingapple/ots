<%@page import="ots.Student"%>
<%@page import="ots.Quiz"%>
<%@page import="ots.KnowledgePoint"%>
<%@ page import="ots.AssignmentStatus"%>
<%@ page import="ots.Assignment"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="bmain">
<g:set var="entityName" value="${message(code: 'assignmentStatus.label', default: '作业')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<g:render template="inc_list" ></g:render>
</body>
</html>
