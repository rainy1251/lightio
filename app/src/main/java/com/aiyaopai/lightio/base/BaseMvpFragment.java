package com.aiyaopai.lightio.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewbinding.ViewBinding;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.components.activity.LoginActivity;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.MyProgressLoading;
import com.qiniu.android.utils.AsyncRun;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public abstract class BaseMvpFragment<P extends BasePresenter,T extends ViewBinding> extends Fragment implements BaseView {

    protected T viewBinding;
    private Context mContext;
    private MyProgressLoading mLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Type superclass =  getClass().getGenericSuperclass();
        Class<?> cls = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            viewBinding = (T) inflate.invoke(null, inflater, container, false);

            mLoading = new MyProgressLoading(mContext, R.style.DialogStyle);
            initView();
            initData();
        }  catch (Exception e) {
            e.printStackTrace();

        }
        return viewBinding.getRoot();


    }

    protected abstract void initView();

    protected abstract void initData();
    protected abstract int getLayoutId();


    protected P mPresenter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            viewBinding = null;
        }
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
        AsyncRun.runInMain(() -> {
            if (!TextUtils.isEmpty(msg)) {
                MyToast.show(msg);
            }
        });

    }
    public void initRefreshLayout(SmartRefreshLayout refreshLayout) {
        refreshLayout.setDragRate(0.5f);//??????????????????/????????????????????????=????????????
        refreshLayout.setReboundDuration(300);//??????????????????????????????
        refreshLayout.setEnableRefresh(true);//??????????????????????????????
//        if (isLoadMore) {
            refreshLayout.setEnableLoadMore(true);//??????????????????????????????
//        } else {
//            refreshLayout.setEnableLoadMore(false);//??????????????????????????????
//        }
        refreshLayout.setEnableOverScrollBounce(true);//????????????????????????
        refreshLayout.setEnableAutoLoadMore(true);//????????????????????????????????????????????????????????????
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        //?????? Footer ??? ?????????
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
                refresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore();
                loadMore();
            }
        });

    }


    /**
     * ????????????
     */
    public void refresh() {
    }

    /**
     * ??????????????????
     */
    public void loadMore() {

    }

    /**
     * ?????????????????? ??????MVP????????????
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
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
                if (!TextUtils.isEmpty((String) msg.obj)) {
                    MyToast.show((String) msg.obj);
                }
            } else {
                SPUtils.remove(Contents.access_token);
                LoginActivity.start(mContext);
            }
        }
    };
}
