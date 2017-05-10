package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

public class Uebung  extends AppCompatActivity {
    private int uebung_id;
    private int plan_id;
    private String name;
    private String reps;
    private String start;
    private String split;

    public Uebung(int uebung_id, int plan_id, String name, String reps, String start, String split) {
        this.uebung_id = uebung_id;
        this.plan_id = plan_id;
        this.name = name;
        this.reps = reps;
        this.start = start;
        this.split = split;
    }

    public Uebung(String name, String reps, String start, String split) {
        this.uebung_id = 0;
        this.plan_id = 0;
        this.name = name;
        this.reps = reps;
        this.start = start;
        this.split = split;
    }

    public int getUebung_id() {
        return uebung_id;
    }

    public void setUebung_id(int uebung_id) {
        this.uebung_id = uebung_id;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }


}
