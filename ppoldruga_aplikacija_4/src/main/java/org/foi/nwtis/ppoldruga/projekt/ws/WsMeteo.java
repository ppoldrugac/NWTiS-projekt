package org.foi.nwtis.ppoldruga.projekt.ws;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@WebService(serviceName = "meteo")
public class WsMeteo {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  private ServletContext kontekst;

  @WebMethod
  public MeteoPodaci dajMeteo(@WebParam String icao) {

    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");

    String apiKljuc = konfig.dajPostavku("OpenWeatherMap.apikey");

    OWMKlijent owmKlijent = new OWMKlijent(apiKljuc);

    MeteoPodaci meteoPodaci = null;

    
    try {
      
      Response response = ClientBuilder.newClient()
          .target("http://webpredmeti:8080/ppoldruga_aplikacija_2/api/aerodromi/" + icao)
          .request(MediaType.APPLICATION_JSON).get();
      
      if (response.getStatus() == Response.Status.OK.getStatusCode()) {
        
        Aerodrom a = response.readEntity(Aerodrom.class);
        
        org.foi.nwtis.podaci.Lokacija lokacija = a.getLokacija();
        
        if (lokacija != null) {
          System.out.println(lokacija.getLongitude());
          System.out.println(lokacija.getLatitude());
          meteoPodaci =
              owmKlijent.getRealTimeWeather(lokacija.getLongitude(), lokacija.getLatitude());
        } else {
          System.out.println("Neuspješan GET zahtjev za dobivanje lokacije aerodroma");
        }
      } else {
        System.out.println("Neuspješan GET zahtjev za dobivanje aerodroma");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return meteoPodaci;
  }

}
