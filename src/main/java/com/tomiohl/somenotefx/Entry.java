package com.tomiohl.somenotefx;

// Az alkalmazás belépési pontja, főleg a jar miatt kellett.
public class Entry {
    public static void main(String[] args) {
        /* A következő sor megoldja a javafx és a GTK kölcsönös problémáit.
        Ebből adódóan nem minden platformon érvényes, ha hibát tapasztalsz, próbáld kikommentezni.*/
        System.setProperty("jdk.gtk.version","2");
        // Ez lejjebb pedig az alkalmazás indítása, nehogy kiszedd.
        App.main(args);
    }
}
