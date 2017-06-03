<%@ page import="ots.Teacher"%>
<dl class="dl-horizontal">
	<dt>
		<label for="userName"> <g:message code="teacher.userName.label" default="User Name" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:textField name="userName" required="" value="${teacherInstance?.userName}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="password"> <g:message code="teacher.password.label" default="Password" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:field type="password" name="password" required="" value="${teacherInstance?.password}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="fullName"> <g:message code="teacher.fullName.label" default="Full Name" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:textField name="fullName" required="" value="${teacherInstance?.fullName}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="email"> <g:message code="teacher.email.label" default="Email" />

		</label>
	</dt>
	<dd>
		<g:field type="email" name="email" value="${teacherInstance?.email}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="telephone1"> <g:message code="teacher.telephone1.label" default="Telephone1" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:textField name="telephone1" value="${teacherInstance.telephone1}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="telephone2"> <g:message code="teacher.telephone2.label" default="Telephone2" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:textField name="telephone2" value="${teacherInstance.telephone2}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="entryTime"> <g:message code="teacher.entryTime.label" default="加盟日期" /> <span
			class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:datePicker name="entryTime" precision="day" value="${teacherInstance?.entryTime}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="sex"> <g:message code="teacher.sex.label" default="Sex" />

		</label>
	</dt>
	<dd>
		<g:select name="sex" from="${teacherInstance.constraints.sex.inList}" value="${teacherInstance?.sex}"
			valueMessagePrefix="teacher.sex" noSelection="['': '']" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="degree"> <g:message code="teacher.degree.label" default="Degree" />

		</label>
	</dt>
	<dd>
		<g:select name="degree" from="${teacherInstance.constraints.degree.inList}"
			value="${teacherInstance?.degree}" valueMessagePrefix="teacher.degree" noSelection="['': '']" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="briefComment"> <g:message code="teacher.briefComment.label" default="Brief Comment" />

		</label>
	</dt>
	<dd>
		<g:textArea name="briefComment" cols="40" rows="5" maxlength="2000"
			value="${teacherInstance?.briefComment}" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="identificationNum"> <g:message code="teacher.identificationNum.label"
				default="Identification Num" /> <span class="required-indicator">*</span>
		</label>
	</dt>
	<dd>
		<g:field name="identificationNum" type="number" value="${teacherInstance.identificationNum}" required="" />
	</dd>
</dl>

<dl class="dl-horizontal">
	<dt>
		<label for="toShowKnowledgeGraph"> <g:message code="teacher.toShowKnowledgeGraph.label" default="Show Knowledge Graph" />

		</label>
	</dt>
	<dd>
		<g:checkBox name="toShowKnowledgeGraph" value="${teacherInstance?.toShowKnowledgeGraph}" />
	</dd>
</dl>
	
<g:if test="${session.user instanceof ots.AdminUser}">

	<dl class="dl-horizontal">
		<dt>
			<label for="active"> <g:message code="teacher.active.label" default="Active" />

			</label>
		</dt>
		<dd>
			<g:checkBox name="active" value="${teacherInstance?.active}" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="teacherType"> <g:message code="teacher.teacherType.label" default="Teacher Type" />

			</label>
		</dt>
		<dd>
			<g:select name="teacherType" from="${teacherInstance.constraints.teacherType.inList}"
				value="${teacherInstance?.teacherType}" valueMessagePrefix="teacher.teacherType" noSelection="['': '']" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="nativePlace"> <g:message code="teacher.nativePlace.label" default="Native Place" />

			</label>
		</dt>
		<dd>
			<g:select name="nativePlace" from="${teacherInstance.constraints.nativePlace.inList}"
				value="${teacherInstance?.nativePlace}" valueMessagePrefix="teacher.nativePlace" noSelection="['': '']" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="residentAddr"> <g:message code="teacher.residentAddr.label" default="Resident Addr" />

			</label>
		</dt>
		<dd>
			<g:select name="residentAddr" from="${teacherInstance.constraints.residentAddr.inList}"
				value="${teacherInstance?.residentAddr}" valueMessagePrefix="teacher.residentAddr"
				noSelection="['': '']" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="status"> <g:message code="teacher.status.label" default="Status" />

			</label>
		</dt>
		<dd>
			<g:select name="status" from="${teacherInstance.constraints.status.inList}"
				value="${teacherInstance?.status}" valueMessagePrefix="teacher.status" noSelection="['': '']" />
		</dd>
	</dl>

	<dl class="dl-horizontal">
		<dt>
			<label for="school"> <g:message code="teacher.school.label" default="School" />

			</label>
		</dt>
		<dd>
			<g:select id="school" name="school.id" from="${ots.School.list()}" optionKey="id"
				value="${teacherInstance?.school?.id}" class="many-to-one" noSelection="['null': '']" />
		</dd>
	</dl>
</g:if>

