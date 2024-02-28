package org.foi.nwtis.ppoldruga.projekt;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

public class PokretacPosluzitelja {

  public static void main(String[] args) {
    var pokretac = new PokretacPosluzitelja();
    if (!pokretac.provjeriArgumente(args) || args.length == 0) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke ili argumenti nisu ispravni!");
      return;
    }

    try {
      var konf = pokretac.ucitajPostavke(args[0]);
      var glavniPosluzitelj = new GlavniPosluzitelj(konf);
      glavniPosluzitelj.pokreniPosluzitelja();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
    }
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
