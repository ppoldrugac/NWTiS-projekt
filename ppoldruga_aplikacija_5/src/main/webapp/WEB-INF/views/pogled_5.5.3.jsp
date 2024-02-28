<%@page
	import="org.foi.nwtis.ppoldruga.projekt.ws.WsAerodromi.endpoint.Aerodrom"%>
<%@ include file="predlozak5.jsp"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled aerodroma za polaske</title>
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

.gumbovi {
	display: flex;
	justify-content: flex-start;
	gap: 10px;
	margin-top: 20px;
}

.gumbovi form {
	display: flex;
}

.gumbovi button {
	width: 150px;
}
</style>
</head>
<body>
	<h1>Pregled aerodroma za koje se preuzimaju podaci o polascima</h1>
	<br>
	<p id="ukupanBrojUpisanihAerodroma"></p>
	<table border=1>
		<tr>
			<th>Icao</th>
			<th>Naziv</th>
			<th>Država</th>
			<th>Kordinate</th>
			<th>Status</th>
			<th>Promijeni status</th>
		</tr>
		<%
		List<Aerodrom> lista = (List<Aerodrom>) request.getAttribute("aerodromi");
		List<String> listaStatusa = (List<String>) request.getAttribute("statusi");
		int brojac = 0;
		for (Aerodrom a : lista) {
		%>
		<tr>
			<td><%=a.getIcao()%></td>
			<td><%=a.getNaziv()%></td>
			<td><%=a.getDrzava()%></td>
			<td><%=a.getLokacija().getLatitude() + " / " + a.getLokacija().getLongitude()%></td>
			<td>
				<%
				if (listaStatusa.get(brojac).equals("TRUE")) {
					out.println("Da");
				} else {
					out.println("Pauza");
				}
				%>
			</td>
			<%
			if (listaStatusa.get(brojac).equals("TRUE")) {
			%>
			<td><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pauziraj?icao=<%=a.getIcao()%>">Promijeni
					status</a></td>
			<%
			} else {
			%><td><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aktiviraj?icao=<%=a.getIcao()%>">Promijeni
					status</a></td>
			<%
			}
			%>
		</tr>
		<%
		brojac++;
		}
		%>
	</table>
	<br>
	<script>
		var uticnica = new WebSocket(
				'ws://localhost:8080/ppoldruga_aplikacija_4/info');
		uticnica.onmessage = function(event) {
			var poruka = event.data;
			var element = document
					.getElementById("ukupanBrojUpisanihAerodroma");
			element.textContent = event.data;
		}
	</script>
</body>
</html>