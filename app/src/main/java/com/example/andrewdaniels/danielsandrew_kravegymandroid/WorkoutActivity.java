package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Map.Entry;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.classes.WorkoutLogAdapter;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Workout;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.BitmapHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.StringFormatter;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.WorkoutLogListener;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.models.WorkoutLogView;

import java.util.HashMap;

public class WorkoutActivity extends AppCompatActivity implements FirebaseCallback, WorkoutLogListener {

    public static final String TAG = "WorkoutActivity.TAG";

    public static final String START_WORKOUT_ACTION = "WorkoutActivity.START_WORKOUT_ACTION";
    public static final String SELECTED_ATHLETE_EXTRA = "WorkoutActivity.SELECTED_ATHLETE_EXTRA";

    private HashMap<String, HashMap<String, String>> mAllWorkoutMusclesAndTypes = new HashMap<>();
    private HashMap<String, String> mCurrentTypesAndID = new HashMap<>();
    private HashMap<WorkoutCategory, String[]> mAllMusclesCurrentTypes = new HashMap<>();

    private ArrayList<WorkoutLogView> mWorkoutModel = new ArrayList<WorkoutLogView>() {{
        add(new WorkoutLogView(WorkoutLogView.ViewType.ADD));
    }};
    private int mWorkoutID;

    private Athlete mAthlete;
    private String mLastSavedWorkout;

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
        FirebaseHelper.getWorkoutCategories(this);

        ImageView profileIV = findViewById(R.id.iv_athlete);
        TextView initials = findViewById(R.id.tv_initials);

        if (mAthlete.hasProfileImage()) {
            Bitmap profileImage = BitmapHelper.loadImage(this, mAthlete.getUsername());
            profileIV.setImageBitmap(profileImage);
        } else {
            initials.setText(StringFormatter.getInitials(mAthlete));
        }
        profileIV.setClipToOutline(true);


        ListView lv = findViewById(R.id.lv_workout_log);
        WorkoutLogAdapter adapter = new WorkoutLogAdapter(this, mWorkoutModel);
        lv.setAdapter(adapter);
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
                saveWorkout();
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
            case FirebaseHelper.WORKOUT_SAVED:
                mWorkoutModel.add(new WorkoutLogView(WorkoutLogView.ViewType.SAVE));
                mLastSavedWorkout = (String)data;
                notifyDataSetHasChanged();
                break;
            case FirebaseHelper.UNDO_WORKOUT:
                mWorkoutModel.remove(mWorkoutModel.size() - 1);
                notifyDataSetHasChanged();
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
            pkr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    String selectedWorkoutType = getWorkoutTypeFromPicker(newVal);
                    workoutTypeChanged(selectedWorkoutType);
                }
            });
            pkr.setMinValue(0);
            pkr.setMaxValue(array.length - 1);
            pkr.setDisplayedValues(array);
            pkr.setValue(0);
            String selectedWorkoutType = getWorkoutTypeFromPicker(0);
            workoutTypeChanged(selectedWorkoutType);
        }
    }

    private String getWorkoutTypeFromPicker(int atIndex) {
        String[] types = mAllMusclesCurrentTypes.get(WorkoutCategory.TYPE);
        return types[atIndex];
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

    @Override
    public void addSetButtonClicked() {
        int indexToAdd = mWorkoutModel.size() - 1;
        String setNumber = String.valueOf(indexToAdd + 1);
        NumberPicker pkr = findViewById(R.id.pkr_workout_type);
        String workoutType = getWorkoutTypeFromPicker(pkr.getValue());
        WorkoutLogView cell = new WorkoutLogView(WorkoutLogView.ViewType.SET, setNumber, "10", workoutType);
        mWorkoutModel.add(indexToAdd, cell);

        notifyDataSetHasChanged();
    }

    @Override
    public void undoButtonClicked() {
        FirebaseHelper.undoWorkout(this, mAthlete, mLastSavedWorkout);
    }

    public void notifyDataSetHasChanged() {
        ListView lv = findViewById(R.id.lv_workout_log);
        WorkoutLogAdapter adapter = new WorkoutLogAdapter(this, mWorkoutModel);
        lv.setAdapter(adapter);
    }

    private void workoutTypeChanged(String toType) {
        for (WorkoutLogView view : mWorkoutModel) {
            if (view.getType() == WorkoutLogView.ViewType.SET) {
                view.setWorkoutType(toType);
            }
        }
        for (Entry<String, String> entry : mCurrentTypesAndID.entrySet()) {
            if (entry.getValue().equals(toType)) {
                mWorkoutID = Integer.valueOf(entry.getKey());
            }
        }
        notifyDataSetHasChanged();
    }

    private void saveWorkout() {
        ArrayList<Integer> sets = new ArrayList<>();
        for (WorkoutLogView cell : mWorkoutModel) {
            if (cell.getType().equals(WorkoutLogView.ViewType.SET)) {
                sets.add(Integer.valueOf(cell.getSet()));
            }
        }
        Workout workoutToSave = new Workout(sets);
        FirebaseHelper.saveWorkout(this, mAthlete, workoutToSave, mWorkoutID);
    }

}
