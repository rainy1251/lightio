package com.aiyaopai.lightio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aiyaopai.lightio.R;


@SuppressLint("AppCompatCustomView")
public class MyDrawableTextView extends TextView {

    //image width、height
    private int imageWidth;
    private int imageHeight;

    private Drawable leftImage;
    private Drawable topImage;
    private Drawable rightImage;
    private Drawable bottomImage;

    public MyDrawableTextView(Context context) {
        this(context, null);
    }
    public MyDrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomDrawableTextView,0,0);
        int countNum = ta.getIndexCount();
        for (int i = 0; i < countNum; i++) {

            int attr = ta.getIndex(i);
            if (attr == R.styleable.CustomDrawableTextView_leftImage) {
                leftImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_topImage) {
                topImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_rightImage) {
                rightImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_bottomImage) {
                bottomImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_imageWidth) {
                imageWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.CustomDrawableTextView_imageHeight) {
                imageHeight = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
            }
        }
        int startspan = ta.getInteger(R.styleable.CustomDrawableTextView_startspan, -1);
        int endspan = ta.getInteger(R.styleable.CustomDrawableTextView_endspan, -1);
        int spancolor = ta.getColor(R.styleable.CustomDrawableTextView_spancolor, 0xff333333);
        if (startspan != -1&&endspan!=-1) {

            SpannableString sty = new SpannableString(getText().toString().trim());
            sty.setSpan(new ForegroundColorSpan(spancolor), startspan, endspan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sty.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    //协议
                    if (mOnSpanClickListener != null) {
                        mOnSpanClickListener.clickSpan();
                    }
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            },startspan,endspan,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.setMovementMethod(LinkMovementMethod.getInstance());
            this.setText(sty);
        }
        ta.recycle();
        init();
    }

    /**
     * init views
     */
    private void init() {
        setCompoundDrawablesWithIntrinsicBounds(leftImage,topImage,rightImage,bottomImage);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if(left != null) {
            left.setBounds(0,0,imageWidth,imageHeight);
        }

        if(top != null) {
            top.setBounds(0,0,imageWidth,imageHeight);
        }

        if(right != null) {
            right.setBounds(0,0,imageWidth,imageHeight);
        }

        if(bottom != null) {
            bottom.setBounds(0,0,imageWidth,imageHeight);
        }

        setCompoundDrawables(left,top,right,bottom);
    }
    private OnSpanClickListener mOnSpanClickListener;
    public interface OnSpanClickListener{
        void clickSpan();
    }

    public void setOnSpanClickListener(OnSpanClickListener onSpanClickListener) {
        mOnSpanClickListener = onSpanClickListener;
    }
}
