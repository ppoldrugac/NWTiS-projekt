package org.foi.nwtis.ppoldruga.projekt.filter;

import java.io.IOException;
import org.foi.nwtis.podaci.Dnevnik;
import org.foi.nwtis.ppoldruga.projekt.rest.RestKlijentDnevnik;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class FilterAP5 implements Filter {

  RestKlijentDnevnik rd = new RestKlijentDnevnik();

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
    
    String vrsta = "AP5";

    Dnevnik zapis = new Dnevnik();
    zapis.setMetoda(metoda);
    zapis.setRuta(ruta);
    zapis.setVrsta(vrsta);
    zapis.setDatum(null);
    
    rd.posaljiZapisZaSpremanjeUDnevnik(zapis);
    
  }

}
