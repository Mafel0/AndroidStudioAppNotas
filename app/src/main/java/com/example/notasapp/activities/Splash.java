package com.example.notasapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.notasapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent principal = new Intent();
                principal.setClass(Splash.this, MainActivity.class);
                Splash.this.startActivity(principal);
                Splash.this.finish();
            }
        }, 5000);
    }
}