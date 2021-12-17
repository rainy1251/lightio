package com.aiyaopai.lightio.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {
    protected T viewBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(this.getLayoutId(), container, false);
//        return view;

        Type superclass = getClass().getGenericSuperclass();
        Class<?> cls = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            viewBinding = (T) inflate.invoke(null, inflater, container, false);
           initView(viewBinding.getRoot());
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewBinding.getRoot();


    }

    protected abstract void initView(View view);

    protected abstract void initData();

    //    protected abstract void initView(View view);
    protected abstract int getLayoutId();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;

    }
}
