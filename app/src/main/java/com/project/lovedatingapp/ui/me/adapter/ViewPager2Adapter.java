package com.project.lovedatingapp.ui.me.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.lovedatingapp.ui.me.view_pager2.FavoriteFragment;
import com.project.lovedatingapp.ui.me.view_pager2.PhotoFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Adapter extends FragmentStateAdapter {

    private List<Fragment> mListFragment;

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mListFragment = new ArrayList<>();
        mListFragment.add(PhotoFragment.newInstance());
        mListFragment.add(FavoriteFragment.newInstance());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return mListFragment.size();
    }
}
