package com.gobara.musicplayerapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gobara.musicplayerapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

public class Main_Contact extends AppCompatActivity {
    private static final int REQ_PERMISSION_READ_CONTACT = 1;
    ActivityMainBinding binding;
    ArrayList<ContactItem> contacts;
    ProgressBar PB;
    BottomNavigationView homeNav;
    ConstraintLayout contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PB = (ProgressBar)findViewById(R.id.pb);
        homeNav=findViewById(R.id.homeVav);
        contact= findViewById(R.id.contacctId);
        homeNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        startActivity(new Intent(Main_Contact.this,account_Activity.class));
                        overridePendingTransition(0,0);
                        return true ;
                    case R.id.contact:
                        return true ;
                    case R.id.home:
                        startActivity(new Intent(Main_Contact.this,MainActivity.class));
                        return true;

                }
                return false;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQ_PERMISSION_READ_CONTACT);
        } else {
            new MyTask().execute();
        }
    }
    @SuppressLint("Range")
    private void readContacts() {
        contacts = new ArrayList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex("_ID"));
                Uri cUri = ContactsContract.Data.CONTENT_URI;
                Cursor contactCurser = getContentResolver().query(cUri, null, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(id)}, null);
                String displayName = "";
                String mobilePhone = "";
                String photoPath = "" + com.google.android.material.R.drawable.abc_ic_arrow_drop_right_black_24dp; // Photo path
                String contactNumbers = "";
                if (contactCurser.moveToFirst()) {
                    mobilePhone = contactCurser.getString(contactCurser
                            .getColumnIndex("data1"));
                    String name=contactCurser.getString(contactCurser.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contactNumbers += "Contact : "
                            + name + " " + mobilePhone;

                    contacts.add(new ContactItem(Long.toString(id), displayName, contactNumbers, photoPath));
                }


            } while (cursor.moveToNext());
        }
    }
    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            readContacts();
            return null;
        }

      @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            populateDataIntoRecyclerView(contacts);
        }
    }

    private void populateDataIntoRecyclerView(ArrayList<ContactItem> items) {
        ContactsAdapter adapter = new ContactsAdapter(items);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION_READ_CONTACT && grantResults.length > 0) {
            new MyTask().execute();
        }
    }
}
