<#import "utils.ftl" as utils />
<link rel='stylesheet' href='/css/form.css' />
<@utils.disablefields receipt.id receipt.status />
<#assign overpay =  ((receipt.brutto < receipt.fizetesOsszeg)?string("class='error'","")) />
<#assign underpay = ((receipt.fizetesOsszeg < receipt.brutto)?string("class='error'","")) />
<#assign paid = (receipt.fizetesOsszeg > 0 && receipt.fizetesOsszeg == receipt.brutto) />

<#-- siker/hiba kezelése -->
<#if sikeres??>
	<#assign message = (sikeres?string((receipt.nyugtaszam)!"nyugtaszam", (hibauzenet)!"")) />
	<@popup sikeres message />
	<#if pdf??>
		<@utils.pdfwindow pdf true/>
	</#if>
</#if>

<a href="/list">Vissza a listához</a>
<#if utils.hasid>
	<form action="/updatereceipt/${receipt.id}" method="post">
<#else>
	<form action="/savereceipt" method="post">
</#if>
<fieldset ${utils.disabletag}>
<legend>Nyugta</legend>
	<div class="row">
		<label for="id">Id</label>
		<input type=number name="id" value="${(receipt.id)!0}" disabled/>
	</div>
	<div class="row">
		<label for="kelt">Kelt</label>
		<input type=date name="kelt" value="${(receipt.kelt)!""}" disabled/>
	</div>
	<div class="row">
		<label for="nyugtaszam">Nyugtaszám</label>
		<input type=text name="nyugtaszam" value="${(receipt.nyugtaszam)!""}" disabled/>
	</div>
	<div class="row">
		<label for="elotag">Előtag</label>
		<input type=text name="elotag" value="${(receipt.elotag)!""}" />
	</div>
	<div class="row">
		<label for="fizmod">Fizetési mód</label>
		<@utils.select "fizmod" receipt.fizmod fizmodList false/>
	</div>
	<div class="row">
		<label for="penznem">Pénznem</label>
		<input type=text name="penznem" value="${(receipt.penznem)!""}" />
	</div>
	<div class="row">
		<label for="pdfSablon">PDF sablon</label>
		<@utils.select "pdfSablon" receipt.pdfSablon pdfSablonList true/>		
	</div>
	
	<#if utils.hasid>
		<div class="row">
			<label for="status">Státusz</label>
			<span id="status" class="${(receipt.status)!""}">${(receipt.status.description)!""}</span>		
		</div>
	</#if>
	
	<div class="row">
		<label for="brutto">Bruttó</label>
		<span id="brutto" ${overpay}>${(receipt.brutto)!0} ${receipt.penznem}</span>
	</div>
	<div class="row">
		<label for="fizosszeg">Fizetett összeg</label>
		<span id="fizosszeg" ${underpay}>${(receipt.fizetesOsszeg)!0} ${receipt.penznem}</span>
	</div>
	
	<input type="submit" value="Mentés">

<#if !utils.disabled && utils.hasid && paid>
	<br /><a href="/sendreceipt/${receipt.id}">Küldés</a>
	<#--br /><a href="/export/${receipt.id}" target="_blank">Export</a-->
</#if>
</fieldset>
</form>

<#if utils.hasid>
<fieldset>
	<legend>Tételek</legend>
	<#if !utils.disabled> 
		<a href="/additem/${receipt.id}">Új tétel</a>
	</#if>
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
					<td><a href="/item/${item.id}">${item.id}</a></td>
					<td><a href="/item/${item.id}">${item.megnevezes}</a></td>
					<td><a href="/item/${item.id}">${item.mennyiseg} ${item.mennyisegiEgyseg}</a></td>
					<td><a href="/item/${item.id}">${item.brutto} ${receipt.penznem}</a></td>
				</tr>
			</#items>
		</table>
	<#else><#if !utils.disabled><br /></#if>Még nincs
	</#list>
</fieldset>

<fieldset>
	<legend>Kifizetések</legend>
	<#if !utils.disabled>
		<a href="/addpayment/${receipt.id}">Új kifizetés</a>
	</#if>
	<#list receipt.payments>
		<table>
			<tr>
				<th>Id</th>
				<th>Fizetőeszköz</th>
				<th>Bruttó összeg</th>
			</tr>
			<#items as payment>
				<tr>
					<td><a href="/payment/${payment.id}">${payment.id}</a></td>
					<td><a href="/payment/${payment.id}">${payment.fizetoeszkoz}</a></td>
					<td><a href="/payment/${payment.id}">${payment.osszeg} ${receipt.penznem}</a></td>
				</tr>
			</#items>
		</table>
	<#else><#if !utils.disabled><br /></#if>Még nincs
	</#list>
</fieldset>
</#if>

<#macro popup success message>
<div class="popup">
	<div onclick="this.parentNode.classList.toggle('hidden');" class="closer">X</div>
	<#if (success!false) == true>
		SIKERES TRANZAKCIÓ
		<br />
		<br />Nyugtaszám: ${message!""}
		<br /><a href="javascript:document.getElementById('pdfwindow').classList.toggle('hidden');">PDF letöltés</a>
	<#else>
		SIKERTELEN TRANZAKCIÓ
		<br />
		<br />Hibaüzenet: ${message!""}
	</#if>
</div>
</#macro>