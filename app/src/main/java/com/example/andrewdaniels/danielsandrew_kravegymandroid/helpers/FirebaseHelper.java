package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public void testFirebase() {
        ref.child("Accounts/Trainer/test/Password").setValue("testpassword");
    }
}
