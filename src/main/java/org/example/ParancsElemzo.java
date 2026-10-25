package org.example;

public class ParancsElemzo {
    private static final String[] ISMERT_IGEK = {
            "menj", "vedd fel", "hasznald", "nezd", "taska",
            "tamad", "menekul", "segitseg", "kilep"
    };
    private static final String EKEZETES = "áéíóöőúüű";
    private static final String EKEZET_NELKUL = "aeiooouuu";

    public Parancs elemez(String sor) {
        String[] szavak = ekezetNelkul(sor.trim().toLowerCase()).split("\\s+");
        String ige = szavak[0];
        int argumentumHelye = 1;

        if (ige.equals("vedd") && szavak.length > 1 && szavak[1].equals("fel")) {
            ige = "vedd fel";
            argumentumHelye = 2;
        }

        String argumentum = null;
        if (szavak.length > argumentumHelye) {
            argumentum = szavak[argumentumHelye];
        }
        return new Parancs(ige, argumentum, ismertIge(ige));
    }

    public static String ekezetNelkul(String szoveg) {
        StringBuilder eredmeny = new StringBuilder();
        for (int i = 0; i < szoveg.length(); i++) {
            char betu = szoveg.charAt(i);
            int index = EKEZETES.indexOf(betu);
            if (index >= 0) {
                eredmeny.append(EKEZET_NELKUL.charAt(index));
            } else {
                eredmeny.append(betu);
            }
        }
        return eredmeny.toString();
    }

    private boolean ismertIge(String ige) {
        for (String ismert : ISMERT_IGEK) {
            if (ismert.equals(ige)) {
                return true;
            }
        }
        return false;
    }
}
