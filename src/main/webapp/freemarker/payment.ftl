<#import "utils.ftl" as utils />
<link rel='stylesheet' href='/css/form.css' />
<@utils.disablefields payment.id payment.receipt.status/>

<a href="/receipt/${(payment.receipt.id)!0}">Vissza a nyugtára</a>
<#if utils.hasid>
	<form action="/updatepayment/${(payment.id)!0}" method="post">
<#else>
	<form action="/savepayment/${(payment.receipt.id)!0}" method="post">
</#if>
<fieldset ${utils.disabletag}>
	<legend>Kifizetés</legend>
	<input type=hidden name ="receipt" value ="${(payment.receipt.id)!0}" />
	<div class="row">
		<label>Id</label>
		<input type=number name="id" value="${(payment.id)!0}" disabled />
	</div>
	<div class="row">
		<label>Fizetőeszköz</label>
		<@utils.select "fizetoeszkoz" payment.fizetoeszkoz fizmodList false/>
	</div>
	<div class="row">
		<label>Összeg</label>
		<input type=number name="osszeg" value="${(payment.osszeg?c)!0}" />${payment.receipt.penznem}
	</div>
	
	<input type="submit" value="Mentés">
</fieldset>
</form>