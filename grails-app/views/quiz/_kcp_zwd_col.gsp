
<zing:chart type="bar" width="900" height="450" 
    container="zwdCol" data="${zwdColData}" xLabels="${zwdColCol}"  effect="4" /> 

<zing:chart type="line" width="900" height="450" 
    container="zwdLine" data="${zwdLineData}" xLabels="${zwdLineCol}" effect="4" />
<br>注意：如果知识点太多，线条图展示将会失去可读性，选择列出更小正确率的知识点（通过“知识点正确率小于等于”下拉框）可以显示可读性更强的线条图

<%--

<div id="zwdCol"></div>
<gvisualization:columnCoreChart elementId="zwdCol" dynamicLoading="${true}" 
	title="${"到本次练习为止的知识点掌握度 - "+assignStatus.assignment.name}" width="${900}" height="${300}"
	hAxis="${new Expando(slantedText:'true', slantedTextAngle:15, textStyle:'{color:red}')}"
	columns="${zwdColCol}" data="${zwdColData}" />
		
<div id="zwdLine"></div>
<gvisualization:lineCoreChart elementId="zwdLine" dynamicLoading="${true}"  width="${900}" height="${300}"
	title="${"历次练习的知识点掌握度: "+assignStatus.assignment.name}" columns="${zwdLineCol}"
	hAxis="${new Expando(slantedText:'true', slantedTextAngle:15)}" curveType="function"
	data="${zwdLineData}" />
	
--%>