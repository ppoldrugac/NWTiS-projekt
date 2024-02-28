<%@page
	import="org.foi.nwtis.ppoldruga.projekt.ws.WsKorisnici.endpoint.Korisnik"%>
<%@ include file="predlozak5.jsp"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled svih korisnika</title>
<style>
input[type="submit"] {
	margin-top: 15px;
	padding: 5px 10px;
	border: none;
	border-radius: 5px;
	background-color: #3498db;
	color: #fff;
	font-size: 14px;
	cursor: pointer;
	transition: background-color 0.3s ease;
	height: 30px;
}

td a {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100%;
	width: 100%;
	text-decoration: none;
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
	<%
	boolean logiran = (boolean) request.getAttribute("logiran");
	if (logiran) {
	%>
	<h1>Pregled svih korisnika</h1>
	<br>
	<p id="ukupanBrojUpisanihKorisnika"></p>
	<br>
	<div class="filter">
		<p>Filtriranje po imenu i/ili prezimenu:</p>
		<form method="get"
			action="${pageContext.servletContext.contextPath}/mvc/korisnici/svi">
			<label class="oznaka" for="traziImeKorisnika">Ime:</label> <input
				class="unos" name="traziImeKorisnika" type="text"><br>
			<label class="oznaka" for="traziPrezimeKorisnika">Prezime:</label> <input
				class="unos" name="traziPrezimeKorisnika" type="text"> <br>
			<input type="submit" value="Filtriraj"><br> <br>
		</form>
	</div>
	<table border=1>
		<tr>
			<th>Korisničko ime</th>
			<th>Lozinka</th>
			<th>Ime</th>
			<th>Prezime</th>
			<th>Email</th>
		</tr>
		<%
		List<Korisnik> lista = (List<Korisnik>) request.getAttribute("korisnici");
		for (Korisnik k : lista) {
		%>
		<tr>
			<td><%=k.getKorIme()%></td>
			<td><%=k.getLozinka()%></td>
			<td><%=k.getIme()%></td>
			<td><%=k.getPrezime()%></td>
			<td><%=k.getEmail()%></td>
		</tr>
		<%
		}
		%>
	</table>
	<br>
	<%
	} else {
	%>
	<p>Niste logirani!</p>
	<%
	}
	%>
	<script>
		var uticnica = new WebSocket(
				'ws://localhost:8080/ppoldruga_aplikacija_4/info');
		uticnica.onmessage = function(event) {
			var poruka = event.data;
			var element = document
					.getElementById("ukupanBrojUpisanihKorisnika");
			element.textContent = event.data;
		}
	</script>
</body>
</html>