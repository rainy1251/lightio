package com.aiyaopai.lightio.util;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BindingUtil {

    /**
     * activity 的 创建方式
     *
     * @param <Binding> Binding
     * @param appCompatActivity AppCompatActivity
     * @param index 参数位置
     * @return <Binding> Binding
     */
    public static <Binding> Binding createBinding(@NonNull AppCompatActivity
                                                          appCompatActivity, int index) {
        LayoutInflater layoutInflater = appCompatActivity.getLayoutInflater();
        Class<? extends ViewBinding> viewBindClass =
          findViewBinding(appCompatActivity,index);
      
        return createViewBinding(viewBindClass, layoutInflater);
    }
    /**
     * 得到 Binding 的实例
     *
     * @param viewBindingClass Class<? extends ViewBinding>
     * @param layoutInflater LayoutInflater
     * @param <Binding> Binding
     * @return Binding
     */
    @SuppressWarnings("unchecked")
    private static <Binding> Binding createViewBinding(Class<? extends ViewBinding> viewBindingClass, LayoutInflater layoutInflater) {

        try {
            Method method = viewBindingClass.getMethod("inflate", LayoutInflater.class);

            ViewBinding viewBinding = (ViewBinding) method.invoke(viewBindingClass,
                    layoutInflater);

            return (Binding) viewBinding;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 找到 ViewBinding 的 class
     *
     * @param object obj : activity
     * @return Class<? extends ViewBinding>
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends ViewBinding> findViewBinding(Object object, int index) {
        //获取到 BaseBindingActivity
        Type type = object.getClass().getGenericSuperclass();
        if (type == null) {
            return null;
        }
        // 获取 BaseBindingActivity 的 所包含的泛型参数列表，这里是核心。
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();

        if (types.length == 0) {
            return null;
        }

        //目前泛型只有一个，所以拿第0 个
        return (Class<? extends ViewBinding>) types[index];
    }
}