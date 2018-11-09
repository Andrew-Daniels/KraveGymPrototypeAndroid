package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.databaseContext.Athlete;

import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper.NAME_BLANK;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper.PASSWORD_4_CHARS;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper.PASSWORD_NO_MATCH;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper.PHONE_10_DIGITS;
import static com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers.ErrorHelper.PHONE_NUMBERS_ONLY;

public class ValidationHelper {

    public static String isPhoneValid(String phone) {
        if (!phone.matches("[0-9]+")) {
            return PHONE_NUMBERS_ONLY;
        } else if (phone.trim().length() != 10) {
            return PHONE_10_DIGITS;
        }
        return null;
    }

    public static String isPasswordValid(String password, String confirm) {
        if (password.length() < 4) {
            return PASSWORD_4_CHARS;
        } else if (!password.equals(confirm)) {
            return PASSWORD_NO_MATCH;
        }
        return null;
    }

    public static String isNameValid(String name) {
        if (name.trim().length() < 1) {
            return NAME_BLANK;
        }
        return null;
    }

    public static boolean nameContainsSearchText(Athlete athlete, String searchText) {

        String fullname = athlete.getFirst().concat(" ").concat(athlete.getLast()).toLowerCase();
        searchText = searchText.toLowerCase();

        return (athlete.getFirst().toLowerCase().contains(searchText) ||
                athlete.getLast().toLowerCase().contains(searchText) ||
                fullname.contains(searchText));
    }
}
