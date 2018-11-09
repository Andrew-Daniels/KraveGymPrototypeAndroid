package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Account;

public class StringFormatter {

    public static String getInitials(Account account) {
        String firstInitial = account.getFirst().substring(0, 1);
        String lastInitial = account.getLast().substring(0, 1);
        return firstInitial.concat(lastInitial);
    }
}
