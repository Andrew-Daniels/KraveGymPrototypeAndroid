package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.support.annotation.NonNull;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.DatabaseContext.Account;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

import java.lang.reflect.Array;
import java.util.Objects;

public class FirebaseHelper {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference ref = database.getReference();

    public static final String CREATE_ACCOUNT = "FirebaseHelper.CREATE_ACCOUNT";
    public static final String LOGIN = "FirebaseHelper.LOGIN";

    public static void createAccount(final FirebaseCallback callback, final String phone, final String first, final String last, final String password) {
        final String basePath = "Accounts/Trainer/".concat(phone);

        ref.child(basePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Account value = dataSnapshot.getValue(Account.class);

                if (Objects.equals(value, null)) {
                    ref.child(basePath.concat("/First Name")).setValue(first);
                    ref.child(basePath.concat("/Last Name")).setValue(last);
                    ref.child(basePath.concat("/Password")).setValue(password);
                    callback.onCallback(CREATE_ACCOUNT, true);
                } else {
                    callback.onCallback(CREATE_ACCOUNT, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void login(final FirebaseCallback callback, final String phone, final String password) {
        final String basePath = "Accounts/Trainer/".concat(phone);

        ref.child(basePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account value = dataSnapshot.getValue(Account.class);

                if (Objects.requireNonNull(value).getPassword().equals(password)) {
                    callback.onCallback(LOGIN, true);
                } else {
                    callback.onCallback(LOGIN, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
