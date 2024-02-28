package org.foi.nwtis.ppoldruga.projekt.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.PogresnaAutentikacija;
import org.foi.nwtis.ppoldruga.projekt.web.WebSocket;
import org.foi.nwtis.ppoldruga.projekt.zrna.Autentikacija;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;


@WebService(serviceName = "aerodromi")
public class WsAerodromi {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @EJB
  Autentikacija autentikacija;

  @WebMethod
  public List<Aerodrom> dajAerodromeZaLetove(@WebParam String korisnik, @WebParam String lozinka)
      throws PogresnaAutentikacija {

    List<Aerodrom> aerodromiZaPreuzimanje = new ArrayList<Aerodrom>();

    if (autentikacija.autenticirajKorisnika(korisnik, lozinka)) {

      String query =
          "SELECT airports.icao, airports.name, airports.iso_country, airports.coordinates FROM airports JOIN aerodromi_letovi ON airports.icao = aerodromi_letovi.icao";

      PreparedStatement stmt = null;
      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          String icao = rs.getString("icao");
          String naziv = rs.getString("name");
          String drzava = rs.getString("iso_country");
          String koordinate = rs.getString("coordinates");
          Lokacija lokacija = new Lokacija(koordinate.split(",")[0], koordinate.split(",")[1]);
          Aerodrom ad = new Aerodrom(icao, naziv, drzava, lokacija);
          aerodromiZaPreuzimanje.add(ad);
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

    } else {
      throw new PogresnaAutentikacija();
    }

    return aerodromiZaPreuzimanje;
  }

  @WebMethod
  public List<String> dajStatuse() {

    List<String> aerodromiStatusi = new ArrayList<String>();
    
    String query =
        "SELECT aerodromi_letovi.status AS status FROM aerodromi_letovi JOIN airports ON airports.icao = aerodromi_letovi.icao";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String status = rs.getString("status");
        aerodromiStatusi.add(status);
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
    
    return aerodromiStatusi;
    
  }

  @WebMethod
  public boolean dodajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) throws PogresnaAutentikacija {

    boolean ok = false;

    if (autentikacija.autenticirajKorisnika(korisnik, lozinka)) {

      if (icao != null && !icao.isEmpty()) {

        String query = "INSERT INTO PUBLIC.PUBLIC.AERODROMI_LETOVI (ICAO, STATUS) VALUES (?, true)";

        PreparedStatement stmt = null;

        try (var con = ds.getConnection()) {

          stmt = con.prepareStatement(query);
          stmt.setString(1, icao);
          stmt.execute();

          if (stmt.getUpdateCount() > 0) {

            ok = true;

            int brojUpisanihAerodroma = dohvatiUkupanBrojUpisanihAerodroma();

            String obavijest = "Broj upisanih aerodroma: " + brojUpisanihAerodroma;

            WebSocket.proslijediObavijest(obavijest);

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
      }

    } else {
      throw new PogresnaAutentikacija();
    }

    return ok;
  }

  @WebMethod
  public boolean pauzirajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) throws PogresnaAutentikacija {

    boolean ok = false;

    if (autentikacija.autenticirajKorisnika(korisnik, lozinka)) {

      if (icao != null && !icao.isEmpty()) {

        String query = "UPDATE PUBLIC.PUBLIC.AERODROMI_LETOVI SET STATUS=false WHERE ICAO=?";

        PreparedStatement stmt = null;

        try (var con = ds.getConnection()) {

          stmt = con.prepareStatement(query);
          stmt.setString(1, icao);
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
      }

    } else {
      throw new PogresnaAutentikacija();
    }

    return ok;
  }

  @WebMethod
  public boolean aktivirajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) throws PogresnaAutentikacija {

    boolean ok = false;

    if (autentikacija.autenticirajKorisnika(korisnik, lozinka)) {

      if (icao != null && !icao.isEmpty()) {

        String query = "UPDATE PUBLIC.PUBLIC.AERODROMI_LETOVI SET STATUS=true WHERE ICAO=?";

        PreparedStatement stmt = null;

        try (var con = ds.getConnection()) {

          stmt = con.prepareStatement(query);
          stmt.setString(1, icao);
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
      }

    } else {
      throw new PogresnaAutentikacija();
    }

    return ok;
  }

  private int dohvatiUkupanBrojUpisanihAerodroma() {

    Integer broj = 0;

    String query = "SELECT COUNT(*) AS broj_upisanih_aerodroma FROM aerodromi_letovi";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        broj = rs.getInt("broj_upisanih_aerodroma");
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
    return broj;
  }

}
