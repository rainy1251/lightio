package com.aiyaopai.lightio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.util.DimenUtil;


public class MyItemTextView extends View {


    private String leftText;
    private int leftTextColor;
    private int leftTextSize;
    private String rightText;
    private int rightTextColor;
    private int rightTextSize;
    private Paint mPaint;
    private Rect mBound;
    private Paint rightPaint;
    private Rect rightBound;
    private Drawable leftTextImage;
    private Drawable rightTextImage;
    private Context mContext;
    private Paint mLinePaint;

    public MyItemTextView(Context context) {
        this(context, null);
    }

    public MyItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext =context;

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);
        int countNum = ta.getIndexCount();
        for (int i = 0; i < countNum; i++) {

            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTextView_leftText:
                    leftText = ta.getString(attr);
                    break;
                case R.styleable.CustomTextView_leftTextColor:
                    leftTextColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_leftTextSize:
                    leftTextSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

                    break;
                case R.styleable.CustomTextView_leftTextImage:
                    leftTextImage = ta.getDrawable(attr);
                    break;
                case R.styleable.CustomTextView_rightTextImage:
                    rightTextImage = ta.getDrawable(attr);
                    break;
                case R.styleable.CustomTextView_rightText:
                    rightText = ta.getString(attr);
                    break;
                case R.styleable.CustomTextView_rightTextColor:
                    rightTextColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_rightTextSize:
                    rightTextSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));

                    break;
            }
        }

        ta.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(leftTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(leftText, 0, leftText.length(), mBound);

        mLinePaint = new Paint();

        rightPaint = new Paint();
        rightPaint.setTextSize(rightTextSize);
        rightBound = new Rect();
        rightPaint.getTextBounds(rightText, 0, rightText.length(), rightBound);

    }

    public void setRightText(String value){
        rightText=value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.WHITE);

        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        Bitmap bitmap=null;
        Bitmap bitmapRight=null;
        bitmap=((BitmapDrawable)leftTextImage).getBitmap();
        canvas.drawBitmap(bitmap, DimenUtil.dpToPx(mContext,24), getHeight() / 2 -bitmap.getHeight() / 2 , null);

        bitmapRight=((BitmapDrawable)rightTextImage).getBitmap();
        canvas.drawBitmap(bitmapRight, getWidth() - bitmapRight.getWidth()- DimenUtil.dpToPx(mContext,16), getHeight() / 2 -bitmapRight.getHeight() / 2 , null);

        mPaint.setColor(leftTextColor);

        canvas.drawText(leftText, bitmap.getWidth()+DimenUtil.dpToPx(mContext,54), getHeight() / 2 + mBound.height() / 2, mPaint);

        rightPaint.setColor(rightTextColor);

        canvas.drawText(rightText, getWidth() - rightBound.width()-bitmapRight.getWidth()-DimenUtil.dpToPx(mContext,40), getHeight() / 2 + rightBound.height() / 2, rightPaint);

        mLinePaint.setColor(mContext.getResources().getColor(R.color.under_line));
        canvas.drawLine(bitmap.getWidth()+DimenUtil.dpToPx(mContext,54), DimenUtil.dpToPx(mContext,51),getWidth() , DimenUtil.dpToPx(mContext,51), mLinePaint);// 画线

        super.onDraw(canvas);
    }

}