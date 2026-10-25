package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class Helyszin {
    private static final String[] IRANYOK = {"eszak", "del", "kelet", "nyugat", "fel", "le"};

    private final String nev;
    private final String leiras;
    private final boolean sotet;
    private boolean zarva = false;
    private final HashMap<String, Helyszin> kijaratok = new HashMap<>();
    private final ArrayList<Targy> targyak = new ArrayList<>();
    private Ellenseg ellenseg;

    public Helyszin(String nev, String leiras, boolean sotet) {
        this.nev = nev;
        this.leiras = leiras;
        this.sotet = sotet;
    }

    public String getNev() {
        return nev;
    }

    public boolean isSotet() {
        return sotet;
    }

    public boolean isZarva() {
        return zarva;
    }

    public void setZarva(boolean zarva) {
        this.zarva = zarva;
    }

    public Ellenseg getEllenseg() {
        return ellenseg;
    }

    public void setEllenseg(Ellenseg ellenseg) {
        this.ellenseg = ellenseg;
    }

    public void setKijarat(String irany, Helyszin hova) {
        kijaratok.put(irany, hova);
    }

    public Helyszin getKijarat(String irany) {
        return kijaratok.get(irany);
    }

    public Helyszin zartSzomszed() {
        for (Helyszin szomszed : kijaratok.values()) {
            if (szomszed.isZarva()) {
                return szomszed;
            }
        }
        return null;
    }

    public void targyatLerak(Targy targy) {
        targyak.add(targy);
    }

    public Targy targyatElvesz(String targyNev) {
        for (Targy targy : targyak) {
            if (targy.nevEgyezik(targyNev)) {
                targyak.remove(targy);
                return targy;
            }
        }
        return null;
    }

    public String teljesLeiras() {
        String szoveg = "--- " + nev + " ---\n" + leiras
                + "\nTárgyak: " + targyakSzovege()
                + "\nKijáratok: " + kijaratokSzovege();
        if (ellenseg != null) {
            szoveg += "\nVigyázz! " + ellenseg.getLeiras();
        }
        return szoveg;
    }

    private String targyakSzovege() {
        if (targyak.isEmpty()) {
            return "nincs";
        }
        StringBuilder szoveg = new StringBuilder();
        for (Targy targy : targyak) {
            if (szoveg.length() > 0) {
                szoveg.append(", ");
            }
            szoveg.append(targy);
        }
        return szoveg.toString();
    }

    private String kijaratokSzovege() {
        StringBuilder szoveg = new StringBuilder();
        for (String irany : IRANYOK) {
            if (kijaratok.containsKey(irany)) {
                szoveg.append(irany).append(" ");
            }
        }
        return szoveg.toString().trim();
    }
}
