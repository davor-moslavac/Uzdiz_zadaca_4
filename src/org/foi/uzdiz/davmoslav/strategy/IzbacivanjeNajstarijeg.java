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
public class IzbacivanjeNajstarijeg implements Strategy {

    @Override
    public void execute(List<Resource> dokumenti, ConcreteCache c, long broj, int cacheSize) {

        for (int i = 0; i < broj; i++) {
            izbacuj(c, dokumenti);
        }

    }

    private void izbacuj(ConcreteCache c, List<Resource> dokumenti) {
        Resource najstariji = null;
        for (Resource resource : dokumenti) {
            if (najstariji == null) {
                najstariji = resource;
            } else {
                if (resource.getStvaranje() < najstariji.getStvaranje()) {
                    najstariji = resource;
                }
            }
        }

        if (najstariji != null) {
            System.out.println("Izbacujem " + najstariji.getDokument().getName() + " startegijom NS");
            c.release(najstariji);
        }

    }

}
