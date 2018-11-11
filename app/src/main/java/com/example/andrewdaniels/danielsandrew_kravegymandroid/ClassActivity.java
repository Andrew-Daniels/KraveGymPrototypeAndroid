package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper;
import com.google.common.base.Predicate;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.classes.ClassAdapter;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ClassActivity extends AppCompatActivity implements FirebaseCallback, View.OnClickListener, SearchView.OnQueryTextListener, GridView.OnItemClickListener {

    public static final String TAG = "ClassActivity.TAG";
    private String mUID;

    public static final String LOGGED_IN_ACTION = "com.example.andrewdaniels.danielsandrew_kravegymandroid.LOGGED_IN_ACTION";
    public static final String LOGGED_IN_UID_EXTRA = "com.example.andrewdaniels.danielsandrew_kravegymandroid.LOGGED_IN_UID_EXTRA";

    private List<Athlete> mModel;
    private List<Athlete> mModelFiltered;
    private boolean isSearching = false;

    private ClassState classState;

    private enum ClassState {
        ALL, CURRENT
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        handleIntent(getIntent());

        if (savedInstanceState == null) {
            setupActivity();
        }
    }

    private void setupActivity() {
        classState = ClassState.CURRENT;
        FirebaseHelper.retrieveClass(this, FirebaseHelper.CLASS_TYPE_CURRENT);
        findViewById(R.id.btn_current_class).setOnClickListener(this);
        findViewById(R.id.btn_all).setOnClickListener(this);
        ((GridView)findViewById(R.id.gv_class)).setOnItemClickListener(this);
        SearchView v = findViewById(R.id.sv_class);
        v.setOnQueryTextListener(this);
        v.setOnClickListener(this);
    }

    private void handleSegmentedButtonClick(int buttonId) {
        int colorPrimaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        switch(buttonId) {
            case R.id.btn_current_class:
                TextView a = findViewById(R.id.btn_all);
                a.setBackgroundColor(colorPrimary);
                a.setTextColor(colorPrimaryDark);
                break;
            case R.id.btn_all:
                TextView b = findViewById(R.id.btn_current_class);
                b.setBackgroundColor(colorPrimary);
                b.setTextColor(colorPrimaryDark);
                break;
        }
        TextView c = findViewById(buttonId);
        c.setBackgroundColor(colorPrimaryDark);
        c.setTextColor(colorPrimary);
    }

    private void setupGridView() {

        notifyDataSetChanged(mModel);

        if (isSearching) {
            mModelFiltered = null;
            searchForAthletesWithNameMatchingCurrentSearchText();
        }
    }

    @Override
    public <E> void onCallback(String key, E data) {
        switch(key) {
            case FirebaseHelper.CLASS:
                mModel = (List<Athlete>)data;
                if (mModel != null && mModel.size() > 0) {
                    setupGridView();
                    for (Athlete athlete: mModel) {
                        FirebaseHelper.downloadProfileImage(this, athlete);
                    }
                }
                break;
            case FirebaseHelper.DOWNLOAD_PROFILE_IMAGE:
                if (data instanceof Bundle) {
                    Bundle bundle = (Bundle)data;

                    Bitmap image = bundle.getParcelable(FirebaseHelper.DOWNLOAD_PROFILE_IMAGE);
                    String username = bundle.getString(FirebaseHelper.PROFILE_IMAGE_UID);
                    for (Athlete athlete: mModel) {
                        if (athlete.getUsername().equals(username)) {
                            athlete.setProfileImage(image);
                            notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch(action) {
            case LOGGED_IN_ACTION:
                mUID = intent.getStringExtra(LOGGED_IN_UID_EXTRA);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_current_class:
                if (classState != ClassState.CURRENT) {
                    classState = ClassState.CURRENT;
                    FirebaseHelper.retrieveClass(this, FirebaseHelper.CLASS_TYPE_CURRENT);
                    handleSegmentedButtonClick(v.getId());
                }
                break;
            case R.id.btn_all:
                if (classState != ClassState.ALL) {
                    classState = ClassState.ALL;
                    FirebaseHelper.retrieveClass(this, FirebaseHelper.CLASS_TYPE_ALL);
                    handleSegmentedButtonClick(v.getId());
                }
                break;
            case R.id.sv_class:
                ((SearchView)v).setIconified(false);
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String searchText) {
        searchForAthletesWithNameMatching(searchText);
        return false;
    }

    private void notifyDataSetChanged() {
        GridView gv = findViewById(R.id.gv_class);
        ClassAdapter adapter = (ClassAdapter)gv.getAdapter();
        adapter.notifyDataSetChanged();
    }
    private void notifyDataSetChanged(List<Athlete> model) {
        GridView gv = findViewById(R.id.gv_class);
        ClassAdapter adapter = new ClassAdapter(this, model, R.layout.single_athlete);
        gv.setAdapter(adapter);
    }

    private void searchForAthletesWithNameMatchingCurrentSearchText() {
        String searchText = String.valueOf(((SearchView)findViewById(R.id.sv_class)).getQuery());
        searchForAthletesWithNameMatching(searchText);
    }

    private void searchForAthletesWithNameMatching(final String searchText) {
        List<Athlete> oldModelFiltered = mModelFiltered;

        boolean searchTextIsEmpty = searchText.equals("");

        if (mModelFiltered == null && mModel != null) {
            mModelFiltered = mModel;
        } else if (searchTextIsEmpty) {
            mModelFiltered = mModel;
            isSearching = false;
        }

        if (!searchTextIsEmpty) {

            isSearching = true;

            Predicate<Athlete> nameContainsText = new Predicate<Athlete>() {
                @Override
                public boolean apply(@Nullable Athlete athlete) {
                    return athlete != null && (ValidationHelper.nameContainsSearchText(athlete, searchText));
                }
            };

            mModelFiltered = Lists.newArrayList(Collections2.filter(mModel, nameContainsText));
        }

        if ((oldModelFiltered == null && mModelFiltered != null) || mModelFiltered != null && !oldModelFiltered.equals(mModelFiltered)) {
            notifyDataSetChanged(mModelFiltered);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.btn_logout:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Athlete> model = mModelFiltered != null ? mModelFiltered : mModel;

        Athlete a =  model.get(position);

        Intent workoutIntent = new Intent(this, WorkoutActivity.class);
        workoutIntent.setAction(WorkoutActivity.START_WORKOUT_ACTION);
        workoutIntent.putExtra(WorkoutActivity.SELECTED_ATHLETE_EXTRA, a);
        startActivity(workoutIntent);
    }
}
