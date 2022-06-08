package com.project.lovedatingapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.lovedatingapp.MainActivity;
import com.project.lovedatingapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(i);
            }
        });
    }
    private void login() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String email = binding.edEmail.getText().toString();
        String password = binding.edPass.getText().toString();

        if (email.isEmpty()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.edEmail.setError("Email is required !");
            binding.edEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.edEmail.setError("Email is not invalid !");
            binding.edEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.edPass.setError("Password is required !");
            binding.edPass.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                binding.progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(LoginActivity.this, "Login Successfully !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Email or password is incorrect !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}