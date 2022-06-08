package com.project.lovedatingapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.lovedatingapp.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        binding.btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = binding.edEmail.getText().toString();
        if (email.isEmpty()) {
            binding.edEmail.setError("Email is required !");
            binding.edEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Email is not invalid !");
            binding.edEmail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Check your email !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Something wrong, try again !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}