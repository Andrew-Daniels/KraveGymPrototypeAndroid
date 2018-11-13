package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;

import java.util.Collections;
import java.util.ArrayList;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.StringFormatter;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;

import java.util.HashMap;

public class WorkoutActivity extends AppCompatActivity implements FirebaseCallback {

    public static final String TAG = "WorkoutActivity.TAG";

    public static final String START_WORKOUT_ACTION = "WorkoutActivity.START_WORKOUT_ACTION";
    public static final String SELECTED_ATHLETE_EXTRA = "WorkoutActivity.SELECTED_ATHLETE_EXTRA";

    private HashMap<String, HashMap<String, String>> mAllWorkoutMusclesAndTypes = new HashMap<>();
    private HashMap<String, String> mCurrentTypesAndID = new HashMap<>();
    private HashMap<WorkoutCategory, String[]> mAllMusclesCurrentTypes = new HashMap<>();

    private Athlete mAthlete;

    private enum WorkoutCategory {
        TYPE, MUSCLE
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        onHandleIntent(getIntent());

        if (savedInstanceState == null) {
            setupActivity();
        }
    }

    private void setupActivity() {
        //TODO: Grab all of the workout types and setup the pickers.
        FirebaseHelper.getWorkoutCategories(this);
    }

    private void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action != null && !action.equals("")) {
            switch(action) {
                case START_WORKOUT_ACTION:
                    mAthlete = (Athlete)intent.getSerializableExtra(SELECTED_ATHLETE_EXTRA);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_save:
                //TODO: Handle save here.
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public <E> void onCallback(String key, E data) {
        switch(key) {
            case FirebaseHelper.WORKOUT_CATEGORIES:
                HashMap<String, HashMap<String, String>> categories = (HashMap<String, HashMap<String, String>>)data;
                mAllWorkoutMusclesAndTypes = categories;
                setupPickers();
                break;
        }
    }

    private void setupPickers() {

        parseWorkoutCategories();

        String[] muscles = mAllMusclesCurrentTypes.get(WorkoutCategory.MUSCLE);
        if (muscles != null) {

            NumberPicker pkr = findViewById(R.id.pkr_workout_muscle);
            pkr.setMinValue(0);
            pkr.setMaxValue(muscles.length - 1);
            pkr.setDisplayedValues(muscles);
            pkr.setOnValueChangedListener(pickerChanged);

            workoutMuscleChanged(muscles[0]);
        }
    }

    private void parseWorkoutCategories() {

        ArrayList<String> model = new ArrayList<>(mAllWorkoutMusclesAndTypes.keySet());
        Collections.sort(model, StringFormatter.getAlphabeticalSort());
        String[] array = new String[model.size()];
        model.toArray(array);
        mAllMusclesCurrentTypes.put(WorkoutCategory.MUSCLE, array);
    }

    private void workoutMuscleChanged(String toMuscle) {
        mCurrentTypesAndID = mAllWorkoutMusclesAndTypes.get(toMuscle);

        ArrayList<String> model = new ArrayList<>(mCurrentTypesAndID.values());
        Collections.sort(model, StringFormatter.getAlphabeticalSort());
        String[] array = new String[model.size()];
        model.toArray(array);
        mAllMusclesCurrentTypes.put(WorkoutCategory.TYPE, array);

        if(array.length > 0) {
            NumberPicker pkr = findViewById(R.id.pkr_workout_type);
            pkr.setMinValue(0);
            pkr.setMaxValue(array.length - 1);
            pkr.setDisplayedValues(array);
        }
    }

    @Override
    public Context getCallbackContext() {
        return this;
    }

    NumberPicker.OnValueChangeListener pickerChanged = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            switch(picker.getId()) {
                case R.id.pkr_workout_muscle:
                    String[] muscle = mAllMusclesCurrentTypes.get(WorkoutCategory.MUSCLE);
                    workoutMuscleChanged(muscle[newVal]);
                    break;
            }
        }
    };
}
