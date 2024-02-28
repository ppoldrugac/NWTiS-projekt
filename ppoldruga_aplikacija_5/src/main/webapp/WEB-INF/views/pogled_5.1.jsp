<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NWTiS Projekt</title>
<link href="https://fonts.googleapis.com/css?family=Roboto"
	rel="stylesheet">
<style>
body {
	font-family: "Roboto", sans-serif;
	margin: 0;
	padding: 0;
	background-color: #f2f2f2;
}

.container {
	max-width: 800px;
	margin: 20px;
}

.menu {
	list-style-type: none;
	padding: 0;
	margin: 0;
}

.menu li {
	margin-bottom: 10px;
}

.menu li a {
	display: block;
	padding: 12px 24px;
	text-decoration: none;
	color: #fff;
	background-color: #3498db;
	border-radius: 5px;
	transition: background-color 0.3s ease;
	font-size: 18px;
}

.menu li a:hover {
	background-color: #2980b9;
}

h1 {
	font-size: 24px;
	margin-bottom: 10px;
}

h2 {
	font-size: 18px;
	margin-bottom: 20px;
}
</style>
</head>
<body>
	<div class="container">
		<h1>NWTiS projekt 2023</h1>
		<h2>Početna stranica</h2>
		<ul class="menu">
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Aktivnosti
					vezane uz korisnike</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/nadzor">Upravljanje
					poslužiteljem</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetna">Aktivnosti
					vezane uz aerodrome</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/dnevnik">Pregled
					zapisa dnevnika</a></li>
			<br>
		</ul>
	</div>
</body>
</html>