<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Predložak</title>
</head>
<body>
	<header>
		<p>
			Autor:
			<%=request.getAttribute("ime") + " " + request.getAttribute("prezime")%></p>
		<p>
			Predmet:
			<%=request.getAttribute("predmet")%></p>
		<p>
			Godina:
			<%=request.getAttribute("godina")%></p>
		<p>
			Verzija aplikacije:
			<%=request.getAttribute("verzija")%></p>
	</header>

	<form action="${pageContext.servletContext.contextPath}/index.jsp">
		<button type="submit" value="Povratak na početnu stranicu">Početna stranica</button>
	</form>
</body>
</html>