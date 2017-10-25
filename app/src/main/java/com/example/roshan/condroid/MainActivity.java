package com.example.roshan.condroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    finish();
                    Intent intent = new Intent(getApplicationContext(), Permissions.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }
}
