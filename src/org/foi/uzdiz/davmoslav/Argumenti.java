/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.davmoslav;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Davor
 */
public class Argumenti {

    private int podaciZaIspis;
    private int intervalDretve;
    private String spremiste;
    private int kapacitet;
    private boolean KB = false;
    private String strategijaIzbacivanja;
    private String dnevnik;
    private boolean clean = false;
    private static final String regexPattern = "([1-9]\\d*);([1-9]\\d*);(([A-Z|a-z]:\\\\[^*|\"<>;?\\n]*)|(\\\\\\\\.*?\\\\.*));([1-9]\\d*);(KB;)?(NS|NK);([A-Za-z\\s0-9]+\\.txt);(clean;)?";

    public Argumenti(String[] args) {
        build(args);
    }

    public int getPodaciZaIspis() {
        return podaciZaIspis;
    }

    public void setPodaciZaIspis(int podaciZaIspis) {
        this.podaciZaIspis = podaciZaIspis;
    }

    public int getIntervalDretve() {
        return intervalDretve;
    }

    public void setIntervalDretve(int intervalDretve) {
        this.intervalDretve = intervalDretve;
    }

    public String getSpremiste() {
        return spremiste;
    }

    public void setSpremiste(String spremiste) {
        this.spremiste = spremiste;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(int kapacitet) {
        this.kapacitet = kapacitet;
    }

    public boolean isKB() {
        return KB;
    }

    public void setKB(boolean KB) {
        this.KB = KB;
    }

    public String getStrategijaIzbacivanja() {
        return strategijaIzbacivanja;
    }

    public void setStrategijaIzbacivanja(String strategijaIzbacivanja) {
        this.strategijaIzbacivanja = strategijaIzbacivanja;
    }

    public String getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(String dnevnik) {
        this.dnevnik = dnevnik;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    private void build(String[] args) {
        StringBuilder sb = new StringBuilder("");
        for (String string : args) {
            sb.append(string);
            sb.append(";");
        }

        regex(sb.toString());
    }

    private void regex(String string) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            System.out.println("Argumenti nisu po pravilima");
            return;
        }
        
        setPodaciZaIspis(Integer.valueOf(matcher.group(1)));
        setIntervalDretve(Integer.valueOf(matcher.group(2)));
        setSpremiste(matcher.group(3));
        setKapacitet(Integer.valueOf(matcher.group(6)));
        if (matcher.group(7) != null) {
            setKB(true);
        }
        setStrategijaIzbacivanja(matcher.group(8));
        setDnevnik(matcher.group(9));
        if (matcher.group(10) != null) {
            setClean(true);
        }
    }

    @Override
    public String toString() {
        return "Argumenti{" + "podaciZaIspis=" + podaciZaIspis + ", intervalDretve=" + intervalDretve + ", spremiste=" + spremiste + ", kapacitet=" + kapacitet + ", KB=" + KB + ", strategijaIzbacivanja=" + strategijaIzbacivanja + ", dnevnik=" + dnevnik + ", clean=" + clean + '}';
    }

}
