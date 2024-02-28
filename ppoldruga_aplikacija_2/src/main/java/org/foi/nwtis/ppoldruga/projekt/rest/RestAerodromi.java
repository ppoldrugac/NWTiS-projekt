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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import org.foi.nwtis.podaci.UdaljenostKlasa;
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

@Path("aerodromi")
@RequestScoped
public class RestAerodromi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  private ServletContext kontekst;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("odBroja") Integer odBroja,
      @QueryParam("broj") Integer broj, @QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu) {

    if (odBroja == null) {
      odBroja = 1;
    }

    if (broj == null) {
      broj = 20;
    }

    List<Aerodrom> sviaerodromi = new ArrayList<>();

    String query;
    if ((traziNaziv == null || traziNaziv.isEmpty())
        && (traziDrzavu == null || traziDrzavu.isEmpty())) {
      query = "SELECT icao, name, iso_country, coordinates FROM airports";
    } else if ((traziNaziv != null && !traziNaziv.isEmpty())
        && (traziDrzavu == null || traziDrzavu.isEmpty())) {
      query = "SELECT icao, name, iso_country, coordinates FROM airports WHERE name LIKE ?";
    } else if ((traziNaziv == null || traziNaziv.isEmpty())
        && (traziDrzavu != null && !traziDrzavu.isEmpty())) {
      query = "SELECT icao, name, iso_country, coordinates FROM airports WHERE iso_country = ?";
    } else {
      query =
          "SELECT icao, name, iso_country, coordinates FROM airports WHERE name LIKE ? AND iso_country = ?";
    }

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      int parametar = 1;

      if (traziNaziv != null && !traziNaziv.isEmpty()) {
        stmt.setString(parametar, "%" + traziNaziv + "%");
        parametar++;
      }

      if (traziDrzavu != null && !traziDrzavu.isEmpty()) {
        stmt.setString(parametar, traziDrzavu);
      }

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String icao = rs.getString("icao");
        String naziv = rs.getString("name");
        String drzava = rs.getString("iso_country");
        String koordinate = rs.getString("coordinates");
        Lokacija lokacija = new Lokacija(koordinate.split(",")[0], koordinate.split(",")[1]);
        Aerodrom ad = new Aerodrom(icao, naziv, drzava, lokacija);
        sviaerodromi.add(ad);
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

    List<Aerodrom> aerodromi = new ArrayList<>();

    int ukupnoAerodroma = sviaerodromi.size();
    int doBroja = Math.min(odBroja + broj - 1, ukupnoAerodroma);

    for (int i = odBroja - 1; i < doBroja; i++) {
      aerodromi.add(sviaerodromi.get(i));
    }


    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  // b
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {

    String query = "SELECT icao, name, iso_country, coordinates FROM airports WHERE icao = ?";

    PreparedStatement stmt = null;
    Aerodrom aerodrom = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String naziv = rs.getString("name");
        String drzava = rs.getString("iso_country");
        String koordinate = rs.getString("coordinates");
        Lokacija lokacija = new Lokacija(koordinate.split(",")[0], koordinate.split(",")[1]);
        aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      return Response.status(500).build();
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }
    }

    if (aerodrom == null) {
      return Response.status(404).build();
    } else {
      var gson = new Gson();
      var jsonAerodrmi = gson.toJson(aerodrom);
      var odgovor = Response.ok().entity(jsonAerodrmi).build();

      return odgovor;
    }

  }

  // c
  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiAerodoma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {

    var udaljenosti = new ArrayList<UdaljenostKlasa>();

    String query = "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX "
        + "WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoFrom);
      stmt.setString(2, icaoTo);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljenost = rs.getFloat("DIST_CTRY");
        var u = new UdaljenostKlasa(drzava, udaljenost);
        udaljenosti.add(u);
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

    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  // d
  @Path("{icao}/udaljenosti")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveUdaljenostiOdJednogAerodoma(@PathParam("icao") String icao,
      @QueryParam("odBroja") Integer odBroja, @QueryParam("broj") Integer broj) {

    var udaljenosti = new ArrayList<UdaljenostAerodrom>();

    String query =
        "SELECT DISTINCT icao_to, dist_tot FROM PUBLIC.AIRPORTS_DISTANCE_MATRIX  WHERE icao_from = ?";

    try (var con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {

      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String icaoAerodroma = rs.getString("icao_to");
        float udaljenost = rs.getFloat("dist_tot");

        UdaljenostAerodrom uda = new UdaljenostAerodrom(icaoAerodroma, udaljenost);
        udaljenosti.add(uda);
      }
      rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      return Response.status(500).build();
    }

    int ukupnoAerodroma = udaljenosti.size();

    if (odBroja == null) {
      odBroja = 1;
    }
    if (broj == null) {
      broj = 20;
    }

    int doBroja = Math.min(odBroja + broj - 1, ukupnoAerodroma);

    List<UdaljenostAerodrom> filtriraneUdaljenosti = new ArrayList<>();
    for (int i = odBroja - 1; i < doBroja; i++) {
      filtriraneUdaljenosti.add(udaljenosti.get(i));
    }

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(filtriraneUdaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();

    return odgovor;
  }

  // e
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response izracunajUdaljenost(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    String kordinateOdStr = dohvatiKordinateIzBaze(icaoOd);
    String kordinateDoStr = dohvatiKordinateIzBaze(icaoDo);

    Lokacija lokacijaOd = dobiLokacijuIzKordinate(kordinateOdStr);
    Lokacija lokacijaDo = dobiLokacijuIzKordinate(kordinateDoStr);

    String komanda = "UDALJENOST " + lokacijaOd.getLatitude() + lokacijaOd.getLongitude() + " "
        + lokacijaDo.getLatitude() + lokacijaDo.getLongitude();



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

    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(poruka);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  private Lokacija dobiLokacijuIzKordinate(String kordinate) {
    return new Lokacija(kordinate.split(",")[0], kordinate.split(",")[1]);
  }

  private String dohvatiKordinateIzBaze(String icao) {

    String upit = "SELECT coordinates FROM airports WHERE icao = ?";

    String kordinate = null;

    try (var con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(upit)) {

      stmt.setString(1, icao);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        kordinate = rs.getString("coordinates");
      }
      rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());

    }

    return kordinate;
  }

  // f
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenost1(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    String kordinateOdStr = dohvatiKordinateIzBaze(icaoOd);
    String kordinateDoStr = dohvatiKordinateIzBaze(icaoDo);

    Lokacija lokacijaOd = dobiLokacijuIzKordinate(kordinateOdStr);
    Lokacija lokacijaDo = dobiLokacijuIzKordinate(kordinateDoStr);

    String komanda = "UDALJENOST " + lokacijaOd.getLatitude() + lokacijaOd.getLongitude() + " "
        + lokacijaDo.getLatitude() + lokacijaDo.getLongitude();

    StringBuilder poruka = posaljiKomandu(komanda);

    double udaljenost = Double.parseDouble(poruka.toString().split(" ")[1]);

    String drzava = dohvatiDrzavu(icaoDo);

    List<Aerodrom> sviAerodromiIzDrzave = dohvatiAerodromeUDrzavi(drzava);

    // List<Aerodrom> filtriraniAerodromi = new ArrayList<Aerodrom>();

    List<UdaljenostAerodrom> udaljenostiAerodroma = new ArrayList<UdaljenostAerodrom>();

    for (Aerodrom a : sviAerodromiIzDrzave) {

      Lokacija loa = a.getLokacija();

      String komanda2 = "UDALJENOST " + lokacijaOd.getLatitude() + lokacijaOd.getLongitude() + " "
          + loa.getLatitude() + loa.getLongitude();

      StringBuilder poruka2 = posaljiKomandu(komanda2);

      float udaljenost2 = Float.parseFloat(poruka2.toString().split(" ")[1]);

      if (udaljenost2 < udaljenost) {
        UdaljenostAerodrom ua = new UdaljenostAerodrom(a.getIcao(), udaljenost2);
        udaljenostiAerodroma.add(ua);
      }

    }

    var gson = new Gson();
    var jsonFiltriraniAerodromi = gson.toJson(udaljenostiAerodroma);
    var odgovor = Response.ok().entity(jsonFiltriraniAerodromi).build();

    return odgovor;
  }

  private StringBuilder posaljiKomandu(String komanda) throws NumberFormatException {
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
    return poruka;
  }

  private String dohvatiDrzavu(String icaoDo) {

    String upit = "SELECT iso_country FROM airports WHERE icao = ?";

    String drzava = null;

    try (var con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(upit)) {

      stmt.setString(1, icaoDo);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        drzava = rs.getString("iso_country");
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
    return drzava;
  }

  private List<Aerodrom> dohvatiAerodromeUDrzavi(String drzavaIcaoDo) {

    List<Aerodrom> sviAerodromiIzDrzave = new ArrayList<Aerodrom>();

    String query =
        "SELECT icao, name, iso_country, coordinates FROM airports WHERE iso_country = ?";

    try (var con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {

      stmt.setString(1, drzavaIcaoDo);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String icao = rs.getString("icao");
        String naziv = rs.getString("name");
        String drzava = rs.getString("iso_country");
        String koordinate = rs.getString("coordinates");
        Lokacija lokacija = new Lokacija(koordinate.split(",")[0], koordinate.split(",")[1]);
        Aerodrom ad = new Aerodrom(icao, naziv, drzava, lokacija);
        sviAerodromiIzDrzave.add(ad);
      }
      rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }

    return sviAerodromiIzDrzave;
  }
  
//g
 @Path("{icaoOd}/udaljenost2")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response dajUdaljenost2(@PathParam("icaoOd") String icaoOd,
     @PathParam("icaoDo") String icaoDo, @QueryParam("drzava") String drzava, @QueryParam("km") Float km) {

   String kordinateOdStr = dohvatiKordinateIzBaze(icaoOd);

   Lokacija lokacijaOd = dobiLokacijuIzKordinate(kordinateOdStr);

   List<Aerodrom> sviAerodromiIzDrzave = dohvatiAerodromeUDrzavi(drzava);

   List<UdaljenostAerodrom> udaljenostiAerodroma = new ArrayList<UdaljenostAerodrom>();

   for (Aerodrom a : sviAerodromiIzDrzave) {

     Lokacija loa = a.getLokacija();

     String komanda2 = "UDALJENOST " + lokacijaOd.getLatitude() + lokacijaOd.getLongitude() + " "
         + loa.getLatitude() + loa.getLongitude();

     StringBuilder poruka2 = posaljiKomandu(komanda2);

     float udaljenost2 = Float.parseFloat(poruka2.toString().split(" ")[1]);

     if (udaljenost2 < km) {
       UdaljenostAerodrom ua = new UdaljenostAerodrom(a.getIcao(), udaljenost2);
       udaljenostiAerodroma.add(ua);
     }

   }

   var gson = new Gson();
   var jsonFiltriraniAerodromi = gson.toJson(udaljenostiAerodroma);
   var odgovor = Response.ok().entity(jsonFiltriraniAerodromi).build();

   return odgovor;
 }

}

