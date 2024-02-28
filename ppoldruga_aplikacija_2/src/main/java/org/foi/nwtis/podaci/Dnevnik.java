package org.foi.nwtis.podaci;


import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Patricio Poldrugač
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Dnevnik {

  @Getter
  @Setter
  private String vrsta;
  @Getter
  @Setter
  private String metoda;
  @Getter
  @Setter
  private String ruta;
  @Getter
  @Setter
  private Timestamp datum;

  public Dnevnik() {}
}
