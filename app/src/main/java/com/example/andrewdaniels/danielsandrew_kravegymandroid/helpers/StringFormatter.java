package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class StringFormatter {

    public static String getInitials(Account account) {
        String firstInitial = account.getFirst().substring(0, 1);
        String lastInitial = account.getLast().substring(0, 1);
        return firstInitial.concat(lastInitial);
    }

    public static Comparator<String> getAlphabeticalSort() {

        return new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        };
    }

    public static String currentDateAndTimeString() {
        DateFormat df = new SimpleDateFormat("MMddyyyyHHmm", Locale.US);
        Date today = new Date();
        return df.format(today);
    }
}
