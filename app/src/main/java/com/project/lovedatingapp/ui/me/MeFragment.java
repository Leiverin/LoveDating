package com.project.lovedatingapp.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.models.Image;
import com.project.lovedatingapp.models.User;
import com.project.lovedatingapp.models.UserCategory;
import com.project.lovedatingapp.views.SettingActivity;
import com.project.lovedatingapp.databinding.FragmentMeBinding;
import com.project.lovedatingapp.ui.me.adapter.ViewPager2Adapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;
    private ViewPager2Adapter viewPager2Adapter;
    private DatabaseReference reference;
    private FragmentMeBinding binding;
    private FirebaseUser firebaseUser;
    private List<Image> mListImage;
    private UserCategory user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        viewPager2Adapter = new ViewPager2Adapter((FragmentActivity) getContext());
        binding.viewPager2.setAdapter(viewPager2Adapter);

        int tabImage[] = {
                R.drawable.ic_baseline_grid_on_24,
                R.drawable.ic_baseline_favorite_24
        };
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, (tab, pos) -> {
            tab.setIcon(tabImage[pos]);
        }).attach();

        // Get information current user
        mListImage = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User userChild = snapshot.getValue(User.class);
                reference.child("images").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        mListImage = new ArrayList<>();
                        for (DataSnapshot dsChild : snapshot.getChildren()) {
                            HashMap<String,Object> map = (HashMap<String, Object>) dsChild.getValue();
                            String idImg = (String) map.get("id");
                            String urlImg = (String) map.get("imageURL");
                            mListImage.add(new Image(idImg, urlImg));
                        }
                        user = new UserCategory(userChild, mListImage);
                        binding.tvFullName.setText(user.getUser().getFullName());
                        Glide.with(getContext()).load(user.getImages().get(0).getUrl()).into(binding.imgAvatar);
                        binding.tvUser.setText(user.getUser().getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Log.d("zzzzz", new Gson().toJson(user));

        binding.imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}