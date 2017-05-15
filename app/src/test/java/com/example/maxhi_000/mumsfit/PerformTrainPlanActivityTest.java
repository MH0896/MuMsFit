package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import static org.junit.Assert.*;


public class PerformTrainPlanActivityTest {

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