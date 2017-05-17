package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UebungTest {
    @Test
    public void testToString() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertEquals("toTest", testU.toString());
    }

    @Test
    public void getUebungID() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertEquals(0, testU.getUebungID());
    }

    @Test
    public void setUebungID() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        testU.setUebungID(65);
        assertEquals(65, testU.getUebungID());
    }

    @Test
    public void getPlanID() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertEquals(0, testU.getPlanID());
    }

    @Test
    public void setPlanID() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        testU.setPlanID(65);
        assertEquals(65, testU.getPlanID());
    }

    @Test
    public void getName() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertEquals("toTest", testU.getName());
    }

    @Test
    public void setName() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        testU.setName("Übung");
        assertEquals("Übung", testU.getName());
    }

    @Test
    public void getReps() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertEquals("5mal", testU.getReps());
    }

    @Test
    public void setReps() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        testU.setReps("12mal");
        assertEquals("12mal", testU.getReps());
    }

    @Test
    public void getStart() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertTrue(7 == testU.getStart());
    }

    @Test
    public void setStart() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        testU.setStart(99);
        assertTrue(99 == testU.getStart());
    }

    @Test
    public void getSplit() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        assertEquals("Beine", testU.getSplit());
    }

    @Test
    public void setSplit() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", 7, "Beine");
        testU.setSplit("Arme");
        assertEquals("Arme", testU.getSplit());
    }

    @Test
    public void newUebung() throws Exception {
        Uebung u = new Uebung(7, 8, "name", "reps", 7, "split");
        assertEquals("reps", u.getReps());
    }

}