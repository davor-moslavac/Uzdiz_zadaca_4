/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davor
 */
public class Resource implements Serializable, EvictionInterface {

    long stvaranje;
    long zadnjiPristup;
    long brojacPristupa = -1;
    File dokument;

    public Resource() {
    }

    public Resource(long stvaranje, long zadnjiPristup, long brojacPristupa, File dokument) {
        this.stvaranje = stvaranje;
        this.zadnjiPristup = zadnjiPristup;
        this.brojacPristupa = brojacPristupa;
        this.dokument = dokument;
    }

    public long getStvaranje() {
        return stvaranje;
    }

    public void setStvaranje(long stvaranje) {
        this.stvaranje = stvaranje;
    }

    public long getZadnjiPristup() {
        return zadnjiPristup;
    }

    public void setZadnjiPristup(long zadnjiPristup) {
        this.zadnjiPristup = zadnjiPristup;
    }

    public long getBrojacPristupa() {
        return brojacPristupa;
    }

    public void setBrojacPristupa(long brojacPristupa) {
        this.brojacPristupa = brojacPristupa;
    }

    public File getDokument() {
        return dokument;
    }

    public void setDokument(File dokument) {
        this.dokument = dokument;
    }

    @Override
    public boolean isEvictable() {
        return true;
    }

    @Override
    public Object info() {
        return new Object[]{brojacPristupa, stvaranje};
    }

    @Override
    public void beforeEviction() {
        izbrisiStvarniDokument();
    }

    private void izbrisiStvarniDokument() {
        dokument.delete();
    }

    public String prikaziBajtove() {
        InputStream is = null;
        try {
            byte[] bytes = new byte[4];
            is = new FileInputStream(dokument.getAbsolutePath());
            is.read(bytes);
            is.close();

            String bs = "";
            for (byte b : bytes) {
                bs += b + " ";
            }
            return bs;
        } catch (Exception ex) {
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "Nemogu prikazat!";
    }

    void registrirajUzimanje() {
        brojacPristupa++;
        zadnjiPristup = new Date().getTime();
    }
}
