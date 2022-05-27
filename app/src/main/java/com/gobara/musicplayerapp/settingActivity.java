package com.gobara.musicplayerapp;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class settingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.xml.pref);
        addPreferencesFromResource(R.xml.setteing_prefrences);
        load_setting();
    }
    private void load_setting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("NIGHT", false);
        if (check_night) {
            getListView().setBackgroundColor(Color.parseColor("#222222"));
        } else {
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }
        CheckBoxPreference check_night2 = (CheckBoxPreference) findPreference("NIGHT");
        check_night2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object obj) {
                boolean yes = (boolean) obj;
                if (yes) {
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                } else {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                return true;
            }
        });
        ListPreference list_pref = (ListPreference) findPreference("ORIENTATION");
        String orientation = sp.getString("ORIENTATION", "false");
        if ("1".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        list_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference pref, Object obj) {
                String items = (String) obj;
                if (pref.getKey().equals("ORIENTATION")) {
                    switch (items) {
                        case "1":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
                            break;
                        case "2":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "3":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;


                    }
                }
                return true;
            }
        });

    }
    @Override
    protected void onResume() {
        load_setting();
        super.onResume();
    }
}
