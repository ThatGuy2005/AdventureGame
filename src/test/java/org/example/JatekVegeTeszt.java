package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JatekVegeTeszt {
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

    @Test
    void kulccsalKinyitvaAVizsgatermetAJatekosNyer() {
        parancsok("menj kelet", "menj del", "menj nyugat", "vedd fel seprű",
                "menj kelet", "menj eszak", "menj kelet", "hasznald seprű",
                "menj fel", "vedd fel lampa", "hasznald lampa", "menj le", "menj le",
                "vedd fel kulcs", "menj fel", "menj nyugat", "hasznald kulcs");

        assertTrue(jatek.isNyert());
        assertFalse(jatek.isFut());
        assertEquals(11, jatek.getLepesek());
    }

    @Test
    void kulcsNelkulAVizsgaterembeNemLehetBelepni() {
        parancsok("menj kelet", "menj eszak");

        assertEquals("Folyosó", jatek.getAktualisHelyszin().getNev());
        assertTrue(jatek.isFut());
    }

    @Test
    void serultenUjabbSebetKapvaAJatekosVeszit() {
        parancsok("menj kelet", "menj kelet", "tamad", "tamad");

        assertFalse(jatek.isFut());
        assertFalse(jatek.isNyert());
    }

    @Test
    void harmincLepesUtanAJatekosElkesik() {
        for (int i = 0; i < 14; i++) {
            parancsok("menj kelet", "menj nyugat");
        }
        parancsok("menj kelet");
        assertTrue(jatek.isFut());

        parancsok("menj nyugat");

        assertEquals(30, jatek.getLepesek());
        assertFalse(jatek.isFut());
        assertFalse(jatek.isNyert());
    }

    @Test
    void ismeretlenParancsNemOkozHibatEsNemValtAllapotot() {
        parancsok("tancolj", "", "vedd", "menj", "hasznald", "menj delnyugat");

        assertTrue(jatek.isFut());
        assertInstanceOf(NormalAllapot.class, jatek.getAllapot());
        assertEquals(0, jatek.getLepesek());
    }
}
