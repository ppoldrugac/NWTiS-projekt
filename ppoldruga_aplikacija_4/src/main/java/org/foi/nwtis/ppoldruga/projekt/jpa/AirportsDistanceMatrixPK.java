package org.foi.nwtis.ppoldruga.projekt.jpa;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The primary key class for the AIRPORTS_DISTANCE_MATRIX database table.
 * 
 */
@Embeddable
public class AirportsDistanceMatrixPK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "ICAO_FROM", insertable = false, updatable = false, unique = true,
      nullable = false, length = 10)
  private String icaoFrom;

  @Column(name = "ICAO_TO", insertable = false, updatable = false, unique = true, nullable = false,
      length = 10)
  private String icaoTo;

  @Column(name = "COUNTRY", unique = true, nullable = false, length = 30)
  private String country;

  public AirportsDistanceMatrixPK() {}

  public String getIcaoFrom() {
    return this.icaoFrom;
  }

  public void setIcaoFrom(String icaoFrom) {
    this.icaoFrom = icaoFrom;
  }

  public String getIcaoTo() {
    return this.icaoTo;
  }

  public void setIcaoTo(String icaoTo) {
    this.icaoTo = icaoTo;
  }

  public String getCountry() {
    return this.country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AirportsDistanceMatrixPK)) {
      return false;
    }
    AirportsDistanceMatrixPK castOther = (AirportsDistanceMatrixPK) other;
    return this.icaoFrom.equals(castOther.icaoFrom) && this.icaoTo.equals(castOther.icaoTo)
        && this.country.equals(castOther.country);
  }

  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.icaoFrom.hashCode();
    hash = hash * prime + this.icaoTo.hashCode();
    hash = hash * prime + this.country.hashCode();

    return hash;
  }
}
