package com.example.roshan.condroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

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
            Snackbar.make(view, "Enter valid Phone Number\nPassword must be more than 5 characters", Snackbar.LENGTH_LONG).show();
        } else if (numLen != 10) {
            Snackbar.make(view, "Enter valid Phone Number", Snackbar.LENGTH_SHORT).show();
            num.setText("");
        } else if (passLen < 5) {
            Snackbar.make(view, "Password must be more than 5 characters", Snackbar.LENGTH_SHORT).show();
            pass.setText("");
        } else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Credentials", MODE_PRIVATE);
            String passCheck = sharedPreferences.getString("Password", null);
            if (passCheck == null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PhoneNo", phoneNo);
                editor.putString("Password", password);
                editor.apply();
                Intent contactIntent = new Intent(this, UserContacts.class);
                startActivity(contactIntent);
            } else {
                if (!passCheck.equals(password)) {
                    Snackbar.make(view, "Password  doesn't match, Enter valid password!!!", Snackbar.LENGTH_LONG).show();
                    pass.setText("");
                } else {
                    Intent contactIntent = new Intent(this, UserContacts.class);
                    startActivity(contactIntent);
                }
             }

        }
    }
}