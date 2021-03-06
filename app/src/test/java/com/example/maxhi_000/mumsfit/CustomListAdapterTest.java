package com.example.maxhi_000.mumsfit;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomListAdapterTest {
    public Context c;
    public List<String> list = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        for(int i = 0; i<10; i++){
            list.add(i+"");
        }
    }

    @Test
    public void setNewSelection() throws Exception {
        CustomListAdapter cla = new CustomListAdapter(c, 10, 87, list);
        cla.setNewSelection(5, true);
        assertEquals(true, cla.isPositionChecked(5));
    }

    @Test
    public void isPositionChecked() throws Exception {
        CustomListAdapter cla = new CustomListAdapter(c, 10, 87, list);
        assertEquals(false, cla.isPositionChecked(5));
    }

    @Test
    public void getCurrentCheckedPosition() throws Exception {
        CustomListAdapter cla = new CustomListAdapter(c, 10, 87, list);
        cla.setNewSelection(5, true);
        cla.setNewSelection(7, true);
        assertEquals(true, cla.isPositionChecked(5));
    }

    @Test
    public void removeSelection() throws Exception {
        CustomListAdapter cla = new CustomListAdapter(c, 10, 87, list);
        cla.setNewSelection(5, true);
        assertEquals(true, cla.isPositionChecked(5));
        cla.removeSelection(5);
        assertEquals(false, cla.isPositionChecked(5));
    }

    @Test
    public void clearSelection() throws Exception {
        CustomListAdapter cla = new CustomListAdapter(c, 10, 87, list);
        cla.setNewSelection(5, true);
        cla.setNewSelection(7, true);
        assertEquals(true, cla.isPositionChecked(5));
        assertEquals(true, cla.isPositionChecked(7));
        cla.clearSelection();
        assertEquals(false, cla.isPositionChecked(5));
        assertEquals(false, cla.isPositionChecked(7));
    }
}