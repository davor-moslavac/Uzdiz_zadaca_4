/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.davmoslav.caching.Cache;
import org.foi.uzdiz.davmoslav.caching.ConcreteCache;
import org.foi.uzdiz.davmoslav.caching.Resource;

/**
 *
 * @author Davor
 */
public class Program {

    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
    Argumenti argumenti;
    Cache c;
    Dretva dretva;

    public Program(String[] args) {
        argumenti = new Argumenti(args);
        c = new ConcreteCache(argumenti);
        dretva = new Dretva(argumenti.getIntervalDretve(), c);

    }

    public void start() {
        System.out.println("Pocnite unositi dokumente. Za prestanak unosa dokumenta unesite 'q' i pritisnite enter.");
        unosDokumenta();
    }

    void unosDokumenta() {
        System.out.print("Unesite putanju dokumenta: ");
        String naziv = "";
        try {
            naziv = bufferRead.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!naziv.equals("q")) {
            Resource d = c.acquire(naziv);
            if (d != null) {
                dretva.setZadnjiUnos(naziv);
                System.out.println(d.prikaziBajtove());
            }
            unosDokumenta();
        } else {
            printMenu();
        }
    }

    void printMenu() {
        System.out.println("");
        System.out.println("[u] unos dokumenata");
        System.out.println("[i] ispis");
        System.out.println("[p] praznjenje");
        System.out.println("[d] djelomicno praznjenje");
        System.out.println("[q] izlaz");
        System.out.print("Izbor: ");

        String izbor = "";
        try {
            izbor = bufferRead.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (null != izbor) {
            switch (izbor) {
                case "u":
                    unosDokumenta();
                    break;
                case "i":
                    c.printContent();
                    printMenu();
                    break;
                case "p":
                    c.flushCache();
                    printMenu();
                    break;
                case "d":
                    System.out.println("(Ako je spremnik po br. ele. onda je 1 jednako 1 dokument, a ako je u KB onda je 1 jednako 1 KB. Broj oznacava koliko zelite prazniti.");
                    System.out.print("Broj ");

                    try {
                        c.flushCache(bufferRead.readLine());
                    } catch (IOException ex) {
                        Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    printMenu();
                    break;
                case "q":
                    try {
                        ((ConcreteCache) c).serijalizacija();
                        ((ConcreteCache) c).pisanjeUdnevnikGOtovo();
                        dretva.stopDretva();
                    } catch (Exception ex) {
                        Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                default:
                    System.out.println("Ponovite unos komanda ne valja!");
                    break;
            }
        }
    }

}
