package com.example.maxhi_000.mumsfit;



public class TrainPlan {

    private long id;
    private String exercise;
    private String reps;
    private double start_weight;

    public TrainPlan(String exercise, String reps, double start_weight, long id) {
        this.id = id;
        this.exercise = exercise;
        this.reps = reps;
        this.start_weight = start_weight;
    }

    @Override
    public String toString(){
        return this.reps + " times " + this.exercise + " with a weight of " + this.start_weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public double getStart_weight() {
        return start_weight;
    }

    public void setStart_weight(double start_weight) {
        this.start_weight = start_weight;
    }
}
