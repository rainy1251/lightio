package com.aiyaopai.lightio.components.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseActivity;
import com.aiyaopai.lightio.databinding.ActivitySettingBinding;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.SPUtils;

public class SettingActivity extends BaseActivity<ActivitySettingBinding>
        implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private Boolean isAutoScan;
    private Boolean isVibrate;
    public static final int Type = 101;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        isAutoScan = SPUtils.getBooleanDefTrue(Contents.AUTO);
        isVibrate = SPUtils.getBooleanDefTrue(Contents.VIBRATE);
        String uploadMode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        viewBinding.includeSet.tvToolbarTitle.setText("上传设置");

        if (uploadMode.equals(Contents.DIRECT_UPLOAD)) {
            viewBinding.tvType.setText("边拍边传");
        } else if (uploadMode.equals(Contents.HAND_UPLOAD)) {
            viewBinding.tvType.setText("点选上传");
        } else if (uploadMode.equals(Contents.RATING_UPLOAD)) {
            viewBinding.tvType.setText("标记上传");
        }

        if (isVibrate) {
            viewBinding.ivVibrate.setImageResource(R.mipmap.article_checked);
        } else {
            viewBinding.ivVibrate.setImageResource(R.mipmap.article_unchecked);
        }

        if (isAutoScan) {
            viewBinding.ivAutoscan.setImageResource(R.mipmap.article_checked);
        } else {
            viewBinding.ivAutoscan.setImageResource(R.mipmap.article_unchecked);
        }
    }

    @Override
    protected void initData() {

        setPhotoType();
        initListener();
    }

    private void initListener() {
        viewBinding.ivAutoscan.setOnClickListener(this);
        viewBinding.ivVibrate.setOnClickListener(this);
        viewBinding.rlHelp.setOnClickListener(this);
        viewBinding.rlType.setOnClickListener(this);
        viewBinding.includeSet.ivBack.setOnClickListener(this);
        viewBinding.rgPhotoType.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.rb_j:
                SPUtils.save(Contents.PHOTO_PX, Contents.TopSpeed);
                break;
            case R.id.rb_b:
                SPUtils.save(Contents.PHOTO_PX, Contents.Standard);
                break;
            case R.id.rb_g:
                SPUtils.save(Contents.PHOTO_PX, Contents.Original);
                break;

        }
    }

    private void switchAutoScan() {
        if (isAutoScan) {
            viewBinding.ivAutoscan.setImageResource(R.mipmap.article_unchecked);
            isAutoScan = false;
        } else {
            viewBinding.ivAutoscan.setImageResource(R.mipmap.article_checked);
            isAutoScan = true;
        }
        SPUtils.save(Contents.AUTO, isAutoScan);
    }

    private void switchVibrate() {
        if (isVibrate) {
            viewBinding.ivVibrate.setImageResource(R.mipmap.article_unchecked);
            isVibrate = false;
        } else {
            viewBinding.ivVibrate.setImageResource(R.mipmap.article_checked);
            isVibrate = true;
        }
        SPUtils.save(Contents.VIBRATE, isVibrate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_autoscan:
                switchAutoScan();
                break;
            case R.id.iv_vibrate:
                switchVibrate();
                break;

            case R.id.rl_type:

                Intent intent = new Intent(this, UploadTypeActivity.class);
                startActivityForResult(intent, Type);

                break;
            case R.id.rl_help:
                WebActivity.start(this, Contents.HelpUrl, "帮助中心");
                break;
        }
    }

    /**
     * 设置照片尺寸
     */
    private void setPhotoType() {
        String value = SPUtils.getPxString(Contents.PHOTO_PX);
        if (value.equals(Contents.TopSpeed)) {
            viewBinding.rbJ.setChecked(true);
        } else if (value.equals(Contents.Original)) {
            viewBinding.rbG.setChecked(true);
        } else {
            viewBinding.rbB.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Type) {

                assert data != null;
                viewBinding.tvType.setText(data.getStringExtra("type"));
            }
        }

    }
}