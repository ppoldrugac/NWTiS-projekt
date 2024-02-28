package org.foi.nwtis.ppoldruga.projekt.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Dnevnik;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class RestKlijentDnevnik {
  
  public RestKlijentDnevnik() {}

  public List<Dnevnik> dohvatiPodatkeIzDnevnika(String vrsta, Integer odBroja, Integer broj) {
    RestKKlijent rc = new RestKKlijent();
    Dnevnik[] json_Dnevnik = rc.dohvatiPodatkeIzDnevnika(vrsta, odBroja, broj);
    List<Dnevnik> zapisiIzDnevnika;
    if (json_Dnevnik == null) {
      zapisiIzDnevnika = new ArrayList<>();
    } else {
      zapisiIzDnevnika = Arrays.asList(json_Dnevnik);
    }
    rc.close();
    return zapisiIzDnevnika;
  }
  
  public void posaljiZapisZaSpremanjeUDnevnik(Dnevnik zapis) {
    RestKKlijent rd = new RestKKlijent();
    rd.posaljiZapisZaSpremanjeUDnevnik(zapis);

    rd.close();
  }


  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/ppoldruga_aplikacija_2/api";

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("dnevnik");
    }

    public void posaljiZapisZaSpremanjeUDnevnik(Dnevnik zapis) {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      String podaciZaSpremanje = gson.toJson(zapis);

      Entity<String> entity = Entity.entity(podaciZaSpremanje, MediaType.APPLICATION_JSON);

      request.post(entity);
      
    }

    public Dnevnik[] dohvatiPodatkeIzDnevnika(String vrsta, Integer odBroja, Integer broj)
        throws ClientErrorException {

      WebTarget resource = webTarget.queryParam("odBroja", odBroja).queryParam("broj", broj).queryParam("vrsta", vrsta);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Dnevnik[] zapisiIzDnevnika = gson.fromJson(request.get(String.class), Dnevnik[].class);

      System.out.println(request.get(String.class));

      return zapisiIzDnevnika;
    }

    public void close() {
      client.close();
    }
  }


}
