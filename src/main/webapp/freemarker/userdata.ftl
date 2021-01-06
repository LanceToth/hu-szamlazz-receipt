
<link rel='stylesheet' href='/css/form.css' />

<a href="/list">Vissza a listához</a>
<form action="/setuserdata" method="post">
<fieldset>
<legend>Beállítások</legend>
	<div class="row">
		<label for="elotag">Számla agent kulcs</label>
		<input type=text name="agentKey" value="${(userdata.agentKey)!""}" />
	</div>

	<input type="submit" value="Mentés">

</fieldset>
</form>
