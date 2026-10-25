package org.example;

// State minta: harc közben csak a tamad, menekul és hasznald parancs működik.
// Megjegyzi, hogy a játékos sérült-e: ha sérülten kap újabb sebet, vége a játéknak.
public class HarcAllapot implements JatekosAllapot {
    private final Ellenseg ellenseg;
    private final boolean serult;

    public HarcAllapot(Ellenseg ellenseg, boolean serult) {
        this.ellenseg = ellenseg;
        this.serult = serult;
    }

    @Override
    public void parancs(Jatek j, Parancs p) {
        switch (p.getIge()) {
            case "tamad":
                System.out.println("Puszta kézzel rátámadsz: " + ellenseg.getNev() + ".");
                ellenseg.sebez(1);
                utesUtan(j);
                break;
            case "menekul":
                menekul(j);
                break;
            case "hasznald":
                hasznald(j, p.getArgumentum());
                break;
            default:
                System.out.println(ellenseg.getNev() + " elállja az utad! Harc közben csak ezek működnek: "
                        + "tamad, menekul, hasznald <targy>");
        }
    }

    private void utesUtan(Jatek j) {
        if (ellenseg.legyozve()) {
            System.out.println(ellenseg.getNev() + " vinnyogva elszalad. Szabad az út!");
            j.getAktualisHelyszin().setEllenseg(null);
            // State minta: a harc végén visszatérünk a harc előtti állapotba
            j.setAllapot(harcUtaniAllapot());
        } else if (serult) {
            j.jatekVege(ellenseg.getNev() + " újra megharap, és összeesel. Nem érsz oda a vizsgára. VESZTETTÉL.");
        } else {
            System.out.println(ellenseg.getNev() + " visszatámad és megharap! Megsérültél.");
            // State minta: tovább harcolunk, de már sérülten
            j.setAllapot(new HarcAllapot(ellenseg, true));
        }
    }

    private void menekul(Jatek j) {
        int koltseg = 1;
        if (serult) {
            koltseg = 2;
        }
        j.visszamenekul(koltseg);
        System.out.println("Gyorsan visszamenekülsz oda, ahonnan jöttél.");
        // State minta: menekülés után vége a harcnak
        j.setAllapot(harcUtaniAllapot());
        j.helyszinKiir();
    }

    private void hasznald(Jatek j, String targyNev) {
        Targy targy = j.hasznalandoTargy(targyNev);
        if (targy == null) {
            return;
        }
        switch (targy.getNev()) {
            case "seprű":
                System.out.println("Meglengeted a seprűt, és hatalmasat csapsz vele!");
                ellenseg.sebez(3);
                utesUtan(j);
                break;
            case "elsosegely":
                if (serult) {
                    j.targyElhasznal(targy);
                    System.out.println("Gyorsan bekötözöd a sebedet.");
                    j.setAllapot(new HarcAllapot(ellenseg, false));
                } else {
                    System.out.println("Nem vagy megsérülve, most nincs rá szükséged.");
                }
                break;
            default:
                System.out.println("Ezzel most nem mész semmire a harcban.");
        }
    }

    private JatekosAllapot harcUtaniAllapot() {
        if (serult) {
            return new SerultAllapot();
        }
        return new NormalAllapot();
    }

    @Override
    public String leiras() {
        if (serult) {
            return "Harcban (" + ellenseg.getNev() + "), sérülten";
        }
        return "Harcban (" + ellenseg.getNev() + ")";
    }
}
