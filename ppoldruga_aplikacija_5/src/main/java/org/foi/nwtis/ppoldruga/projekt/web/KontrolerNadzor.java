package org.foi.nwtis.ppoldruga.projekt.web;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ppoldruga.projekt.rest.RestKlijentNadzor;
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
@Path("nadzor")
@RequestScoped
public class KontrolerNadzor {

  @Inject
  private Models model;

  @Inject
  private ServletContext kontekst;


  @GET
  @Path("")
  @View("pogled_5.3.jsp")
  public void upravljajPosluziteljemStatus(){
    
    try {
      RestKlijentNadzor rcn = new RestKlijentNadzor();
      
      var odgovor = rcn.dohvatiStatus();
      model.put("odgovor", odgovor);
      
//     String opis = odgovor.get("opis").getAsString();
//      String status = odgovor.get("status").getAsString();
      
//      model.put("status", status);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @GET
  @Path("komanda")
  @View("pogled_5.3.jsp")
  public void upravljajPosluziteljemKomanda(@QueryParam("komanda") String komanda){
    try {
      RestKlijentNadzor rcn = new RestKlijentNadzor();
      var odgovor = rcn.dohvatiKomandu(komanda);
      model.put("odgovor", odgovor);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("vrsta")
  @View("pogled_5.3.jsp")
  public void upravljajPosluziteljemInfo(@QueryParam("vrsta") String vrsta){
    try {
      RestKlijentNadzor rcn = new RestKlijentNadzor();
      var odgovor = rcn.dohvatiInfo(vrsta);
      model.put("odgovor", odgovor);
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
