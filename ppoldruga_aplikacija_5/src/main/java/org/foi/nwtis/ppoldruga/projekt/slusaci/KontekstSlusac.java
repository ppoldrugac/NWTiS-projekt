package org.foi.nwtis.ppoldruga.projekt.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;



public final class KontekstSlusac implements ServletContextListener {

  private ServletContext context = null;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    context = event.getServletContext();

    System.out.println("Kreiraj kontekst: " + context.getContextPath());

    ucitajKonfiguraciju();

    provjeriStatusPosluzitelja();

  }

  private void ucitajKonfiguraciju() {

    String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
    String datoteka = context.getInitParameter("konfiguracija");
    try {
      Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + datoteka);

      context.setAttribute("konfig", konf);

      Logger.getGlobal().log(Level.INFO, "Ucitana konfiguracija!");
    } catch (NeispravnaKonfiguracija e) {
      Logger.getGlobal().log(Level.SEVERE, "Problem s konfiguracijom!");
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    context = event.getServletContext();
    context.removeAttribute("hitCounter");
    System.out.println("Obrisan kontekst: " + context.getContextPath());
  }

  private void provjeriStatusPosluzitelja() {

    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    var adresa = konfig.dajPostavku("adresaAplikacija1");
    var mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));

    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.INFO, "Poslužitelj nije aktivan! Prekida se rad!");
      throw new RuntimeException();
    }

  }

}
