package com.aiyaopai.lightio.adapter;

import android.content.Context;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.GlideUtils;
import com.aiyaopai.lightio.util.MyLog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PicAdapter extends BaseQuickAdapter<PicBean, BaseViewHolder> {

    private Context mContext;
    private boolean handType;

    public PicAdapter(Context context, int layoutResId, @Nullable List<PicBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, PicBean bean) {
        vh.setText(R.id.tv_name, bean.getPicName());
        vh.setText(R.id.tv_size, FilesUtil.formatSize(bean.getPicSize()));
        if (bean.getPicPath().contains("http")) {
            GlideUtils.showSta(mContext, vh.getView(R.id.iv_pic), bean.getPicPath());
        } else {
            GlideUtils.show(mContext, vh.getView(R.id.iv_pic), bean.getPicPath());
        }

        switch (bean.getStatus()) {
            case -1:
                vh.setVisible(R.id.tv_check, false);
                vh.setText(R.id.tv_state, "上传失败");
                vh.setBackgroundResource(R.id.tv_state, R.color.color_error);
                break;
            case 0:
                vh.setVisible(R.id.tv_check, handType);
                if (bean.getProgress() > 0) {
                    vh.setText(R.id.tv_state, bean.getProgress() + "%");
                } else {
                    vh.setText(R.id.tv_state, "等待上传");
                }
                vh.setBackgroundResource(R.id.tv_state, R.color.yp_blue);
                break;
            case 1:
                vh.setText(R.id.tv_state, "已上传");
                vh.setVisible(R.id.tv_check, false);
                vh.setBackgroundResource(R.id.tv_state, R.color.color_success);
                break;
        }

    }


    public boolean isHandType() {
        return handType;
    }

    public void setHandType(boolean handType) {
        this.handType = handType;
        notifyDataSetChanged();
    }


}
