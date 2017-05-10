package com.example.maxhi_000.mumsfit;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;


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