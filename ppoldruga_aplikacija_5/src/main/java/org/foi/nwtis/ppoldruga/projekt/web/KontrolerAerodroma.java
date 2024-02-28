package org.foi.nwtis.ppoldruga.projekt.web;


import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ppoldruga.projekt.rest.RestKlijentAerodroma;
import org.foi.nwtis.ppoldruga.projekt.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.ppoldruga.projekt.ws.WsMeteo.endpoint.Meteo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/ppoldruga_aplikacija_4/aerodromi?wsdl")
  private Aerodromi service;
  
  @WebServiceRef(wsdlLocation = "http://localhost:8080/ppoldruga_aplikacija_4/meteo?wsdl")
  private Meteo servisMeteo;

  @Inject
  private Models model;
  
  @Inject
  private HttpSession session;

  @Inject
  private ServletContext kontekst;

  @GET
  @Path("pocetak")
  @View("pogled_5.1.jsp")
  public void pocetak() {}

  @GET
  @Path("pocetna")
  @View("pogled_5.5.jsp")
  public void pocetna() {}

  @GET
  @Path("icaoAerodrom")
  @View("icaoAerodrom.jsp")
  public void unasanjeIcaoAerodrom() {}

  @GET
  @Path("unos2udaljenosti")
  @View("pogled_5.5.4.jsp")
  public void unasanjeIcaoOdIcaoDo() {}
  
  @GET
  @Path("unos2udaljenosti2")
  @View("pogled_5.5.4unos2.jsp")
  public void unasanjeIcaoOdIcaoDo2() {}
  
  @GET
  @Path("unos2udaljenosti3")
  @View("pogled_5.5.4unos3.jsp")
  public void unasanjeIcaoOdIcaoDo3() {}

  @GET
  @Path("udaljenosti")
  @View("icaoAerodromUdaljenosti.jsp")
  public void unosIcaoZaUdaljenosti() {}
  
  @GET
  @Path("unosIcaoDrzavaKm")
  @View("pogled_5.5.7unos.jsp")
  public void unosIcaoDrzavaKm() {}
  

  @GET
  @Path("svi")
  @View("pogled_5.5.1.jsp")
  public void getAerodromi(@QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu, @QueryParam("odBroja") Integer odBroja,
      @QueryParam("broj") Integer broj) {

    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");
    var brojRedova = konfig.dajPostavku("stranica.brojRedova");
    
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();

      if (odBroja == null) {
        odBroja = 1;
      }

      if (broj == null) {
        broj = 20;
      }

      var aerodromi = rca.getAerodromi(odBroja, broj ,traziNaziv, traziDrzavu);
      model.put("aerodromi", aerodromi);
      model.put("brojRedova", brojRedova);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("dodaj")
  @View("pogled_5.5.1dodan.jsp")
  public void dodajAerodromZaPreuzimanjeLetovaPolazaka(@QueryParam("icao") String icao) {

    String korisnik = (String) session.getAttribute("korime");
    String lozinka = (String) session.getAttribute("lozinka");

    try {
      var port = service.getWsAerodromiPort();
      var dodan = port.dodajAerodromZaLetove(korisnik, lozinka, icao);
      model.put("dodan", dodan);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("icao")
  @View("pogled_5.5.2.jsp")
  public void getAerodrom(@QueryParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var aerodrom = rca.getAerodrom(icao);
      var port = servisMeteo.getWsMeteoPort();
      var meteo = port.dajMeteo(icao);
      model.put("aerodrom", aerodrom);
      model.put("meteoPodaci", meteo);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("udaljenosti2aerodroma")
  @View("pogled_5.5.4udaljenost.jsp")
  public void dajUdaljenostIzmeduDvaAerodroma(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var udaljenosti = rca.dajUdaljenostIzmeduDvaAerodroma(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
      model.put("icaoOd", icaoOd);
      model.put("icaoDo", icaoDo);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("izracunajudaljenosti")
  @View("pogled_5.5.5.jsp")
  public void izracunajUdaljenostIzmeduDvaAerodroma(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var udaljenost = rca.izracunajUdaljenostIzmeduDvaAerodroma(icaoOd, icaoDo);
      model.put("udaljenost", udaljenost);
      model.put("icaoOd", icaoOd);
      model.put("icaoDo", icaoDo);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("udaljenost1")
  @View("pogled_5.5.6.jsp")
  public void dajUdaljenost1(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var udaljenosti = rca.dajUdaljenost1(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
      model.put("icaoOd", icaoOd);
      model.put("icaoDo", icaoDo);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("udaljenost2")
  @View("pogled_5.5.7.jsp")
  public void dajUdaljenost2(@QueryParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") Float km) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var udaljenosti = rca.dajUdaljenost2(icaoOd, drzava, km);
      model.put("udaljenosti", udaljenosti);
      model.put("icaoOd", icaoOd);
      model.put("drzava", drzava);
      model.put("km", km);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("polasci")
  @View("pogled_5.5.3.jsp")
  public void dajAerodromeZaPolaske() {
    
    String korisnik = (String) session.getAttribute("korime");
    String lozinka = (String) session.getAttribute("lozinka");

    try {
      var port = service.getWsAerodromiPort();
      var aerodromi = port.dajAerodromeZaLetove(korisnik, lozinka);
      var statusi = port.dajStatuse();
      model.put("aerodromi", aerodromi);
      model.put("statusi", statusi);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("aktiviraj")
  @View("pogled_5.5.3.jsp")
  public void aktiviraj(@QueryParam("icao") String icao) {
    
    String korisnik = (String) session.getAttribute("korime");
    String lozinka = (String) session.getAttribute("lozinka");

    try {
      var port = service.getWsAerodromiPort();
      var aerodromi = port.aktivirajAerodromZaLetove(korisnik, lozinka, icao);
      var aerodromiOsvjezeni = port.dajAerodromeZaLetove(korisnik, lozinka);
      var statusi = port.dajStatuse();
      model.put("aerodromi", aerodromiOsvjezeni);
      model.put("statusi", statusi);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @GET
  @Path("pauziraj")
  @View("pogled_5.5.3.jsp")
  public void pauziraj(@QueryParam("icao") String icao) {
    
    String korisnik = (String) session.getAttribute("korime");
    String lozinka = (String) session.getAttribute("lozinka");

    try {
      var port = service.getWsAerodromiPort();
      var aerodromi = port.pauzirajAerodromZaLetove(korisnik, lozinka, icao);
      var aerodromiOsvjezeni = port.dajAerodromeZaLetove(korisnik, lozinka);
      var statusi = port.dajStatuse();
      model.put("aerodromi", aerodromiOsvjezeni);
      model.put("statusi", statusi);
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
