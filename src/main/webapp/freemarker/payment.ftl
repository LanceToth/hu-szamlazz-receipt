<#import "utils.ftl" as utils />
<link rel='stylesheet' href='/css/form.css'>
<a href="/receipt/${(payment.receipt.id)!""}">Vissza a nyugtára</a>
<form action="/savepayment" method="post">
	<input type=hidden name ="receipt" value ="${(item.receipt.id)!""}" />
	<div class="row">
		<label>Id</label>
		<input type=text name="id" value="${(payment.id)!""}" disabled />
	</div>
	<div class="row">
		<label>Fizetőeszköz</label>
		<@utils.select "fizetoeszkoz" payment.fizetoeszkoz fizmodList false/>
	</div>
	<div class="row">
		<label>Összeg</label>
		<input type=number name="osszeg" value="${(payment.osszeg)!""}" />
	</div>
	
	<input type="submit" value="Mentés">
</form>