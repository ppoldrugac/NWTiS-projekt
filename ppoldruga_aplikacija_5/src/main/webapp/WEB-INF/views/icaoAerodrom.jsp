<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Unašanje icao za jedan aerodrom</title>
<style>
input[type="submit"] {
	margin-top: 15px;
}
</style>
</head>
<body>
	<form method="get"
		action="${pageContext.servletContext.contextPath}/mvc/aerodromi/icao">
		<label for="icao">Icao:</label> <input name="icao" type="text">
		<br> <input type="submit" value="Učitaj podatke">
	</form>
</body>
</html>