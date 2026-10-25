package org.example;

// State minta: sötét helyen lámpa nélkül nem látszanak a tárgyak és a kijáratok.
// Megjegyzi, hogy a játékos sérült-e, hogy fény esetén a jó állapotba térjen vissza.
public class SotetbenAllapot implements JatekosAllapot {
    private final boolean serult;

    public SotetbenAllapot(boolean serult) {
        this.serult = serult;
    }

    @Override
    public void parancs(Jatek j, Parancs p) {
        switch (p.getIge()) {
            case "menj":
                menj(j, p.getArgumentum());
                break;
            case "vedd fel":
                System.out.println("Túl sötét van, nem látod, mit vehetnél fel.");
                break;
            case "hasznald":
                hasznald(j, p.getArgumentum());
                break;
            case "nezd":
                j.sotetKiir();
                break;
            case "taska":
                j.taskaKiir();
                break;
            case "tamad":
                System.out.println("Vaktában hadonászol, de nincs itt senki.");
                break;
            case "menekul":
                System.out.println("A sötét elől nem lehet elmenekülni.");
                break;
            default:
                System.out.println("Ezt most nem teheted.");
        }
    }

    private void menj(Jatek j, String irany) {
        int koltseg = 1;
        if (serult) {
            koltseg = 2;
        }
        if (!j.mozog(irany, koltseg)) {
            return;
        }
        System.out.println("Tapogatózva elindulsz.");
        Helyszin h = j.getAktualisHelyszin();
        if (h.getEllenseg() != null) {
            j.helyszinKiir();
            // State minta: a sötétből egyenesen harcba kerülünk
            j.setAllapot(new HarcAllapot(h.getEllenseg(), serult));
        } else if (h.isSotet()) {
            j.sotetKiir();
        } else {
            // State minta: világos helyre érve kilépünk a sötétben állapotból
            j.setAllapot(vilagosAllapot());
            j.helyszinKiir();
        }
    }

    private void hasznald(Jatek j, String targyNev) {
        Targy targy = j.hasznalandoTargy(targyNev);
        if (targy == null) {
            return;
        }
        switch (targy.getNev()) {
            case "lampa":
                j.lampaFelkapcsol();
                // State minta: a lámpa fényénél újra látunk
                j.setAllapot(vilagosAllapot());
                j.helyszinKiir();
                break;
            case "elsosegely":
                if (serult) {
                    j.targyElhasznal(targy);
                    System.out.println("Vakon is sikerül bekötöznöd a sebeidet.");
                    j.setAllapot(new SotetbenAllapot(false));
                } else {
                    System.out.println("Nem vagy megsérülve, most nincs rá szükséged.");
                }
                break;
            default:
                System.out.println("Sötétben semmire sem mész vele.");
        }
    }

    private JatekosAllapot vilagosAllapot() {
        if (serult) {
            return new SerultAllapot();
        }
        return new NormalAllapot();
    }

    @Override
    public String leiras() {
        if (serult) {
            return "Sötétben, sérülten";
        }
        return "Sötétben";
    }
}
