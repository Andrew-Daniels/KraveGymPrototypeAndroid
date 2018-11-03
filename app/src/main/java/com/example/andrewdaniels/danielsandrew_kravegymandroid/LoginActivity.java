package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.fragments.login.LoginFragment;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.fragments.login.RegisterFragment;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.LoginListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginListener {

    private int mLoginContainer = R.id.login_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        if (savedInstanceState == null) {
            setupLogin();
        }
    }

    private void setupRegister() {
        getFragmentManager().beginTransaction()
                .add(mLoginContainer, RegisterFragment.newInstance(), RegisterFragment.TAG)
                .addToBackStack(RegisterFragment.TAG)
                .commit();
    }

    private void setupLogin() {
        getFragmentManager().beginTransaction()
                .add(mLoginContainer, LoginFragment.newInstance(), LoginFragment.TAG)
                .commit();
    }

    @Override
    public void navigateTo(String fragmentTag) {
        switch(fragmentTag) {
            case LoginFragment.TAG:
                getFragmentManager().popBackStack();
                break;
            case RegisterFragment.TAG:
                setupRegister();
                break;
        }
    }
}

