package org.foi.nwtis.podaci;

public class PogresnaAutentikacija extends Exception {
  private static final long serialVersionUID = -7895621829764988100L;

  public PogresnaAutentikacija() {
    super("Neuspjela autentikacija!");
  }
}
