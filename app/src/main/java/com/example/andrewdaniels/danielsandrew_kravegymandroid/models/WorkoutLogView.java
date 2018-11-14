package com.example.andrewdaniels.danielsandrew_kravegymandroid.models;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.WorkoutActivity;

public class WorkoutLogView {

    public enum ViewType {
        SET, ADD, SAVE
    }

    private ViewType type;

    //Set Type
    private String set;
    private String rep;
    private String workoutType;

    //Add Type

    //Save Type


    public WorkoutLogView(ViewType type) {
        this.type = type;
    }

    public WorkoutLogView(ViewType type, String set, String rep, String workoutType) {
        this.type = type;
        this.set = set;
        this.rep = rep;
        this.workoutType = workoutType;
    }

    public ViewType getType() {
        return type;
    }

    public void setType(ViewType type) {
        this.type = type;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }
}
