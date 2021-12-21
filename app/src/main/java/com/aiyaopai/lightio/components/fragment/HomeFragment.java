package com.aiyaopai.lightio.components.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.components.activity.WebActivity;
import com.aiyaopai.lightio.adapter.HomeTabPagerAdapter;
import com.aiyaopai.lightio.base.BaseMvpFragment;
import com.aiyaopai.lightio.bean.BannerBean;

import com.aiyaopai.lightio.databinding.FragmentHomeBinding;
import com.aiyaopai.lightio.mvp.contract.HomeContract;
import com.aiyaopai.lightio.mvp.presenter.HomePresenter;
import com.aiyaopai.lightio.util.ApiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.transformer.DepthPageTransformer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseMvpFragment<HomePresenter, FragmentHomeBinding> implements
        HomeContract.View , View.OnClickListener {

    private final List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void initView() {
        viewBinding.includeHome.ivIcon.setVisibility(View.VISIBLE);
        viewBinding.includeHome.ivBack.setVisibility(View.GONE);
        viewBinding.includeHome.tvToolbarTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mPresenter = new HomePresenter(this);
        mPresenter.banner();


        String[] tab_key = getResources().getStringArray(R.array.home_tab_key);
        String[] tab_value = getResources().getStringArray(R.array.home_tab_value);

        //添加tab
        for (int i = 0; i < tab_key.length; i++) {
            viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText(tab_key[i]));
            mFragments.add(HomeTabFragment.newInstance(tab_value[i]));
        }

        viewBinding.viewpage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        HomeTabPagerAdapter mPagerAdapter = new HomeTabPagerAdapter(this,mFragments);
        viewBinding.viewpage.setAdapter(mPagerAdapter);

        viewBinding.viewpage.setOffscreenPageLimit(3);

        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewpage
             ,true   , new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(tab_key[position]);
            }
        }).attach();
        viewBinding.tvClass1.setOnClickListener(this);
        viewBinding.tvClass2.setOnClickListener(this);
        viewBinding.tvClass3.setOnClickListener(this);
        viewBinding.tvClass4.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onSuccess(List<BannerBean> banners) {
        viewBinding.banner.setAdapter(new BannerImageAdapter<BannerBean>(banners) {

            @Override
            public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data.getCover())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebActivity.start(getActivity(), data.getLink(),data.getTitle());

                    }
                });
            }
        })
                .setPageTransformer(new DepthPageTransformer())
                .addBannerLifecycleObserver(this)
                .setIndicator(new RoundLinesIndicator(getActivity()));
    }

    public void skip(View view) {


    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_class1:

                WebActivity.start(getActivity(), ApiUtils.Home_Class_One,"");
                break;
            case R.id.tv_class2:

                WebActivity.start(getActivity(), ApiUtils.Home_Class_Two,"");
                break;
            case R.id.tv_class3:

                WebActivity.start(getActivity(), ApiUtils.Home_Class_Three,"");
                break;
            case R.id.tv_class4:

                WebActivity.start(getActivity(), ApiUtils.Home_Class_Four,"");
                break;
        }
    }
}
