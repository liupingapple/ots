<!DOCTYPE html>
<%@page import="ots.Quiz"%>
<%@page import="ots.Teacher"%>
<html>
<head>
<meta name="layout" content="bmain">
<title>课程</title>
</head>

<body>
	<div class="container bs-docs-container">		
		<g:include action="msg" id="${session?.user?.id }" params="${[chakanFlag:'true' ]}"/>
	</div>
</body>

</html>