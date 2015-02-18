/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.davmoslav.Argumenti;
import org.foi.uzdiz.davmoslav.strategy.IzbacivanjeNajkoristenijeg;
import org.foi.uzdiz.davmoslav.strategy.IzbacivanjeNajkoristenijegKB;
import org.foi.uzdiz.davmoslav.strategy.IzbacivanjeNajstarijeg;
import org.foi.uzdiz.davmoslav.strategy.IzbacivanjeNajstarijegKB;
import org.foi.uzdiz.davmoslav.strategy.Strategy;

/**
 *
 * @author Davor
 */
public class ConcreteCache implements Cache {

    ArrayList<Resource> dokumentiUCacheu = new ArrayList();
    Argumenti argumenti;
    Strategy s;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    FileWriter out;

    public ConcreteCache(Argumenti argumenti) {
        this.argumenti = argumenti;
        init();
    }

    @Override
    public void release(Resource resource) {
        if (resource.isEvictable()) {
            try {
                resource.beforeEviction();
                dokumentiUCacheu.remove(resource);
                String ss = "NIKAD";
                if (resource.zadnjiPristup != 0) {
                    ss = sdf.format(new Date(resource.zadnjiPristup));
                }

                out.write(resource.getDokument().getName() + " izbacen " + " - " + sdf.format(new Date()) + " uzet " + resource.brojacPristupa + " zadnji put " + ss + "\n");
            } catch (IOException ex) {
                Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void flushCache() {
        while (!dokumentiUCacheu.isEmpty()) {
            for (Resource resource : dokumentiUCacheu) {
                try {
                    resource.beforeEviction();
                    String ss = "NIKAD";
                    if (resource.zadnjiPristup != 0) {
                        ss = sdf.format(new Date(resource.zadnjiPristup));
                    }

                    out.write(resource.getDokument().getName() + " izbacen " + " - " + sdf.format(new Date()) + " uzet " + resource.brojacPristupa + " zadnji put " + ss + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            dokumentiUCacheu.clear();
        }
    }

    @Override
    public Resource acquire(String naziv) {
        File provjeraIliUnos = new File(naziv);
        if (!provjeraIliUnos.exists() || provjeraIliUnos.isDirectory()) {
            System.out.println("Dokument ne postoji ili nije datoteka!");
            return null;
        }
        if (!isInCache(provjeraIliUnos)) {
            return pospremiUCache(provjeraIliUnos);
        } else {
            return uzmiIzCachea(provjeraIliUnos);
        }
    }

    public void serijalizacija() throws Exception {
        FileOutputStream out = new FileOutputStream("serijaliziraniSpremnik.dat");
        ObjectOutputStream s = new ObjectOutputStream(out);
        s.writeObject(dokumentiUCacheu);
        s.close();
        out.close();

    }

    void deserijalizacija() {
        FileInputStream in = null;
        try {
            in = new FileInputStream("serijaliziraniSpremnik.dat");
            ObjectInputStream s = new ObjectInputStream(in);
            dokumentiUCacheu = (ArrayList<Resource>) s.readObject();
            s.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException | ClassNotFoundException ex) {
        } finally {
            try {
                if (in != null) {

                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    boolean isInCache(File fileZaProvjeru) {
        for (Resource resource : dokumentiUCacheu) {
            if (resource.getDokument().getName() == null ? fileZaProvjeru.getName() == null : resource.getDokument().getName().equals(fileZaProvjeru.getName())) {
                return true;
            }
        }
        return false;
    }

    Resource pospremiUCache(File f) {
        try {
            if (argumenti.isKB()) {
                if (f.length() > argumenti.getKapacitet() * 1024) {
                    System.out.println("Ne stane ovoliko veliki dokument u spremiste!!");
                    return null;
                }
            }

            removeTilFits(f.length());
            File kopija = new File(argumenti.getSpremiste() + "/" + f.getName());
            Files.copy(f.toPath(), kopija.toPath());
            Resource dok = new Resource(new Date().getTime(), 0, 0, kopija);
            dokumentiUCacheu.add(dok);
            out.write(kopija.getName() + " dodan" + " - " + sdf.format(new Date()) + "\n");
            return dok;
        } catch (IOException ex) {
            Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    Resource uzmiIzCachea(File fileZaProvjeru) {
        for (Resource resource : dokumentiUCacheu) {
            if (resource.getDokument().getName() == null ? fileZaProvjeru.getName() == null : resource.getDokument().getName().equals(fileZaProvjeru.getName())) {
                resource.registrirajUzimanje();
                return resource;
            }
        }
        return null;
    }

    private void init() {
        try {
            out = new FileWriter(argumenti.getDnevnik(), true);
        } catch (IOException ex) {
            Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        if ("NK".equals(argumenti.getStrategijaIzbacivanja()) && argumenti.isKB()) {
            s = new IzbacivanjeNajkoristenijegKB();
        } else if ("NK".equals(argumenti.getStrategijaIzbacivanja()) && !argumenti.isKB()) {
            s = new IzbacivanjeNajkoristenijeg();
        } else if ("NS".equals(argumenti.getStrategijaIzbacivanja()) && argumenti.isKB()) {
            s = new IzbacivanjeNajstarijegKB();
        } else if ("NS".equals(argumenti.getStrategijaIzbacivanja()) && !argumenti.isKB()) {
            s = new IzbacivanjeNajstarijeg();
        }

        try {
            deserijalizacija();
        } catch (Exception ex) {
            Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (argumenti.isClean()) {
            flushCache();
        }

    }

    void removeTilFits(long broj) {

        if (provjeri(broj)) {
            return;
        }

        if (!argumenti.isKB()) {
            broj = 1;
        }

        s.execute(
                dokumentiUCacheu,
                this,
                broj,
                argumenti.getKapacitet()
        );
    }

    @Override
    public void printContent() {
        for (Resource resource : dokumentiUCacheu) {
            String s = "Dokument '" + resource.getDokument().getName() + "' je dodan " + new Date(resource.getStvaranje()) + ", a koristen " + resource.brojacPristupa + " puta i zadnji puta ";
            if (resource.getZadnjiPristup() == 0) {
                s += "jos nije koristen";
            } else {
                s += new Date(resource.getZadnjiPristup());
            }
            System.out.println(s);
        }
    }

    @Override
    public void flushCache(String readLine) {
        try {
            Integer.valueOf(readLine);
        } catch (NumberFormatException e) {
            System.out.println("Nije broj!");
            return;
        }
        int broj = Integer.valueOf(readLine);
        if (argumenti.isKB()) {
            broj = broj * 1024;
        }
        s.execute(
                dokumentiUCacheu,
                this,
                broj,
                argumenti.getKapacitet()
        );
    }

    public long velicina() {
        if (argumenti.isKB()) {
            long ukupno = 0;
            for (Resource resource : dokumentiUCacheu) {
                ukupno += resource.getDokument().length();
            }
            return ukupno;
        } else {
            return dokumentiUCacheu.size();
        }
    }

    public void pisanjeUdnevnikGOtovo() {
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ConcreteCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean provjeri(long broj) {
        if (!argumenti.isKB()) {
            if (dokumentiUCacheu.size() + 1 > argumenti.getKapacitet()) {
                return false;
            }
        } else {
            if ((velicina() + broj) > argumenti.getKapacitet() * 1024) {
                return false;
            }
        }

        return true;
    }
}
