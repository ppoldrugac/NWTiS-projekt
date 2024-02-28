package org.foi.nwtis.ppoldruga.projekt.pomocnici;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.ppoldruga.projekt.podaci.Lokacija;

public class CitanjeLokacija {
  public Map<String, Lokacija> ucitajDatoteku(String nazivDatoteke) throws IOException {
    var putanja = Path.of(nazivDatoteke);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var lokacije = new HashMap<String, Lokacija>();
    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;

      var atributi = red.split(";");
      if (!redImaCetiriAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        var lokacija = new Lokacija(atributi[0], atributi[1], atributi[2], atributi[3]);
        lokacije.put(atributi[1], lokacija);
      }
    }

    return lokacije;
  }


  private boolean redImaCetiriAtributa(String[] atribut) {
    return atribut.length == 4;
  }

}
