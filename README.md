# SomeNotesFX - egy szövegszerkesztő

### Alkalmazásfejlesztés 1 kötelező program

> Pro tipp: nyisd meg ezt a fájlt a programomban, majd a menüből válaszd az előnézetet, és ott formázva láthatod a tartalmat.

> Útmutató a futtatáshoz a leírás alatt, talán érdemes azzal kezdeni, ha nem boldogulsz.

##### Leírás

A követelmények megtalálhatók [a gyakorlat oldalán](https://okt.sed.hu/alkfejl1/gyakorlat/kotprog/szovegszerkeszto/). Az alkalmazás szövegfájlok szerkesztésére alkalmas, főleg txt és md-re, utóbbit képes formázottan is megjeleníteni előnézetben.  
A kezdőképernyőn lévő TextAreába tudunk jegyzetelni, fölötte a menüsor ezeket a funkciókat rejti:  

- **Megnyitás**:
    - **Fájl megnyitása**: lehetőséget nyújt egy jegyzet megnyitására. Extension filtert állítsd, ha nem csak a támogatott formátumokat akarod látni. Olyanokat megnyitva warningot kapsz, ahogy kell.
    - **Legutóbbiak**: az utolsó 10 db megnyitott/mentett fájl listája, kattintásra megnyithatók, az Eltávolítás gomb eltávolítja őket a listáról.
- **Fájl**:
    - **Létrehozás**: elveti a jelen szerkesztett jegyzet változtatásait, újat kezd, ami még nincs mentve.
    - **Előnézet**: elmenti a fájlt és megjeleníti, md-t formázva, egy külön ablakban. A markdown feldolgozását külön szálon végzi.
- **Mentés**:
    - **Mentés**: elmenti a fájlt, új fájl esetén ugyanaz, mint a Mentés másként
    - **Mentés másként**: egy custom ablakban bekéri a nevet (kiterjesztéssel együtt) és ki kell választani a mentés helyét, ha mindkettő megvan, jobb oldali gombbal lehet menteni.
    - **Exportálás PDF-be**: külön szálon feldolgozza és kiexportálja a jegyzetet pdf formátumba. A folyamatról a címsoron tájékoztat, ha kész, nyom egy dialogot. *(Ez ugyebár opcionális követelmény volt)*

### Futtatás

Az alkalmazást a gyakorlaton tanult módon, a következő paranccsal futtattam:  
`mvn clean compiler:compile resources:resources javafx:run`  
Jar fájl létrehozásához elég a következő parancs:  
`mvn clean package`  

### Hibaelhárítás

Néhány Linux alapú rendszeren jelentkezik [egy probléma](https://stackoverflow.com/questions/53372200/missing-titlebar-on-javafx-app-with-openjfx) a JavaFX programokkal, ennek javítására egy parancssori paramétert kell megadni futtatáskor.  
Hogy ezt ne kelljen megadnunk állandóan, a program a belépési pontján (*Entry.java*-ban található *main* függvény) beállítja a kellő propertyt.  
Ez elvileg nem okoz gondot más rendszereken, én teszteltem Windows alatt is, de ha mégis bármilyen anomáliát tapasztalnál, érdemes megpróbálni kikommentezni. Tippemre bizonyos *más* Linux alapú rendszereken nem feltétlen előnyös.

A feljebb írt dologgal függ össze, hogy a program induláskor kiírhat pár warningot a konzolra, amik a piros színük miatt ijesztőek is lehetnek. Ezek a gtk-val kapcsolatosak, tehát nem a programom hatásköre lekezelni őket.
 
