package org.example;

public class PalyaEpito {

    public Helyszin felepit() {
        Helyszin eloadoterem = new Helyszin("Előadóterem",
                "Egy kihalt előadóterem padjai között ébredsz. A falon lógó óra kegyetlenül ketyeg.", false);
        Helyszin folyoso = new Helyszin("Folyosó",
                "Hosszú, poros folyosó. Északra a vizsgaterem ajtaja, keletre a lépcsőház.", false);
        Helyszin vizsgaterem = new Helyszin("Vizsgaterem",
                "A padokon már ott vannak a vizsgalapok.", false);
        Helyszin bufe = new Helyszin("Büfé",
                "Üres pultok és egy elfelejtett szendvics. A falon egy elsősegélydoboz lóg.", false);
        Helyszin konyvtar = new Helyszin("Könyvtár",
                "Polcok tele régi jegyzetekkel. A sarokban a takarító itt felejtette a holmiját.", false);
        Helyszin lepcsohaz = new Helyszin("Lépcsőház",
                "Kopott lépcsők vezetnek fel a gondnoki irodába és le a pincébe.", false);
        Helyszin gondnokiIroda = new Helyszin("Gondnoki iroda",
                "Szűk iroda kulcstartó táblával. A vizsgaterem kulcsa hiányzik, mellette egy cetli: \"Pince\".", false);
        Helyszin pince = new Helyszin("Pince",
                "Nyirkos, dohos pince. Valahol csöpög a víz.", true);
        Helyszin kazanhaz = new Helyszin("Kazánház",
                "A kazán tüze vöröses fényt vet a falakra. Meleg van, de semmi hasznosat nem látsz.", false);

        vizsgaterem.setZarva(true);

        osszekot(eloadoterem, "kelet", folyoso, "nyugat");
        osszekot(folyoso, "eszak", vizsgaterem, "del");
        osszekot(folyoso, "kelet", lepcsohaz, "nyugat");
        osszekot(folyoso, "del", bufe, "eszak");
        osszekot(bufe, "nyugat", konyvtar, "kelet");
        osszekot(lepcsohaz, "fel", gondnokiIroda, "le");
        osszekot(lepcsohaz, "le", pince, "fel");
        osszekot(pince, "kelet", kazanhaz, "nyugat");

        konyvtar.targyatLerak(new Targy("seprű", "erős nyelű seprű"));
        bufe.targyatLerak(new Targy("elsosegely", "elsősegélycsomag"));
        gondnokiIroda.targyatLerak(new Targy("lampa", "zseblámpa"));
        pince.targyatLerak(new Targy("kulcs", "a vizsgaterem kulcsa"));

        lepcsohaz.setEllenseg(new Ellenseg("Bodri",
                "Bodri, a gondnok harapós kutyája morogva elállja az utad!", 2));

        return eloadoterem;
    }

    private void osszekot(Helyszin egyik, String irany, Helyszin masik, String visszaIrany) {
        egyik.setKijarat(irany, masik);
        masik.setKijarat(visszaIrany, egyik);
    }
}
