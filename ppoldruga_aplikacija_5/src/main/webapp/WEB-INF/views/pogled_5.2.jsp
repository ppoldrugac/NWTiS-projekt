<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Početni izbornik - korisnici</title>
<link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
<style>
body {
	font-family: "Roboto", sans-serif;
	margin: 0;
	padding: 0;
	background-color: #f2f2f2;
}

.okvir {
	max-width: 800px;
	margin: 20px;
}

.izbornik {
	list-style-type: none;
	padding: 0;
	margin: 0;
}

.izbornik li {
	margin-bottom: 10px;
}

.izbornik li a {
	display: block;
	padding: 12px 24px;
	text-decoration: none;
	color: #fff;
	background-color: #3498db;
	border-radius: 5px;
	transition: background-color 0.3s ease;
	font-size: 18px;
}

.izbornik li a:hover {
	background-color: #2980b9;
}

.izbornik li.povratak a {
	background-color: transparent;
	border: 2px solid rgba(46, 204, 113, 0.5);
	color: #000;
}

.izbornik li.povratak a:hover {
	background-color: rgba(46, 204, 113, 0.5);
	color: #fff;
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
	<div class="okvir">
		<h1>NWTiS projekt 2023</h1>
		<h2>Početni izbornik - KORISNICI</h2>
		<ul class="izbornik">
		<li class="povratak"><a href="${pageContext.servletContext.contextPath}/index.jsp">Povratak na početnu stranicu</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">Registracija korisnika</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">Prijavljivanje korisnika</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/korisnici/svi">Pregled korisnika</a></li>
			<br>
		</ul>
	</div>
</body>
</html>