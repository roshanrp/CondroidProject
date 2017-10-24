package com.example.roshan.condroid;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contacts);

    }

    public void SubmitContacts (View view) {
        EditText num1 = (EditText) findViewById(R.id.contact1);
        EditText num2 = (EditText) findViewById(R.id.contact2);
        EditText num3 = (EditText) findViewById(R.id.contact3);
        EditText num4 = (EditText) findViewById(R.id.contact4);

        String phoneNo1 = num1.getText().toString();
        String phoneNo2 = num2.getText().toString();
        String phoneNo3 = num3.getText().toString();
        String phoneNo4 = num4.getText().toString();

        int num1len = num1.getText().length();
        int num2len = num2.getText().length();
        int num3len = num3.getText().length();
        int num4len = num4.getText().length();

        if (num1len != 10) {
            Snackbar.make(view, "Please Enter 1st Number Valid", Snackbar.LENGTH_SHORT).show();
            num1.setText("");
        } else if (num2len != 10) {
            Snackbar.make(view, "Please Enter 2nd Number Valid", Snackbar.LENGTH_SHORT).show();
            num2.setText("");
        } else if (num3len != 10) {
            Snackbar.make(view, "Please Enter 3rd Number Valid", Snackbar.LENGTH_SHORT).show();
            num3.setText("");
        } else if (num4len != 10) {
            Snackbar.make(view, "Please Enter 4th Number Valid", Snackbar.LENGTH_SHORT).show();
            num4.setText("");
        } else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Credentials", MODE_APPEND);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Contact1", phoneNo1);
            editor.putString("Contact2", phoneNo2);
            editor.putString("Contact3", phoneNo3);
            editor.putString("Contact4", phoneNo4);
            editor.apply();
        }





    }
}
