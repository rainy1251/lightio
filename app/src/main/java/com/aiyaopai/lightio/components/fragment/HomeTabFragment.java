package com.aiyaopai.lightio.components.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.components.activity.WebActivity;
import com.aiyaopai.lightio.adapter.HomeTabAdapter;
import com.aiyaopai.lightio.base.BaseMvpFragment;
import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.databinding.FragmentHomeTabBinding;
import com.aiyaopai.lightio.mvp.contract.HomeTabContract;
import com.aiyaopai.lightio.mvp.presenter.HomeTabPresenter;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HomeTabFragment extends BaseMvpFragment<HomeTabPresenter, FragmentHomeTabBinding>
        implements HomeTabContract.View {

    private HomeTabPresenter homeTabPresenter;
    private int pageIndex = 1;
    private List<ActivityListBean.ResultBean> dataList;
    private HomeTabAdapter mAdapter;
    private String tagName;

    public static Fragment newInstance(String tagNames) {
        HomeTabFragment homeChildFragment = new HomeTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Contents.TagNames, tagNames);
        homeChildFragment.setArguments(bundle);
        return homeChildFragment;
    }

    @Override
    protected void initView() {
        homeTabPresenter = new HomeTabPresenter(this);
        initRefreshLayout(viewBinding.srlView);
    }

    @Override
    protected void initData() {
        tagName = getArguments().getString(Contents.TagNames);
        homeTabPresenter.activitySearch(pageIndex, tagName);

        dataList = new ArrayList<>();
        mAdapter = new HomeTabAdapter(getActivity(), R.layout.item_home_tab, dataList);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        viewBinding.rvView.setLayoutManager(manager);
        viewBinding.rvView.setAdapter(mAdapter);
        viewBinding.rvView.addItemDecoration(new SpaceItemDecoration(10, 20));
        mAdapter.setEmptyView(R.layout.empty_layout);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<ActivityListBean.ResultBean> data = (List<ActivityListBean.ResultBean>)adapter.getData();
            String url =Contents.ACTIVITY_URL+data.get(position).getId();
            WebActivity.start(getActivity(),url,data.get(position).getName());

        });
    }

    @Override
    public void refresh() {
        super.refresh();
        pageIndex=1;
        dataList.clear();
        homeTabPresenter.activitySearch(pageIndex, tagName);
    }

    @Override
    public void loadMore() {
        super.loadMore();
        pageIndex++;
        homeTabPresenter.activitySearch(pageIndex, tagName);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_tab;
    }

    @Override
    public void onSuccess(ActivityListBean activityListBean) {
        //ArrayList<ActivityListBean.ResultBean> result = activityListBean.getResult();
//        dataList.addAll(result);
//        mAdapter.setDiffNewData(result);
//        mAdapter.notifyDataSetChanged();

    }
}
