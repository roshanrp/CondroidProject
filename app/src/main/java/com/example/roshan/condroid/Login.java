package com.example.roshan.condroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView text;
    private static final int Permission_All = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View view) {

        EditText num = (EditText) findViewById(R.id.phoneNo);
        EditText pass = (EditText) findViewById(R.id.password);
        String phoneNo = num.getText().toString();
        int numLen = num.getText().length();
        String password = pass.getText().toString();
        int passLen = pass.getText().length();

        if (numLen != 10 && passLen < 5) {
           // Toast.makeText(this, (CharSequence) "Enter valid Phone Number\nPassword must be more than 5 characters", Toast.LENGTH_SHORT).show();
            Snackbar.make(view, (CharSequence) "Enter valid Phone Number\nPassword must be more than 5 characters", Snackbar.LENGTH_LONG).show();
        } else if (numLen != 10) {
            Snackbar.make(view, (CharSequence) "Enter valid Phone Number", Snackbar.LENGTH_SHORT).show();
            num.setText("");
        } else if (passLen < 5) {
            Snackbar.make(view, (CharSequence) "Password must be more than 5 characters", Snackbar.LENGTH_SHORT).show();
            pass.setText("");
        } else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Credentials", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("PhoneNo", phoneNo);
            editor.putString("Password", password);
            editor.commit();
           // this.myDBHandler.enterUserDetails(phoneNo, password);
            Intent loginIntent = new Intent(this, UserContacts.class);
            startActivity(loginIntent);
        }
    }



}