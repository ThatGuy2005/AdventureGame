package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParancsElemzoTeszt {
    private final ParancsElemzo elemzo = new ParancsElemzo();

    @Test
    void ketSzavasIgetEsArgumentumotFelismer() {
        Parancs p = elemzo.elemez("vedd fel lampa");

        assertTrue(p.isIsmert());
        assertEquals("vedd fel", p.getIge());
        assertEquals("lampa", p.getArgumentum());
    }

    @Test
    void nagybetuketEkezeteketEsSzokozoketKezeli() {
        Parancs p = elemzo.elemez("  MENJ   Észak ");

        assertTrue(p.isIsmert());
        assertEquals("menj", p.getIge());
        assertEquals("eszak", p.getArgumentum());
    }

    @Test
    void argumentumNelkuliParancs() {
        Parancs p = elemzo.elemez("nezd");

        assertTrue(p.isIsmert());
        assertNull(p.getArgumentum());
    }

    @Test
    void ismeretlenParancsotJelez() {
        assertFalse(elemzo.elemez("tancolj").isIsmert());
        assertFalse(elemzo.elemez("").isIsmert());
        assertFalse(elemzo.elemez("vedd lampa").isIsmert());
    }
}
