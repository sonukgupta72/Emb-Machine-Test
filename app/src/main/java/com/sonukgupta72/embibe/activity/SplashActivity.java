package com.sonukgupta72.embibe.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sonukgupta72.embibe.R;

public class SplashActivity extends AppCompatActivity {

    private static final long POST_DELAY_TIME_S = 2000L;
    private boolean isAlive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAlive = true;
        setPostDelay();
    }

    private void setPostDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callNextActivity();
            }
        }, POST_DELAY_TIME_S);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAlive = false;
    }

    private void callNextActivity() {
        if (!isAlive)  {
            return;
        }

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
