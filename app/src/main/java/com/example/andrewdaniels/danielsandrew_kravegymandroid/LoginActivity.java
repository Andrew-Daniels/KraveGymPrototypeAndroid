package com.example.andrewdaniels.danielsandrew_kravegymandroid;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.fragments.login.LoginFragment;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.fragments.login.RegisterFragment;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.LoginListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginListener {

    private final int mLoginContainer = R.id.login_container;

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

