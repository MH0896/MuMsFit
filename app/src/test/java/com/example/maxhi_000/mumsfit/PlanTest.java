package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class PlanTest {
    @Test
    public void getPlanID() throws Exception {
        Plan testPlan = new Plan("toTest");
        assertEquals(0, testPlan.getPlanID());
    }

    @Test
    public void setPlanID() throws Exception {
        Plan testPlan = new Plan("toTest");
        testPlan.setPlanID(87);
        assertEquals(87, testPlan.getPlanID());
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
    public void getDateCreate() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", date, "-");
        assertEquals(date, testPlan.getDateCreate());
    }

    @Test
    public void setDateCreate() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", date, "-");
        testPlan.setDateCreate("newDate");
        assertEquals("newDate", testPlan.getDateCreate());
    }

    @Test
    public void getDateLast() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", "today", date);
        assertEquals(date, testPlan.getDateLast());
    }

    @Test
    public void setDateLast() throws Exception {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Plan testPlan = new Plan("toTest", "today", date);
        testPlan.setDateLast("newDate");
        assertEquals("newDate", testPlan.getDateLast());
    }

    @Test
    public void newPlan() throws Exception {
        Plan testPlan = new Plan(87, "toTest", "today", "date");
        assertEquals("date", testPlan.getDateLast());
    }

}