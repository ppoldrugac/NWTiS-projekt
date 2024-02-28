INSERT INTO PUBLIC.PUBLIC.DNEVNIK
(VRSTA, METODA, RUTA, DATUM)
VALUES 
('AP2', 'GET', 'http://webpredmeti:8080/ppoldruga_aplikacija_2/api/nadzor/INFO DA', '2023-05-28 18:33:01.10'),
('AP2', 'GET', 'http://webpredmeti:8080/ppoldruga_aplikacija_2/api/nadzor/INFO DA', '2023-05-28 18:33:13.17'),
('AP2', 'GET', 'http://webpredmeti:8080/ppoldruga_aplikacija_2/api/nadzor/INFO NE', '2023-05-28 18:33:25.150'),
('AP2', 'GET', 'http://webpredmeti:8080/ppoldruga_aplikacija_2/api/nadzor/INFO NE', '2023-05-28 19:33:47.53');


SELECT vrsta, metoda, ruta, datum FROM DNEVNIK;

SELECT vrsta, metoda, ruta, datum FROM DNEVNIK WHERE vrsta = 'AP1';


GRANT SELECT, UPDATE, INSERT ON TABLE DNEVNIK TO APLIKACIJA;

GRANT SELECT, UPDATE, INSERT ON TABLE KORISNICI TO APLIKACIJA;

GRANT SELECT, UPDATE, INSERT ON TABLE AERODROMI_LETOVI TO APLIKACIJA;

SELECT * FROM PUBLIC.AIRPORTS;

SELECT icao, name, iso_country, municipality,coordinates FROM airports WHERE name LIKE '%cro%' AND iso_country = 'US';


SELECT coordinates FROM airports WHERE icao = 'LDZA';

SELECT iso_country FROM airports WHERE icao = 'LOWW';


SELECT korisnicko_ime, lozinka, ime, prezime FROM korisnici;

SELECT COUNT(*) AS broj_upisanih_korisnika FROM korisnici;

INSERT INTO PUBLIC.PUBLIC.AERODROMI_LETOVI
(ICAO, STATUS)
VALUES
('KSFO', true),
('KSEA', true),
('KLGA', true),
('KLAS', true),
('KLAX', true),
('RJTT', true),
('YMML', true),
('LFPO', true),
('UUDD', true),
('CYVR', true);

SELECT airports.icao, airports.name, airports.iso_country, airports.coordinates FROM airports JOIN aerodromi_letovi ON airports.icao = aerodromi_letovi.icao;


SELECT korisnicko_ime, lozinka FROM korisnici WHERE korisnicko_ime = 'ppoldruga' AND lozinka = '123456';

SELECT aerodromi_letovi.status AS status FROM aerodromi_letovi JOIN airports ON airports.icao = aerodromi_letovi.icao

