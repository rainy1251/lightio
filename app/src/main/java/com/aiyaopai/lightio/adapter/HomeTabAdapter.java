package com.aiyaopai.lightio.adapter;

import android.content.Context;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.AlbumListBean;
import com.aiyaopai.lightio.util.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HomeTabAdapter extends BaseQuickAdapter<AlbumListBean.ResultBean, BaseViewHolder> {

    private Context mContext;

    public HomeTabAdapter(Context context, int layoutResId, @Nullable List<AlbumListBean.ResultBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, AlbumListBean.ResultBean data) {
        vh.setText(R.id.tv_title, data.getName());
        GlideUtils.show(mContext, vh.getView(R.id.iv_pic), data
                .getBanner());
        vh.setText(R.id.tv_address, data.getLocation().getAddress());
       // vh.setText(R.id.tv_date, DateFormatUtils.formatDate(data.getBeginAt()));
    }

}
