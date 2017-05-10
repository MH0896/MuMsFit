package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class PlanTest {
    @Test
    public void getPlan_id() throws Exception {
        Plan testPlan = new Plan("toTest");
        assertEquals(0, testPlan.getPlan_id());
    }

    @Test
    public void setPlan_id() throws Exception {
        Plan testPlan = new Plan("toTest");
        testPlan.setPlan_id(87);
        assertEquals(87, testPlan.getPlan_id());
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
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", date, "-");
        assertEquals(date, testPlan.getDate_create());
    }

    @Test
    public void setDate_create() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", date, "-");
        testPlan.setDate_create("newDate");
        assertEquals("newDate", testPlan.getDate_create());
    }

    @Test
    public void getDate_last() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", "today", date);
        assertEquals(date, testPlan.getDate_last());
    }

    @Test
    public void setDate_last() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", "today", date);
        testPlan.setDate_last("newDate");
        assertEquals("newDate", testPlan.getDate_last());
    }

    @Test
    public void newPlan() throws Exception {
        Plan testPlan = new Plan(87, "toTest", "today", "date");
        assertEquals("date", testPlan.getDate_last());
    }

}