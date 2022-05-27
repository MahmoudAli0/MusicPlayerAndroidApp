package com.gobara.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup;
    DBHelper DB;
    SharedPreferences sharedPreferences;

    // create shared prefrences

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASS = "pass";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        DB = new DBHelper(this);

        // Shared Prefrencess
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                // when click a button put data on shared preferences to account
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString(KEY_NAME,username.getText().toString());
                editor.putString(KEY_PASS,password.getText().toString());
                editor.apply();

                if(user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(registerActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkUserName(user);
                        if(checkuser==false){
                            Boolean insert = DB.inseartData(user, pass);
                            if(insert==true){
                                Toast.makeText(registerActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(registerActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(registerActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(registerActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                } }
        });

    }
}