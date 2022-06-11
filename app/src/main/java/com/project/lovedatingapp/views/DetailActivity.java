package com.project.lovedatingapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.databinding.ActivityDetailBinding;
import com.project.lovedatingapp.models.User;

import org.jetbrains.annotations.NotNull;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID_USER = "EXTRA_ID";
    public static final String EXTRA_URL_IMAGE = "EXTRA_URL_IMAGE";

    private ActivityDetailBinding binding;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String id = getIntent().getStringExtra(EXTRA_ID_USER);
        String url = getIntent().getStringExtra(EXTRA_URL_IMAGE);
        Glide.with(this).load(url).into(binding.imgUser);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                binding.tvName.setText(user.getFullName()+", ");
                binding.tvAge.setText(user.getAge()+"");
                binding.tvIntroduce.setText(user.getIntroduce());
                binding.tvAddress.setText(user.getAddress());
                binding.tvAddressDetail.setText(user.getAddress());
                binding.tvStatusYourself.setText(user.getStatus());
                binding.tvFind.setText(user.getObjectFind());
                binding.tvHeight.setText(user.getHeight());
                binding.tvWeight.setText(user.getWeight());
                binding.tvLevel.setText(user.getLevel());
                binding.tvWork.setText(user.getWork());
                binding.tvSalary.setText(user.getSalary());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}