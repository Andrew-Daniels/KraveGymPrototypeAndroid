package com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext;

import java.util.ArrayList;

public class Workout {
    private ArrayList<Integer> sets;

    public Workout() {
    }

    public Workout(ArrayList<Integer> sets) {
        this.sets = sets;
    }

    public ArrayList<Integer> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Integer> sets) {
        this.sets = sets;
    }
}
