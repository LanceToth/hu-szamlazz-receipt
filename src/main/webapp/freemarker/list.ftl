<#import "utils.ftl" as utils />
<link rel='stylesheet' href='/css/form.css' />
<#if pdf??>
	<@utils.pdfwindow pdf false/>
</#if>
<a href="/addreceipt">Új nyugta</a>
<a href="/userdata" style="float: right;">Beállítások</a>
<#list receipts>
	<table>
		<tr>
			<th>Id</th>
			<th>Kelt</th>
			<th>Előtag</th>
			<th>Fizetési mód</th>
			<th>Bruttó összeg</th>
			<th>Státusz</th>
			<th>Nyugtaszám</th>
			<th>Letöltés</th>
		</tr>
		<#items as receipt>
			<tr>
				<td><a href="/receipt/${receipt.id}">${receipt.id}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.kelt}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.elotag}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.fizmod}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.brutto} ${receipt.penznem}</a></td>
				<td><a href="/receipt/${receipt.id}"  class="${(receipt.status)!""}">${receipt.status.description}</a></td>
				<td><#if receipt.nyugtaszam??><a href="/receipt/${receipt.id}">${receipt.nyugtaszam}</a></#if></td>
				<td><#if receipt.nyugtaszam??><a href="/download/${receipt.id}">Letöltés</a></#if></td>
			</tr>
		</#items>
	</table>
 <#else><br />Még nincs
</#list>
