package org.foi.nwtis.ppoldruga.projekt.rest;

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
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import org.foi.nwtis.rest.podaci.LetAvionaID;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("letovi")
@RequestScoped
public class RestLetovi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  private ServletContext konfig;
  private String korisnik;
  private String lozinka;

  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajPolaskeLetovaSAerodromaNaOdabraniDan(@PathParam("icao") String icao,
      @QueryParam("dan") String dan, @QueryParam("odBroja") Integer odBroja,
      @QueryParam("broj") Integer broj) {

    Timestamp[] ts = konvertirajDatum(dan);


    Object korisnikObj = konfig.getAttribute("OpenSkyNetwork.korisnik");
    Object lozinkaObj = konfig.getAttribute("OpenSkyNetwork.lozinka");

    if (korisnikObj != null && lozinkaObj != null) {
      korisnik = korisnikObj.toString();
      lozinka = lozinkaObj.toString();
    }

    OSKlijent oSKlijent = new OSKlijent(korisnik, lozinka);

    System.out.println("Polasci s aerodroma: " + icao);
    List<LetAviona> sviLetoviPolasci = null;
    try {
      sviLetoviPolasci = oSKlijent.getDepartures(icao, ts[0], ts[1]);
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
    }


    int ukupnoLetovaPolazaka = sviLetoviPolasci.size();

    if (odBroja == null) {
      odBroja = 1;
    }
    if (broj == null) {
      broj = 20;
    }

    int doBroja = Math.min(odBroja + broj - 1, ukupnoLetovaPolazaka);

    List<LetAviona> filtriraniLetoviPolasci = new ArrayList<>();
    for (int i = odBroja - 1; i < doBroja; i++) {
      filtriraniLetoviPolasci.add(sviLetoviPolasci.get(i));
    }


    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(filtriraniLetoviPolasci);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  private Timestamp[] konvertirajDatum(String broj) {
    try {
      LocalDate date = LocalDate.parse(broj, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

      LocalDateTime odVremena = date.atStartOfDay();
      LocalDateTime doVremena = date.atTime(23, 59, 59, 999999999);

      return new Timestamp[] {Timestamp.from(odVremena.toInstant(ZoneOffset.UTC)),
          Timestamp.from(doVremena.toInstant(ZoneOffset.UTC))};
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajPolaskeLetovaIzmeduOdabraniAerodomaNaOdabraniDan(
      @PathParam("icaoOd") String icaoFrom, @PathParam("icaoDo") String icaoTo,
      @QueryParam("dan") String dan) {

    Object korisnikObj = konfig.getAttribute("OpenSkyNetwork.korisnik");
    Object lozinkaObj = konfig.getAttribute("OpenSkyNetwork.lozinka");

    if (korisnikObj != null && lozinkaObj != null) {
      korisnik = korisnikObj.toString();
      lozinka = lozinkaObj.toString();
    }

    Timestamp[] ts = konvertirajDatum(dan);

    OSKlijent oSKlijent = new OSKlijent(korisnik, lozinka);

    List<LetAviona> sviLetoviPolasci = null;

    try {
      sviLetoviPolasci = oSKlijent.getDepartures(icaoFrom, ts[0], ts[1]);
    } catch (NwtisRestIznimka e) {
      e.printStackTrace();
    }

    List<LetAviona> filtriranaLista = new ArrayList<>();

    if (sviLetoviPolasci != null) {
      for (LetAviona let : sviLetoviPolasci) {
        if (let.getEstArrivalAirport() != null && let.getEstArrivalAirport().equals(icaoTo)) {
          filtriranaLista.add(let);
        }
      }
    }

    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(filtriranaLista);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  public Response spremiLet(LetAviona let) {

    Response odgovor = null;
    boolean ok = false;

    String query =
        "INSERT INTO PUBLIC.PUBLIC.LETOVI_POLASCI (ICAO24, FIRSTSEEN, ESTDEPARTUREAIRPORT, LASTSEEN, ESTARRIVALAIRPORT, CALLSIGN, ESTDEPARTUREAIRPORTHORIZDISTANCE, ESTDEPARTUREAIRPORTVERTDISTANCE, ESTARRIVALAIRPORTHORIZDISTANCE, ESTARRIVALAIRPORTVERTDISTANCE, DEPARTUREAIRPORTCANDIDATESCOUNT, ARRIVALAIRPORTCANDIDATESCOUNT, STORED) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    PreparedStatement stmt = null;

    try (var con = ds.getConnection()) {

      stmt = con.prepareStatement(query);

      stmt.setString(1, let.getIcao24());
      stmt.setInt(2, let.getFirstSeen());
      stmt.setString(3, let.getEstDepartureAirport());
      stmt.setInt(4, let.getLastSeen());
      stmt.setString(5, let.getEstArrivalAirport());
      stmt.setString(6, let.getCallsign());
      stmt.setInt(7, let.getEstDepartureAirportHorizDistance());
      stmt.setInt(8, let.getEstDepartureAirportVertDistance());
      stmt.setInt(9, let.getEstArrivalAirportHorizDistance());
      stmt.setInt(10, let.getEstArrivalAirportVertDistance());
      stmt.setInt(11, let.getDepartureAirportCandidatesCount());
      stmt.setInt(12, let.getArrivalAirportCandidatesCount());

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
          Response.status(Response.Status.OK).entity("Odabrani let je uspješno unesen.").build();
    } else {
      odgovor =
          Response.status(Response.Status.NOT_FOUND).entity("Odabrani let nije unesen.").build();
    }

    return odgovor;
  }

  @Path("/spremljeni")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSpremljeneOdabranePolaskeLetova() {

    var spremljeniLetovi = new ArrayList<LetAvionaID>();

    String query = "SELECT * FROM PUBLIC.PUBLIC.LETOVI_POLASCI";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);


      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Long id = rs.getLong("ID");
        String icao24 = rs.getString("ICAO24");
        Integer firstSeen = rs.getInt("FIRSTSEEN");
        String estDepartureAirport = rs.getString("ESTDEPARTUREAIRPORT");
        Integer lastSeen = rs.getInt("LASTSEEN");
        String estArrivalAirport = rs.getString("ESTARRIVALAIRPORT");
        String callSign = rs.getString("CALLSIGN");
        Integer estDepartureAirportHorizDistance = rs.getInt("ESTDEPARTUREAIRPORTHORIZDISTANCE");
        Integer estDepartureAirportVertDistance = rs.getInt("ESTDEPARTUREAIRPORTVERTDISTANCE");
        Integer estArrivalAirportHorizDistance = rs.getInt("ESTARRIVALAIRPORTHORIZDISTANCE");
        Integer estArrivalAirportVertDistance = rs.getInt("ESTARRIVALAIRPORTVERTDISTANCE");
        Integer departureAirportCandidatesCount = rs.getInt("DEPARTUREAIRPORTCANDIDATESCOUNT");
        Integer arrivalAirportCandidatesCount = rs.getInt("ARRIVALAIRPORTCANDIDATESCOUNT");

        var let =
            new LetAvionaID(id, icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport,
                callSign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance,
                estArrivalAirportHorizDistance, estArrivalAirportVertDistance,
                departureAirportCandidatesCount, arrivalAirportCandidatesCount);

        spremljeniLetovi.add(let);
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
    var jsonAerodrmi = gson.toJson(spremljeniLetovi);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }


  @Path("{id}")
  @DELETE
  public Response obrisiLetAviona(@PathParam("id") Integer id) {

    boolean ok = false;

    String query = "DELETE FROM PUBLIC.PUBLIC.LETOVI_POLASCI WHERE ID = ?";

    PreparedStatement stmt = null;

    try (var con = ds.getConnection()) {

      stmt = con.prepareStatement(query);
      stmt.setInt(1, id);

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

    Response odgovor = Response.serverError().build();
    if (ok) {
      odgovor =
          Response.status(Response.Status.OK).entity("Odabrani let je uspješno obrisan.").build();
    } else {
      odgovor = Response.status(Response.Status.NOT_FOUND)
          .entity("Pogreška kod brisanja odabranog leta.").build();
    }

    return odgovor;
  }


}

