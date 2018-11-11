package com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext;

import android.content.Context;

import java.io.File;

public class Athlete extends Account {
    private String username;
    private boolean hasProfileImage;

    public Athlete() {
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean hasProfileImage() {
        return hasProfileImage;
    }

    public void setHasProfileImage(boolean hasProfileImage) {
        this.hasProfileImage = hasProfileImage;
    }

    public boolean checkForExistingProfilePicture(Context c) {
        File localStorage = c.getFilesDir();

        File imageToSave = new File(localStorage, username);
        hasProfileImage = imageToSave.exists();
        return hasProfileImage;
    }
}
