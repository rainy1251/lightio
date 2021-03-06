package com.aiyaopai.lightio.components.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.UserBean;
import com.aiyaopai.lightio.components.activity.LoginActivity;
import com.aiyaopai.lightio.components.activity.SearchPwdActivity;
import com.aiyaopai.lightio.base.BaseMvpFragment;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.SignInBean;
import com.aiyaopai.lightio.databinding.FragmentMineBinding;
import com.aiyaopai.lightio.event.LoginSuccessEvent;
import com.aiyaopai.lightio.mvp.contract.MineContract;
import com.aiyaopai.lightio.mvp.presenter.MinePresenter;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.GlideUtils;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.CacheDataManager;
import com.aiyaopai.lightio.view.CommonDialog;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MineFragment extends BaseMvpFragment<MinePresenter, FragmentMineBinding> implements
        MineContract.View, View.OnClickListener, CommonDialog.OnConfirmListener {

    private MinePresenter presenter;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        presenter = new MinePresenter(this);

        presenter.getUserInfo();
        setCache();
        upData(false);
        initListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onSuccess(UserBean bean) {

        viewBinding.tvName.setText(bean.getNickname());
        GlideUtils.showHead(getActivity(), viewBinding.ivImg, bean.getAvatar());
        SPUtils.save(Contents.Id,bean.getId());

    }

    @Override
    public void onSignOutSuccess() {

        SPUtils.save(Contents.access_token, "");
        SPUtils.save(Contents.refresh_token, "");
        SPUtils.save(Contents.Id, "");
        SPUtils.save(Contents.tokenBeginAt, "");
        viewBinding.tvName.setText("?????????");
        MyToast.show("????????????");
        LoginActivity.start(getActivity());
        EventBus.getDefault().post(new LoginSuccessEvent(true));

    }

    @Override
    public void onClean() {
        MyToast.show("????????????");
        viewBinding.tvClear.setRightText("0.0B");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginSuccessEvent event) {
        String id = SPUtils.getString(Contents.Id);
        if (event.isLogin()) {
            presenter.getUserInfo();
        }
    }

    public void initListener() {
        viewBinding.tvName.setOnClickListener(this);
        viewBinding.tvEdit.setOnClickListener(this);
        viewBinding.tvClear.setOnClickListener(this);
        viewBinding.tvUpdate.setOnClickListener(this);
        viewBinding.tvService.setOnClickListener(this);
        viewBinding.tvExit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String title;
        String content;
        int type;
        CommonDialog dialog;
        switch (v.getId()) {

            case R.id.tv_name:
                if (viewBinding.tvName.getText().equals("?????????")) {
                    LoginActivity.start(getActivity());
                }
                break;
            case R.id.tv_edit:
                 SearchPwdActivity.start(getActivity());
                break;
            case R.id.tv_clear:
                title = "????????????";
                content = "????????????????????????????????????";
                type = 1;
                dialog = new CommonDialog(getActivity(), title, content, type);
                dialog.setOnConfirmListener(this);
                dialog.show();
                break;
            case R.id.tv_update:
                upData(true);
                break;
            case R.id.tv_service:
                title = "???????????????";
                content = "???????????????400-021-0916";
                type = 2;
                dialog = new CommonDialog(getActivity(), title, content, type);
                dialog.setOnConfirmListener(this);
                dialog.show();
                break;
            case R.id.tv_exit:
                String access_token = SPUtils.getString(Contents.access_token);
                if (TextUtils.isEmpty(access_token)) {
                    MyToast.show("????????????????????????");
                    return ;
                }
                title = "????????????";
                content = "????????????????????????????";
                type = 3;
                dialog = new CommonDialog(getActivity(), title, content, type);
                dialog.setOnConfirmListener(this);
                dialog.show();
                break;

        }
    }

    @Override
    public void confirm(int type) {
        switch (type) {
            case 1:
                presenter.cleanCache();
                break;
            case 2:
                callPhone();
                break;
            case 3:
                presenter.signOut();
                break;
        }
    }

    /**
     * ????????????
     */
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:400-021-0916"));
        startActivity(intent);
    }

    /**
     * ???????????????????????????
     */
    private void setCache() {
        try {
            viewBinding.tvClear.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????
     *
     * @param isUpData
     */
    private void upData(boolean isUpData) {
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo != null) {
            if (isUpData) {
                Beta.checkUpgrade();
            } else {
                viewBinding.tvUpdate.setRightText("?????????");
            }
        } else {
            if (isUpData) {
                MyToast.show("????????????????????????");
            } else {
                String version = RetrofitClient.getVersion(getActivity());
                viewBinding.tvUpdate.setRightText("V"+version);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}