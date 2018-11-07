package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;

public class ClassActivity extends AppCompatActivity implements FirebaseCallback {

    public static final String TAG = "ClassActivity.TAG";
    private String mUID;

    public static final String LOGGED_IN_ACTION = "com.example.andrewdaniels.danielsandrew_kravegymandroid.LOGGED_IN_ACTION";
    public static final String LOGGED_IN_UID_EXTRA = "com.example.andrewdaniels.danielsandrew_kravegymandroid.LOGGED_IN_UID_EXTRA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        handleIntent(getIntent());

        if (savedInstanceState == null) {
            //TODO: Grab firebase data here and setup gridView adapter
            FirebaseHelper.retrieveClass(this, FirebaseHelper.CLASS_TYPE_CURRENT);
        }
    }

    @Override
    public <E> void onCallback(String key, E data) {

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
}
