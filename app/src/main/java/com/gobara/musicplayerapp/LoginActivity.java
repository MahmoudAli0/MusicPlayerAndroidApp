package com.gobara.musicplayerapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    TextView textView;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView google_button;

    SharedPreferences sharedPreferences;

    // create shared prefrences

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASS = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // requestWindowFeature(Window.FEATURE_NO_TITLE);


        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        textView = (TextView) findViewById(R.id.btnsignin);
        DB = new DBHelper(this);

        // Shared Prefrencess
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                // when click a button put data on shared preferences to account
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString(KEY_NAME,username.getText().toString());
                editor.putString(KEY_PASS,password.getText().toString());
                editor.apply();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkUserNamepassword(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(LoginActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), registerActivity.class);
                startActivity(intent);
            }
        });


        // sign in with google facebook twitter
        google_button = findViewById(R.id.sing_google_btn);


        //google
        gso = new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct =GoogleSignIn.getLastSignedInAccount(this);
        if (acct !=null){
            navigateToSecondActivity();
        }


        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signin();
            }
        });



        //facebook

    }



    //google
    void signin(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    // google
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this,"some thing went wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //google
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);

    }


}