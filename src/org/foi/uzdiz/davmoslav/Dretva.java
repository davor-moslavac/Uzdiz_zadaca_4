/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.davmoslav.caching.Cache;
import org.foi.uzdiz.davmoslav.caching.Resource;

/**
 *
 * @author Davor
 */
public class Dretva extends Thread {

    int interval;
    Cache c;
    String zadnjiUnos = "";
    boolean flag = true;
    public Dretva(int interval, Cache c) {
        this.interval = interval;
        this.c = c;
        start();
    }

    @Override
    public void run() {
        while (flag) {
            long pocetak = System.currentTimeMillis();
            if (!"".equals(zadnjiUnos)) {
                Resource d = c.acquire(zadnjiUnos);
                if (d != null) {
                    System.out.println(d.prikaziBajtove());
                }
            }
            long trajanje = System.currentTimeMillis() - pocetak;
            try {
                Thread.sleep((interval * 1000) - trajanje);
            } catch (InterruptedException ex) {
                Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void setZadnjiUnos(String zadnjiUnos) {
        this.zadnjiUnos = zadnjiUnos;
    }
    
    public void stopDretva() {
        flag = false;
    }

}
