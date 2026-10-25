package org.example;

// State minta: sérülten minden lépés 2-be kerül, az elsősegély meggyógyít.
public class SerultAllapot implements JatekosAllapot {
    private static final int LEPES_KOLTSEG = 2;

    @Override
    public void parancs(Jatek j, Parancs p) {
        switch (p.getIge()) {
            case "menj":
                menj(j, p.getArgumentum());
                break;
            case "vedd fel":
                j.targyFelvesz(p.getArgumentum());
                break;
            case "hasznald":
                hasznald(j, p.getArgumentum());
                break;
            case "nezd":
                j.helyszinKiir();
                break;
            case "taska":
                j.taskaKiir();
                break;
            case "tamad":
                System.out.println("Nincs itt senki, akit megtámadhatnál.");
                break;
            case "menekul":
                System.out.println("Nincs itt semmi, ami elől menekülni kellene.");
                break;
            default:
                System.out.println("Ezt most nem teheted.");
        }
    }

    private void menj(Jatek j, String irany) {
        if (!j.mozog(irany, LEPES_KOLTSEG)) {
            return;
        }
        System.out.println("Sántikálva haladsz, ez " + LEPES_KOLTSEG + " lépésbe került.");
        Helyszin h = j.getAktualisHelyszin();
        if (h.getEllenseg() != null) {
            j.helyszinKiir();
            // State minta: sérülten kezdünk harcba
            j.setAllapot(new HarcAllapot(h.getEllenseg(), true));
        } else if (h.isSotet() && !j.isLampaVilagit()) {
            j.sotetKiir();
            // State minta: sötétben is sérültek maradunk
            j.setAllapot(new SotetbenAllapot(true));
        } else {
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
                break;
            case "kulcs":
                j.ajtotNyit();
                break;
            case "elsosegely":
                j.targyElhasznal(targy);
                System.out.println("Bekötözöd a sebeidet. Sokkal jobban vagy!");
                // State minta: gyógyulás után visszaváltunk normál állapotba
                j.setAllapot(new NormalAllapot());
                break;
            default:
                System.out.println("Most nem veszed hasznát ennek: " + targy.getNev());
        }
    }

    @Override
    public String leiras() {
        return "Sérült";
    }
}
