package com.gobara.musicplayerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private   TextView noSonges,music_title1;
    private   RecyclerView ourSonges;
    private   BottomNavigationView bottom_view;
    private   ArrayList<MediaModel> songesList=new ArrayList<>();
    private   long backPressedTime ;
    private   Toast backToast ;
    private   MusicListAdapter listAdapter;
    private   RelativeLayout mainl;
    private   BottomNavigationView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRevernces();
        if(checkPremision() ==false){// if it dosen't have the premission 
            requestPremision();
            return;
        }
        String projection[]={
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection=MediaStore.Audio.Media.IS_MUSIC+ " !=0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,selection,null,null);

        while (cursor.moveToNext()){
            MediaModel songesData=new MediaModel(cursor.getString(1),
                    cursor.getString(0),cursor.getString(2));
            if(new File(songesData.getPath()).exists())
            songesList.add(songesData);
        }
        if(songesList .size()==0){
            noSonges.setVisibility(View.VISIBLE);
        }else{
            ourSonges.setLayoutManager(new LinearLayoutManager(this));
            ourSonges.setAdapter(new MusicListAdapter(songesList,getApplicationContext()));
        }
        bottom_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        startActivity(new Intent(MainActivity.this,account_Activity.class));
                        return true ;
                    case R.id.home:
                        return true ;
                    case R.id.contact:
                        startActivity(new Intent(MainActivity.this,Main_Contact.class));
                        return true;

                }
                return false;
            }
        });

    }
    private void load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("NIGHT", false);
        if (check_night) {
           mainl.setBackgroundColor(Color.parseColor("#222222"));
        } else {
            mainl.setBackgroundColor(Color.parseColor("#ffffff"));
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
    public void onBackPressed() {
        if(backPressedTime +2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
           backToast = Toast.makeText(this,"press back again to exit",Toast.LENGTH_SHORT);
           backToast.show();
           finish();
        }
        backPressedTime =System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_items,menu);
        MenuItem item =menu.findItem(R.id.search);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        return true;
    }

    private void filter(String newText) {
        ArrayList<MediaModel> filterdList = new ArrayList<>();
        for (MediaModel item : songesList){
            if(item.getName().toLowerCase().startsWith(newText.toLowerCase())){
                filterdList.add(item);
            }

        }listAdapter.filterdList(filterdList);
    }

    // Method for check permission(read external storage) if it's not have it will ask for it
    boolean checkPremision(){

        int result= ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else
            return false;
    }
    // Method for request premission
    void requestPremision(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){// if user refuse to allow
            Toast.makeText(MainActivity.this,"READ PREMISSION REQUIERD PLEAS ALLOW FROM SETTING",
                    Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    @Override
    protected void onResume() {
        load_setting();
        super.onResume();
        if(ourSonges!=null){
            ourSonges.setAdapter(new MusicListAdapter(songesList,getApplicationContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        switch(id){
            case R.id.exit:
                showExitDialog();
                return true ;
            case R.id.settings:
                Intent intentw = new Intent(MainActivity.this,settingActivity.class);
                startActivity(intentw);
                return true ;
            case R.id.cancel:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void showExitDialog(){
            new AlertDialog.Builder(this).
                    setMessage("Are you sure you want to exit ?").setCancelable(false).
                    setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNegativeButton("no",null).show();
        }

        public void getRevernces(){
            bottom_view=findViewById(R.id.homeVav);
            mainl= findViewById(R.id.mainl);
            home = findViewById(R.id.homeVav);
            load_setting();
            bottom_view.setSelectedItemId(R.id.home);
            noSonges=findViewById(R.id.no_songes_tv);
            ourSonges=findViewById(R.id.recycler_view);
            music_title1=findViewById(R.id.music_title);
            listAdapter=new MusicListAdapter(songesList,this);
        }


}