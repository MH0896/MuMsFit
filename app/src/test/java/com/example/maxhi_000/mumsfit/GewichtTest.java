package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maxhi_000 on 05.05.2017.
 */
public class GewichtTest {
    @Test
    public void getUebung_id() throws Exception {
        Gewicht g = new Gewicht(87, "15kg");
        assertEquals(87, g.getUebung_id());
    }

    @Test
    public void setUebung_id() throws Exception {
        Gewicht g = new Gewicht(87, "15kg");
        g.setUebung_id(987);
        assertEquals(987, g.getUebung_id());
    }

    @Test
    public void getGewicht_id() throws Exception {
        Gewicht g = new Gewicht(87, "15kg");
        assertEquals(0, g.getGewicht_id());
    }

    @Test
    public void setGewicht_id() throws Exception {
        Gewicht g = new Gewicht(87, "15kg");
        g.setGewicht_id(987);
        assertEquals(987, g.getGewicht_id());
    }

    @Test
    public void getGewicht() throws Exception {
        Gewicht g = new Gewicht(87, "15kg");
        assertEquals("15kg", g.getGewicht());
    }

    @Test
    public void setGewicht() throws Exception {
        Gewicht g = new Gewicht(87, "15kg");
        g.setGewicht("50kg");
        assertEquals("50kg", g.getGewicht());
    }

}