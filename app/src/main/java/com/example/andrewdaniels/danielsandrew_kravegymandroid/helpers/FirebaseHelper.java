package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.support.annotation.NonNull;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Account;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class FirebaseHelper {

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference ref = database.getReference();

    private String mCurrentWork;
    private static FirebaseCallback delegate;

    public static final String CREATE_ACCOUNT = "FirebaseHelper.CREATE_ACCOUNT";
    public static final String LOGIN = "FirebaseHelper.LOGIN";
    public static final String CLASS = "FirebaseHelper.CLASS";

    public static final String CLASS_TYPE_ALL = "FirebaseHelper.CLASS_TYPE_ALL";
    public static final String CLASS_TYPE_CURRENT = "FirebaseHelper.CLASS_TYPE_CURRENT";

    private FirebaseHelper(String currentWork) {
        mCurrentWork = currentWork;
    }

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

                if (value != null && value.getPassword().equals(password)) {
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

    public static void retrieveClass(final FirebaseCallback callback, String classType) {

        delegate = callback;
        switch(classType) {
            case CLASS_TYPE_CURRENT:
                String currentDateAndTime = "041320180600";

                ref.child("Class/".concat(currentDateAndTime))
                        .addListenerForSingleValueEvent(new FirebaseHelper(CLASS_TYPE_CURRENT).listener);
                break;
            case CLASS_TYPE_ALL:
                ref.child("Accounts/Athlete/")
                        .addListenerForSingleValueEvent(new FirebaseHelper(CLASS_TYPE_ALL).listener);
                break;
        }
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            switch(mCurrentWork) {
                case CLASS_TYPE_CURRENT:
                case CLASS_TYPE_ALL:
                    GenericTypeIndicator<HashMap<String, Athlete>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Athlete>>() {};
                    Map<String, Athlete> athletesMap = dataSnapshot.getValue(objectsGTypeInd);
                    ArrayList<Athlete> athletes = new ArrayList<>();

                    if (athletesMap == null) { return; }

                    for (Map.Entry<String, Athlete> athleteEntry : athletesMap.entrySet()) {
                        athleteEntry.getValue().setUsername(athleteEntry.getKey());
                        athletes.add(athleteEntry.getValue());
                    }

                    //TODO: Get access to storage of images. Add images to athlete objects.
                    delegate.onCallback(CLASS, athletes);
                    break;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
