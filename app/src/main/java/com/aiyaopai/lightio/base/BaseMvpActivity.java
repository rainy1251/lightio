package com.aiyaopai.lightio.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewbinding.ViewBinding;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.components.activity.LoginActivity;
import com.aiyaopai.lightio.util.BindingUtil;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.MyProgressLoading;
import com.gyf.immersionbar.ImmersionBar;
import com.qiniu.android.utils.AsyncRun;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public abstract class BaseMvpActivity<P extends BasePresenter,T extends ViewBinding> extends AppCompatActivity implements BaseView {

    protected P mPresenter;
    private MyProgressLoading mLoading;
    private Context mContext;
    protected T viewBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =this;

        viewBinding = BindingUtil.createBinding(this,1);
        if (viewBinding == null) {
            throw new NullPointerException("binding is null");
        }
        mLoading = new MyProgressLoading(this, R.style.DialogStyle);
        setContentView(((ViewBinding) viewBinding).getRoot());
        initView();
        initData();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        viewBinding = null;
        super.onDestroy();
    }
    /**
     * 设置布局
     */
    public abstract int getLayoutId();
    /**
     * 初始化视图
     */
    public abstract void initView();

    protected abstract void initData();
    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <P>
     * @return
     */
    @Override
    public <P> AutoDisposeConverter<P> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.dismiss();
    }

    @Override
    public void onError(String msg) {
        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onAgainLogin() {
        mHandler.sendEmptyMessage(1);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                MyToast.show((String) msg.obj);
            } else {
                SPUtils.remove(Contents.access_token);
                LoginActivity.start(mContext);
            }
        }
    };

}
