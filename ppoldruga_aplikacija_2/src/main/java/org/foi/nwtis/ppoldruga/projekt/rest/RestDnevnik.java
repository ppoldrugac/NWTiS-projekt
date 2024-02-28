package org.foi.nwtis.ppoldruga.projekt.rest;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Dnevnik;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("dnevnik")
@RequestScoped
public class RestDnevnik {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  private ServletContext kontekst;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dohvatiPodatkeIzDnevnika(@QueryParam("odBroja") Integer odBroja,
      @QueryParam("broj") Integer broj, @QueryParam("vrsta") String vrsta) {

    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");

    String query;
    if (vrsta == null || vrsta.isEmpty()) {
      query = "SELECT vrsta, metoda, ruta, datum FROM DNEVNIK";
    } else {
      query = "SELECT vrsta, metoda, ruta, datum FROM DNEVNIK WHERE vrsta = ?";
    }

    List<Dnevnik> sviZapisi = new ArrayList<>();

    PreparedStatement stmt = null;

    ResultSet rs;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      if (vrsta == null || vrsta.isEmpty()) {
        rs = stmt.executeQuery();
      } else {
        stmt.setString(1, vrsta);
        rs = stmt.executeQuery();
      }

      while (rs.next()) {
        String vrsta2 = rs.getString("vrsta");
        String metoda = rs.getString("metoda");
        String ruta = rs.getString("ruta");
        String datumString = rs.getString("datum");
        Timestamp datum = pretvoriDatum(datumString);
        Dnevnik dnevnik = new Dnevnik(vrsta2, metoda, ruta, datum);
        sviZapisi.add(dnevnik);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

    }

    int ukupnoZapisaUDnevniku = sviZapisi.size();

    if (odBroja == null) {
      odBroja = 1;
    }
    if (broj == null) {
      broj = 20;
    }

    int doBroja = Math.min(odBroja + broj - 1, ukupnoZapisaUDnevniku);

    List<Dnevnik> filtriraniZapisiDnevnika = new ArrayList<>();
    for (int i = odBroja - 1; i < doBroja; i++) {
      filtriraniZapisiDnevnika.add(sviZapisi.get(i));
    }

    var gson = new Gson();
    var jsonDnevnik = gson.toJson(filtriraniZapisiDnevnika);
    var odgovor = Response.ok().entity(jsonDnevnik).build();

    return odgovor;

  }

  private Timestamp pretvoriDatum(String datumString) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date parsedDate = null;
    try {
      parsedDate = dateFormat.parse(datumString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Timestamp datum = new Timestamp(parsedDate.getTime());
    return datum;
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  public Response spremiZapis(Dnevnik dnevnik) {

    Response odgovor = null;
    boolean ok = false;

    String query =
        "INSERT INTO PUBLIC.PUBLIC.DNEVNIK (VRSTA, METODA, RUTA, DATUM) VALUES(?, ?, ?, CURRENT_TIMESTAMP)";

    PreparedStatement stmt = null;

    try (var con = ds.getConnection()) {

      stmt = con.prepareStatement(query);

      stmt.setString(1, dnevnik.getVrsta());
      stmt.setString(2, dnevnik.getMetoda());
      stmt.setString(3, dnevnik.getRuta());
      // stmt.setTimestamp(4, dnevnik.getDatum());

      stmt.execute();

      if (stmt.getUpdateCount() > 0) {
        ok = true;
      }


    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

    }

    if (ok) {
      odgovor =
          Response.status(Response.Status.OK).entity("Zapis je uspješno unesen.").build();
    } else {
      odgovor =
          Response.status(Response.Status.NOT_FOUND).entity("Zapis nije unesen.").build();
    }

    return odgovor;
  }

}
