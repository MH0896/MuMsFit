package com.example.maxhi_000.mumsfit;

/**
 * Created by maxhi_000 on 05.05.2017.
 */

public class Gewicht {
    private int uebung_id;
    private int gewicht_id;
    private String gewicht;

    public Gewicht(int uebung_id, String gewicht) {
        this.uebung_id = uebung_id;
        this.gewicht_id = 0;
        this.gewicht = gewicht;
    }

    public int getUebung_id() {
        return uebung_id;
    }

    public void setUebung_id(int uebung_id) {
        this.uebung_id = uebung_id;
    }

    public int getGewicht_id() {
        return gewicht_id;
    }

    public void setGewicht_id(int gewicht_id) {
        this.gewicht_id = gewicht_id;
    }

    public String getGewicht() {
        return gewicht;
    }

    public void setGewicht(String gewicht) {
        this.gewicht = gewicht;
    }
}
