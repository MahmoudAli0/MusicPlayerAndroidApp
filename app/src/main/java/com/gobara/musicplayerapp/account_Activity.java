package com.gobara.musicplayerapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class account_Activity extends AppCompatActivity {

    TextView textView_name,textView_pass;
    Button btn2,btn3;
    ImageView imageView;
    int SELECT_IMAGE_CODE=1;
    BottomNavigationView bottom_view;
    MusicListAdapter musicListAdapter;
    ArrayList<MediaModel> FAV_LIST ;
    LinearLayout account_layout;
    String[] permissions ={Manifest.permission.READ_EXTERNAL_STORAGE};
    int REQUEST_CODE =12345;
    boolean isPermissionGranted =false;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASS = "pass";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        textView_name= findViewById(R.id.text_fullname);
        textView_pass= findViewById(R.id.text_pass);
        btn3= findViewById(R.id.btnsign);
        account_layout= findViewById(R.id.account_layout);
        load_setting();
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name =sharedPreferences.getString(KEY_NAME,null);
        String pass =sharedPreferences.getString(KEY_PASS,null);

        if (name !=null || pass !=null){
            //So set the data on textview
            textView_name.setText("Full Name :"+name);
            textView_pass.setText("Password :"+pass);
        }

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent AA =new Intent(account_Activity.this,LoginActivity.class);
                startActivity(AA);
                finish();

            }
        });
        bottom_view=findViewById(R.id.homeVav);
        bottom_view.setSelectedItemId(R.id.account);
        musicListAdapter= new MusicListAdapter(FAV_LIST,this);
        bottom_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(account_Activity.this,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.account:
                        return true ;
                    case R.id.contact:
                        startActivity(new Intent(account_Activity.this,Main_Contact.class));


                }
                return false;
            }
        });

        //pick image from gallary
        btn2=findViewById(R.id.pickbtn);
        imageView=findViewById(R.id.pickimage);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Intent SS =new Intent();
                SS.setType("image/*");
                SS.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(SS,"Title"),SELECT_IMAGE_CODE);

            }
        });
    }

    private void load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("NIGHT", false);
        if (check_night) {
            account_layout.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            account_layout.setBackgroundColor(Color.parseColor("#C6A4CC"));
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
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            isPermissionGranted =true;
        }else {
            ActivityCompat.requestPermissions(account_Activity.this,permissions,REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true;
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            Uri uri=data.getData();
            imageView.setImageURI(uri);
            btn2.setText("Done");
        }
    }

    @Override
    protected void onResume() {
        load_setting();
        super.onResume();
    }
}