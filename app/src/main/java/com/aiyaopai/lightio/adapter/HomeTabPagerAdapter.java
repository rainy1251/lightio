package com.aiyaopai.lightio.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeTabPagerAdapter extends FragmentStateAdapter{


    private List<Fragment> mFragment;

    public HomeTabPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public HomeTabPagerAdapter(@NonNull @NotNull Fragment fragment,List<Fragment> tabs) {
        super(fragment);
        mFragment = tabs;
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragment.size();
    }
}
