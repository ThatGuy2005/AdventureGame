# Késésben – szöveges kalandjáték

Konzolos szöveges kalandjáték Java nyelven, egyetemi beadandó a **State tervezési minta** gyakorlására.

## A történet

Egy elhagyatott egyetemi épületben ébredsz. 10 perc múlva kezdődik a vizsgád, de a vizsgaterem kulcsa lent van a
pincében. Meg kell találnod, és ki kell nyitnod vele a vizsgatermet, mielőtt lejár az idő.

## Futtatás

Szükséges: **JDK 25** (a `pom.xml` erre van beállítva).

- **IntelliJ IDEA**: nyisd meg a `src/main/java/org/example/Main.java` fájlt, és futtasd a `main` metódust.
- **Maven**, parancssorból:
  ```
  mvn compile
  java -cp target/classes org.example.Main
  ```
- **Tesztek**: `mvn test`, vagy IntelliJ-ben jobb klikk a `src/test/java` mappán → *Run 'All Tests'*.

> Ha a Windows-konzol rosszul kezeli az ékezetes bevitelt, írd a parancsokat ékezet nélkül (`sepru`, `eszak`).
> A játék mindkét formát elfogadja.

## A játék célja

- **Győzelem**: szerezd meg a kulcsot a pincéből, és a Folyosón, a Vizsgaterem ajtaja előtt használd: `hasznald kulcs`.
- **Vereség**:
  - elfogy a 30 lépés („Elkéstél a vizsgáról!”), vagy
  - harc közben sérülten újabb sebet kapsz.

A prompt mindig mutatja az aktuális állapotot és a hátralévő lépések számát:

```
[Sérült | hátralévő lépések: 24] >
```

## Parancsok

| Parancs | Leírás |
|---|---|
| `menj <irany>` | Mozgás. Irányok: `eszak`, `del`, `kelet`, `nyugat`, `fel`, `le`. Lépésbe kerül. |
| `vedd fel <targy>` | Tárgy felvétele az aktuális helyszínről |
| `hasznald <targy>` | Tárgy használata; hogy mit csinál, az a játékos állapotától függ |
| `nezd` | Körülnézés: a helyszín leírása, a tárgyak és a kijáratok |
| `taska` | A nálad lévő tárgyak listája |
| `tamad` | Támadás puszta kézzel (harc közben) |
| `menekul` | Menekülés vissza az előző helyszínre (harc közben); lépésbe kerül |
| `segitseg` | A parancsok listája |
| `kilep` | Kilépés a játékból |

Ismeretlen parancsra a játék udvarias hibaüzenetet ír ki, és fut tovább.

## Tárgyak

| Tárgy | Hol található | Mire jó |
|---|---|---|
| `lampa` | Gondnoki iroda | Ha bekapcsolod, a sötét helyeken is látsz |
| `kulcs` | Pince | Kinyitja a Vizsgatermet |
| `elsosegely` | Büfé | Meggyógyít, ha sérült vagy (egyszer használható) |
| `seprű` | Könyvtár | Harcban 3 sebzés: Bodri egyetlen ütéstől elszalad |

## Állapotok

| Állapot | Mit jelent |
|---|---|
| **Normál** | Minden parancs működik, egy mozgás 1 lépés. |
| **Sötétben** | Sötét helyen vagy, és nem ég a lámpa. Nem látod a tárgyakat és a kijáratokat, és nem tudsz semmit felvenni. Tapogatózva továbbmehetsz, vagy felkapcsolhatod a lámpát. |
| **Sérült** | Egy mozgás 2 lépésbe kerül. Az elsősegély meggyógyít. |
| **Harc** | Ellenség állja el az utat. Csak a `tamad`, `menekul` és `hasznald` működik. Ha sérülten megharapnak, vesztettél. |

Az állapotdiagram és a State minta osztálydiagramja itt található: [docs/UML.md](docs/UML.md).

## Térkép

```
                    [Vizsgaterem]
                       (zárva)
                          |
[Előadóterem] ------- [Folyosó] ------- [Lépcsőház] --fel-- [Gondnoki iroda]
   (start)                |               (Bodri)
                          |                  | le
[Könyvtár] ----------- [Büfé]             [Pince] -------- [Kazánház]
                                          (sötét)
```

Észak felfelé van. A Lépcsőházból `fel` a Gondnoki irodába, `le` a Pincébe lehet menni.

## Végigjátszás

### Leggyorsabb út (11 lépés)

1. `menj kelet`: Folyosó
2. `menj del`, `menj nyugat`: a Büfén át a Könyvtárba
3. `vedd fel seprű`
4. `menj kelet`, `menj eszak`: vissza a Folyosóra
5. `menj kelet`: Lépcsőház, Bodri elállja az utat (**Harc**)
6. `hasznald seprű`: Bodri elszalad (**Normál**)
7. `menj fel`, `vedd fel lampa`, `hasznald lampa`: Gondnoki iroda, a lámpa már ég
8. `menj le`, `menj le`: Pince; a lámpa ég, ezért nem lesz sötét
9. `vedd fel kulcs`
10. `menj fel`, `menj nyugat`: vissza a Folyosóra
11. `hasznald kulcs`: **NYERTÉL!**

### Minden állapotot bemutató játék

Ez a játék végigmegy az összes állapoton. A játékos puszta kézzel támad Bodrira, megsérül (**Harc, sérülten**),
elmenekül (**Sérült**, a mozgás 2 lépés), elsősegéllyel meggyógyul (**Normál**), majd a seprűvel legyőzi Bodrit.
Lámpa nélkül lemegy a pincébe (**Sötétben**), ahol nem tudja felvenni a kulcsot. Visszamegy a lámpáért, felkapcsolja
(**Normál**), és kinyitja a vizsgatermet.

<details>
<summary>A teljes kimenet megtekintése</summary>

```text
===== KÉSÉSBEN =====
Egy elhagyatott egyetemi épületben ébredsz. 10 perc múlva kezdődik a vizsgád,
de a vizsgaterem kulcsa a pincében van. Siess, 30 lépésed van!
A parancsok listájához írd be: segitseg

--- Előadóterem ---
Egy kihalt előadóterem padjai között ébredsz. A falon lógó óra kegyetlenül ketyeg.
Tárgyak: nincs
Kijáratok: kelet

[Normál | hátralévő lépések: 30] > menj kelet
--- Folyosó ---
Hosszú, poros folyosó. Északra a vizsgaterem ajtaja, keletre a lépcsőház.
Tárgyak: nincs
Kijáratok: eszak del kelet nyugat

[Normál | hátralévő lépések: 29] > menj kelet
--- Lépcsőház ---
Kopott lépcsők vezetnek fel a gondnoki irodába és le a pincébe.
Tárgyak: nincs
Kijáratok: nyugat fel le
Vigyázz! Bodri, a gondnok harapós kutyája morogva elállja az utad!

[Harcban (Bodri) | hátralévő lépések: 28] > nezd
Bodri elállja az utad! Harc közben csak ezek működnek: tamad, menekul, hasznald <targy>

[Harcban (Bodri) | hátralévő lépések: 28] > tamad
Puszta kézzel rátámadsz: Bodri.
Bodri visszatámad és megharap! Megsérültél.

[Harcban (Bodri), sérülten | hátralévő lépések: 28] > menekul
Gyorsan visszamenekülsz oda, ahonnan jöttél.
--- Folyosó ---
Hosszú, poros folyosó. Északra a vizsgaterem ajtaja, keletre a lépcsőház.
Tárgyak: nincs
Kijáratok: eszak del kelet nyugat

[Sérült | hátralévő lépések: 26] > menj del
Sántikálva haladsz, ez 2 lépésbe került.
--- Büfé ---
Üres pultok és egy elfelejtett szendvics. A falon egy elsősegélydoboz lóg.
Tárgyak: elsosegely (elsősegélycsomag)
Kijáratok: eszak nyugat

[Sérült | hátralévő lépések: 24] > vedd fel elsosegely
Felvetted: elsosegely

[Sérült | hátralévő lépések: 24] > menj nyugat
Sántikálva haladsz, ez 2 lépésbe került.
--- Könyvtár ---
Polcok tele régi jegyzetekkel. A sarokban a takarító itt felejtette a holmiját.
Tárgyak: seprű (erős nyelű seprű)
Kijáratok: kelet

[Sérült | hátralévő lépések: 22] > vedd fel seprű
Felvetted: seprű

[Sérült | hátralévő lépések: 22] > hasznald elsosegely
Bekötözöd a sebeidet. Sokkal jobban vagy!

[Normál | hátralévő lépések: 22] > menj kelet
--- Büfé ---
Üres pultok és egy elfelejtett szendvics. A falon egy elsősegélydoboz lóg.
Tárgyak: nincs
Kijáratok: eszak nyugat

[Normál | hátralévő lépések: 21] > menj eszak
--- Folyosó ---
Hosszú, poros folyosó. Északra a vizsgaterem ajtaja, keletre a lépcsőház.
Tárgyak: nincs
Kijáratok: eszak del kelet nyugat

[Normál | hátralévő lépések: 20] > menj kelet
--- Lépcsőház ---
Kopott lépcsők vezetnek fel a gondnoki irodába és le a pincébe.
Tárgyak: nincs
Kijáratok: nyugat fel le
Vigyázz! Bodri, a gondnok harapós kutyája morogva elállja az utad!

[Harcban (Bodri) | hátralévő lépések: 19] > hasznald seprű
Meglengeted a seprűt, és hatalmasat csapsz vele!
Bodri vinnyogva elszalad. Szabad az út!

[Normál | hátralévő lépések: 19] > menj le
--- Pince ---
Koromsötét van. Nem látsz se tárgyakat, se kijáratokat.

[Sötétben | hátralévő lépések: 18] > vedd fel kulcs
Túl sötét van, nem látod, mit vehetnél fel.

[Sötétben | hátralévő lépések: 18] > menj fel
Tapogatózva elindulsz.
--- Lépcsőház ---
Kopott lépcsők vezetnek fel a gondnoki irodába és le a pincébe.
Tárgyak: nincs
Kijáratok: nyugat fel le

[Normál | hátralévő lépések: 17] > menj fel
--- Gondnoki iroda ---
Szűk iroda kulcstartó táblával. A vizsgaterem kulcsa hiányzik, mellette egy cetli: "Pince".
Tárgyak: lampa (zseblámpa)
Kijáratok: le

[Normál | hátralévő lépések: 16] > vedd fel lampa
Felvetted: lampa

[Normál | hátralévő lépések: 16] > menj le
--- Lépcsőház ---
Kopott lépcsők vezetnek fel a gondnoki irodába és le a pincébe.
Tárgyak: nincs
Kijáratok: nyugat fel le

[Normál | hátralévő lépések: 15] > menj le
--- Pince ---
Koromsötét van. Nem látsz se tárgyakat, se kijáratokat.

[Sötétben | hátralévő lépések: 14] > hasznald lampa
Felkapcsoltad a lámpát. Most már a sötétben is látsz.
--- Pince ---
Nyirkos, dohos pince. Valahol csöpög a víz.
Tárgyak: kulcs (a vizsgaterem kulcsa)
Kijáratok: kelet fel

[Normál | hátralévő lépések: 14] > vedd fel kulcs
Felvetted: kulcs

[Normál | hátralévő lépések: 14] > tancolj
Elnézést, ezt a parancsot nem ismerem. A parancsok listájához írd be: segitseg

[Normál | hátralévő lépések: 14] > menj fel
--- Lépcsőház ---
Kopott lépcsők vezetnek fel a gondnoki irodába és le a pincébe.
Tárgyak: nincs
Kijáratok: nyugat fel le

[Normál | hátralévő lépések: 13] > menj nyugat
--- Folyosó ---
Hosszú, poros folyosó. Északra a vizsgaterem ajtaja, keletre a lépcsőház.
Tárgyak: nincs
Kijáratok: eszak del kelet nyugat

[Normál | hátralévő lépések: 12] > menj eszak
Ez az ajtó zárva van. Kulcs kell hozzá!

[Normál | hátralévő lépések: 12] > hasznald kulcs

A kulcs elfordul a zárban, kinyílik az ajtó: Vizsgaterem. Az utolsó pillanatban beesel a vizsgára. NYERTÉL!
```

</details>

### Vereség: újabb seb sérülten

```text
[Harcban (Bodri) | hátralévő lépések: 28] > tamad
Puszta kézzel rátámadsz: Bodri.
Bodri visszatámad és megharap! Megsérültél.

[Harcban (Bodri), sérülten | hátralévő lépések: 28] > tamad
Puszta kézzel rátámadsz: Bodri.

Bodri újra megharap, és összeesel. Nem érsz oda a vizsgára. VESZTETTÉL.
```

## Tesztek

19 JUnit 5 teszt, a `mvn test` paranccsal futtathatók. Mindegyik a valódi pályán, valódi parancsokkal játszik.

| Tesztosztály | Mit ellenőriz |
|---|---|
| `AllapotAtmenetTeszt` (10 teszt) | Állapotátmenetek (Normál → Harc → Harc, sérülten → Sérült → Normál, Normál → Sötétben → Normál), tiltott parancsok (`menj` harc közben, `vedd fel` sötétben), sérülten 2 lépés egy mozgás |
| `JatekVegeTeszt` (5 teszt) | Győzelem kulccsal, zárt ajtó kulcs nélkül, vereség sérülten, vereség 30 lépés után, ismeretlen parancsok |
| `ParancsElemzoTeszt` (4 teszt) | A bevitt sor szétbontása igére és argumentumra, ékezetek és nagybetűk kezelése, ismeretlen parancsok |

## Projekt felépítése

```
src/main/java/org/example/
├── Main.java              belépési pont
├── Jatek.java             a State minta Context-je: helyszín, állapot, táska, lépésszámláló, főciklus
├── JatekosAllapot.java    State interfész
├── NormalAllapot.java     ┐
├── SotetbenAllapot.java   │ konkrét állapotok
├── SerultAllapot.java     │
├── HarcAllapot.java       ┘
├── Parancs.java           egy beírt parancs (ige + argumentum)
├── ParancsElemzo.java     a beírt sor feldolgozása
├── Helyszin.java          helyszín: kijáratok, tárgyak, ellenség
├── Targy.java             tárgy
├── Ellenseg.java          ellenség (Bodri)
└── PalyaEpito.java        a teljes pálya felépítése
src/test/java/org/example/ JUnit tesztek
docs/UML.md                állapotdiagram és osztálydiagram
```
