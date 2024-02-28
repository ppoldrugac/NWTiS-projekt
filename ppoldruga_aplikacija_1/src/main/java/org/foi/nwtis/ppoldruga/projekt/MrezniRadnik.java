package org.foi.nwtis.ppoldruga.projekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.ppoldruga.projekt.podaci.Korisnik;

public class MrezniRadnik extends Thread {

  protected Socket mreznaUticnica;
  protected Konfiguracija konfig;
  private GlavniPosluzitelj gp;
  private Matcher m;

  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig, GlavniPosluzitelj gp) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
    this.gp = gp;
  }

  @Override
  public synchronized void start() {
    super.start();
  }

  @Override
  public void run() {
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        if (gp.ispis == true)
          Logger.getGlobal().log(Level.INFO, red);
        poruka.append(red);
      }

      String odgovor = "";

      if (!provjeriFormatKomande(poruka)) {
        odgovor = "ERROR 05 Neispravan format komande!";
      } else {
        odgovor = this.obradiZahtjev(poruka.toString());

      }

      // Logger.getGlobal().log(Level.INFO, "Odgovor: " + odgovor); // na kraju se ovo makiva
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (gp.krajRada == true) {
      gp.zatvoriMreznaVrata();
    }
  }


  private boolean provjeriFormatKomande(StringBuilder komanda) {
    String izraz =
        "((?<status>STATUS)|(?<kraj>KRAJ)|(?<init>INIT)|(?<pauza>PAUZA)|(INFO (?<info>(DA|NE))|(UDALJENOST (?<udaljenost>(\\d+\\.\\d+ \\d+\\.\\d+ \\d+\\.\\d+ \\d+\\.\\d+$)))))";

    Pattern p = Pattern.compile(izraz);
    m = p.matcher(komanda.toString());

    if (m.matches()) {
      return true;
    } else {
      return false;
    }
  }

  private String obradiZahtjev(String poruka) {

    String odgovor = "";

    if (poruka.equals("STATUS")) {
      odgovor = obradiStatus();
    }

    else if (poruka.equals("KRAJ")) {
      odgovor = obradiKraj();
    }

    else if (poruka.equals("INIT")) {
      odgovor = obradiInit();
    }

    else if (poruka.equals("PAUZA")) {
      odgovor = obradiPauza();
    }

    else if (poruka.equals("INFO DA")) {
      odgovor = obradiInfoDa();
    }

    else if (poruka.equals("INFO NE")) {
      odgovor = obradiInfoNe();
    }

    else {
      if (gp.status == false) {
        odgovor = "ERROR 01 Poslužitelj je pauziran!";
      } else {
        var udaljenosti = poruka.split(" ");
        double gpsSirina1 = Double.parseDouble(udaljenosti[1]);
        double gosDuzina1 = Double.parseDouble(udaljenosti[2]);
        double gpsSirina2 = Double.parseDouble(udaljenosti[3]);
        double gosDuzina2 = Double.parseDouble(udaljenosti[4]);

        boolean ispravneKordinate =
            provjeriKordinate(gpsSirina1, gosDuzina1, gpsSirina2, gosDuzina2);

        if (!ispravneKordinate) {
          odgovor = "ERROR 05 Unesene kordinate nisu ispravne!";
        } else {
          double udaljenost = izracunajUdaljenost(gpsSirina1, gosDuzina1, gpsSirina2, gosDuzina2);
          odgovor = "OK " + udaljenost;
        }
      }

    }
    return odgovor;
  }

  private String obradiKraj() {
    String odgovor;
    odgovor = "OK";
    gp.krajRada = true;
    return odgovor;
  }

  private String obradiStatus() {
    String odgovor = "";
    if (gp.status == false) {
      odgovor = "OK 0";
    }

    if (gp.status == true) {
      odgovor = "OK 1";
    }
    return odgovor;
  }

  private String obradiInit() {
    String odgovor;
    if (gp.status == true) {
      odgovor = "ERROR 02 Poslužitelj je već aktivan!";
    } else {
      gp.status = true;
      gp.brojacObradenihZahtjeva = 0;
      odgovor = "OK";
    }
    return odgovor;
  }

  private String obradiPauza() {
    String odgovor;
    if (gp.status == false) {
      odgovor = "ERROR 01 Poslužitelj je već pauziran!";
    } else {
      gp.status = false;
      odgovor = "OK " + gp.brojacObradenihZahtjeva;
    }
    return odgovor;
  }

  private String obradiInfoNe() {
    String odgovor;
    if (gp.status == false) {
      odgovor = "ERROR 01 Poslužitelj je pauziran!";
    } else {
      if (gp.ispis == false) {
        odgovor = "ERORR 04 Poslužitelj već ne ispisuje podatke o svom radu na standardni izlaz!";
      } else {
        gp.ispis = false;
        odgovor = "OK";
      }
    }
    return odgovor;
  }

  private String obradiInfoDa() {
    String odgovor;
    if (gp.status == false) {
      odgovor = "ERROR 01 Poslužitelj je pauziran!";
    } else {
      if (gp.ispis == true) {
        odgovor = "ERROR 03 Poslužitelj već ispisuje podatke o svom radu na standardni izlaz!";
      } else {
        gp.ispis = true;
        odgovor = "OK";
      }
    }
    return odgovor;
  }

  private boolean provjeriKordinate(double gpsSirina1, double gosDuzina1, double gpsSirina2,
      double gosDuzina2) {

    if (!jeLiValidnaKordinata(gpsSirina1) || !jeLiValidnaKordinata(gosDuzina1)
        || !jeLiValidnaKordinata(gpsSirina2) || !jeLiValidnaKordinata(gosDuzina2)) {
      return false;
    }

    if (!provjeriGpsSirinu(gpsSirina2) || !provjeriGpsSirinu(gpsSirina2)
        || !provjeriGosDuzinu(gosDuzina1) || !provjeriGosDuzinu(gosDuzina2)) {
      return false;
    }

    return true;
  }

  private boolean jeLiValidnaKordinata(double kordinata) {
    // provjera da li je kordinata broj
    return !Double.isNaN(kordinata) && !Double.isInfinite(kordinata);
  }

  private boolean provjeriGpsSirinu(double gpsSirina) {
    return gpsSirina >= -90 && gpsSirina <= 90;
  }

  private boolean provjeriGosDuzinu(double gosDuzina) {
    return gosDuzina >= -180 && gosDuzina <= 180;
  }

  // Izvor:
  // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
  private double izracunajUdaljenost(double gpsSirina1, double gosDuzina1, double gpsSirina2,
      double gosDuzina2) {

    double lat1 = Math.toRadians(gpsSirina1);
    double lon1 = Math.toRadians(gosDuzina1);
    double lat2 = Math.toRadians(gpsSirina2);
    double lon2 = Math.toRadians(gosDuzina2);

    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a = Math.pow(Math.sin(dlat / 2), 2)
        + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double radijusZemlje = 6371; // RADIUS_ZEMLJE u km
    double udaljenost = radijusZemlje * c;

    return Math.round(udaljenost * 100) / 100.0; // na dvije decimale
  }

  @Override
  public void interrupt() {
    super.interrupt();
  }

}
