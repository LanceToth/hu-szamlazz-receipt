<#import "utils.ftl" as utils />
<link rel='stylesheet' href='/css/form.css' />
<@utils.disablefields item.id item.receipt.status/>

<a href="/receipt/${(item.receipt.id)!0}">Vissza a nyugtára</a>
<#if utils.hasid>
	<form action="/updateitem/${(item.id)!0}" method="post">
<#else>
	<form action="/saveitem/${(item.receipt.id)!0}" method="post">
</#if>
<fieldset ${utils.disabletag}>
	<legend>Tétel</legend>
	<input type=hidden name ="receipt" value ="${(item.receipt.id)!0}" />
	<div class="row">
		<label>Id</label>
		<input type=text name="id" value="${(item.id)!0}" disabled />
	</div>
	<div class="row">
		<label>Megnevezés</label>
		<input type=text name="megnevezes" value="${(item.megnevezes)!""}" />
	</div>
	<div class="row">
		<label>Mennyiség</label>
		<input type=number name="mennyiseg" value="${(item.mennyiseg?c)!0}" />
	</div>
	<div class="row">
		<label>Mennyiségi egység</label>
		<input type=text name="mennyisegiEgyseg" value="${(item.mennyisegiEgyseg)!""}" />
	</div>
	<div class="row">
		<label>Nettó egységár</label>
		<input type=number name="nettoEgysegar" value="${(item.nettoEgysegar?c)!0}" />${item.receipt.penznem}
	</div>
	<div class="row">
		<label>Áfakulcs</label>
		<input type=number name="afakulcs" value="${(item.afakulcs?c)!0}" />%
	</div>
	
	<div class="row">
		<label for="netto">Nettó</label>
		<span id="netto">${(item.netto)!0}</span> ${item.receipt.penznem}
	</div>
	<div class="row">
		<label for="afa">Áfa</label>
		<span id="afa">${(item.afa)!0}</span> ${item.receipt.penznem}
	</div>
	<div class="row">
		<label for="brutto">Bruttó</label>
		<span id="brutto">${(item.brutto)!0}</span> ${item.receipt.penznem}
	</div>

	<input type="submit" value="Mentés">
</fieldset>
</form>