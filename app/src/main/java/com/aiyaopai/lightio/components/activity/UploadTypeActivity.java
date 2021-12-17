package com.aiyaopai.lightio.components.activity;

import android.content.Intent;
import android.view.View;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseActivity;
import com.aiyaopai.lightio.databinding.ActivityUploadTypeBinding;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.SPUtils;

public class UploadTypeActivity extends BaseActivity<ActivityUploadTypeBinding> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_type;
    }

    @Override
    public void initView() {
        viewBinding.includeType.tvToolbarTitle.setText("上传方式");
        String mode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        if (mode != null) {
            switch (mode) {
                case Contents.DIRECT_UPLOAD:
                    viewBinding.ivDirect.setImageResource(R.mipmap.check_on);
                    viewBinding.ivHand.setImageResource(R.mipmap.check_off);
                    viewBinding.ivRating.setImageResource(R.mipmap.check_off);
                    break;
                case Contents.HAND_UPLOAD:
                    viewBinding.ivDirect.setImageResource(R.mipmap.check_off);
                    viewBinding.ivHand.setImageResource(R.mipmap.check_on);
                    viewBinding.ivRating.setImageResource(R.mipmap.check_off);
                    break;
                case Contents.RATING_UPLOAD:
                    viewBinding.ivHand.setImageResource(R.mipmap.check_off);
                    viewBinding.ivDirect.setImageResource(R.mipmap.check_off);
                    viewBinding.ivRating.setImageResource(R.mipmap.check_on);

                    break;
            }

        }
    }

    @Override
    protected void initData() {
        viewBinding.rlDirect.setOnClickListener(this);
        viewBinding.rlHand.setOnClickListener(this);
        viewBinding.rlRating.setOnClickListener(this);
        viewBinding.includeType.ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_direct:
                viewBinding.ivDirect.setImageResource(R.mipmap.check_on);
                viewBinding.ivHand.setImageResource(R.mipmap.check_off);
                viewBinding.ivRating.setImageResource(R.mipmap.check_off);
                SPUtils.save(Contents.UPLOAD_MODE, Contents.DIRECT_UPLOAD);
                intent.putExtra("type", "边拍边传");
                setResult(RESULT_OK, intent);
                break;
            case R.id.rl_hand:

                viewBinding.ivDirect.setImageResource(R.mipmap.check_off);
                viewBinding.ivHand.setImageResource(R.mipmap.check_on);
                viewBinding.ivRating.setImageResource(R.mipmap.check_off);
                SPUtils.save(Contents.UPLOAD_MODE, Contents.HAND_UPLOAD);
                intent.putExtra("type", "点选上传");
                setResult(RESULT_OK, intent);
                break;
            case R.id.rl_rating:

                viewBinding.ivDirect.setImageResource(R.mipmap.check_off);
                viewBinding.ivHand.setImageResource(R.mipmap.check_off);
                viewBinding.ivRating.setImageResource(R.mipmap.check_on);
                SPUtils.save(Contents.UPLOAD_MODE, Contents.RATING_UPLOAD);
                intent.putExtra("type", "标记上传");
                setResult(RESULT_OK, intent);
                break;


        }
    }
}