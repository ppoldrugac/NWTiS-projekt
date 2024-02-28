package org.foi.nwtis.ppoldruga.projekt.filter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class FilterAP2 implements Filter {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    
    chain.doFilter(request, response);
    
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    
    String ruta = httpRequest.getRequestURL().toString();
    
    if(httpRequest.getQueryString() != null) {
      ruta += "?" + httpRequest.getQueryString();
    }
    
    String metoda = httpRequest.getMethod();
    
    String vrsta = "AP2";
    
    String query =
        "INSERT INTO PUBLIC.PUBLIC.DNEVNIK (VRSTA, METODA, RUTA, DATUM) VALUES(?, ?, ?, CURRENT_TIMESTAMP)";

    PreparedStatement stmt = null;

    try (var con = ds.getConnection()) {

      stmt = con.prepareStatement(query);

      stmt.setString(1, vrsta);
      stmt.setString(2, metoda);
      stmt.setString(3, ruta);

      stmt.execute();

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

}
