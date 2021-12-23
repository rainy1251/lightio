package com.aiyaopai.lightio.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;


import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.DateFormatUtils;
import com.aiyaopai.lightio.util.GlideUtils;
import com.aiyaopai.lightio.view.QrCodeDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.king.zxing.util.CodeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ActivityListAdapter extends BaseQuickAdapter<AlbumListBean.ResultBean, BaseViewHolder> {

    private final Context mContext;

    public ActivityListAdapter(Context context, int layoutResId, @Nullable List<AlbumListBean.ResultBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, AlbumListBean.ResultBean data) {
        String beginAt = DateFormatUtils.formatMils( data.getBeginAt()*1000);
        String endAt = DateFormatUtils.formatMils(data.getEndAt()*1000);
        vh.setText(R.id.tv_title, data.getName());
        GlideUtils.show(mContext, vh.getView(R.id.iv_image), data.getBanner());
        vh.setText(R.id.tv_address, "直播地点：\n" + data.getLocation().getAddress());
        vh.setText(R.id.tv_date, "直播时间：\n" +beginAt+"~"+endAt);
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
