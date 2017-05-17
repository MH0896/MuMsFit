package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DBTest {
    public Context c;

    @Test
    public void getOpened() throws Exception {
        assertEquals(false, new TrainPlanDataSource(c).isOpened());
    }

    @Test
    public void getClosed() throws Exception {
        assertEquals(false, new TrainPlanDataSource(c).isClosed());
    }

    @Test
    public void opens() throws Exception {
        TrainPlanDataSource ds = new TrainPlanDataSource(c);
        ds.open();
        assertEquals(false, new TrainPlanDataSource(c).isOpened());
    }

    @Test
    public void closes() throws Exception {
        TrainPlanDataSource ds = new TrainPlanDataSource(c);
        ds.open();
        ds.close();
        assertEquals(false, new TrainPlanDataSource(c).isClosed());
    }

    @Test
    public void create() throws Exception {
        TrainPlanDbHelper help = new TrainPlanDbHelper(c);
        help.onCreate(help.getWritableDatabase());
        assertEquals(false, help.isCreated());
    }

    @Test
    public void getCreated() throws Exception {
        assertEquals(false, new TrainPlanDbHelper(c).isCreated());
    }

    @Test
    public void update() throws Exception {
        TrainPlanDbHelper help = new TrainPlanDbHelper(c);
        SQLiteDatabase db = help.getWritableDatabase();
        help.onUpgrade(db, 1, 5);
        assertEquals(true, help.isCreated());
    }
}
