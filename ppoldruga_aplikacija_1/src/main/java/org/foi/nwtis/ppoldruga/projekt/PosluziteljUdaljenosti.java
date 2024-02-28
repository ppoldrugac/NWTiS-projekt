package org.foi.nwtis.ppoldruga.projekt;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

public class PosluziteljUdaljenosti {

  private static String datotekaSerijalizacija;
  private static int mreznaVrata;
  private static int brojCekaca;
  private static int ispis;
  private static String brojZadnjihSpremljenih;

  public static void main(String[] args) throws IOException {
    var pu = new PosluziteljUdaljenosti();
    if (!pu.provjeriArgumente(args) || args.length == 0) {
      Logger.getLogger(PosluziteljUdaljenosti.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke ili argumenti nisu ispravni!");
      return;
    }
    if (!pu.postojiDatotekaKonfiguracije(args)) {
      Logger.getLogger(PosluziteljUdaljenosti.class.getName()).log(Level.SEVERE,
          "Ne postoji datoteka konfiguracije!");
      return;
    } else {
      try {

        var konf = pu.ucitajPostavke(args[0]);

        preuzmiPostavke(konf);

        if (postojiDatotekaSerijalizacija()) {

        } else {
          throw new IOException(
              "Datoteka '" + datotekaSerijalizacija + "' nije datoteka ili nije moguće otvoriti!");
        }

        if (!provjeriZauzetostPorta(mreznaVrata)) {
          return;
        }

        otvoriMreznaVrata(mreznaVrata, brojCekaca);



      } catch (NeispravnaKonfiguracija e) {
        Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
            "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
      }
    }

  }

  private static void otvoriMreznaVrata(int mv, int cekac) {
    try (var posluzitelj = new ServerSocket(mv, cekac)) {

    } catch (IOException e) {
      Logger.getLogger(PosluziteljUdaljenosti.class.getName()).log(Level.SEVERE,
          "Nije moguće otvoriti port!");
    }
  }


  private static boolean provjeriZauzetostPorta(int mv) {
    try (var posluzitelj = new ServerSocket(mv)) {
      posluzitelj.close();
      return true;
    } catch (IOException e) {
      Logger.getLogger(PosluziteljUdaljenosti.class.getName()).log(Level.SEVERE,
          "Port " + mv + " je zauzet!");
      return false;
    }
  }

  private boolean postojiDatotekaKonfiguracije(String[] args) {
    var putanja = Path.of(args[0]);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      return false;
    }
    return true;
  }

  private static boolean postojiDatotekaSerijalizacija() {
    var putanja = Path.of(datotekaSerijalizacija);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      return false;
    } else
      return true;
  }

  private static void preuzmiPostavke(Konfiguracija konf) {
    ispis = Integer.parseInt(konf.dajPostavku("ispis"));
    mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    brojCekaca = Integer.parseInt(konf.dajPostavku("brojCekaca"));
    datotekaSerijalizacija = konf.dajPostavku("datotekaSerijalizacija");
    brojZadnjihSpremljenih = konf.dajPostavku("brojZadnjihSpremljenih");
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

  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

}
