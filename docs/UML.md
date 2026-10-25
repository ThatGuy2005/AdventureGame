# UML diagramok

A diagramok [Mermaid](https://mermaid.js.org/) formátumban vannak, a GitHub és az IntelliJ Markdown-előnézete
(*Settings → Languages & Frameworks → Markdown → Mermaid*) kirajzolja őket.

## Állapotdiagram

A `SotetbenAllapot` és a `HarcAllapot` egy `serult` jelzőt is tárol, ezért a diagramon mindkettő két változatban
szerepel (sérülten és sérülés nélkül). Így látszik, hogy a sötétség vagy a harc után a játékos a megfelelő
állapotba (Normál vagy Sérült) tér vissza.

```mermaid
stateDiagram-v2
    direction LR
    state "Normál" as Normal
    state "Sérült" as Serult
    state "Sötétben" as Sotet
    state "Sötétben, sérülten" as SotetSerult
    state "Harc" as Harc
    state "Harc, sérülten" as HarcSerult
    state "Győzelem" as Gyozelem
    state "Vereség" as Vereseg

    [*] --> Normal

    Normal --> Sotet : sötét hely
    Sotet --> Normal : lámpa / világos hely
    Sotet --> Sotet : sötét hely
    Normal --> Harc : ellenség
    Sotet --> Harc : ellenség
    Harc --> Normal : győzelem / menekül

    Harc --> HarcSerult : harapás
    HarcSerult --> Harc : elsősegély
    HarcSerult --> Serult : győzelem / menekül
    HarcSerult --> Vereseg : újabb harapás

    Serult --> Normal : elsősegély
    Serult --> HarcSerult : ellenség
    Serult --> SotetSerult : sötét hely
    SotetSerult --> Serult : lámpa / világos hely
    SotetSerult --> Sotet : elsősegély
    SotetSerult --> HarcSerult : ellenség

    Normal --> Gyozelem : kulcs
    Serult --> Gyozelem : kulcs
    Gyozelem --> [*]
    Vereseg --> [*]
```

| Felirat | Parancs és feltétel |
|---|---|
| sötét hely | `menj <irany>` sötét helyre, és a lámpa nem ég |
| világos hely | `menj <irany>` világos helyre |
| lámpa | `hasznald lampa` (a lámpa onnantól égve marad) |
| ellenség | `menj <irany>` olyan helyre, ahol ellenség van |
| győzelem | ütés, amitől az ellenség életereje 0-ra csökken |
| menekül | `menekul`: visszalépés az előző helyszínre |
| harapás | ütés, de az ellenség még áll, és visszaharap |
| újabb harapás | ugyanez, de a játékos már sérült: **elveszíti a játékot** |
| elsősegély | `hasznald elsosegely` (elhasználódik) |
| kulcs | `hasznald kulcs` a zárt Vizsgaterem melletti helyszínen |

- **Ütés**: `tamad` (1 sebzés) vagy `hasznald seprű` (3 sebzés). Bodrinak 3 életereje van.
- **Időkorlát**: bármelyik állapotban, ha a lépések száma eléri a 30-at, a játék véget ér
  („Elkéstél a vizsgáról!”). Ezt a `Jatek` ellenőrzi minden parancs után.
- **Lépésköltség**: egy mozgás (`menj` vagy `menekul`) Normál és Sötétben állapotban 1 lépés, sérülten 2 lépés.
- A diagramon nem szereplő parancsok (`nezd`, `taska`, `vedd fel`, haszontalan tárgy használata) nem váltanak
  állapotot. Harc közben ezek egyáltalán nem működnek, csak a `tamad`, `menekul` és `hasznald`.
- A `segitseg`, a `kilep` és az ismeretlen parancsokat a `Jatek` maga kezeli, ezek egyik állapottól sem függnek.

## Osztálydiagram – State minta

| State minta szerep | Osztály |
|---|---|
| Context | `Jatek` |
| State | `JatekosAllapot` (interfész) |
| ConcreteState | `NormalAllapot`, `SerultAllapot`, `SotetbenAllapot`, `HarcAllapot` |

```mermaid
classDiagram
    class Jatek {
        -Helyszin aktualisHelyszin
        -JatekosAllapot allapot
        -ArrayList~Targy~ taska
        -int lepesek
        +parancsotFeldolgoz(String sor) void
        +setAllapot(JatekosAllapot ujAllapot) void
        +getAllapot() JatekosAllapot
        +mozog(String irany, int koltseg) boolean
        +visszamenekul(int koltseg) void
        +helyszinKiir() void
    }

    class JatekosAllapot {
        <<interface>>
        +parancs(Jatek j, Parancs p) void
        +leiras() String
    }

    class NormalAllapot {
        +parancs(Jatek j, Parancs p) void
        +leiras() String
    }

    class SerultAllapot {
        +parancs(Jatek j, Parancs p) void
        +leiras() String
    }

    class SotetbenAllapot {
        -boolean serult
        +SotetbenAllapot(boolean serult)
        +parancs(Jatek j, Parancs p) void
        +leiras() String
    }

    class HarcAllapot {
        -Ellenseg ellenseg
        -boolean serult
        +HarcAllapot(Ellenseg ellenseg, boolean serult)
        +parancs(Jatek j, Parancs p) void
        +leiras() String
    }

    class Parancs {
        -String ige
        -String argumentum
        +getIge() String
        +getArgumentum() String
    }

    class Ellenseg {
        -String nev
        -int eletero
        +sebez(int mennyit) void
        +legyozve() boolean
    }

    Jatek --> JatekosAllapot : aktuális állapot
    JatekosAllapot <|.. NormalAllapot
    JatekosAllapot <|.. SerultAllapot
    JatekosAllapot <|.. SotetbenAllapot
    JatekosAllapot <|.. HarcAllapot
    JatekosAllapot ..> Jatek : setAllapot hívása
    JatekosAllapot ..> Parancs : feldolgozza
    HarcAllapot --> Ellenseg
```

A `Jatek` minden játékbeli parancsot továbbad az aktuális állapotnak (`allapot.parancs(this, p)`). Hogy mi lesz a
következő állapot, azt mindig az aktuális állapot dönti el: létrehozza az új állapotobjektumot, és átadja a
`j.setAllapot(...)` hívással. Ezért nincs a kódban egyetlen `if (allapot == ...)` vagy `switch` sem az állapotra.
