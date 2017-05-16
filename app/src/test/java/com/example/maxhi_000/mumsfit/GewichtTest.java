package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.*;

public class GewichtTest {
    @Test
    public void getUebung_id() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        assertEquals(87, g.getUebung_id());
    }

    @Test
    public void setUebung_id() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        g.setUebung_id(987);
        assertEquals(987, g.getUebung_id());
    }

    @Test
    public void getGewicht_id() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        assertEquals(0, g.getGewicht_id());
    }

    @Test
    public void setGewicht_id() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        g.setGewicht_id(987);
        assertEquals(987, g.getGewicht_id());
    }

    @Test
    public void getGewicht() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        assertTrue(15 == g.getGewicht());
    }

    @Test
    public void setGewicht() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        g.setGewicht(50);
        assertTrue(50 == g.getGewicht());
    }

}