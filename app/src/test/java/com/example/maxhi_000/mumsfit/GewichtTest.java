package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GewichtTest {
    @Test
    public void getUebungID() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        assertEquals(87, g.getUebungID());
    }

    @Test
    public void setUebungID() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        g.setUebungID(987);
        assertEquals(987, g.getUebungID());
    }

    @Test
    public void getGewichtID() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        assertEquals(0, g.getGewichtID());
    }

    @Test
    public void setGewichtID() throws Exception {
        Gewicht g = new Gewicht(87, 15);
        g.setGewichtID(987);
        assertEquals(987, g.getGewichtID());
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