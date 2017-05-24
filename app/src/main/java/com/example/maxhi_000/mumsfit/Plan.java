package com.example.maxhi_000.mumsfit;

import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Plan  extends AppCompatActivity {
    private int plan_id;
    private String name;
    private String date_create;
    private String date_last;

    public Plan(int plan_id, String name, String date_create, String date_last) {
        this.plan_id = plan_id;
        this.name = name;
        this.date_create = date_create;
        this.date_last = date_last;
    }

    public Plan(String name) {
        this.plan_id = 0;
        this.name = name;
        this.date_create = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        this.date_last = "-";
    }

    public Plan(String name, String date_create, String date_last) {
        this.name = name;
        this.date_create = date_create;
        this.date_last = date_last;
    }

    public int getPlanID() {
        return plan_id;
    }

    public void setPlanID(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreate() {
        return date_create;
    }

    public void setDateCreate(String date_create) {
        this.date_create = date_create;
    }

    public String getDateLast() {
        return date_last;
    }

    public void setDateLast(String date_last) {
        this.date_last = date_last;
    }

}
