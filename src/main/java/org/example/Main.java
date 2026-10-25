package org.example;

public class Main {
    public static void main(String[] args) {
        Helyszin kezdoHelyszin = new PalyaEpito().felepit();
        Jatek jatek = new Jatek(kezdoHelyszin);
        jatek.indit();
    }
}
