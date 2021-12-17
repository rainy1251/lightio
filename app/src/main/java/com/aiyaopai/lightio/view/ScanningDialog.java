package com.aiyaopai.lightio.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiyaopai.lightio.R;

import java.util.Objects;

public class ScanningDialog extends Dialog {

    private Context context;
    private ProgressBar progressBar;

    public void setProgress(int progress) {
        this.progress = progress;

        if (progressBar != null) {

            progressBar.setProgress(progress);
        }
    }

    private int progress;

    public ScanningDialog(Context context) {
        super(context);
        this.context = context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_import_layout, null);

        progressBar = mView.findViewById(R.id.pb_bar);

        //   progressBar.setProgress(progress);

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
        setCanceledOnTouchOutside(false);
        //设置点击返回键不消失
        setCancelable(false);
    }

    @Override
    public void dismiss() {
        // HideUtil.hideDialogSoftKeyboard(this);
        super.dismiss();

    }

}