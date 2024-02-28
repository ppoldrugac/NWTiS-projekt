package org.foi.nwtis.ppoldruga.projekt.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostKlasa;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class RestKlijentAerodroma {

  public RestKlijentAerodroma() {}

  public List<Aerodrom> getAerodromi(Integer odBroja, Integer broj, String traziNaziv, String traziDrzavu) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom[] json_Aerodromi = rc.getAerodromi(traziNaziv, traziDrzavu, odBroja, broj);
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rc.close();
    return aerodromi;
  }

  public List<UdaljenostKlasa> dajUdaljenostIzmeduDvaAerodroma(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostKlasa[] json_Udaljenosti = rc.dajUdaljenostIzmeduDvaAerodroma(icaoOd, icaoDo);
    List<UdaljenostKlasa> udaljenosti;
    if (json_Udaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(json_Udaljenosti);
    }
    rc.close();
    return udaljenosti;
  }


  public Aerodrom getAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom k = rc.getAerodrom(icao);
    rc.close();
    return k;
  }
  
  public String izracunajUdaljenostIzmeduDvaAerodroma(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    String udaljenost = rc.izracunajUdaljenostIzmeduDvaAerodroma(icaoOd, icaoDo);
    rc.close();
    return udaljenost;
  }

  public List<UdaljenostAerodrom> dajUdaljenostiSvihAerodromaOdOdabranogAerodroma(String icao) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodrom[] json_UdaljenostiAerodroma =
        rc.dajUdaljenostiSvihAerodromaOdOdabranogAerodroma(icao);
    List<UdaljenostAerodrom> udaljenostiAerodroma;
    if (json_UdaljenostiAerodroma == null) {
      udaljenostiAerodroma = new ArrayList<>();
    } else {
      udaljenostiAerodroma = Arrays.asList(json_UdaljenostiAerodroma);
    }
    rc.close();
    return udaljenostiAerodroma;
  }
  
  public List<UdaljenostAerodrom> dajUdaljenost1(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    
    UdaljenostAerodrom[] json_UdaljenostiAerodroma =
        rc.dajUdaljenost1(icaoOd, icaoDo);
    
    List<UdaljenostAerodrom> udaljenostiAerodroma;
    if (json_UdaljenostiAerodroma == null) {
      udaljenostiAerodroma = new ArrayList<>();
    } else {
      udaljenostiAerodroma = Arrays.asList(json_UdaljenostiAerodroma);
    }
    rc.close();
    return udaljenostiAerodroma;
  }
  
  public List<UdaljenostAerodrom> dajUdaljenost2(String icaoOd, String drzava, Float km) {
    RestKKlijent rc = new RestKKlijent();
    
    UdaljenostAerodrom[] json_UdaljenostiAerodroma =
        rc.dajUdaljenost2(icaoOd, drzava, km);
    
    List<UdaljenostAerodrom> udaljenostiAerodroma;
    if (json_UdaljenostiAerodroma == null) {
      udaljenostiAerodroma = new ArrayList<>();
    } else {
      udaljenostiAerodroma = Arrays.asList(json_UdaljenostiAerodroma);
    }
    rc.close();
    return udaljenostiAerodroma;
  }


  static class RestKKlijent {

    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/ppoldruga_aplikacija_2/api";

    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    public UdaljenostAerodrom[] dajUdaljenost2(String icaoOd, String drzava, Float km) {
      WebTarget resource = webTarget;
      
      resource = resource.path(java.text.MessageFormat.format("{0}/udaljenost2/", icaoOd));
      
      resource = resource.queryParam("drzava", drzava).queryParam("km", km);
      
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenosti = gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);

      return udaljenosti;
    }

    public UdaljenostAerodrom[] dajUdaljenost1(String icaoOd, String icaoDo) {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/udaljenost1/{1}", icaoOd, icaoDo));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenosti = gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);

      return udaljenosti;
    }

    public String izracunajUdaljenostIzmeduDvaAerodroma(String icaoOd, String icaoDo) {
      
      WebTarget resource = webTarget;
      
      resource = resource.path(java.text.MessageFormat.format("{0}/izracunaj/{1}", icaoOd, icaoDo));
      
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      
      Gson gson = new Gson();
      String udaljenosti = gson.fromJson(request.get(String.class), String.class);

      return udaljenosti;
    }

    public UdaljenostAerodrom[] dajUdaljenostiSvihAerodromaOdOdabranogAerodroma(String icao)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/udaljenosti", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenostiAerodroma =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);

      return udaljenostiAerodroma;

    }

    public UdaljenostKlasa[] dajUdaljenostIzmeduDvaAerodroma(String icaoOd, String icaoDo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/{1}", icaoOd, icaoDo));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostKlasa[] udaljenosti = gson.fromJson(request.get(String.class), UdaljenostKlasa[].class);

      return udaljenosti;

    }

    public Aerodrom[] getAerodromi(String traziNaziv, String traziDrzavu, int odBroja, int broj)
        throws ClientErrorException {
      WebTarget resource = webTarget.queryParam("odBroja", odBroja).queryParam("broj", broj)
          .queryParam("traziDrzavu", traziDrzavu).queryParam("traziNaziv", traziNaziv);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }

      Gson gson = new Gson();
      Aerodrom[] aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);

      return aerodromi;
    }

    public Aerodrom getAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);

      return aerodrom;
    }


    public void close() {
      client.close();
    }
  }

}
