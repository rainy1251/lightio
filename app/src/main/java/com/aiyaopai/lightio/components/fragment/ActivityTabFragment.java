package com.aiyaopai.lightio.components.fragment;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.components.activity.NoticeActivity;
import com.aiyaopai.lightio.adapter.ActivityListAdapter;
import com.aiyaopai.lightio.base.BaseMvpFragment;
import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.databinding.FragmentActivityTabBinding;
import com.aiyaopai.lightio.event.LoginSuccessEvent;
import com.aiyaopai.lightio.mvp.contract.ActivityTabContract;
import com.aiyaopai.lightio.mvp.presenter.ActivityTabPresenter;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.view.SpaceItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ActivityTabFragment extends BaseMvpFragment<ActivityTabPresenter, FragmentActivityTabBinding>
        implements ActivityTabContract.View {

    private List<ActivityListBean.ResultBean> dataList;
    private ActivityListAdapter mAdapter;
    private ActivityTabPresenter presenter;
    private int pageIndex = 1;
    private String mType;

    public ActivityTabFragment(String type) {
        this.mType = type;
    }

    public ActivityTabFragment() {

    }

    @Override
    protected void initView() {
        presenter = new ActivityTabPresenter();
        presenter.attachView(this);
        initRefreshLayout(viewBinding.srlView);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

        presenter.getList(pageIndex, mType);
        dataList = new ArrayList<>();
        mAdapter = new ActivityListAdapter(getActivity(), R.layout.item_list_tab, dataList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        viewBinding.rvView.setLayoutManager(manager);
        viewBinding.rvView.setAdapter(mAdapter);
        viewBinding.rvView.addItemDecoration(new SpaceItemDecoration(0, 10));
        mAdapter.setEmptyView(R.layout.empty_layout);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), NoticeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Contents.Title, dataList.get(position).getTitle());
            intent.putExtra(Contents.ActivityId, dataList.get(position).getId());
            startActivity(intent);

        });


    }

    @Override
    public void loadMore() {
        super.loadMore();
        pageIndex++;
        presenter.getList(pageIndex, mType);
    }

    @Override
    public void refresh() {
        super.refresh();
        pageIndex = 1;
        dataList.clear();
        presenter.getList(pageIndex, mType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginSuccessEvent event) {
        if (event.isLogin()) {
            pageIndex = 1;
            dataList.clear();
            presenter.getList(pageIndex, mType);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_tab;
    }

    @Override
    public void onSuccess(ActivityListBean bean) {
        ArrayList<ActivityListBean.ResultBean> result = bean.getResult();
        if (pageIndex > 1 && result.size() == 0) {
            MyToast.show("没有更多了");
        }
        dataList.addAll(result);
        mAdapter.setDiffNewData(result);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}