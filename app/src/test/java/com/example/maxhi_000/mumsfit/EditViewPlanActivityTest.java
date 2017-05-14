package com.example.maxhi_000.mumsfit;

import android.content.Context;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EditViewPlanActivityTest {

    @Test
    public void createOrderOfUebungUnordered() throws Exception {
        int temp = EditPlanActivity.uebung.size();
        for(int i = 0; i< temp; i++){
            EditPlanActivity.uebung.remove(0);
        }
        EditPlanActivity.uebung.add(new Uebung("u1", "5", "7kg", "split1"));
        EditPlanActivity.uebung.add(new Uebung("u2", "5", "7kg", "split1"));
        EditPlanActivity.uebung.add(new Uebung("u3", "5", "7kg", "split2"));
        EditPlanActivity.uebung.add(new Uebung("u4", "5", "7kg", "split2"));
        EditPlanActivity.uebung.add(new Uebung("u7", "5", "7kg", "split2"));
        EditPlanActivity.uebung.add(new Uebung("u5", "5", "7kg", "split1"));
        EditPlanActivity.uebung.add(new Uebung("u6", "5", "7kg", "split2"));
        EditPlanActivity.createOrderOfUebung();

        assertEquals("u2", EditPlanActivity.uebung.get(2).getName());

        ViewPlanActivity.uebung.add(new Uebung("u1", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u2", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u3", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u4", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u7", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u5", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u6", "5", "7kg", "split2"));
        ViewPlanActivity.createOrderOfUebung();

        assertEquals("u2", ViewPlanActivity.uebung.get(2).getName());
    }

    @Test
    public void createOrderOfUebungOrdered() throws Exception {
        int temp = EditPlanActivity.uebung.size();
        for(int i = 0; i< temp; i++){
            EditPlanActivity.uebung.remove(0);
        }
        EditPlanActivity.uebung.add(new Uebung("u1", "5", "7kg", "split1"));
        EditPlanActivity.uebung.add(new Uebung("u2", "5", "7kg", "split1"));
        EditPlanActivity.uebung.add(new Uebung("u3", "5", "7kg", "split2"));
        EditPlanActivity.uebung.add(new Uebung("u4", "5", "7kg", "split2"));
        EditPlanActivity.uebung.add(new Uebung("u7", "5", "7kg", "split2"));
        EditPlanActivity.uebung.add(new Uebung("u5", "5", "7kg", "split3"));
        EditPlanActivity.uebung.add(new Uebung("u6", "5", "7kg", "split3"));
        EditPlanActivity.createOrderOfUebung();

        assertEquals("u3", EditPlanActivity.uebung.get(2).getName());

        temp = ViewPlanActivity.uebung.size();
        for(int i = 0; i< temp; i++){
            ViewPlanActivity.uebung.remove(0);
        }
        ViewPlanActivity.uebung.add(new Uebung("u1", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u2", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u3", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u4", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u7", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u5", "5", "7kg", "split3"));
        ViewPlanActivity.uebung.add(new Uebung("u6", "5", "7kg", "split3"));
        ViewPlanActivity.createOrderOfUebung();

        assertEquals("u3", ViewPlanActivity.uebung.get(2).getName());
    }
}