package com.example.andrewdaniels.danielsandrew_kravegymandroid.fragments.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.ClassActivity;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.FirebaseHelper;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.LoginListener;

import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper.isNameValid;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper.isPasswordValid;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ValidationHelper.isPhoneValid;

public class RegisterFragment extends Fragment implements FirebaseCallback {

    public static final String TAG = "RegisterFragment.TAG";

    private String mUID;
    private LoginListener delegate;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            delegate = (LoginListener)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            View v = getView();
            View btn = v.findViewById(R.id.btn_register);
            btn.setOnClickListener(btnClicked);
            btn = v.findViewById(R.id.tv_nav_login);
            btn.setOnClickListener(btnClicked);
        }
    }

    private final View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_register:
                    tryRegister();
                    break;
                case R.id.tv_nav_login:
                    delegate.navigateTo(LoginFragment.TAG);
                    break;
            }
        }
    };

    private void tryRegister() {
        if (getView() != null) {
            boolean errorsExist = false;

            View v = getView();
            String phone;
            String first;
            String last;
            String password;
            String confirmPassword;

            String errorKey;

            TextView field = v.findViewById(R.id.et_phone);
            phone = field.getText().toString();
            errorKey = isPhoneValid(phone);
            if (ErrorHelper.setErrorOnField(getActivity(), field, errorKey)) {
                errorsExist = true;
            }

            field = v.findViewById(R.id.tv_first);
            first = field.getText().toString();
            errorKey = isNameValid(first);
            if (ErrorHelper.setErrorOnField(getActivity(), field, errorKey)) {
                errorsExist = true;
            }

            field = v.findViewById(R.id.tv_last);
            last = field.getText().toString();
            errorKey = isNameValid(last);
            if (ErrorHelper.setErrorOnField(getActivity(), field, errorKey)) {
                errorsExist = true;
            }

            field = v.findViewById(R.id.et_password);
            password = field.getText().toString();

            TextView confirm = v.findViewById(R.id.et_password_confirm);
            confirmPassword = confirm.getText().toString();

            errorKey = isPasswordValid(password, confirmPassword);
            if (ErrorHelper.setErrorOnField(getActivity(), field, errorKey)) {
                ErrorHelper.setErrorOnField(getActivity(), confirm, errorKey);
                errorsExist = true;
            }

            if (!errorsExist) {
                FirebaseHelper.createAccount(this, phone, first, last, password);
                mUID = phone;
            }
        }
    }


    @Override
    public <E> void onCallback(String key, E data) {
        switch(key) {
            case FirebaseHelper.CREATE_ACCOUNT:
                if (data instanceof Boolean) {
                    boolean accountCreated = (Boolean)data;

                    if (accountCreated) {
                        delegate.setUID(mUID);
                        delegate.navigateTo(ClassActivity.TAG);
                    } else {
                        Toast.makeText(getActivity(), "Account not created", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public Context getCallbackContext() {
        return getActivity();
    }
}
