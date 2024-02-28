<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Početni izbornik - aerodromi</title>
<link href="https://fonts.googleapis.com/css?family=Roboto"
	rel="stylesheet">
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
		<h2>Početni izbornik - AERODROMI</h2>
		<ul class="izbornik">
			<li class="povratak"><a
				href="${pageContext.servletContext.contextPath}/index.jsp">Povratak
					na početnu stranicu</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Pregled
					svih aerodroma</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/polasci">Pregled
					aerodroma za koje se preuzimaju podaci o polascima</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/unos2udaljenosti">Pregled
					udaljenosti između dva aerodroma unutar država preko kojih se leti
					te ukupna udaljenost</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/unos2udaljenosti2">Pregled
					udaljenosti između dva aerodroma</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/unos2udaljenosti3">Pregled
					aerodroma i udaljenosti do polaznog aerodroma unutar države
					odredišnog aerodroma koji su manje udaljeni od udaljenosti između
					polaznog i odredišnog aerodroma</a></li>
			<br>
			<li><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/unosIcaoDrzavaKm">Pregled
					aerodroma i udaljenosti do polaznog aerodroma unutar zadane države
					koje su manje od zadane udaljenosti</a></li>
			<br>
		</ul>
	</div>
</body>
</html>