<%@ include file="predlozak5.jsp"%>
<%@page import="org.foi.nwtis.podaci.Dnevnik"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled svih zapisa dnevnika</title>
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
	<h1>Pregled dnevnika</h1>
	<div class="filter">
		<p>Filtriranje po vrsti:</p>
		<form method="get"
			action="${pageContext.servletContext.contextPath}/mvc/dnevnik">
			<label class="oznaka" for="vrsta">Vrsta:</label> <input class="unos"
				name="vrsta" type="text"><br> <br> <input
				type="submit" value="Filtriraj"><br> <br>
		</form>
	</div>
	<table border=1>
		<tr>
			<th>Vrsta</th>
			<th>Metoda</th>
			<th>Ruta</th>
			<th>Datum</th>
		</tr>
		<%
		List<Dnevnik> lista = (List<Dnevnik>) request.getAttribute("zapisiDnevnik");
		for (Dnevnik d : lista) {
		%>
		<tr>
			<td><%=d.getVrsta()%></td>
			<td><%=d.getMetoda()%></td>
			<td><%=d.getRuta()%></td>
			<td><%=d.getDatum()%></td>
		</tr>
		<%
		}
		%>
	</table>
	<div class="gumbovi">
		<form action="${pageContext.servletContext.contextPath}/mvc/dnevnik">
			<%
			String odBrojaPoc = "1";
			String brojPoc = String.valueOf(request.getAttribute("brojRedova"));
			%>
			<input type="hidden" name="odBroja" value="<%=odBrojaPoc%>">
			<input type="hidden" name="broj" value="<%=brojPoc%>">
			<button type="submit">Početak</button>
		</form>

		<form action="${pageContext.servletContext.contextPath}/mvc/dnevnik">
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
				type="hidden" name="vrsta" value="${param.vrsta}">
			<button type="submit">Prethodna stranica</button>
		</form>

		<form action="${pageContext.servletContext.contextPath}/mvc/dnevnik">
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
				type="hidden" name="vrsta" value="${param.vrsta}">
			<button type="submit">Sljedeća stranica</button>
		</form>
	</div>
</body>
</html>
