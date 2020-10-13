package com.coderpakistan.learningbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;

public class SplashScreenActivity extends AppCompatActivity {
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        userSessionManager = new UserSessionManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userSessionManager.isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, 4000);
    }
}
