package com.project.lovedatingapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.lovedatingapp.databinding.ActivityRegisterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String email = binding.edEmail.getText().toString();
        String password = binding.edPass.getText().toString();
        String age = binding.edAge.getText().toString();
        String username = binding.edUser.getText().toString();
        //

        if (username.isEmpty()) {
            binding.edUser.setError("Username is required!");
            binding.edUser.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            binding.edEmail.setError("Email is required !");
            binding.edEmail.requestFocus();

            return;
        }
        if(password.isEmpty()){
            binding.edPass.setError("Password is required!");
            binding.edPass.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            binding.edAge.setError("Username is required!");
            binding.edAge.requestFocus();
            return;
        }
       else if (password.length() < 6) {
            binding.edPass.setError("Min password length  must be 6 characters!");
            binding.edPass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Email provide valid email");
            binding.edEmail.requestFocus();
            return;
        }
        //
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userId);
                            map.put("username", username.toLowerCase());
                            map.put("email", email.toLowerCase());
                            map.put("password", password);
                            map.put("age", age);
                            map.put("search", username.toLowerCase());

                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        binding.edPass.setText("");
                                        binding.edUser.setText("");
                                        binding.edEmail.setText("");
                                        binding.edAge.setText("");

                                        Toast.makeText(RegisterActivity.this, "Sucesfully !!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed !!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

}