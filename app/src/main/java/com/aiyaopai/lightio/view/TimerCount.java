package com.aiyaopai.lightio.view;

import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 计时器
 */
public class TimerCount extends CountDownTimer {

	private TextView bnt;


    public TimerCount(long millisInFuture, long countDownInterval, TextView bnt) {
        super(millisInFuture, countDownInterval);
        this.bnt = bnt;
    }

    public TimerCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);

    }

    @Override
    public void onFinish() {

        bnt.setClickable(true);
        bnt.setText("获取验证码");

    }

    @Override
    public void onTick(long arg0) {

        bnt.setClickable(false);
        bnt.setText(arg0 / 1000 + "\ts");

    }


}