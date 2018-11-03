package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.content.Context;
import android.widget.TextView;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;

public class ErrorHelper {
    public static final String PHONE_10_DIGITS = "PHONE_10_DIGITS";
    public static final String PHONE_NUMBERS_ONLY = "PHONE_NUMBERS_ONLY";
    public static final String NAME_BLANK = "NAME_BLANK";
    public static final String PASSWORD_4_CHARS = "PASSWORD_4_CHARS";
    public static final String PASSWORD_NO_MATCH = "PASSWORD_NO_MATCH";

    public static Boolean setErrorOnField(Context c, TextView field, String errorKey) {
        if (errorKey != null) {
            String errorMsg = getErrorMessageForKey(c, errorKey);
            if (errorMsg != null) {
                field.setError(errorMsg);
            }
            return true;
        }
        return false;
    }

    private static String getErrorMessageForKey(Context c, String errorKey) {
        switch(errorKey) {
            case PHONE_NUMBERS_ONLY:
                return c.getString(R.string.numbers_only);
            case PHONE_10_DIGITS:
                return c.getString(R.string.phone_10);
            case NAME_BLANK:
                return c.getString(R.string.field_blank);
            case PASSWORD_4_CHARS:
                return c.getString(R.string.pass_minimum);
            case PASSWORD_NO_MATCH:
                return c.getString(R.string.pass_match);
        }
        return null;
    }
}
