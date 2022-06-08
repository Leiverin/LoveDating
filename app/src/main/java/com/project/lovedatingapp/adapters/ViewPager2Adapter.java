package com.project.lovedatingapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.lovedatingapp.fragment.ImageFragment;
import com.project.lovedatingapp.fragment.VideoFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Adapter extends FragmentStateAdapter {
    private List<Fragment> list;

    public ViewPager2Adapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        list = new ArrayList<>();
        list.add(ImageFragment.newInstance());
        list.add(VideoFragment.newInstance());
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
