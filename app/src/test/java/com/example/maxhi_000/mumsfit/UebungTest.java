package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.*;

public class UebungTest {
    @Test
    public void getUebung_id() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        assertEquals(0, testU.getUebung_id());
    }

    @Test
    public void setUebung_id() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        testU.setUebung_id(65);
        assertEquals(65, testU.getUebung_id());
    }

    @Test
    public void getPlan_id() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        assertEquals(0, testU.getPlan_id());
    }

    @Test
    public void setPlan_id() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        testU.setPlan_id(65);
        assertEquals(65, testU.getPlan_id());
    }

    @Test
    public void getName() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        assertEquals("toTest", testU.getName());
    }

    @Test
    public void setName() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        testU.setName("Übung");
        assertEquals("Übung", testU.getName());
    }

    @Test
    public void getReps() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        assertEquals("5mal", testU.getReps());
    }

    @Test
    public void setReps() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        testU.setReps("12mal");
        assertEquals("12mal", testU.getReps());
    }

    @Test
    public void getStart() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        assertEquals("7kg", testU.getStart());
    }

    @Test
    public void setStart() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        testU.setStart("90kg");
        assertEquals("90kg", testU.getStart());
    }

    @Test
    public void getSplit() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        assertEquals("Beine", testU.getSplit());
    }

    @Test
    public void setSplit() throws Exception {
        Uebung testU = new Uebung("toTest", "5mal", "7kg", "Beine");
        testU.setSplit("Arme");
        assertEquals("Arme", testU.getSplit());
    }

    @Test
    public void newUebung() throws Exception {
        Uebung u = new Uebung(7, 8, "name", "reps", "start", "split");
        assertEquals("reps", u.getReps());
    }

}