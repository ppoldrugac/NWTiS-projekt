package org.foi.nwtis.ppoldruga.projekt.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.podaci.PogresnaAutentikacija;
import org.foi.nwtis.ppoldruga.projekt.web.WebSocket;
import org.foi.nwtis.ppoldruga.projekt.zrna.Autentikacija;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;


@WebService(serviceName = "korisnici")
public class WsKorisnici {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @EJB
  Autentikacija autentikacija;

  @WebMethod
  public List<Korisnik> dajKorisnike(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String traziImeKorisnika, @WebParam String traziPrezimeKorisnika)
      throws PogresnaAutentikacija {

    List<Korisnik> korisnici = new ArrayList<Korisnik>();

    if (autentikacija.autenticirajKorisnika(korisnik, lozinka)) {

      String query;
      if ((traziImeKorisnika == null || traziImeKorisnika.isEmpty())
          && (traziPrezimeKorisnika == null || traziPrezimeKorisnika.isEmpty())) {
        query = "SELECT korisnicko_ime, lozinka, ime, prezime, email FROM korisnici";
      } else if ((traziImeKorisnika != null && !traziImeKorisnika.isEmpty())
          && (traziPrezimeKorisnika == null || traziPrezimeKorisnika.isEmpty())) {
        query =
            "SELECT korisnicko_ime, lozinka, ime, prezime, email FROM korisnici WHERE ime LIKE ?";
      } else if ((traziImeKorisnika == null || traziImeKorisnika.isEmpty())
          && (traziPrezimeKorisnika != null && !traziPrezimeKorisnika.isEmpty())) {
        query =
            "SELECT korisnicko_ime, lozinka, ime, prezime, email FROM korisnici WHERE prezime LIKE ?";
      } else {
        query =
            "SELECT korisnicko_ime, lozinka, ime, prezime, email FROM korisnici WHERE ime LIKE ? AND prezime LIKE ?";
      }

      PreparedStatement stmt = null;
      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);

        int parametar = 1;

        if (traziImeKorisnika != null && !traziImeKorisnika.isEmpty()) {
          stmt.setString(parametar, "%" + traziImeKorisnika + "%");
          parametar++;
        }

        if (traziPrezimeKorisnika != null && !traziPrezimeKorisnika.isEmpty()) {
          stmt.setString(parametar, "%" + traziPrezimeKorisnika + "%");
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          String ime = rs.getString("ime");
          String korisnickoIme = rs.getString("korisnicko_ime");
          String loz = rs.getString("lozinka");
          String prezime = rs.getString("prezime");
          String email = rs.getString("email");
          Korisnik noviKorisnik = new Korisnik(korisnickoIme, ime, prezime, loz, email);
          korisnici.add(noviKorisnik);
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

    return korisnici;
  }

  @WebMethod
  public Korisnik dajKorisnika(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String traziKorisnika) throws PogresnaAutentikacija {
    
    Korisnik k = null;
    

    if (autentikacija.autenticirajKorisnika(korisnik, lozinka)) {

      String query =
          "SELECT korisnicko_ime, lozinka, ime, prezime, email FROM korisnici WHERE korisnicko_ime = ?";

      PreparedStatement stmt = null;

      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);
        stmt.setString(1, traziKorisnika);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
          String ime = rs.getString("ime");
          String korisnickoIme = rs.getString("korisnicko_ime");
          String loz = rs.getString("lozinka");
          String prezime = rs.getString("prezime");
          String email = rs.getString("email");
          k = new Korisnik(korisnickoIme, ime, prezime, loz, email);
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


    return k;
  }

  @WebMethod
  public boolean dodajKorisnika(Korisnik korisnik) {

    boolean ok = false;

    if (korisnik.getEmail() != null && !korisnik.getEmail().isEmpty() && korisnik.getIme() != null
        && !korisnik.getIme().isEmpty() && korisnik.getKorIme() != null
        && !korisnik.getKorIme().isEmpty() && korisnik.getLozinka() != null
        && !korisnik.getLozinka().isEmpty() && korisnik.getPrezime() != null
        && !korisnik.getPrezime().isEmpty()) {
      
      String query =
          "INSERT INTO PUBLIC.PUBLIC.KORISNICI (KORISNICKO_IME, LOZINKA, IME, PREZIME, EMAIL) VALUES (?, ?, ?, ?, ?)";

      PreparedStatement stmt = null;

      try (var con = ds.getConnection()) {

        stmt = con.prepareStatement(query);

        stmt.setString(1, korisnik.getKorIme());
        stmt.setString(2, korisnik.getLozinka());
        stmt.setString(3, korisnik.getIme());
        stmt.setString(4, korisnik.getPrezime());
        stmt.setString(5, korisnik.getEmail());

        stmt.execute();

        if (stmt.getUpdateCount() > 0) {

          ok = true;

          int brojUpisanihKorisnika = dohvatiUkupanBrojUpisanihKorisnika();

          String obavijest = "Broj upisanih korisnika: " + brojUpisanihKorisnika;

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

    return ok;
  }

  private int dohvatiUkupanBrojUpisanihKorisnika() {

    Integer broj = 0;

    String query = "SELECT COUNT(*) AS broj_upisanih_korisnika FROM korisnici";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        broj = rs.getInt("broj_upisanih_korisnika");
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
