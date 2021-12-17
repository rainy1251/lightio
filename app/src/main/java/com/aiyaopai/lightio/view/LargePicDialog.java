package com.aiyaopai.lightio.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LargePicDialog extends Dialog {

    private final Context mContext;

    public LargePicDialog(Context context, String url) {
        super(context);
        this.mContext = context;
        setCustomDialog(url);
    }

    @SuppressLint("InflateParams")
    private void setCustomDialog(String url) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_large_pic_layout, null);
        ImageView ivLarge = mView.findViewById(R.id.iv_large);
        ImageView ivClose= mView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ProgressBar bar = mView.findViewById(R.id.pb_bar);
        bar.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions())
                .into(new DrawableImageViewTarget(ivLarge){
                    @Override
                    public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        bar.setVisibility(View.GONE);
                    }

                });

        super.setContentView(mView);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(getWindow()).getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        windowDeploy();

        getWindow().setAttributes(layoutParams);

    }

    public void windowDeploy() {
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.trans_color); //设置对话框背景为透明
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

}