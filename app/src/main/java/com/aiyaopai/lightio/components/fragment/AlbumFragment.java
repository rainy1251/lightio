package com.aiyaopai.lightio.components.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.adapter.HomeTabPagerAdapter;
import com.aiyaopai.lightio.base.BaseFragment;
import com.aiyaopai.lightio.databinding.FragmentActivityListBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends BaseFragment<FragmentActivityListBinding> {

    private final List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void initView(View view) {
        viewBinding.includeList.ivIcon.setVisibility(View.GONE);
        viewBinding.includeList.ivBack.setVisibility(View.GONE);
        viewBinding.includeList.tvToolbarTitle.setVisibility(View.VISIBLE);
        viewBinding.includeList.tvToolbarTitle.setText("活动列表");

        String[] tab_key = getResources().getStringArray(R.array.list_type);
        String[] list_cn_type = getResources().getStringArray(R.array.list_cn_type);

        //添加tab
        for (int i = 0; i < tab_key.length; i++) {
            viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText(list_cn_type[i]));
            mFragments.add( new AlbumTabFragment(tab_key[i]));
        }


        viewBinding.vpView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        HomeTabPagerAdapter mPagerAdapter = new HomeTabPagerAdapter(this,mFragments);
        viewBinding.vpView.setAdapter(mPagerAdapter);

        viewBinding.vpView.setOffscreenPageLimit(3);

        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.vpView
                ,true   , new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(list_cn_type[position]);
            }
        }).attach();
    }

    @Override
    protected void initData() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_list;
    }
}