package org.foi.nwtis.ppoldruga.projekt.pomocnici;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.ppoldruga.projekt.podaci.Uredaj;
import org.foi.nwtis.ppoldruga.projekt.podaci.UredajVrsta;

public class CitanjeUredaja {
  public Map<String, Uredaj> ucitajDatoteku(String nazivDatoteke) throws IOException {
    var putanja = Path.of(nazivDatoteke);
    if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new IOException(
          "Datoteka '" + nazivDatoteke + "' nije datoteka ili nije moguće otvoriti!");
    }

    var uredaji = new HashMap<String, Uredaj>();
    var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));

    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;

      var atributi = red.split(";");
      if (!redImaCetiriAtributa(atributi)) {
        Logger.getGlobal().log(Level.WARNING, red);
      } else {
        UredajVrsta uv = odrediVrstuUredaja(atributi);
        var uredaj = new Uredaj(atributi[0], atributi[1], atributi[2], uv);
        uredaji.put(atributi[1], uredaj);
      }
    }

    return uredaji;
  }


  public UredajVrsta odrediVrstuUredaja(String[] atributi) {
    var broj = Integer.parseInt(atributi[3]);
    UredajVrsta uv = UredajVrsta.odBroja(broj);
    return uv;
  }


  private boolean redImaCetiriAtributa(String[] atribut) {
    return atribut.length == 4;
  }
}
