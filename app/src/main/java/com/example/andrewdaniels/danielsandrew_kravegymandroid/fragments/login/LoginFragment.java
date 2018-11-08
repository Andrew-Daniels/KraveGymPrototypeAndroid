package com.example.andrewdaniels.danielsandrew_kravegymandroid.fragments.login;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.ClassActivity;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.LoginListener;

import java.util.Objects;

import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper.isPasswordValid;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper.isPhoneValid;

public class LoginFragment extends Fragment implements FirebaseCallback {

    public static final String TAG = "LoginFragment.TAG";
    private LoginListener delegate;
    private String mUID;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            delegate = (LoginListener)context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            View v = getView();
            View btn = v.findViewById(R.id.btn_login);
            btn.setOnClickListener(btnClicked);
            btn = v.findViewById(R.id.tv_nav_register);
            btn.setOnClickListener(btnClicked);
        }
        Athlete test = new Athlete();
        test.setUsername("4123590221");
        FirebaseHelper.downloadProfileImage(this, test);
    }

    private final View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_login:
                    tryLogin();
                    break;
                case R.id.tv_nav_register:
                    delegate.navigateTo(RegisterFragment.TAG);
                    break;
            }
        }
    };

    private void tryLogin() {
        boolean errorsExist = false;

        View v = getView();
        String phone;
        String password;

        String errorKey;

        TextView field = Objects.requireNonNull(v).findViewById(R.id.et_phone);
        phone = field.getText().toString();
        errorKey = isPhoneValid(phone);
        if (ErrorHelper.setErrorOnField(getActivity(), field, errorKey)) {
            errorsExist = true;
        }

        field = v.findViewById(R.id.et_password);
        password = field.getText().toString();
        errorKey = isPasswordValid(password, password);
        if (ErrorHelper.setErrorOnField(getActivity(), field, errorKey)) {
            errorsExist = true;
        }

        if (!errorsExist) {
            FirebaseHelper.login(this, phone, password);
            mUID = phone;
        }
    }

    @Override
    public <E> void onCallback(String key, E data) {
        switch(key) {
            case FirebaseHelper.LOGIN:
                if (data instanceof Boolean) {
                    boolean accountCreated = (Boolean)data;

                    if (accountCreated) {
                        delegate.setUID(mUID);
                        delegate.navigateTo(ClassActivity.TAG);
                    } else {
                        Toast.makeText(getActivity(), "Not Logged In", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
