<%@page import="org.foi.nwtis.podaci.UdaljenostKlasa"%>
<%@ include file="predlozak5.jsp"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled udaljenosti po državama između dva aerodroma i
	ukupne udaljenosti</title>
</head>
<body>
	<h1>Pregled udaljenosti po državama između dva aerodroma i ukupne
		udaljenosti</h1>
	<p>
		Icao od:
		<%=request.getAttribute("icaoOd")%></p>
	<p>
		Icao do:
		<%=request.getAttribute("icaoDo")%></p>
	<table border=1>
		<tr>
			<th>Država</th>
			<th>Udaljenost</th>
		</tr>
		<%
		float ukupnoKm = 0;
		List<UdaljenostKlasa> lista = (List<UdaljenostKlasa>) request.getAttribute("udaljenosti");
		for (UdaljenostKlasa u : lista) {
		  ukupnoKm += u.getKm();
		%>
		<tr>
			<td><%=u.getDrzava()%></td>
			<td><%=u.getKm()%></td>
		</tr>
		<%
		}
		%>
		<tr>
			<td>Ukupno:</td>
			<td><%=ukupnoKm%></td>
		</tr>
	</table>
</body>
</html>