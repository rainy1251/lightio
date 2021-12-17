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

    public static void showQiNiu(Context context, ImageView imageView, Object url) {
        Glide.with(context).load(url + "?imageView2/1/w/200/h/200/interlace/1")
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