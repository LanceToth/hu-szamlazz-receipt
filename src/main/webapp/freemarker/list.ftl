<a href="/addreceipt">Új nyugta</a>
<#list receipts>
	<table>
		<tr>
			<th>Id</th>
			<th>Kelt</th>
			<th>Fizetési mód</th>
			<th>Bruttó összeg</th>
		</tr>
		<#items as receipt>
			<tr>
				<td><a href="/receipt/${receipt.id}">${receipt.id}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.kelt}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.fizmod}</a></td>
				<td><a href="/receipt/${receipt.id}">${receipt.brutto} ${receipt.penznem}</a></td>
			</tr>
		</#items>
	</table>
 <#else><br />Nothing to list
</#list>
