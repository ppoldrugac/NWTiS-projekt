<%@ include file="predlozak5.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dodan aerodrom za preuzimanje letova</title>
</head>
<body>
	<p>
		Dodavanje aerodroma za preuzimanje podataka o polascima:
		<%
	if ((boolean) request.getAttribute("dodan") == true){
	%>
		Uspješno dodan!
		<%
	} else {
	%>
		Neuspjelo dodavanje!
		<%
	}
	%>
	</p>
</body>
</html>