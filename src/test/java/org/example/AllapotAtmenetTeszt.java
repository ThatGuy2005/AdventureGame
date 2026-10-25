package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllapotAtmenetTeszt {
    private Jatek jatek;

    @BeforeEach
    void ujJatek() {
        jatek = new Jatek(new PalyaEpito().felepit());
    }

    private void parancsok(String... sorok) {
        for (String sor : sorok) {
            jatek.parancsotFeldolgoz(sor);
        }
    }

    private void bodritSepruvelLegyozi() {
        parancsok("menj kelet", "menj del", "menj nyugat", "vedd fel seprű",
                "menj kelet", "menj eszak", "menj kelet", "hasznald seprű");
    }

    @Test
    void aJatekNormalAllapotbanIndul() {
        assertInstanceOf(NormalAllapot.class, jatek.getAllapot());
    }

    @Test
    void ellensegesHelyszinreLepveHarcAllapotLesz() {
        parancsok("menj kelet", "menj kelet");

        assertInstanceOf(HarcAllapot.class, jatek.getAllapot());
        assertEquals("Lépcsőház", jatek.getAktualisHelyszin().getNev());
    }

    @Test
    void harcbanAMenjParancsTiltott() {
        parancsok("menj kelet", "menj kelet", "menj nyugat");

        assertInstanceOf(HarcAllapot.class, jatek.getAllapot());
        assertEquals("Lépcsőház", jatek.getAktualisHelyszin().getNev());
        assertEquals(2, jatek.getLepesek());
    }

    @Test
    void sepruvelLegyozveAzEllensegetNormalAllapotbaTerVissza() {
        bodritSepruvelLegyozi();

        assertInstanceOf(NormalAllapot.class, jatek.getAllapot());
        assertNull(jatek.getAktualisHelyszin().getEllenseg());
    }

    @Test
    void pusztaKezzelTamadvaMegserulMenekulesUtanSerultAllapotLesz() {
        parancsok("menj kelet", "menj kelet", "tamad");
        assertInstanceOf(HarcAllapot.class, jatek.getAllapot());
        assertTrue(jatek.getAllapot().leiras().contains("sérülten"));

        parancsok("menekul");
        assertInstanceOf(SerultAllapot.class, jatek.getAllapot());
        assertEquals("Folyosó", jatek.getAktualisHelyszin().getNev());
    }

    @Test
    void serultenEgyLepesKetLepesbeKerul() {
        parancsok("menj kelet", "menj kelet", "tamad", "menekul");
        int lepesekElotte = jatek.getLepesek();

        parancsok("menj del");

        assertEquals(lepesekElotte + 2, jatek.getLepesek());
    }

    @Test
    void elsosegelyMeggyogyitjaASerultJatekost() {
        parancsok("menj kelet", "menj kelet", "tamad", "menekul",
                "menj del", "vedd fel elsosegely", "hasznald elsosegely");

        assertInstanceOf(NormalAllapot.class, jatek.getAllapot());
        assertFalse(jatek.vanNala("elsosegely"));
    }

    @Test
    void sotetPincebeLampaNelkulSotetbenAllapotLesz() {
        bodritSepruvelLegyozi();

        parancsok("menj le");

        assertInstanceOf(SotetbenAllapot.class, jatek.getAllapot());
    }

    @Test
    void sotetbenNemLehetTargyatFelvenni() {
        bodritSepruvelLegyozi();

        parancsok("menj le", "vedd fel kulcs");

        assertFalse(jatek.vanNala("kulcs"));
    }

    @Test
    void lampaFelkapcsolasaUtanNormalAllapotLeszEsLatszikAKulcs() {
        bodritSepruvelLegyozi();
        parancsok("menj fel", "vedd fel lampa", "menj le", "menj le");
        assertInstanceOf(SotetbenAllapot.class, jatek.getAllapot());

        parancsok("hasznald lampa", "vedd fel kulcs");

        assertInstanceOf(NormalAllapot.class, jatek.getAllapot());
        assertTrue(jatek.vanNala("kulcs"));
    }
}
