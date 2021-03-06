package com.aiyaopai.lightio.components.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseMvpActivity;
import com.aiyaopai.lightio.bean.OriginalPicBean;
import com.aiyaopai.lightio.databinding.ActivityNoticeBinding;
import com.aiyaopai.lightio.mvp.contract.NoticeContract;
import com.aiyaopai.lightio.mvp.presenter.NoticePresenter;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.AppDB;
import com.aiyaopai.lightio.view.CommonDialog;

import java.util.ArrayList;

public class NoticeActivity extends BaseMvpActivity<NoticePresenter, ActivityNoticeBinding>
        implements NoticeContract.View, View.OnClickListener, CommonDialog.OnConfirmListener {

    private NoticePresenter presenter;
    private String photographerId;
    private String albumId;
    private int pageIndex = 0;
    private String title;
    private int total;
    private CommonDialog dialog;
    private String token;
    private int isEnter = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    public void initView() {

        viewBinding.includeNotice.ivIcon.setVisibility(View.VISIBLE);
        viewBinding.includeNotice.ivBack.setVisibility(View.VISIBLE);
        viewBinding.includeNotice.tvToolbarTitle.setVisibility(View.GONE);
        presenter = new NoticePresenter(this);
    }

    @Override
    protected void initData() {
        albumId = getIntent().getStringExtra(Contents.AlbumId);
        title = getIntent().getStringExtra(Contents.Title);
        photographerId = SPUtils.getString(Contents.Id);

        presenter.getOriginalPic(pageIndex,albumId, photographerId);

        viewBinding.tvEnter.setOnClickListener(this);
        viewBinding.includeNotice.ivBack.setOnClickListener(this);
    }

    @Override
    public void getError() {
        if (viewBinding != null) {
            isEnter = 2;
            viewBinding.tvEnter.setText("????????????,?????????");
        }

    }

    @Override
    public void getOriginalPic(OriginalPicBean bean) {
        ArrayList<OriginalPicBean.ResultBean> result = (ArrayList<OriginalPicBean.ResultBean>) bean.getResult();
        total = bean.getTotal();

        if (pageIndex*Contents.offsetNum <total) {
            presenter.syncData(result);
        } else {
            //??????SDCard
            presenter.syncSD(albumId);
        }

    }

    @Override
    public void syncComplete() {
        pageIndex++;
        if (pageIndex == 100) {
            return;
        }
       presenter.getOriginalPic(pageIndex,albumId, photographerId);
    }

    @Override
    public void syncSDComplete() {
        if (viewBinding != null) {
            isEnter = 1;
            viewBinding.tvEnter.setText("????????????");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void syncNum(int syncNum) {
        if (viewBinding == null) {
            return;
        }
        viewBinding.tvEnter.setText("???????????????(" + syncNum + "/" + total + ")...");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_enter:
                switch (isEnter) {
                    case 1:
                        Intent intent = new Intent(this, LiveActivity.class);
                        intent.putExtra(Contents.Title, title);
                        intent.putExtra(Contents.AlbumId, albumId);
                        intent.putExtra(Contents.Total, total);
                        intent.putExtra(Contents.QiNiuToken, token);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        pageIndex = 0;
                        AppDB.getInstance().picDao().delete();
                        presenter.getOriginalPic(pageIndex,albumId, photographerId);
                        break;
                    default:
                        MyToast.show("????????????????????????");
                        break;
                }
                break;
            case R.id.iv_back:

                showDialog();
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {
        dialog = new CommonDialog(this, "??????", "????????????????????????????", 0);
        dialog.setOnConfirmListener(this);
        dialog.show();
    }

    @Override
    public void confirm(int type) {
        AppDB.getInstance().picDao().delete();
        dialog.dismiss();
        finish();
    }
}
