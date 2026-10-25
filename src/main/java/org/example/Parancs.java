package org.example;

public class Parancs {
    private final String ige;
    private final String argumentum;
    private final boolean ismert;

    public Parancs(String ige, String argumentum, boolean ismert) {
        this.ige = ige;
        this.argumentum = argumentum;
        this.ismert = ismert;
    }

    public String getIge() {
        return ige;
    }

    public String getArgumentum() {
        return argumentum;
    }

    public boolean isIsmert() {
        return ismert;
    }
}
