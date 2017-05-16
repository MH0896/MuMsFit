package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class PerformTrainPlanActivityTest {
    @Test
    public void alreadyIn() throws Exception {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        assertTrue(PerformTrainPlanActivity.alreadyIn("2", list));
        assertFalse(PerformTrainPlanActivity.alreadyIn("7", list));
    }

    @Test
    public void createSplitUebung() throws Exception {
        int temp = PerformTrainPlanActivity.uebung.size();
        for(int i = 0; i< temp; i++){
            PerformTrainPlanActivity.uebung.remove(0);
        }
        PerformTrainPlanActivity.uebung.add(new Uebung("u1", "5", 7, "split1"));
        PerformTrainPlanActivity.uebung.add(new Uebung("u2", "5", 7, "split1"));
        PerformTrainPlanActivity.uebung.add(new Uebung("u3", "5", 7, "split2"));
        PerformTrainPlanActivity.uebung.add(new Uebung("u4", "5", 7, "split2"));
        PerformTrainPlanActivity.uebung.add(new Uebung("u7", "5", 7, "split2"));
        PerformTrainPlanActivity.uebung.add(new Uebung("u5", "5", 7, "split3"));
        PerformTrainPlanActivity.uebung.add(new Uebung("u6", "5", 7, "split3"));

        PerformTrainPlanActivity.current_split = "split2";
        PerformTrainPlanActivity.createSplitUebung();
        assertTrue(3 == PerformTrainPlanActivity.split_uebung.size());
    }
}