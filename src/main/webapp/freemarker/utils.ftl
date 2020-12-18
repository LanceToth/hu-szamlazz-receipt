<#macro select name field list description>
<select name="${name}">
    <#list list as enum>
    	<option value="${enum}" <#if enum = (field)!"">selected</#if> ><#if description = true>${enum.description}<#else>${enum}</#if></option>
    </#list>
</select>
</#macro>