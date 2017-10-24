package com.example.roshan.condroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Permissions extends AppCompatActivity {

    private static final int Permission_All = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasPermissions(this, permissions)) {
                ActivityCompat.requestPermissions(this, permissions, Permission_All);
            } else
                start();
        } else
            start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permission_All) {
            for (int i : grantResults) {
                if(i != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.loginLayout), "Please grant all permissions", Snackbar.LENGTH_LONG).show();
                    hasPermissions(this, permissions);
                    return;
                }
            }
            start();
        }
    }

    private void start() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private static boolean hasPermissions(Context context, String[] permissions) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } else
            return true;
    }

}
