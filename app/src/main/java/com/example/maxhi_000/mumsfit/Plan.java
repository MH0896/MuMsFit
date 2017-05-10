package com.example.maxhi_000.mumsfit;



public class Plan {
    private int plan_id;
    private String name;
    private  String date_create;
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
        this.date_create = "default";
        this.date_last = "default";
    }

    public Plan(String name, String date_create, String date_last) {
        this.name = name;
        this.date_create = date_create;
        this.date_last = date_last;
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

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getDate_last() {
        return date_last;
    }

    public void setDate_last(String date_last) {
        this.date_last = date_last;
    }
}
