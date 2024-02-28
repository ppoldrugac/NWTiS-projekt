<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@ include file="predlozak5.jsp"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenost 2</title>
</head>
<body>
	<h1>Pregled aerodroma i udaljenosti do polaznog aerodroma unutar
		zadane države koje su manje od zadane udaljenosti</h1>
	<p>
		Icao od:
		<%=request.getAttribute("icaoOd")%></p>
	<p>
		Država:
		<%=request.getAttribute("drzava")%></p>
	<p>
		Zadana udaljenost:
		<%=request.getAttribute("km")%></p>
	<table border=1>
		<tr>
			<th>Aerodrom</th>
			<th>Udaljenost</th>
		</tr>
		<%
		List<UdaljenostAerodrom> lista = (List<UdaljenostAerodrom>) request.getAttribute("udaljenosti");
		for (UdaljenostAerodrom ua : lista) {
		%>
		<tr>
			<td><%=ua.icao()%></td>
			<td><%=ua.km()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>