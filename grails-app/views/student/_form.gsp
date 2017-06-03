<%@ page import="ots.Student"%>
<%@ page import="ots.AdminUser"%>

<dl class="dl-horizontal">
	<dt>
		<label for="userName"> <g:message code="student.userName.label" default="User Name" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:if test="${session.user instanceof ots.AdminUser}">
			<g:textField name="userName" required="" value="${studentInstance?.userName}" />
		</g:if>
		<g:else>
			<g:field type="text" name="userName" readonly="readonly" disabled="disabled" value="${studentInstance?.userName}" />
		</g:else>
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="password"> <g:message code="student.password.label" default="Password" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:field type="password" name="password" required="" value="${studentInstance?.password}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="password"> <g:message code="student.password.label" default="Password again" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:field type="password" name="password_again" required="" value="${studentInstance?.password}"
			onblur='verifyPassword()' />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="fullName"> <g:message code="student.fullName.label" default="Full Name" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:textField name="fullName" required="" value="${studentInstance?.fullName}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="teacher"> <g:message code="student.teacher.label" default="teacher" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:if test="${session.user instanceof AdminUser }">
			<g:select id="teacher" name="teacher.id" from="${ots.Teacher.list()}" optionKey="id"
				value="${studentInstance?.teacher?.id}" class="many-to-one" noSelection="['null': '']" />
		</g:if>
		<g:else>
			<fieldset disabled>
				<g:select id="teacher" name="teacher.id" from="${ots.Teacher.list()}" optionKey="id"
					value="${studentInstance?.teacher?.id}" class="many-to-one" noSelection="['null': '']" />
			</fieldset>
		</g:else>
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="email"> <g:message code="student.email.label" default="Email" />

		</label>
	</dt>
	<dd>
		<g:field type="email" name="email" value="${studentInstance?.email}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="telephone1"> <g:message code="student.telephone1.label" default="Telephone1" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:textField name="telephone1" value="${studentInstance.telephone1}" required="" onblur="checkTel()" />
		&nbsp;(例如：1394567893 或者 021-23212333)
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="parents"> <g:message code="student.parents.label" default="家长登录系统用户名" /> 
		</label>
	</dt>
	<dd>
		<g:textField name="parents" value="${studentInstance.parents}" />
		&nbsp;(家长在系统中已经注册) <!-- parents kept in EndUser domain -->
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="term"> <g:message code="student.term.label" default="Term" />

		</label>
	</dt>
	<dd>
		<g:select name="term" from="${studentInstance.constraints.term.inList}" value="${studentInstance?.term}"
			valueMessagePrefix="student.term" noSelection="['': 'Please choose ...']" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="sex"> <g:message code="student.sex.label" default="Sex" />
		</label>
	</dt>
	<dd>
		<g:select name="sex" from="${studentInstance.constraints.sex.inList}" value="${studentInstance?.sex}"
			valueMessagePrefix="student.sex" noSelection="['': 'Please choose ...']" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="birthDate"> <g:message code="student.birthDate.label" default="Birth Date" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:datePicker name="birthDate" precision="day" value="${studentInstance?.birthDate}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="identificationNum"> <g:message code="student.identificationNum.label"
				default="Identification Num" />
		</label>
	</dt>
	<dd>
		<g:field name="identificationNum" type="number" value="${studentInstance.identificationNum}" />
	</dd>
</dl>

<g:unless test="${session.user instanceof Student}">

	<dl class="dl-horizontal">
		<dt>
			<label for="masterLevel"> <g:message code="student.masterLevel.label"
					default="masterLevel" />

			</label>
		</dt>
		<dd>
			<g:field name="masterLevel" type="number"
				value="${studentInstance.masterLevel}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="numberOfQuestionsPerPage"> <g:message code="student.numberOfQuestionsPerPage.label"
					default="每页练习题量" />

			</label>
		</dt>
		<dd>
			<g:field name="numberOfQuestionsPerPage" type="number"
				value="${studentInstance.numberOfQuestionsPerPage}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="targetCorrectRate"> <g:message code="student.targetCorrectRate.label" default="目标成绩" />

			</label>
		</dt>
		<dd>
			<g:field name="targetCorrectRate" type="number"
				value="${(studentInstance.targetCorrectRate*100).intValue()}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="minAssociatedQuestionsToEvaluate"> <g:message
					code="student.minAssociatedQuestionsToEvaluate.label" default="最少评估关联题量" />

			</label>
		</dt>
		<dd>
			<g:field name="minAssociatedQuestionsToEvaluate" type="number"
				value="${studentInstance.minAssociatedQuestionsToEvaluate}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="maxAssociatedQuestionsToEvaluate"> <g:message
					code="student.maxAssociatedQuestionsToEvaluate.label" default="最多评估关联题量" />

			</label>
		</dt>
		<dd>
			<g:field name="maxAssociatedQuestionsToEvaluate" type="number"
				value="${studentInstance.maxAssociatedQuestionsToEvaluate}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="minTotalQuestionsToEvaluate"> <g:message
					code="student.minTotalQuestionsToEvaluate.label" default="最少评估家族题量" />

			</label>
		</dt>
		<dd>
			<g:field name="minTotalQuestionsToEvaluate" type="number"
				value="${studentInstance.minTotalQuestionsToEvaluate}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="maxTotalQuestionsToEvaluate"> <g:message
					code="student.maxTotalQuestionsToEvaluate.label" default="最多评估家族题量" />

			</label>
		</dt>
		<dd>
			<g:field name="maxTotalQuestionsToEvaluate" type="number"
				value="${studentInstance.maxTotalQuestionsToEvaluate}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="evaluationPower"> <g:message code="student.evaluationPower.label" default="历史成绩权重(%)" />

			</label>
		</dt>
		<dd>
			<g:field name="evaluationPower" type="number" value="${(studentInstance.evaluationPower*100).intValue()}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="maxKPToShow"> <g:message code="student.maxKPToShow.label" default="薄弱点显示个数" />

			</label>
		</dt>
		<dd>
			<g:field name="maxKPToShow" type="number" value="${studentInstance.maxKPToShow}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="toShowKnowledgeGraph"> <g:message code="student.toShowKnowledgeGraph.label"
					default="Show Knowledge Graph" />

			</label>
		</dt>
		<dd>
			<g:checkBox name="toShowKnowledgeGraph" value="${studentInstance?.toShowKnowledgeGraph}" />
		</dd>
	</dl>
	
</g:unless>

<g:if test="${session.user instanceof AdminUser && session.user?.role == 'admin'}">
	<dl class="dl-horizontal">
		<dt>
			<label for="expiredDate"> <g:message code="student.expiredDate.label" default="账户有效期至" /> <span
				class="required-indicator">*</span>
			</label>
		</dt>
		<dd>
			<g:datePicker name="expiredDate" precision="day" value="${studentInstance?.expiredDate ? studentInstance.expiredDate : new Date()+14}" />
		</dd>
	</dl>

	<!-- has been used for 留言 -->
	<dl class="dl-horizontal">
		<dt>
			<label for="telephone2"> <g:message code="student.telephone2.label" default="留言" /> <span
				class="required-indicator">*</span>
			</label>
		</dt>
		<dd>
			<g:textField name="telephone2" value="${studentInstance.telephone2}" onblur="checkTel()" />
			&nbsp;(例如：1394567893 或者 021-23212333)
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="homeProvince"> <g:message code="student.homeProvince.label" default="Home Province" />
			</label>
		</dt>
		<dd>
			<g:select name="homeProvince" from="${studentInstance.constraints.homeProvince.inList}"
				value="${studentInstance?.homeProvince}" valueMessagePrefix="student.homeProvince"
				noSelection="['': '']" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="homeCity"> <g:message code="student.homeCity.label" default="Home City" />

			</label>
		</dt>
		<dd>
			<g:textField name="homeCity" value="${studentInstance?.homeCity}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="homeAddr"> <g:message code="student.homeAddr.label" default="Home Addr" />

			</label>
		</dt>
		<dd>
			<g:textField name="homeAddr" value="${studentInstance?.homeAddr}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="school"> <g:message code="student.school.label" default="School" />

			</label>
		</dt>
		<dd>
			<g:select id="school" name="school.id" from="${ots.School.list()}" optionKey="id" optionValue="name"
				value="${studentInstance?.school?.id}" class="many-to-one" noSelection="['null': '']" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="active"> <g:message code="student.active.label" default="Active" />

			</label>
		</dt>
		<dd>
			<g:checkBox name="active" value="${studentInstance?.active}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="level"> <g:message code="student.level.label" default="Level" />

			</label>
		</dt>
		<dd>
			<g:select name="level" from="${studentInstance.constraints.level.inList}"
				value="${studentInstance?.level}" valueMessagePrefix="student.level" noSelection="['': '']" />
		</dd>
	</dl>

	<!-- has been used for 辅导记录 -->
	<dl class="dl-horizontal">
		<dt>
			<label for="briefComment"> <g:message code="student.briefComment.label" default="briefComment" />

			</label>
		</dt>
		<dd>
			<g:textArea name="briefComment" value="${studentInstance?.briefComment}" />
		</dd>
	</dl>
</g:if>

<script type="text/javascript">
	function verifyPassword() {
		if ($('#password').val() != $('#password_again').val()) {
			$('#password').val('');
			$('#password_again').val('');
			alert('两次输入的密码不一致，请重新输入');
		}
	}

	function checkTel0(telval) {
		var wrongFlag = false
		var Letters = "1234567890-"; //可以自己增加可输入值
		var i;
		var c;
		if ((telval.charAt(0) == '-')
				|| (telval.charAt(String.length - 1) == '-'))
			wrongFlag = true;
		for (i = 0; i < telval.length; i++) {
			c = telval.charAt(i);
			if (Letters.indexOf(c) < 0)
				wrongFlag = true;
		}

		if (wrongFlag) {
			alert('电话号码格式错误')
			$('#telephone1').val('')
		}
	}

	function checkTel() {
		checkTel0($('#telephone1').val())
		//checkTel0($('#telephone2').val())
	}
</script>
