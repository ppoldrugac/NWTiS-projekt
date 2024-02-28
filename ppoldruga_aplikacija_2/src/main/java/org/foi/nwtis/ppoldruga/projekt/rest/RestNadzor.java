package org.foi.nwtis.ppoldruga.projekt.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nadzor")
@RequestScoped
public class RestNadzor {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  private ServletContext kontekst;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dohvatiStatus() {

    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");

    var adresa = konfig.dajPostavku("adresaAplikacija1");
    var mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));

    StringBuilder poruka = null;
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String komanda = "STATUS";

      pisac.write(komanda);
      pisac.flush();
      mreznaUticnica.shutdownOutput();

      poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append(red);
      }
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
    }

    String status;
    String opis;

    if (poruka.toString().startsWith("OK")) {
      status = "200";
      opis = "Uspješno izvršena komanda. Poruka: " + poruka.toString();
    } else {
      status = "400";
      opis = poruka.toString();
    }

    String jsonOdgovor = "{\"status\": \"" + status + "\", \"opis\": \"" + opis + "\"}";

    return Response.status(Integer.parseInt(status)).entity(jsonOdgovor).build();

  }

  @GET
  @Path("{komanda}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dohvatiKomandu(@PathParam("komanda") String komanda) {

    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");

    var adresa = konfig.dajPostavku("adresaAplikacija1");
    var mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));

    StringBuilder poruka = null;
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      pisac.write(komanda);
      pisac.flush();
      mreznaUticnica.shutdownOutput();

      poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append(red);
      }
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
    }

    String status;
    String opis;
    if (poruka.toString().startsWith("OK")) {
      status = "200";
      opis = "Uspješno izvršena komanda. Poruka: " + poruka.toString();
    } else {
      status = "400";
      opis = poruka.toString();
    }

    String jsonOdgovor = "{\"status\": \"" + status + "\", \"opis\": \"" + opis + "\"}";

    return Response.status(Integer.parseInt(status)).entity(jsonOdgovor).build();

  }

  @GET
  @Path("INFO/{vrsta}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dohvatiInfo(@PathParam("vrsta") String vrsta) {

    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");

    var adresa = konfig.dajPostavku("adresaAplikacija1");
    var mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));

    StringBuilder poruka = null;
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      String komanda = "INFO " + vrsta;

      pisac.write(komanda);
      pisac.flush();
      mreznaUticnica.shutdownOutput();

      poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append(red);
      }
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
    }

    String status;
    String opis;
    if (poruka.toString().startsWith("OK")) {
      status = "200";
      opis = "Uspješno izvršena komanda. Poruka: " + poruka.toString();
    } else {
      status = "400";
      opis = poruka.toString();
    }

    String jsonOdgovor = "{\"status\": \"" + status + "\", \"opis\": \"" + opis + "\"}";

    return Response.status(Integer.parseInt(status)).entity(jsonOdgovor).build();

  }

}

