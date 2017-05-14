package com.example.maxhi_000.mumsfit;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemoveTest {

    @Test
    public void RemoveEPA() throws Exception {
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

        assertEquals(7, EditPlanActivity.uebung.size());

        EditPlanActivity.removeUebungen();
        assertEquals(0, EditPlanActivity.uebung.size());
    }

    @Test
    public void RemoveVPA() throws Exception {
        int temp = ViewPlanActivity.uebung.size();
        for(int i = 0; i< temp; i++){
            ViewPlanActivity.uebung.remove(0);
        }
        ViewPlanActivity.uebung.add(new Uebung("u1", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u2", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u3", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u4", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u7", "5", "7kg", "split2"));
        ViewPlanActivity.uebung.add(new Uebung("u5", "5", "7kg", "split1"));
        ViewPlanActivity.uebung.add(new Uebung("u6", "5", "7kg", "split2"));

        assertEquals(7, ViewPlanActivity.uebung.size());

        ViewPlanActivity.removeUebungen();
        assertEquals(0, ViewPlanActivity.uebung.size());
    }

    @Test
    public void RemoveCPA() throws Exception {
        int temp = CreatePlanActivity.uebungen.size();
        for(int i = 0; i< temp; i++){
            CreatePlanActivity.uebungen.remove(0);
        }
        CreatePlanActivity.uebungen.add(new Uebung("u1", "5", "7kg", "split1"));
        CreatePlanActivity.uebungen.add(new Uebung("u2", "5", "7kg", "split1"));
        CreatePlanActivity.uebungen.add(new Uebung("u3", "5", "7kg", "split2"));
        CreatePlanActivity.uebungen.add(new Uebung("u4", "5", "7kg", "split2"));
        CreatePlanActivity.uebungen.add(new Uebung("u7", "5", "7kg", "split2"));
        CreatePlanActivity.uebungen.add(new Uebung("u5", "5", "7kg", "split1"));
        CreatePlanActivity.uebungen.add(new Uebung("u6", "5", "7kg", "split2"));

        CreatePlanActivity.splits.add("split1");
        CreatePlanActivity.splits.add("split2");

        assertEquals(7, CreatePlanActivity.uebungen.size());
        assertEquals(2, CreatePlanActivity.splits.size());

        CreatePlanActivity.removeUebungen();
        assertEquals(0, CreatePlanActivity.uebungen.size());
        assertEquals(0, CreatePlanActivity.splits.size());
    }
}
