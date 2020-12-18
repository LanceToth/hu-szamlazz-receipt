<a href="/receipt/${(item.receipt.id)!""}">Vissza a nyugtára</a>
<form action="/saveitem" method="post">
	<div class="item">
	. tétel
		<div>
			<label>id</label>
			<label>megnevezes</label>
			<label>mennyiseg</label>
			<label>mennyisegiEgyseg</label>
			<label>nettoEgysegar</label>
			<label>afakulcs</label>
		</div>
		<div>
			<input type=text name="id" value="${(item.id)!""}" disabled />
			<input type=text name="megnevezes" value="${(item.megnevezes)!""}" />
			<input type=number name="mennyiseg" value="${(item.mennyiseg)!""}" />
			<input type=text name="mennyisegiEgyseg" value="${(item.mennyisegiEgyseg)!""}" />
			<input type=number name="nettoEgysegar" value="${(item.nettoEgysegar)!""}" />
			<input type=number name="afakulcs" value="${(item.afakulcs)!""}" />
		</div>
	</div>
	<input type="submit" value="Mentés">
</form>