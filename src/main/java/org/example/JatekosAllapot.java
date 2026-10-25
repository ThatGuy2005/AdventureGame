package org.example;

// State minta: közös interfész, minden állapot maga dönti el, mit jelent egy parancs.
public interface JatekosAllapot {
    void parancs(Jatek j, Parancs p);

    String leiras();
}
