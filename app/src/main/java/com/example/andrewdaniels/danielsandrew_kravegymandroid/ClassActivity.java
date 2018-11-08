package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.classes.ClassAdapter;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;

public class ClassActivity extends AppCompatActivity implements FirebaseCallback, View.OnClickListener {

    public static final String TAG = "ClassActivity.TAG";
    private String mUID;

    public static final String LOGGED_IN_ACTION = "com.example.andrewdaniels.danielsandrew_kravegymandroid.LOGGED_IN_ACTION";
    public static final String LOGGED_IN_UID_EXTRA = "com.example.andrewdaniels.danielsandrew_kravegymandroid.LOGGED_IN_UID_EXTRA";

    private ArrayList<Athlete> mModel;

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
    }

    private void setupGridView() {
        ClassAdapter adapter = new ClassAdapter(this, mModel, R.layout.single_athlete);
        GridView gv = findViewById(R.id.gv_class);
        gv.setAdapter(adapter);
    }

    @Override
    public <E> void onCallback(String key, E data) {
        switch(key) {
            case FirebaseHelper.CLASS:
                mModel = (ArrayList<Athlete>)data;
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
                            GridView gv = findViewById(R.id.gv_class);
                            ClassAdapter adapter = (ClassAdapter)gv.getAdapter();
                            adapter.notifyDataSetChanged();
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
}
