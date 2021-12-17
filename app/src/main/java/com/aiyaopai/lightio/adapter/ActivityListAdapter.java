package com.aiyaopai.lightio.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;


import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.GlideUtils;
import com.aiyaopai.lightio.view.QrCodeDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.king.zxing.util.CodeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ActivityListAdapter extends BaseQuickAdapter<ActivityListBean.ResultBean, BaseViewHolder> {

    private final Context mContext;

    public ActivityListAdapter(Context context, int layoutResId, @Nullable List<ActivityListBean.ResultBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, ActivityListBean.ResultBean data) {
        String beginAt = data.getBeginAt().substring(0, 10).replace("-", ".");
        String endAt = data.getEndAt().substring(0, 10).replace("-", ".");
        vh.setText(R.id.tv_title, data.getTitle());
        GlideUtils.show(mContext, vh.getView(R.id.iv_image), data.getBannerImage());
        vh.setText(R.id.tv_address, "活动地点：\n" + data.getAddress());
        vh.setText(R.id.tv_date, "活动时间：\n" +beginAt+"--"+endAt);
        View view = vh.getView(R.id.tv_share);
        view.setOnClickListener(v -> createQrCode(data.getId()));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createQrCode(String id) {
        Drawable drawable = Objects.requireNonNull(mContext.getResources().getDrawable(R.mipmap.icon));
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bm = bd.getBitmap();
        Bitmap qrCode = CodeUtils.createQRCode(Contents.ACTIVITY_URL + id, 600, bm);
        QrCodeDialog dialog = new QrCodeDialog(mContext, qrCode);
        dialog.show();
    }
}
