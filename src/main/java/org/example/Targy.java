package org.example;

public class Targy {
    private final String nev;
    private final String leiras;

    public Targy(String nev, String leiras) {
        this.nev = nev;
        this.leiras = leiras;
    }

    public String getNev() {
        return nev;
    }

    public String getLeiras() {
        return leiras;
    }

    public boolean nevEgyezik(String beirtNev) {
        return ParancsElemzo.ekezetNelkul(nev).equals(ParancsElemzo.ekezetNelkul(beirtNev));
    }

    @Override
    public String toString() {
        return nev + " (" + leiras + ")";
    }
}
