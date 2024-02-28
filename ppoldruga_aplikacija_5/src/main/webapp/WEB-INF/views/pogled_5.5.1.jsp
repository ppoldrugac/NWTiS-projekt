<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@ include file="predlozak5.jsp"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled svih aerodroma</title>
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
	<h1>Pregled svih aerodroma</h1>
	<br>
	<p id="ukupanBrojUpisanihAerodroma"></p>
	<div class="filter">
		<p>Filtriranje po nazivu i/ili državi aerodroma:</p>
		<form method="get"
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">
			<label class="oznaka" for="traziNaziv">Naziv:</label> <input
				class="unos" name="traziNaziv" type="text"><br> <label
				class="oznaka" for="traziDrzavu">Država:</label> <input class="unos"
				name="traziDrzavu" type="text"> <br> <input
				type="submit" value="Filtriraj"><br> <br>
		</form>
	</div>
	<table border=1>
		<tr>
			<th>Icao</th>
			<th>Naziv</th>
			<th>Država</th>
			<th>Lokacija</th>
			<th>Dodaj za preuzimanje podataka o polascima</th>
		</tr>
		<%
		List<Aerodrom> lista = (List<Aerodrom>) request.getAttribute("aerodromi");
		for (Aerodrom a : lista) {
		%>
		<tr>
			<td><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icao?icao=<%=a.getIcao()%>"><%=a.getIcao()%></a></td>
			<td><%=a.getNaziv()%></td>
			<td><%=a.getDrzava()%></td>
			<td><%=a.getLokacija().getLatitude() + " / " + a.getLokacija().getLongitude()%></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/mvc/aerodromi/dodaj?icao=<%=a.getIcao()%>">Dodaj</a></td>
		</tr>
		<%
		}
		%>
	</table>
	<br>
	<div class="gumbovi">
		<form
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">
			<%
			String odBrojaPoc = "1";
			String brojPoc = String.valueOf(request.getAttribute("brojRedova"));
			%>
			<input type="hidden" name="odBroja" value="<%=odBrojaPoc%>">
			<input type="hidden" name="broj" value="<%=brojPoc%>">
			<button type="submit">Početak</button>
		</form>

		<form
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">
			<%
			String odBrojaPreth = request.getParameter("odBroja");
			String brojPreth = request.getParameter("broj");

			if (odBrojaPreth == null || brojPreth == null) {
				odBrojaPreth = "10";
				brojPreth = String.valueOf(request.getAttribute("brojRedova"));
			} else {
				int odBrojaPrethInt = Integer.parseInt(odBrojaPreth);
				int brojPrethInt = Integer.parseInt(brojPreth);
				odBrojaPrethInt -= brojPrethInt;
				odBrojaPrethInt = Math.max(odBrojaPrethInt, 1);
				odBrojaPreth = String.valueOf(odBrojaPrethInt);
			}
			%>
			<input type="hidden" name="odBroja" value="<%=odBrojaPreth%>">
			<input type="hidden" name="broj" value="<%=brojPreth%>"> <input
				type="hidden" name="traziNaziv" value="${param.traziNaziv}">
			<input type="hidden" name="traziDrzavu" value="${param.traziDrzavu}">
			<button type="submit">Prethodna stranica</button>
		</form>

		<form
			action="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">
			<%
			String odBroja = request.getParameter("odBroja");
			String broj = request.getParameter("broj");

			if (odBroja == null || broj == null) {
				odBroja = "10";
				broj = String.valueOf(request.getAttribute("brojRedova"));
			} else {
				int odBrojaInt = Integer.parseInt(odBroja);
				int brojInt = Integer.parseInt(broj);
				odBrojaInt += brojInt;
				odBroja = String.valueOf(odBrojaInt);
			}
			%>
			<input type="hidden" name="odBroja" value="<%=odBroja%>"> <input
				type="hidden" name="broj" value="<%=broj%>"> <input
				type="hidden" name="traziNaziv" value="${param.traziNaziv}">
			<input type="hidden" name="traziDrzavu" value="${param.traziDrzavu}">
			<button type="submit">Sljedeća stranica</button>
		</form>
	</div>
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