<#import "utils.ftl" as utils />
<a href="/list">Return to list</a>
<form action="/savereceipt" method="post">
	<div>
		<label>id</label>
		<label>elotag</label>
		<label>fizmod</label>
		<label>penznem</label>
		<label>pdf_sablon</label>
	</div>
	<div>
		<input type=text name="id" value="${(receipt.id)!""}" disabled/>
		<input type=text name="elotag" value="${(receipt.elotag)!""}" />
  		<@utils.select "fizmod" receipt.fizmod fizmodList false/>
		<input type=text name="penznem" value="${(receipt.penznem)!""}" />
		<@utils.select "pdfSablon" receipt.pdfSablon pdfSablonList true/>		
	</div>
	
	<input type="submit" value="Mentés">
</form>

<#if 0 != (receipt.brutto)!0>
	<a href="/sendreceipt/${receipt.id}">Küldés</a>
</#if>

<#if 0 != (receipt.id)!0>
	<a href="/additem/${receipt.id}">Új tétel</a>
	<#list receipt.items>
		<table>
			<tr>
				<th>Id</th>
				<th>Termék</th>
				<th>Mennyiség</th>
				<th>Bruttó összeg</th>
			</tr>
			<#items as item>
				<tr>
					<td><a href="/item/${receipt.id}/${item.id}">${item.id}</a></td>
					<td><a href="/item/${receipt.id}/${item.id}">${item.megnevezes}</a></td>
					<td><a href="/item/${receipt.id}/${item.id}">${item.mennyiseg} ${item.mennyisegiEgyseg}</a></td>
					<td><a href="/item/${receipt.id}/${item.id}">${item.brutto}</a></td>
				</tr>
			</#items>
		</table>
	<#else><br />Nothing to list
	</#list>
	
	<a href="/addpayment/${receipt.id}">Új fizetés</a>
	<#list receipt.payments>
		<table>
			<tr>
				<th>Id</th>
				<th>Fizetőeszköz</th>
				<th>Bruttó összeg</th>
			</tr>
			<#items as payment>
				<tr>
					<td><a href="/payment/${receipt.id}/${payment.id}">${payment.id}</a></td>
					<td><a href="/payment/${receipt.id}/${payment.id}">${payment.fizetoeszkoz}</a></td>
					<td><a href="/payment/${receipt.id}/${payment.id}">${payment.osszeg}</a></td>
				</tr>
			</#items>
		</table>
	<#else><br />Nothing to list
	</#list>
</#if>

<#macro popup success message>
<div class="popup">
	<#if (success) == true>
		SIKERES TRANZAKCIÓ
		Nyugtaszám: ${message}
	<#else>
		SIKERTELEN TRANZAKCIÓ
		Hibaüzenet: ${message}
	</#if>
</div>
</#macro>