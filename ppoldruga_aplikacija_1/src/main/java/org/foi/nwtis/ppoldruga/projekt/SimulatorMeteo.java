package org.foi.nwtis.ppoldruga.projekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.ppoldruga.projekt.podaci.MeteoSimulacija;

public class SimulatorMeteo {

  private static int ispis;
  private static int maksCekanje;
  private static int trajanjeSekunde;
  private static int brojPokusaja;
  private static String korisnickoIme;
  private static String korisnickaLozinka;
  private static String datotekaMeteo;
  private static String datotekaProblema;
  private static String posluziteljGlavniAdresa;
  private static int posluziteljGlavniVrata;

  public static void main(String[] args) {
    var sm = new SimulatorMeteo();
    if (!sm.provjeriArgumente(args) || args.length == 0) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke ili argumenti nisu ispravni!");
      return;
    }

    if (!sm.postojiDatotekaKonfiguracije(args)) {
      Logger.getLogger(PosluziteljUdaljenosti.class.getName()).log(Level.SEVERE,
          "Ne postoji datoteka konfiguracije!");
      return;
    }

    try {
      var konf = sm.ucitajPostavke(args[0]);
      preuzmiPostavke(konf);

      sm.pokreniSimulator(konf);
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE, e.getMessage());
    } catch (IOException e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Greška učitavanja meteo podatka" + e.getMessage());
    }
  }

  private boolean postojiDatotekaKonfiguracije(String[] args) {
    var putanja = Path.of(args[0]);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      return false;
    }
    return true;
  }

  private boolean provjeriArgumente(String[] args) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i]).append(" ");
    }
    String s = sb.toString().trim();

    String izraz = "[0-9a-zA-Z_]+.(txt|xml|bin|json|yaml)";

    Pattern patternIzraz = Pattern.compile(izraz);
    Matcher matcherIzraz = patternIzraz.matcher(s);
    boolean ispravanIzraz = matcherIzraz.matches();

    return ispravanIzraz;
  }

  private static void preuzmiPostavke(Konfiguracija konf) {
    ispis = Integer.parseInt(konf.dajPostavku("ispis"));
    maksCekanje = Integer.parseInt(konf.dajPostavku("maksCekanje"));
    trajanjeSekunde = Integer.parseInt(konf.dajPostavku("trajanjeSekunde"));
    brojPokusaja = Integer.parseInt(konf.dajPostavku("brojPokusaja"));
    korisnickoIme = konf.dajPostavku("korisnickoIme");
    korisnickaLozinka = konf.dajPostavku("korisnickaLozinka");
    datotekaMeteo = konf.dajPostavku("datotekaMeteo");
    datotekaProblema = konf.dajPostavku("datotekaProblema");
    posluziteljGlavniAdresa = konf.dajPostavku("posluziteljGlavniAdresa");
    posluziteljGlavniVrata = Integer.parseInt(konf.dajPostavku("posluziteljGlavniVrata"));
  }

  private void pokreniSimulator(Konfiguracija konf) throws IOException {
    var nazivDatoteke = datotekaMeteo;
    var putanja = Path.of(nazivDatoteke);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili ju nije moguće otvoriti!");
    }
    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    MeteoSimulacija prethodniMeteo = null;
    int rbroj = 0;
    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;

      rbroj++;
      if (isZaglavlje(rbroj))
        continue;

      var atributi = red.split(";");
      if (!redImaPetAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        var vazeciMeteo =
            new MeteoSimulacija(atributi[0], atributi[1], Float.parseFloat(atributi[2]),
                Float.parseFloat(atributi[3]), Float.parseFloat(atributi[4]));
        if (!isPrviPodatak(rbroj)) {
          this.izracunajSpavanje(prethodniMeteo, vazeciMeteo);
        }


        this.posaljiMeteoPodatak(vazeciMeteo);
        prethodniMeteo = vazeciMeteo;
      }
    }
  }

  private void izracunajSpavanje(MeteoSimulacija prethodniMeteo, MeteoSimulacija vazeciMeteo) {
    String prviString = prethodniMeteo.vrijeme();
    String drugiString = vazeciMeteo.vrijeme();

    int prvi = izracunajSekunde(prviString);
    int drugi = izracunajSekunde(drugiString);

    int razlika = drugi - prvi;

    int spavanje = korigirajRazliku(razlika);

    if (spavanje > 0) {
      try {
        Thread.sleep(razlika);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  private int korigirajRazliku(int razlika) {
    int korigiranaRazlika = razlika * trajanjeSekunde;
    return korigiranaRazlika;
  }

  private int izracunajSekunde(String vrijemeString) {
    LocalTime trenutnoVrijeme =
        LocalTime.parse(vrijemeString, DateTimeFormatter.ofPattern("H:m:s"));

    int sati = trenutnoVrijeme.getHour();
    int minute = trenutnoVrijeme.getMinute();
    int sekunde = trenutnoVrijeme.getSecond();

    int ukupnoSekundi = sati * 3600 + minute * 60 + sekunde;

    return ukupnoSekundi;
  }


  private Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

  private boolean isPrviPodatak(int rbroj) {
    return rbroj == 2;
  }

  private boolean redImaPetAtributa(String[] atributi) {
    return atributi.length == 5;
  }

  private boolean isZaglavlje(int rbroj) {
    return rbroj == 1;
  }

  private void posaljiMeteoPodatak(MeteoSimulacija vazeciMeteo) {

    String komanda = "KORISNIK " + korisnickoIme + " LOZINKA " + korisnickaLozinka + " SENZOR "
        + vazeciMeteo.id() + " " + vazeciMeteo.vrijeme() + " " + vazeciMeteo.temperatura();

    if (vazeciMeteo.vlaga() != -999) {
      komanda += " " + vazeciMeteo.vlaga();
    }

    if (vazeciMeteo.tlak() != -999) {
      komanda += " " + vazeciMeteo.tlak();
    }

    spojiSeNaPosluzitelj(posluziteljGlavniAdresa, posluziteljGlavniVrata, komanda);

  }

  private void spojiSeNaPosluzitelj(String adresa, int mreznaVrata, String komanda) {
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      pisac.write(komanda);
      pisac.flush();
      mreznaUticnica.shutdownOutput();

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;

        poruka.append("RED:" + red);
      }
      Logger.getGlobal().log(Level.INFO, "Odgovor: " + poruka);
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
