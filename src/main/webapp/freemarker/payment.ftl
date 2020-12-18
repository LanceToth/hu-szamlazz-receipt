<#import "utils.ftl" as utils />
<a href="/receipt/${(payment.receipt.id)!""}">Vissza a nyugtára</a>
<form action="/savepayment" method="post">
	<div class="payment">
		<div>
			<label>Id</label>
			<label>Fizetőeszköz</label>
			<label>Összeg</label>
		</div>
		<div>
			<input type=text name="id" value="${(payment.id)!""}" disabled />
			<@utils.select "fizetoeszkoz" payment.fizetoeszkoz fizmodList false/>
			<input type=number name="osszeg" value="${(payment.osszeg)!""}" />
		</div>
	</div>
	<input type="submit" value="Mentés">
</form>