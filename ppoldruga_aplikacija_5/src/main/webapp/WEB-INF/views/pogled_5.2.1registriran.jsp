<%@ include file="predlozak5.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Prijava</title>
<style>
input[type="submit"] {
	margin-top: 15px;
	padding: 10px 20px;
	border: none;
	border-radius: 5px;
	background-color: #3498db;
	color: #fff;
	font-size: 16px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.oznaka {
	display: block;
	margin-bottom: 5px;
}

.unos {
	margin-bottom: 10px;
	padding: 5px;
	border: 1px solid #ccc;
	border-radius: 3px;
	width: 200px;
}

.unos:focus {
	outline: none;
	border-color: #3498db;
}
</style>
</head>
<body>
	<p>
		REGISTRACIJA:
		<%
	if (request.getAttribute("korisnik") != null){
	%>
		Uspješna registracija!
		<%
	} else {
	%>
		Neuspjela registracija
		<%
	}
	%>
	</p>
</body>
</html>