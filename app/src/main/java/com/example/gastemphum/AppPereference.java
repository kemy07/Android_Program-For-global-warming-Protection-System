package com.example.gastemphum;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPereference {

    private static SharedPreferences sPreferences;

    public static void init(Context context) {
        sPreferences = context.getSharedPreferences("user_details", 0);
    }

    public static SharedPreferences getPrefs() {
        return sPreferences;
    }
}

