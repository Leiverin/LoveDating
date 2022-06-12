package com.project.lovedatingapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.lovedatingapp.MainActivity;
import com.project.lovedatingapp.databinding.ActivitySettingBinding;
import com.project.lovedatingapp.utils.Common;


public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        setSupportActionBar(binding.toobar);
        getSupportActionBar().setTitle("");

        binding.toobarTitle.setText("Setting");
        binding.cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Common.user = null;
                Common.mListUserCategory = null;
                MainActivity.mainActivity.finish();
                finish();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));

            }
        });


    }
}