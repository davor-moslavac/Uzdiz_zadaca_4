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
public interface Strategy {

    public void execute(List<Resource> dokumenti, ConcreteCache c, long broj, int cacheSize);
}
