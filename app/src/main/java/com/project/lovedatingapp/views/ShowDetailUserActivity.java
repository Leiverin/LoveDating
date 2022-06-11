package com.project.lovedatingapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.adapters.ViewPager2Adapter;
import com.project.lovedatingapp.models.User;


import org.jetbrains.annotations.NotNull;

public class ShowDetailUserActivity extends AppCompatActivity {


    private ViewPager2Adapter adapter;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;
    private ImageView btnFinish;
    private ImageView btnFavourite;
    private ImageView btnMessage;
    private TextView txtFullname;
    private TextView txtAge;
    private TextView txtGender;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    public static final String KEY_SHOW="KEY_SHOW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_user);
        btnFinish = findViewById(R.id.btnFinish);
        btnFavourite = findViewById(R.id.btnFavourite);
        btnMessage = findViewById(R.id.btnMessage);
        txtFullname = findViewById(R.id.txtFullname);
        txtAge = findViewById(R.id.txtAge);
        txtGender = findViewById(R.id.txtGender);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        intent = getIntent();
        String userId = intent.getStringExtra(KEY_SHOW);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String upperString = user.getUsername().substring(0, 1).toUpperCase() + user.getUsername().substring(1).toLowerCase();
                txtFullname.setText(upperString);
                txtAge.setText(","+user.getAge());
                txtGender.setText(","+user.getGender());

//                if(user.getImageURL().equals("default")){
//                    circleImage.setImageResource(R.mipmap.ic_launcher);
//                }else {
//                    Gilde.with(MessageActivity.this).load(user.getImageURL()).into(circleImage);
//                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDetailUserActivity.this, MessageActivity.class);
                intent.putExtra(MessageActivity.KEY, userId);
                startActivity(intent);
            }
        });

        adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);
        int tabImage[] = {
                R.drawable.ic_baseline_grid_on_24,
                R.drawable.ic_baseline_favorite
        };
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setIcon(tabImage[position]);
        }).attach();



    }

}