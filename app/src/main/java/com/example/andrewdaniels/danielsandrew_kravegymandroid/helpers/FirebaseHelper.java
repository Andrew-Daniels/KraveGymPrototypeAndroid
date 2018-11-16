package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Account;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Workout;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class FirebaseHelper {

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference ref = database.getReference();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference storageRef = storage.getReference();

    private String mCurrentWork;
    private String mUID;
    private static FirebaseCallback delegate;

    public static final String CREATE_ACCOUNT = "FirebaseHelper.CREATE_ACCOUNT";
    public static final String LOGIN = "FirebaseHelper.LOGIN";
    public static final String CLASS = "FirebaseHelper.CLASS";

    public static final String CLASS_TYPE_ALL = "FirebaseHelper.CLASS_TYPE_ALL";
    public static final String CLASS_TYPE_CURRENT = "FirebaseHelper.CLASS_TYPE_CURRENT";
    public static final String DOWNLOAD_PROFILE_IMAGE = "FirebaseHelper.DOWNLOAD_PROFILE_IMAGE";
    public static final String PROFILE_IMAGE_UID = "FirebaseHelper.PROFILE_IMAGE_UID";
    public static final String WORKOUT_CATEGORIES = "FirebaseHelper.WORKOUT_CATEGORIES";
    public static final String WORKOUT_SAVED = "FirebaseHelper.WORKOUT_SAVED";
    public static final String UNDO_WORKOUT = "FirebaseHelper.UNDO_WORKOUT";

    private FirebaseHelper(FirebaseCallback callback, String currentWork) {
        mCurrentWork = currentWork;
        delegate = callback;
    }

    private FirebaseHelper(FirebaseCallback callback, String mCurrentWork, String mUID) {
        this.mCurrentWork = mCurrentWork;
        this.mUID = mUID;
        delegate = callback;
    }

    public static void saveWorkout(final FirebaseCallback callback, Athlete athlete, Workout workout, Integer workoutID) {
        final String today = StringFormatter.currentDateAndTimeString();
        String ID = String.valueOf(workoutID);
        String basePath = "Logged Workouts/"
                .concat(athlete.getUsername())
                .concat("/")
                .concat(today)
                .concat("/")
                .concat(ID);

        ref.child(basePath).setValue(workout).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onCallback(WORKOUT_SAVED, today);
            }
        });
    }

    public static void undoWorkout(final FirebaseCallback callback, Athlete athlete, String workoutTime) {
        String basePath = "Logged Workouts/"
                .concat(athlete.getUsername())
                .concat("/")
                .concat(workoutTime);
        ref.child(basePath).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(UNDO_WORKOUT, true);
            }
        });
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

        switch(classType) {
            case CLASS_TYPE_CURRENT:
                String currentDateAndTime = "041320180600";

                ref.child("Class/".concat(currentDateAndTime))
                        .addListenerForSingleValueEvent(new FirebaseHelper(callback, CLASS_TYPE_CURRENT).listener);
                break;
            case CLASS_TYPE_ALL:
                ref.child("Accounts/Athlete/")
                        .addListenerForSingleValueEvent(new FirebaseHelper(callback, CLASS_TYPE_ALL).listener);
                break;
        }
    }

    public static <E> void downloadProfileImage(FirebaseCallback callback, E account) {
        String username = null;
        if (account instanceof Athlete) {
            Athlete athlete = (Athlete)account;
            username = athlete.getUsername();
        }
        if (username != null) {
            StorageReference tempRef = storageRef.child(
                    "AthletePhotos/".concat(username).concat(".jpg")
            );
            final long ONE_MEGABYTE = 1024 * 1024;
            FirebaseHelper instance = new FirebaseHelper(callback, DOWNLOAD_PROFILE_IMAGE, username);
            tempRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(instance.storageListener);
        }
    }

    public static void getWorkoutCategories(final FirebaseCallback callback) {
        final String basePath = "Workouts";

        ref.child(basePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, HashMap<String, String>>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, HashMap<String, String>>>() {};
                HashMap<String, HashMap<String, String>> categoriesMap = dataSnapshot.getValue(objectsGTypeInd);
                callback.onCallback(WORKOUT_CATEGORIES, categoriesMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private final ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            switch(mCurrentWork) {
                case CLASS_TYPE_CURRENT:
                case CLASS_TYPE_ALL:
                    GenericTypeIndicator<HashMap<String, Athlete>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Athlete>>() {};
                    Map<String, Athlete> athletesMap = dataSnapshot.getValue(objectsGTypeInd);
                    List<Athlete> athletes = new ArrayList<>();

                    if (athletesMap == null) { return; }

                    for (Map.Entry<String, Athlete> athleteEntry : athletesMap.entrySet()) {
                        athleteEntry.getValue().setUsername(athleteEntry.getKey());
                        athletes.add(athleteEntry.getValue());
                    }

                    delegate.onCallback(CLASS, athletes);
                    break;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private final OnSuccessListener<byte[]> storageListener =  new OnSuccessListener<byte[]>() {
        @Override
        public void onSuccess(byte[] bytes) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            boolean imageSaved = BitmapHelper.scaleBitmapAndKeepRatio(delegate, mUID, image, 300, 200);
            if (imageSaved) {
                delegate.onCallback(DOWNLOAD_PROFILE_IMAGE, mUID);
            }
        }
    };
}
