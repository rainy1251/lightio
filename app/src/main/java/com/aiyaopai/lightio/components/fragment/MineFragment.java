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
        viewBinding.tvName.setText("请登录");
        MyToast.show("退出成功");
        LoginActivity.start(getActivity());
        EventBus.getDefault().post(new LoginSuccessEvent(true));

    }

    @Override
    public void onClean() {
        MyToast.show("清理完成");
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
                if (viewBinding.tvName.getText().equals("请登录")) {
                    LoginActivity.start(getActivity());
                }
                break;
            case R.id.tv_edit:
                 SearchPwdActivity.start(getActivity());
                break;
            case R.id.tv_clear:
                title = "清除缓存";
                content = "确定要清除缓存的内容吗？";
                type = 1;
                dialog = new CommonDialog(getActivity(), title, content, type);
                dialog.setOnConfirmListener(this);
                dialog.show();
                break;
            case R.id.tv_update:
                upData(true);
                break;
            case R.id.tv_service:
                title = "呼叫客服？";
                content = "确定要拨打400-021-0916";
                type = 2;
                dialog = new CommonDialog(getActivity(), title, content, type);
                dialog.setOnConfirmListener(this);
                dialog.show();
                break;
            case R.id.tv_exit:
                String access_token = SPUtils.getString(Contents.access_token);
                if (TextUtils.isEmpty(access_token)) {
                    MyToast.show("尚未登录任何账号");
                    return ;
                }
                title = "退出登录";
                content = "确定要退出当前账号?";
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
     * 拨打电话
     */
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:400-021-0916"));
        startActivity(intent);
    }

    /**
     * 获取缓存，设置缓存
     */
    private void setCache() {
        try {
            viewBinding.tvClear.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查更新
     *
     * @param isUpData
     */
    private void upData(boolean isUpData) {
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo != null) {
            if (isUpData) {
                Beta.checkUpgrade();
            } else {
                viewBinding.tvUpdate.setRightText("可更新");
            }
        } else {
            if (isUpData) {
                MyToast.show("已经是最新版本了");
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