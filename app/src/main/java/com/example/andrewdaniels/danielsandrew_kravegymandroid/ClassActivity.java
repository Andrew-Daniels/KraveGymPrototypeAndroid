package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;

import java.util.List;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper;
import com.google.common.base.Predicate;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.classes.ClassAdapter;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ClassActivity extends AppCompatActivity implements FirebaseCallback, View.OnClickListener, SearchView.OnQueryTextListener {

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
        ((SearchView)findViewById(R.id.sv_class)).setOnQueryTextListener(this);
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
                }
                break;
            case R.id.btn_all:
                if (classState != ClassState.ALL) {
                    classState = ClassState.ALL;
                    FirebaseHelper.retrieveClass(this, FirebaseHelper.CLASS_TYPE_ALL);
                }
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
}
