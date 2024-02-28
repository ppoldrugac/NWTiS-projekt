package org.foi.nwtis.ppoldruga.projekt.web;


import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ppoldruga.projekt.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.ppoldruga.projekt.ws.WsKorisnici.endpoint.Korisnik;
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
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnika {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/ppoldruga_aplikacija_4/korisnici?wsdl")
  private Korisnici service;

  @Inject
  private Models model;

  @Inject
  private HttpSession session;

  @Inject
  private ServletContext kontekst;

  @GET
  @Path("pocetak")
  @View("pogled_5.2.jsp")
  public void pocetak() {}

  @GET
  @Path("registracija")
  @View("pogled_5.2.1.jsp")
  public void registracijaKorisnika() {
    popuniZaglavlje();
  }

  @GET
  @Path("prijava")
  @View("pogled_5.2.2.jsp")
  public void prijavaKorisnika() {
    popuniZaglavlje();
  }

  @GET
  @Path("svi")
  @View("pogled_5.2.3.jsp")
  public void dajKorisnike(@QueryParam("traziImeKorisnika") String traziImeKorisnika,
      @QueryParam("traziPrezimeKorisnika") String traziPrezimeKorisnika) {


    String korisnik = (String) session.getAttribute("korime");
    String lozinka = (String) session.getAttribute("lozinka");

    if (korisnik == null && lozinka == null) {
      model.put("logiran", false);
    } else {
      model.put("logiran", true);
    }

    try {
      var port = service.getWsKorisniciPort();
      var korisnici =
          port.dajKorisnike(korisnik, lozinka, traziImeKorisnika, traziPrezimeKorisnika);
      model.put("korisnici", korisnici);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("prijava2")
  @View("pogled_5.2.2prijavljen.jsp")
  public void prijaviKorisnika(@QueryParam("korisnik") String korisnik,
      @QueryParam("lozinka") String lozinka, @QueryParam("traziKorisnika") String traziKorisnika) {

    try {
      var port = service.getWsKorisniciPort();
      var kor = port.dajKorisnika(korisnik, lozinka, korisnik);
      model.put("korisnik", kor);
      session.setAttribute("korime", korisnik);
      session.setAttribute("lozinka", lozinka);
      popuniZaglavlje();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("registriraj")
  @View("pogled_5.2.1registriran.jsp")
  public void registrirajKorisnika(@QueryParam("korIme") String korIme,
      @QueryParam("lozinka") String lozinka, @QueryParam("ime") String ime,
      @QueryParam("prezime") String prezime, @QueryParam("email") String email) {

    Korisnik korisnik = new Korisnik();
    korisnik.setIme(ime);
    korisnik.setPrezime(prezime);
    korisnik.setKorIme(korIme);
    korisnik.setEmail(email);
    korisnik.setLozinka(lozinka);

    try {
      var port = service.getWsKorisniciPort();
      var kor = port.dodajKorisnika(korisnik);
      model.put("korisnik", kor);
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
