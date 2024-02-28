package org.foi.nwtis.ppoldruga.projekt.pomocnici;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.ppoldruga.projekt.podaci.Korisnik;

public class CitanjeKorisnika {
  public Map<String, Korisnik> ucitajDatoteku(String nazivDatoteke) throws IOException {
    var putanja = Path.of(nazivDatoteke);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var korisnici = new HashMap<String, Korisnik>();
    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;

      var atributi = red.split(";");
      if (!redImaPetAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        boolean administrator = isAdministrator(atributi[4]);
        var korisnik =
            new Korisnik(atributi[0], atributi[1], atributi[2], atributi[3], administrator);
        korisnici.put(atributi[2], korisnik);
      }
    }

    return korisnici;
  }

  private boolean isAdministrator(String atribut) {
    return atribut.compareTo("1") == 0 ? true : false;
  }

  private boolean redImaPetAtributa(String[] atribut) {
    return atribut.length == 5;
  }
}
