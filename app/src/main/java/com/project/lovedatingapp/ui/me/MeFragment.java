package com.project.lovedatingapp.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.lovedatingapp.R;
import com.project.lovedatingapp.views.SettingActivity;
import com.project.lovedatingapp.databinding.FragmentMeBinding;
import com.project.lovedatingapp.ui.me.adapter.ViewPager2Adapter;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;
    private ViewPager2Adapter viewPager2Adapter;

    private FragmentMeBinding binding;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
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