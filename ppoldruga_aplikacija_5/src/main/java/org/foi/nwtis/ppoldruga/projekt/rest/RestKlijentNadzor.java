package org.foi.nwtis.ppoldruga.projekt.rest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentNadzor {

  public RestKlijentNadzor() {}

  public Response dohvatiStatus() {
    RestKKlijent rn = new RestKKlijent();

    Response odgovor = rn.dohvatiStatus();
    
    rn.close();

    return odgovor;
  }

  public Response dohvatiKomandu(String komanda) {
    RestKKlijent rn = new RestKKlijent();

    Response odgovor = rn.dohvatiKomandu(komanda);

    rn.close();

    return odgovor;
  }

  public Response dohvatiInfo(String vrsta) {
    RestKKlijent rn = new RestKKlijent();

    Response odgovor = rn.dohvatiKomandu(vrsta);

    rn.close();

    return odgovor;
  }


  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/ppoldruga_aplikacija_2/api";

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nadzor");
    }

    public Response dohvatiStatus() {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Response odgovor = request.get();
      
      // System.out.println("Ispis 2" + odgovor);

      return odgovor;
    
    }

    public Response dohvatiKomandu(String komanda) {

      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {komanda}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Response odgovor = request.get();

      return odgovor;
    }

    public Response dohvatiInfo(String vrsta) {

      WebTarget resource = webTarget;
      
      resource = resource.path(java.text.MessageFormat.format("INFO/{0}", new Object[] {vrsta}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Response odgovor = request.get();
      
      
      return odgovor;
    }

    public void close() {
      client.close();
    }
  }


}
