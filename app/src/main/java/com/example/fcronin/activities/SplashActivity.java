package com.example.fcronin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.example.fcronin.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AnimateHorizontalProgressBar progressBar = (AnimateHorizontalProgressBar) findViewById(R.id.animate_progress_bar);
        progressBar.setMax(1500);
        progressBar.setProgressWithAnim(1500);

        final Helper helper = new Helper(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, helper.getLoggedInUser() != null ? MainActivity.class : SignInActivity.class));
                finish();
            }
        }, 1500);
    }
    }
}