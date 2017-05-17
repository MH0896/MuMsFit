package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class MainActivityTest {
    @Test
    public void checkEingabe() throws Exception {
        String temp = "    ";
        assertNull(MainActivity.checkEingabe(temp));

        temp = "Horst";
        assertEquals("Horst", MainActivity.checkEingabe(temp));

        temp = "Horst    ";
        assertEquals("Horst", MainActivity.checkEingabe(temp));

        temp = "    Horst";
        assertEquals("Horst", MainActivity.checkEingabe(temp));
    }
}