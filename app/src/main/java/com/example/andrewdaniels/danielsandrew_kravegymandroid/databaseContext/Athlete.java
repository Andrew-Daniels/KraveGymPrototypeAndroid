package com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.PropertyName;

public class Athlete extends Account {
    private String username;
    private Image profileImage;
    private String profileImageString;

    public Athlete() {
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageString() {
        return profileImageString;
    }

    public void setProfileImageString(String profileImageString) {
        this.profileImageString = profileImageString;
    }
}
