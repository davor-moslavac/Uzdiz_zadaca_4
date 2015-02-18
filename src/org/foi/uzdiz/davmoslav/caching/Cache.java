/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav.caching;

/**
 *
 * @author Davor
 */
public interface Cache {

    public void release(Resource resource);

    public Resource acquire(String naziv);

    public void printContent();

    public void flushCache();

    public void flushCache(String readLine);
}
