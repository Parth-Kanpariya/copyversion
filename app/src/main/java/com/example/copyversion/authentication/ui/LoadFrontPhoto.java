package com.example.copyversion.authentication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.copyversion.MainActivity;
import com.example.copyversion.R;

public class LoadFrontPhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_front_photo);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(LoadFrontPhoto.this, MainActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }, 1000);

    }
}