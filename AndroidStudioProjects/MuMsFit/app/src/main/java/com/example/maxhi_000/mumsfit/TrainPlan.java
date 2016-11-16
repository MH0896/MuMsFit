package com.example.maxhi_000.mumsfit;



public class TrainPlan {

    private long id;
    private String plan;
    private int trainings;
    private String date_create;
    private String date_last;

    public TrainPlan(String plan, int trainings, String date_create, String date_last, long id) {
        this.id = id;
        this.plan = plan;
        this.trainings = trainings;
        this.date_create = date_create;
        this.date_last = date_last;
    }

    @Override
    public String toString(){
        return "Plan " + this.plan + ": \nErstellt: " + this.date_create + "\nLast Workout: " + this.date_last + "\n" + this.trainings + " mal durchgeführt";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getTrainings() {
        return trainings;
    }

    public void setTrainings(int trainings) {
        this.trainings = trainings;
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
