package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maxhi_000 on 05.05.2017.
 */
public class PlanTest {
    @Test
    public void getPlan_id() throws Exception {

    }

    @Test
    public void setPlan_id() throws Exception {

    }

    @Test
    public void getName() throws Exception {
        Plan testPlan = new Plan("toTest");
        assertEquals("toTest", testPlan.getName());
    }

    @Test
    public void setName() throws Exception {
        Plan testPlan = new Plan("toTest");
        testPlan.setName("wurdeGetestet");
        assertEquals("wurdeGetestet", testPlan.getName());
    }

    @Test
    public void getDate_create() throws Exception {

    }

    @Test
    public void setDate_create() throws Exception {

    }

    @Test
    public void getDate_last() throws Exception {

    }

    @Test
    public void setDate_last() throws Exception {

    }

}