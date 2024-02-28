<%@ include file="predlozak5.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom i meteo</title>
</head>
<body>
	<h1>Aerodrom</h1>
	Icao: ${aerodrom.icao} Naziv: ${aerodrom.naziv} Država:
	${aerodrom.drzava}
	
	<h2>Pregled meteoroloških podataka za odabrani aerodrom</h2>
	
	<table border=1>
		<tr>
			<td>Stanje oblaka</td>
			<td>${meteoPodaci.cloudsName}</td>
		</tr>
		<tr>
			<td>Vrijednost oblaka</td>
			<td>${meteoPodaci.cloudsValue}</td>
		</tr>
		<tr>
			<td>Jedinica vlažnosti</td>
			<td>${meteoPodaci.humidityUnit}</td>
		</tr>
		<tr>
			<td>Vlažnost</td>
			<td>${meteoPodaci.humidityValue}</td>
		</tr>
		<tr>
			<td>Posljednje ažuriranje</td>
			<td>${meteoPodaci.lastUpdate}</td>
		</tr>
		<tr>
			<td>Jedinica tlaka</td>
			<td>${meteoPodaci.pressureUnit}</td>
		</tr>
		<tr>
			<td>Tlak</td>
			<td>${meteoPodaci.pressureValue}</td>
		</tr>
		<tr>
			<td>Izlazak sunca</td>
			<td>${meteoPodaci.sunRise}</td>
		</tr>
		<tr>
			<td>Zalazak sunca</td>
			<td>${meteoPodaci.sunSet}</td>
		</tr>
		<tr>
			<td>Jedinica temperature</td>
			<td>${meteoPodaci.temperatureUnit}</td>
		</tr>
		<tr>
			<td>Maksimalna temperatura</td>
			<td>${meteoPodaci.temperatureMax}</td>
		</tr>
		<tr>
			<td>Minimalna temperatura</td>
			<td>${meteoPodaci.temperatureMin}</td>
		</tr>
		<tr>
			<td>Trenutna temperatura</td>
			<td>${meteoPodaci.temperatureValue}</td>
		</tr>
		<tr>
			<td>Oznaka vremena</td>
			<td>${meteoPodaci.weatherNumber}</td>
		</tr>
		<tr>
			<td>Vrijeme</td>
			<td>${meteoPodaci.weatherValue}</td>
		</tr>
	</table>
</body>
</html>