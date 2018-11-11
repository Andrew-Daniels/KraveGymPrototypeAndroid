package com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

//@IgnoreExtraProperties
public class Account implements Serializable {


    private String first;
    private String last;
    private String password;

    public Account() {
    }

    @PropertyName("First Name")
    public String getFirst() {
        return first;
    }

    @PropertyName("Last Name")
    public String getLast() {
        return last;
    }

    @PropertyName("Password")
    public String getPassword() {
        return password;
    }

    @PropertyName("First Name")
    public void setFirst(String first) {
        this.first = first;
    }

    @PropertyName("Last Name")
    public void setLast(String last) {
        this.last = last;
    }

    @PropertyName("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    //    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("author", author);
//        result.put("title", title);
//        result.put("body", body);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//
//        return result;
//    }

}
