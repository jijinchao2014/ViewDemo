package com.jijc.viewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by jijinchao on 2017/10/21.
 */

public class SnakeProgressBar extends View {

    private static final int STATE_TOTAL = -1;
    private static final int STATE_CURRENT = 0;
    private static final int STAT_PASSED = 1;

    private Paint mTotalLinesPaint;
    private Paint mTotalPointsPaint;
    private Paint mTotalTextPaint;
    private Paint mCurrentLinesPaint;
    private Paint mCurrentPointsPaint;
    private Paint mCurrentTextPaint;


    private String mTotalLinesColor = "#e8e8e8";
    private String mCurrentLinesColor = "#92c659";
    private String mTotalTotalTextColor = "#111111";
    private String mCurrentTotalTextColor = "#ffffff";
    private int mTotalStep = 6;
    private int mCurrentStep = 3;
    private int mMaxStep = 7; //每一行最大的步数
    private float centerX;
    private float centerY;
    private float mTotalLinesWidth = dipToPx(8);
    private float mTotalTotalTextSize = dipToPx(12);
    private float mBigPointRadius = dipToPx(20);
    private float mNormalPointRadius = dipToPx(15);
    private float mNormalLineSpace;
    private int[] data;

    public SnakeProgressBar(Context context) {
        this(context,null);
    }

    public SnakeProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SnakeProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig(context, attrs);
    }

    private void initConfig(Context context, @Nullable AttributeSet attrs) {

        initView();
    }

    private void initView() {
        data = new int[mTotalStep];
        for (int i = 0;i < mTotalStep;i++){
            if (i < mCurrentStep-1){
                data[i] = STAT_PASSED;
            }else if (i > mCurrentStep-1){
                data[i] = STATE_TOTAL;
            }else {
                data[i] = STATE_CURRENT;
            }
        }
        centerX = mBigPointRadius;
        centerY = mBigPointRadius;

        if (mTotalStep <= mMaxStep){
            mNormalLineSpace = (getScreenWidth()-dipToPx(30) - mBigPointRadius*2*2-mNormalPointRadius*2*(mTotalStep-2))/(mTotalStep-1);
        }else {

        }

        //总进度的线
        mTotalLinesPaint = new Paint();
        mTotalLinesPaint.setAntiAlias(true);
        mTotalLinesPaint.setColor(Color.parseColor(mTotalLinesColor));
        mTotalLinesPaint.setStrokeWidth(mTotalLinesWidth);
        mTotalLinesPaint.setStyle(Paint.Style.STROKE);
        mTotalLinesPaint.setStrokeCap(Paint.Cap.ROUND);

        //总进度的球
        mTotalPointsPaint = new Paint();
        mTotalPointsPaint.setAntiAlias(true);
        mTotalPointsPaint.setDither(true);
        mTotalPointsPaint.setColor(Color.parseColor(mTotalLinesColor));
        mTotalPointsPaint.setStyle(Paint.Style.FILL);

        //显示总进度的文字
        mTotalTextPaint = new Paint();
        mTotalTextPaint.setTextSize(mTotalTotalTextSize);
        mTotalTextPaint.setColor(Color.parseColor(mTotalTotalTextColor));
        mTotalTextPaint.setTextAlign(Paint.Align.CENTER);

        //当前进度的线
        mCurrentLinesPaint = new Paint();
        mCurrentLinesPaint.setAntiAlias(true);
        mCurrentLinesPaint.setColor(Color.parseColor(mCurrentLinesColor));
        mCurrentLinesPaint.setStrokeWidth(mTotalLinesWidth);
        mCurrentLinesPaint.setStyle(Paint.Style.STROKE);
        mCurrentLinesPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的球
        mCurrentPointsPaint = new Paint();
        mCurrentPointsPaint.setAntiAlias(true);
        mCurrentPointsPaint.setDither(true);
        mCurrentPointsPaint.setColor(Color.parseColor(mCurrentLinesColor));
        mCurrentPointsPaint.setStyle(Paint.Style.FILL);

        //显示当前进度的文字
        mCurrentTextPaint = new Paint();
        mCurrentTextPaint.setTextSize(mTotalTotalTextSize);
        mCurrentTextPaint.setColor(Color.parseColor(mCurrentTotalTextColor));
        mCurrentTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        for (int i=0; i<mTotalStep;i++){
            if (mTotalStep <= mMaxStep){ //小于设置的每行的最大数，平分屏幕
                float startX = centerX + mBigPointRadius + mNormalLineSpace * i + (2 * i) * mNormalPointRadius;
                float stopX = startX+mNormalLineSpace;
                if(i == 0){
                    if (STATE_TOTAL == data[i]){
                        canvas.drawCircle(centerX, centerY, mBigPointRadius, mTotalPointsPaint);
                        canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mTotalLinesPaint);

                    }else if (STAT_PASSED == data[i]){
                        canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);
                        canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mCurrentLinesPaint);

                    }else {
                        canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mTotalLinesPaint);
                        canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);

                    }

                }else if (i == mTotalStep-1){
                    if (STATE_TOTAL == data[i]){
                        canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY, mBigPointRadius, mTotalPointsPaint);
                    }else if (STAT_PASSED == data[i]){
                        canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY, mBigPointRadius, mCurrentPointsPaint);
                    }else {
                        canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY, mBigPointRadius, mCurrentPointsPaint);

                    }

                }else {
                    if (STATE_TOTAL == data[i]){
                        canvas.drawLine(startX,centerY, stopX,centerY,mTotalLinesPaint);
                        canvas.drawCircle(startX-mNormalPointRadius, centerY, mNormalPointRadius, mTotalPointsPaint);
                        canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+mTotalTotalTextSize / 3,mTotalTextPaint);
                    }else if (STAT_PASSED == data[i]){
                        canvas.drawLine(startX,centerY, stopX,centerY,mCurrentLinesPaint);
                        canvas.drawCircle(startX-mNormalPointRadius, centerY, mNormalPointRadius, mCurrentPointsPaint);
                        canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+mTotalTotalTextSize / 3,mCurrentTextPaint);
                    }else {
                        canvas.drawLine(startX,centerY, stopX,centerY,mTotalLinesPaint);
                        canvas.drawCircle(startX-mNormalPointRadius, centerY, mNormalPointRadius, mCurrentPointsPaint);
                        canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+mTotalTotalTextSize / 3,mCurrentTextPaint);
                    }
                }
            }else { //大于1行的情况

            }

        }



//        for (int i=0; i<mTotalStep-3;i++){
//            float startX = centerX + mBigPointRadius + mNormalLineSpace * i + 2 * i * mNormalPointRadius;
//            float stopX = centerX + mBigPointRadius + mNormalLineSpace * i + 2 * i * mNormalPointRadius + mNormalLineSpace;
//            canvas.drawLine(startX,centerY, stopX,centerY,mCurrentLinesPaint);
//            canvas.drawCircle(centerX + mBigPointRadius+mNormalLineSpace*(i+1)+(i*2+1)*mNormalPointRadius, centerY, mNormalPointRadius, mCurrentPointsPaint);
//            canvas.drawText((i+2)+"",centerX + mBigPointRadius+mNormalLineSpace*(i+1)+(i*2+1)*mNormalPointRadius,centerY+ mTotalTotalTextSize / 3,mCurrentTextPaint);
////            canvas.drawLine(startX,centerY, stopX,centerY,mTotalLinesPaint);
//        }
//        canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);
    }
    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
