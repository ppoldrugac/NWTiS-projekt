<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenosti aerodroma 1</title>
<style>
input[type="submit"] {
	margin-top: 15px;
}
</style>
</head>
<body>
	<form method="get"
		action="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenost2">
		<label for="icaoOd">Aerodrom od:</label> <input name="icaoOd"
			type="text"> <br> <br> <label for="drzava">Oznaka države</label>
		<input name="drzava" type="text"> <br> <br> <label
			for="km">Udaljenost</label> <input name="km" type="text">
		<br> <input type="submit" value="Učitaj podatke">
	</form>
</body>
</html>