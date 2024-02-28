package org.foi.nwtis.ppoldruga.projekt.zrna;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;

import jakarta.ejb.Stateless;


@Stateless
public class Autentikacija {
  
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;
  
  public Boolean autenticirajKorisnika (String korisnik, String lozinka) {
    
    boolean autenticiran = false;
    
    String query = "SELECT korisnicko_ime, lozinka FROM korisnici WHERE korisnicko_ime = ? AND lozinka = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      
      stmt = con.prepareStatement(query);
      stmt.setString(1, korisnik);
      stmt.setString(2, lozinka);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next()) {
        autenticiran = true;
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
    
    return autenticiran;
  }
}
