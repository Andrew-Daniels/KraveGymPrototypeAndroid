package com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces;

public interface FirebaseCallback {
    <E> void onCallback(String key, E data);
}
