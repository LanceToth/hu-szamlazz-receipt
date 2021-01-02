
<link rel='stylesheet' href='/css/form.css'>
<a href="/receipt/${(item.receipt.id)!""}">Vissza a nyugtára</a>
<form action="/saveitem/${(item.receipt.id)!""}" method="post">
	<input type=hidden name ="receipt" value ="${(item.receipt.id)!""}" />
	<div class="row">
		<label>id</label>
		<input type=text name="id" value="${(item.id)!""}" disabled />
	</div>
	<div class="row">
		<label>megnevezes</label>
		<input type=text name="megnevezes" value="${(item.megnevezes)!""}" />
	</div>
	<div class="row">
		<label>mennyiseg</label>
		<input type=number name="mennyiseg" value="${(item.mennyiseg)!""}" />
	</div>
	<div class="row">
		<label>mennyisegiEgyseg</label>
		<input type=text name="mennyisegiEgyseg" value="${(item.mennyisegiEgyseg)!""}" />
	</div>
	<div class="row">
		<label>nettoEgysegar</label>
		<input type=number name="nettoEgysegar" value="${(item.nettoEgysegar)!""}" />
	</div>
	<div class="row">
		<label>afakulcs</label>
		<input type=number name="afakulcs" value="${(item.afakulcs)!""}" />
	</div>

	<input type="submit" value="Mentés">
</form>