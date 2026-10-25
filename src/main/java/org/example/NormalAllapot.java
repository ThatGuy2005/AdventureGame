package org.example;

// State minta: alapállapot, minden parancs a megszokott módon működik.
public class NormalAllapot implements JatekosAllapot {

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
        if (!j.mozog(irany, 1)) {
            return;
        }
        Helyszin h = j.getAktualisHelyszin();
        if (h.getEllenseg() != null) {
            j.helyszinKiir();
            // State minta: ellenség állja el az utat, harc állapotba váltunk
            j.setAllapot(new HarcAllapot(h.getEllenseg(), false));
        } else if (h.isSotet() && !j.isLampaVilagit()) {
            j.sotetKiir();
            // State minta: sötét helyre lámpa nélkül érve sötétben állapotba váltunk
            j.setAllapot(new SotetbenAllapot(false));
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
                System.out.println("Nem vagy megsérülve, most nincs rá szükséged.");
                break;
            default:
                System.out.println("Most nem veszed hasznát ennek: " + targy.getNev());
        }
    }

    @Override
    public String leiras() {
        return "Normál";
    }
}
