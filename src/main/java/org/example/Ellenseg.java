package org.example;

public class Ellenseg {
    private final String nev;
    private final String leiras;
    private int eletero;

    public Ellenseg(String nev, String leiras, int eletero) {
        this.nev = nev;
        this.leiras = leiras;
        this.eletero = eletero;
    }

    public String getNev() {
        return nev;
    }

    public String getLeiras() {
        return leiras;
    }

    public void sebez(int mennyit) {
        eletero -= mennyit;
    }

    public boolean legyozve() {
        return eletero <= 0;
    }
}
