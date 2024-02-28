package org.foi.nwtis.ppoldruga.projekt.web;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ppoldruga.projekt.rest.RestKlijentDnevnik;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("dnevnik")
@RequestScoped
public class KontrolerDnevnika {

  @Inject
  private Models model;

  @Inject
  private ServletContext kontekst;


  @GET
  @Path("")
  @View("pogled_5.7.jsp")
  public void dajZapiseDnevnika(@QueryParam("odBroja") Integer odBroja,
      @QueryParam("broj") Integer broj, @QueryParam("vrsta") String vrsta) {
    
    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");
    var brojRedova = konfig.dajPostavku("stranica.brojRedova");
    
    try {
      RestKlijentDnevnik rcd = new RestKlijentDnevnik();
      var zapisiDnevnika = rcd.dohvatiPodatkeIzDnevnika(vrsta, odBroja, broj);
      model.put("zapisiDnevnik", zapisiDnevnika);
      model.put("brojRedova", brojRedova);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void popuniZaglavlje() {
    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");
    model.put("ime", konfig.dajPostavku("autor.ime"));
    model.put("prezime", konfig.dajPostavku("autor.prezime"));
    model.put("predmet", konfig.dajPostavku("autor.predmet"));
    model.put("godina", konfig.dajPostavku("aplikacija.godina"));
    model.put("verzija", konfig.dajPostavku("aplikacija.verzija"));
  }
}
