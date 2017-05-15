package com.example.maxhi_000.mumsfit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CreatePlanActivity.class)
public class CreatePlanActivityTest {
    @Test
    public void checkEingabe() throws Exception {
        String temp = "    ";
        assertNull(CreatePlanActivity.checkEingabe(temp));

        temp = "Horst";
        assertEquals("Horst", CreatePlanActivity.checkEingabe(temp));

        temp = "Horst    ";
        assertEquals("Horst", CreatePlanActivity.checkEingabe(temp));

        temp = "    Horst";
        assertEquals("Horst", CreatePlanActivity.checkEingabe(temp));
    }

    @Test
    public void insertUebung() throws Exception {
        Uebung u = new Uebung("toTest", "5mal", 7, "Beine");
        CreatePlanActivity mock = PowerMockito.mock(CreatePlanActivity.class);
        PowerMockito.whenNew(CreatePlanActivity.class).withNoArguments().thenReturn(mock);
        mock.insertUebung(u, "987");

        String reps = mock.selectReps("toTest");
        assertNull(reps);
    }

    @Test
    public void insertPlan() throws Exception {
        Plan plan = new Plan("toTest");

        CreatePlanActivity mock = PowerMockito.mock(CreatePlanActivity.class);
        PowerMockito.whenNew(CreatePlanActivity.class).withNoArguments().thenReturn(mock);
        mock.insertPlan(plan);

        String date = mock.selectDate("toTest");
        assertNull(date);

    }
}