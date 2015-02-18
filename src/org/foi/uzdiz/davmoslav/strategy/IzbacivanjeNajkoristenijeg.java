/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav.strategy;

import org.foi.uzdiz.davmoslav.caching.Resource;
import java.util.List;
import org.foi.uzdiz.davmoslav.caching.ConcreteCache;

/**
 *
 * @author Davor
 */
public class IzbacivanjeNajkoristenijeg implements Strategy {

    @Override
    public void execute(List<Resource> dokumenti, ConcreteCache c, long broj, int cacheSize) {
        for (int i = 0; i < broj; i++) {
            izbacuj(c, dokumenti);
        }
    }

    private void izbacuj(ConcreteCache c, List<Resource> dokumenti) {
        Resource najkoristeniji = null;
        for (Resource resource : dokumenti) {
            if (najkoristeniji == null) {
                najkoristeniji = resource;
            } else {
                if (resource.getBrojacPristupa() > najkoristeniji.getBrojacPristupa()) {
                    najkoristeniji = resource;
                }
            }

        }

        if (najkoristeniji != null) {
            System.out.println("Izbacujem " + najkoristeniji.getDokument().getName() + " startegijom NK");
            c.release(najkoristeniji);
        }

    }

}
