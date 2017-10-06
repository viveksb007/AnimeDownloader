package com.browser.viveksb007.animedownloader;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by viveksb007 on 4/10/17.
 */

public class PreferenceManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String pref_name = "AttendanceClient";
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";

    public PreferenceManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    }

    public void setFirstLaunch(boolean is_first_time) {
        editor = preferences.edit();
        editor.putBoolean(IS_FIRST_LAUNCH, is_first_time);
        editor.apply();
    }

    public boolean isFirstTime() {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true);
    }

}
