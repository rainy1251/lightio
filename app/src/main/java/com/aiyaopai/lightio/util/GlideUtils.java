package com.aiyaopai.lightio.util;

import android.content.Context;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.aiyaopai.lightio.R;


public class GlideUtils {

    public static void show(Context context, ImageView imageView, Object url) {
        Glide.with(context).load(url)
                .apply(new RequestOptions())
                .into(imageView);
    }

    public static void showSta(Context context, ImageView imageView, Object url) {
        Glide.with(context).load(url+"&download=YWEuanBn&type=r80ws")
                .placeholder(R.drawable.icon)
                .dontAnimate()
                .into(imageView);
    }

    public static void showHead(Context context, ImageView imageView, Object url) {
        Glide.with(context).load(url)
                .apply(new RequestOptions().error(R.drawable.icon).circleCrop())
                .into(imageView);
    }
}