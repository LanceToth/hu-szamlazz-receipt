<#macro select name field list description>
<select name="${name}">
    <#list list as enum>
    	<option value="${enum}" <#if enum = (field)!"">selected</#if> ><#if description = true>${enum.description}<#else>${enum}</#if></option>
    </#list>
</select>
</#macro>

<#macro pdfwindow pdf hidden>
<div id="pdfwindow" class="pdfwindow <#if hidden!true>hidden</#if>">
	<div onclick="this.parentNode.classList.toggle('hidden');" class="closer">X</div>
	<object type="application/pdf" id="pdf" style="width: 100%; height: 100%" data="data:application/pdf;base64,${pdf!""}"></object>
</div>
</#macro>

<#macro disablefields id status>
<#assign hasid = (0 != (id)!0) />
<#assign receiptsent = ("S" == (status)!"") />
<#assign disabled = (hasid && receiptsent) />
<#assign disabletag = disabled?string("disabled='disabled'","") />
</#macro>