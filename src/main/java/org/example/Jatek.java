package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Jatek {
    private static final int MAX_LEPES = 30;

    private Helyszin aktualisHelyszin;
    private Helyszin elozoHelyszin;
    private JatekosAllapot allapot;
    private final ArrayList<Targy> taska = new ArrayList<>();
    private final ParancsElemzo elemzo = new ParancsElemzo();
    private int lepesek = 0;
    private boolean lampaVilagit = false;
    private boolean fut = true;
    private boolean nyert = false;

    public Jatek(Helyszin kezdoHelyszin) {
        aktualisHelyszin = kezdoHelyszin;
        // State minta: a játékos normál állapotból indul
        allapot = new NormalAllapot();
    }

    public void indit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===== KÉSÉSBEN =====");
        System.out.println("Egy elhagyatott egyetemi épületben ébredsz. 10 perc múlva kezdődik a vizsgád,");
        System.out.println("de a vizsgaterem kulcsa a pincében van. Siess, " + MAX_LEPES + " lépésed van!");
        System.out.println("A parancsok listájához írd be: segitseg");
        System.out.println();
        helyszinKiir();

        while (fut) {
            System.out.print("\n[" + allapot.leiras() + " | hátralévő lépések: " + (MAX_LEPES - lepesek) + "] > ");
            if (!scanner.hasNextLine()) {
                break;
            }
            parancsotFeldolgoz(scanner.nextLine());
        }
    }

    public void parancsotFeldolgoz(String sor) {
        vegrehajt(elemzo.elemez(sor));
        if (fut && lepesek >= MAX_LEPES) {
            jatekVege("Elkéstél a vizsgáról! Elfogyott az időd. VESZTETTÉL.");
        }
    }

    private void vegrehajt(Parancs p) {
        if (!p.isIsmert()) {
            System.out.println("Elnézést, ezt a parancsot nem ismerem. A parancsok listájához írd be: segitseg");
        } else if (p.getIge().equals("segitseg")) {
            segitsegKiir();
        } else if (p.getIge().equals("kilep")) {
            jatekVege("Kiléptél a játékból. Viszlát!");
        } else {
            // State minta: a parancsot az aktuális állapot dolgozza fel
            allapot.parancs(this, p);
        }
    }

    // State minta: az állapotok ezen keresztül váltanak egymásra
    public void setAllapot(JatekosAllapot ujAllapot) {
        allapot = ujAllapot;
    }

    public JatekosAllapot getAllapot() {
        return allapot;
    }

    public boolean isFut() {
        return fut;
    }

    public boolean isNyert() {
        return nyert;
    }

    public int getLepesek() {
        return lepesek;
    }

    public Helyszin getAktualisHelyszin() {
        return aktualisHelyszin;
    }

    public boolean isLampaVilagit() {
        return lampaVilagit;
    }

    public boolean mozog(String irany, int koltseg) {
        if (irany == null) {
            System.out.println("Merre menjek? Például: menj eszak");
            return false;
        }
        Helyszin cel = aktualisHelyszin.getKijarat(irany);
        if (cel == null) {
            System.out.println("Arra nem lehet menni.");
            return false;
        }
        if (cel.isZarva()) {
            System.out.println("Ez az ajtó zárva van. Kulcs kell hozzá!");
            return false;
        }
        elozoHelyszin = aktualisHelyszin;
        aktualisHelyszin = cel;
        lepesek += koltseg;
        return true;
    }

    public void visszamenekul(int koltseg) {
        Helyszin honnan = aktualisHelyszin;
        aktualisHelyszin = elozoHelyszin;
        elozoHelyszin = honnan;
        lepesek += koltseg;
    }

    public void helyszinKiir() {
        System.out.println(aktualisHelyszin.teljesLeiras());
    }

    public void sotetKiir() {
        System.out.println("--- " + aktualisHelyszin.getNev() + " ---");
        System.out.println("Koromsötét van. Nem látsz se tárgyakat, se kijáratokat.");
    }

    public void targyFelvesz(String targyNev) {
        if (targyNev == null) {
            System.out.println("Mit vegyek fel? Például: vedd fel lampa");
            return;
        }
        Targy targy = aktualisHelyszin.targyatElvesz(targyNev);
        if (targy == null) {
            System.out.println("Itt nincs ilyen tárgy: " + targyNev);
        } else {
            taska.add(targy);
            System.out.println("Felvetted: " + targy.getNev());
        }
    }

    public void taskaKiir() {
        if (taska.isEmpty()) {
            System.out.println("A táskád üres.");
            return;
        }
        System.out.println("A táskádban van:");
        for (Targy targy : taska) {
            System.out.println("  - " + targy);
        }
    }

    public Targy hasznalandoTargy(String targyNev) {
        if (targyNev == null) {
            System.out.println("Mit használjak? Például: hasznald lampa");
            return null;
        }
        for (Targy targy : taska) {
            if (targy.nevEgyezik(targyNev)) {
                return targy;
            }
        }
        System.out.println("Nincs nálad ilyen tárgy: " + targyNev);
        return null;
    }

    public boolean vanNala(String targyNev) {
        for (Targy targy : taska) {
            if (targy.nevEgyezik(targyNev)) {
                return true;
            }
        }
        return false;
    }

    public void targyElhasznal(Targy targy) {
        taska.remove(targy);
    }

    public void lampaFelkapcsol() {
        if (lampaVilagit) {
            System.out.println("A lámpa már világít.");
        } else {
            lampaVilagit = true;
            System.out.println("Felkapcsoltad a lámpát. Most már a sötétben is látsz.");
        }
    }

    public void ajtotNyit() {
        Helyszin zartHelyszin = aktualisHelyszin.zartSzomszed();
        if (zartHelyszin == null) {
            System.out.println("Itt nincs zárt ajtó, amit kinyithatnál.");
            return;
        }
        zartHelyszin.setZarva(false);
        nyert = true;
        jatekVege("A kulcs elfordul a zárban, kinyílik az ajtó: " + zartHelyszin.getNev()
                + ". Az utolsó pillanatban beesel a vizsgára. NYERTÉL!");
    }

    public void jatekVege(String uzenet) {
        System.out.println();
        System.out.println(uzenet);
        fut = false;
    }

    private void segitsegKiir() {
        System.out.println("Parancsok:");
        System.out.println("  menj <irany>      irányok: eszak, del, kelet, nyugat, fel, le");
        System.out.println("  vedd fel <targy>  tárgy felvétele");
        System.out.println("  hasznald <targy>  tárgy használata");
        System.out.println("  nezd              körülnézel");
        System.out.println("  taska             megnézed, mi van nálad");
        System.out.println("  tamad             támadás harc közben");
        System.out.println("  menekul           menekülés harc közben");
        System.out.println("  segitseg          ez a súgó");
        System.out.println("  kilep             kilépés a játékból");
        System.out.println("Ékezetek nélkül is írhatsz, például: vedd fel sepru");
    }
}
