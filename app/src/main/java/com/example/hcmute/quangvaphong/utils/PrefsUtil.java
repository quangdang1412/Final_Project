package com.example.hcmute.quangvaphong.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtil {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_FIRST_RUN = "isFirstRun";

    public static boolean isFirstRun(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_FIRST_RUN, true);
    }

    public static void setFirstRun(Context context, boolean isFirstRun) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_FIRST_RUN, isFirstRun);
        editor.apply();
    }
}
