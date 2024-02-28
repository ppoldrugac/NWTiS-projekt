package org.foi.nwtis.ppoldruga.konfiguracije;

import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u yaml formatu
 * 
 * @author Matija Novak
 */
public class KonfiguracijaYaml extends KonfiguracijaApstraktna {

  /**
   * konstanta
   */
  public static final String TIP = "yaml";

  /**
   * Konstruktor
   * 
   * @param nazivDatoteke - naziv datotek
   */
  public KonfiguracijaYaml(String nazivDatoteke) {
    super(nazivDatoteke);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    // TODO Auto-generated method stub

  }

  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    // TODO Auto-generated method stub

  }

}
