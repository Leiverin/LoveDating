package com.project.lovedatingapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.lovedatingapp.MainActivity;
import com.project.lovedatingapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Thread.sleep(3000);
                    if (firebaseUser != null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }
}