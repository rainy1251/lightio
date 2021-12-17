package com.aiyaopai.lightio.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.GlideUtils;

import java.util.Objects;

public class QrCodeDialog extends Dialog {

    private final Context mContext;
    private final Bitmap bitmap;

    public QrCodeDialog(Context context, Bitmap bitmap) {
        super(context);
        this.mContext = context;
        this.bitmap = bitmap;
        setCustomDialog(bitmap);
    }

    @SuppressLint("InflateParams")
    private void setCustomDialog(Bitmap bitmap) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_qrcode_layout, null);
        ImageView qrcode = mView.findViewById(R.id.iv_code);
        GlideUtils.show(mContext, qrcode, bitmap);

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
        FilesUtil.recycleBitmap(bitmap);
        super.dismiss();

    }

}