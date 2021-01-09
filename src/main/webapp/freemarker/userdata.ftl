
<link rel='stylesheet' href='/css/form.css' />

<a href="/list">Vissza a listához</a>
<form action="/setuserdata" method="post">
<fieldset>
<legend>Beállítások</legend>
	<div class="row">
		<label for="szamlaagentkulcs">Számla agent kulcs</label>
		<input type=text name="szamlaagentkulcs" value="${(userdata.szamlaagentkulcs)!""}" />
	</div>

	<input type="submit" value="Mentés">

</fieldset>
</form>
