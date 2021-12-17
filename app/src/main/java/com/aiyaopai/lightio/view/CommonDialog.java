package com.aiyaopai.lightio.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.aiyaopai.lightio.R;

import java.util.Objects;

public class CommonDialog extends Dialog {

    private Context context;

    public CommonDialog(Context context, String title, String content, int type) {
        super(context);
        this.context = context;
        setCustomDialog(title, content, type);
    }

    private void setCustomDialog(String title, String content, int type) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_common_layout, null);
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        TextView tvContent = mView.findViewById(R.id.tv_content);
        Button btnClose = mView.findViewById(R.id.btn_close);
        Button btnAff = mView.findViewById(R.id.btn_aff);
        tvTitle.setText(title);
        tvContent.setText(content);
        btnClose.setOnClickListener(view -> {
            if (onCancelListener != null) {
                onCancelListener.cancel(type);
            }
            dismiss();
        });
        btnAff.setOnClickListener(view -> {

            if (onConfirmListener != null) {
                onConfirmListener.confirm(type);
            }

            dismiss();
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
        // HideUtil.hideDialogSoftKeyboard(this);
        super.dismiss();

    }

    public interface OnConfirmListener {
        void confirm(int type);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    private OnConfirmListener onConfirmListener;

    public interface OnCancelListener {
        void cancel(int type);
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    private OnCancelListener onCancelListener;
}