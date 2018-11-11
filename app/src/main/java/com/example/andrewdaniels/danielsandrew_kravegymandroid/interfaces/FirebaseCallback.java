package com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces;

import android.content.Context;

public interface FirebaseCallback {
    <E> void onCallback(String key, E data);
    Context getCallbackContext();
}
