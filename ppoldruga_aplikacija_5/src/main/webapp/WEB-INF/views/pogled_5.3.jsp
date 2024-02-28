
<%@ include file="predlozak5.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upravljanje poslužiteljem</title>
<style>
.gumb-container {
	display: flex;
	flex-direction: row;
	justify-content: left;
	align-items: center;
}

.gumb {
	display: inline-block;
	padding: 10px 20px;
	font-size: 16px;
	text-align: center;
	text-decoration: none;
	background-color: #3498db;
	color: #fff;
	border-radius: 5px;
	border: none;
	cursor: pointer;
	margin-right: 10px;
}
</style>
</head>
<body>
	<h1>Upravljanje poslužiteljem</h1>
	<div class="gumb-container">
		<form method="GET"
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/">
			<input type="hidden">
			<button class="gumb" type="submit">STATUS</button>
		</form>

		<form method="GET"
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/komanda">
			<input type="hidden" name="komanda" value="INIT">
			<button class="gumb" type="submit">INIT</button>
		</form>

		<form method="GET"
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/komanda">
			<input type="hidden" name="komanda" value="PAUZA">
			<button class="gumb" type="submit">PAUZA</button>
		</form>

		<form method="GET"
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/vrsta">
			<input type="hidden" name="vrsta" value="INFO DA">
			<button class="gumb" class="gumb" type="submit">INFO DA</button>
		</form>

		<form method="GET"
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/vrsta">
			<input type="hidden" name="vrsta" value="INFO NE">
			<button class="gumb" type="submit">INFO NE</button>
		</form>

		<form method="GET"
			action="${pageContext.servletContext.contextPath}/mvc/nadzor/komanda">
			<input type="hidden" name="komanda" value="KRAJ">
			<button class="gumb" type="submit">KRAJ</button>
		</form>
	</div>
			<p>Poruka: <%=request.getAttribute("odgovor")%></p>

</body>
</html>