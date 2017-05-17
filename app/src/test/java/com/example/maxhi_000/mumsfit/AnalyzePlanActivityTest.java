package com.example.maxhi_000.mumsfit;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnalyzePlanActivityTest {
    @Test
    public void alreadyIn() throws Exception {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        assertTrue(AnalyzePlanActivity.alreadyIn("2", list));
        assertFalse(AnalyzePlanActivity.alreadyIn("7", list));
    }

    @Test
    public void createSplitUebung() throws Exception {
        int temp = AnalyzePlanActivity.uebung.size();
        for(int i = 0; i< temp; i++){
            AnalyzePlanActivity.uebung.remove(0);
        }
        AnalyzePlanActivity.uebung.add(new Uebung("u1", "5", 7, "split1"));
        AnalyzePlanActivity.uebung.add(new Uebung("u2", "5", 7, "split1"));
        AnalyzePlanActivity.uebung.add(new Uebung("u3", "5", 7, "split2"));
        AnalyzePlanActivity.uebung.add(new Uebung("u4", "5", 7, "split2"));
        AnalyzePlanActivity.uebung.add(new Uebung("u7", "5", 7, "split2"));
        AnalyzePlanActivity.uebung.add(new Uebung("u5", "5", 7, "split3"));
        AnalyzePlanActivity.uebung.add(new Uebung("u6", "5", 7, "split3"));

        AnalyzePlanActivity.current_split = "split2";
        AnalyzePlanActivity.createSplitUebung();
        assertTrue(3 == AnalyzePlanActivity.split_uebung.size());
    }
}