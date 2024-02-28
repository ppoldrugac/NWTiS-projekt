<%@page import="org.foi.nwtis.podaci.UdaljenostKlasa"%>
<%@ include file="predlozak5.jsp"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled udaljenosti između dva aerodroma</title>
</head>
<body>
	<h1>Pregled udaljenosti između dva aerodroma</h1>
	<p>
		Icao od:
		<%=request.getAttribute("icaoOd")%></p>
	<p>
		Icao do:
		<%=request.getAttribute("icaoDo")%></p>
	<p>
		Udaljenost:
		<%=request.getAttribute("udaljenost")%></p>
</body>
</html>