package com.example.maxhi_000.mumsfit;

public class Gewicht {
    private int uebung_id;
    private int gewicht_id;
    private double gewicht;

    public Gewicht(int uebung_id, double gewicht) {
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

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }
}
