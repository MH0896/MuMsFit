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

    public int getUebungID() {
        return uebung_id;
    }

    public void setUebungID(int uebung_id) {
        this.uebung_id = uebung_id;
    }

    public int getGewichtID() {
        return gewicht_id;
    }

    public void setGewichtID(int gewicht_id) {
        this.gewicht_id = gewicht_id;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }
}
