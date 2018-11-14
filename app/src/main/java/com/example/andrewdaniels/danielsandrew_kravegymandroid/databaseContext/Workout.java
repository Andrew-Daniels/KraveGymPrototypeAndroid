package com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext;

import java.util.ArrayList;

public class Workout {
    private int id;
    private ArrayList<Integer> sets;

    public Workout() {
    }

    public Workout(int id, ArrayList<Integer> sets) {
        this.id = id;
        this.sets = sets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Integer> sets) {
        this.sets = sets;
    }
}
