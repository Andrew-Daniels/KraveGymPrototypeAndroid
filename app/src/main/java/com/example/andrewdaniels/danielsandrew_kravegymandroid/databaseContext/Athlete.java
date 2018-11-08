package com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.PropertyName;

public class Athlete extends Account {
    private String username;
    private Bitmap profileImage;

    public Athlete() {
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
