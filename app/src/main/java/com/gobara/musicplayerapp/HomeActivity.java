package com.gobara.musicplayerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private static int TIME_OUT=2000;
    private RelativeLayout homel;
    TextView appFact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homel = findViewById(R.id.homel);
        appFact=findViewById(R.id.fact);
        load_setting();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent goToMain=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(goToMain);
                finish();
            }
        },TIME_OUT);
    }
    private void load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("NIGHT", false);
        if (check_night) {
            homel.setBackgroundColor(Color.parseColor("#222222"));
        } else {
            homel.setBackgroundColor(Color.parseColor("#ffffff"));
            appFact.setTextColor(Color.parseColor("#000000"));

        }
        String orientation = sp.getString("ORIENTATION", "false");
        if ("1".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onResume() {
        load_setting();
        super.onResume();
    }
}