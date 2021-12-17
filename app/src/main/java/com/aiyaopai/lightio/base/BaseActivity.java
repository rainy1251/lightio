package com.aiyaopai.lightio.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.aiyaopai.lightio.util.BindingUtil;
import com.gyf.immersionbar.ImmersionBar;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected T viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = BindingUtil.createBinding(this, 0);
        if (viewBinding == null) {
            throw new NullPointerException("binding is null");
        }
        setContentView(((ViewBinding) viewBinding).getRoot());
        initView();
        initData();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinding = null;
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

}
