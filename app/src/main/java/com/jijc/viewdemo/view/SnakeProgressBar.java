package com.jijc.viewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.jijc.viewdemo.R;

/**
 * 蛇形进度条
 * Created by JIJC on 2017/10/21.
 */

public class SnakeProgressBar extends View {

    private static final int STATE_TOTAL = -1;
    private static final int STATE_CURRENT = 0;
    private static final int STAT_PASSED = 1;
    private float PADDING_DP = dipToPx(15);

    private Paint mTotalLinesPaint;
    private Paint mTotalPointsPaint;
    private Paint mTotalTextPaint;
    private Paint mCurrentLinesPaint;
    private Paint mCurrentPointsPaint;
    private Paint mCurrentTextPaint;
    private Paint mCurrentCirclePaint;
    private Paint mCurrentRingPaint;


    private int mTotalLinesColor;
    private int mCurrentLinesColor;
    private int mTotalTextColor;
    private int mCurrentTextColor;
    private int mCurrentCircleColor;
    private int mTotalStep;
    private int mCurrentStep;
    private int mMaxStep; //每一行最大的步数
    private float centerX;
    private float centerY;
    private float mLinesWidth;
    private float mTextSize;
    private float mBigPointRadius;
    private float mNormalPointRadius;
    private float mHorizontalSpace;
    private float mNormalLineSpace;
    private int[] data;
    private int mIconStartON;
    private int mIconStartOFF;
    private int mIconEndON;
    private int mIconEndOFF;

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnakeProgressBar);
        mTotalLinesColor = a.getColor(R.styleable.SnakeProgressBar_total_line_color, Color.LTGRAY);
        mTotalTextColor = a.getColor(R.styleable.SnakeProgressBar_total_text_color, Color.BLACK);
        mCurrentLinesColor = a.getColor(R.styleable.SnakeProgressBar_passed_line_color, Color.parseColor("#28d19d"));
        mCurrentTextColor = a.getColor(R.styleable.SnakeProgressBar_passed_text_color, Color.WHITE);
        mCurrentCircleColor = a.getColor(R.styleable.SnakeProgressBar_current_circle_color, Color.WHITE);
        mTotalStep = a.getInt(R.styleable.SnakeProgressBar_total_step, 7);
        mCurrentStep = a.getInt(R.styleable.SnakeProgressBar_current_step, 4);
        mMaxStep = a.getInt(R.styleable.SnakeProgressBar_max_step, 7);
        mBigPointRadius = a.getDimension(R.styleable.SnakeProgressBar_terminal_radius,dipToPx(18));
        mNormalPointRadius = a.getDimension(R.styleable.SnakeProgressBar_normal_radius,dipToPx(12));
        mTextSize = a.getDimension(R.styleable.SnakeProgressBar_text_size,dipToPx(12));
        mLinesWidth = a.getDimension(R.styleable.SnakeProgressBar_line_width,dipToPx(8));
        mHorizontalSpace = a.getDimension(R.styleable.SnakeProgressBar_horizontal_space,dipToPx(60));
        mIconStartON = a.getResourceId(R.styleable.SnakeProgressBar_icon_start_on, 0);
        mIconStartOFF = a.getResourceId(R.styleable.SnakeProgressBar_icon_start_off, 0);
        mIconEndON = a.getResourceId(R.styleable.SnakeProgressBar_icon_end_on, 0);
        mIconEndOFF = a.getResourceId(R.styleable.SnakeProgressBar_icon_end_off, 0);
        a.recycle();

        initView();
    }

    private void initView() {
        PADDING_DP = mHorizontalSpace/2- mLinesWidth;

        centerX = mBigPointRadius+PADDING_DP;
        centerY = mBigPointRadius;



        //总进度的线
        mTotalLinesPaint = new Paint();
        mTotalLinesPaint.setAntiAlias(true);
        mTotalLinesPaint.setColor(mTotalLinesColor);
        mTotalLinesPaint.setStrokeWidth(mLinesWidth);
        mTotalLinesPaint.setStyle(Paint.Style.STROKE);
        mTotalLinesPaint.setStrokeCap(Paint.Cap.ROUND);

        //总进度的球
        mTotalPointsPaint = new Paint();
        mTotalPointsPaint.setAntiAlias(true);
        mTotalPointsPaint.setDither(true);
        mTotalPointsPaint.setColor(mTotalLinesColor);
        mTotalPointsPaint.setStyle(Paint.Style.FILL);

        //显示总进度的文字
        mTotalTextPaint = new Paint();
        mTotalTextPaint.setAntiAlias(true);
        mTotalTextPaint.setTextSize(mTextSize);
        mTotalTextPaint.setColor(mTotalTextColor);
        mTotalTextPaint.setTextAlign(Paint.Align.CENTER);

        //当前进度的线
        mCurrentLinesPaint = new Paint();
        mCurrentLinesPaint.setAntiAlias(true);
        mCurrentLinesPaint.setColor(mCurrentLinesColor);
        mCurrentLinesPaint.setStrokeWidth(mLinesWidth);
        mCurrentLinesPaint.setStyle(Paint.Style.STROKE);
        mCurrentLinesPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的球
        mCurrentPointsPaint = new Paint();
        mCurrentPointsPaint.setAntiAlias(true);
        mCurrentPointsPaint.setDither(true);
        mCurrentPointsPaint.setColor(mCurrentLinesColor);
        mCurrentPointsPaint.setStyle(Paint.Style.FILL);

        //当前进度的外环
        mCurrentRingPaint = new Paint();
        mCurrentRingPaint.setAntiAlias(true);
        mCurrentRingPaint.setDither(true);
        mCurrentRingPaint.setColor(mCurrentLinesColor);
        mCurrentRingPaint.setStyle(Paint.Style.STROKE);
        mCurrentRingPaint.setStrokeWidth(dipToPx(10));

        //当前进度的内环
        mCurrentCirclePaint = new Paint();
        mCurrentCirclePaint.setAntiAlias(true);
        mCurrentCirclePaint.setDither(true);
        mCurrentCirclePaint.setColor(mCurrentCircleColor);
        mCurrentCirclePaint.setStyle(Paint.Style.STROKE);
        mCurrentCirclePaint.setStrokeWidth(dipToPx(5));

        //显示当前进度的文字
        mCurrentTextPaint = new Paint();
        mCurrentTextPaint.setAntiAlias(true);
        mCurrentTextPaint.setTextSize(mTextSize);
        mCurrentTextPaint.setColor(mCurrentTextColor);
        mCurrentTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getScreenWidth();
        //需要的行数
        int lines = (int)Math.ceil((float)mTotalStep/mMaxStep);
        int height= (int) ((lines-1)*mHorizontalSpace+mBigPointRadius*2);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
        if (mTotalStep <= mMaxStep){
            mNormalLineSpace = (getScreenWidth()-PADDING_DP*2 - mBigPointRadius*2*2-mNormalPointRadius*2*(mTotalStep-2))/(mTotalStep-1);
        }else {
            mNormalLineSpace = (getScreenWidth()-PADDING_DP*2 - mBigPointRadius*2*2-mNormalPointRadius*2*(mMaxStep-2))/(mMaxStep-1);
        }

        for (int i=0; i<mTotalStep;i++){
            if (mTotalStep <= mMaxStep){ //小于设置的每行的最大数，平分屏幕
                float startX = centerX + mBigPointRadius + mNormalLineSpace * i + (2 * i) * mNormalPointRadius;
                float stopX = startX+mNormalLineSpace;
                if(i == 0){
                    if (STATE_TOTAL == data[i]){
                        canvas.drawCircle(centerX, centerY, mBigPointRadius, mTotalPointsPaint);
                        canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mTotalLinesPaint);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconStartOFF);
                        canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                    }else if (STAT_PASSED == data[i]){
                        canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);
                        canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mCurrentLinesPaint);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconStartON);
                        canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                    }else {
                        canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mTotalLinesPaint);
                        canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconStartON);
                        canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                    }

                }else if (i == mTotalStep-1){
                    if (STATE_TOTAL == data[i]){
                        canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY, mBigPointRadius, mTotalPointsPaint);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndOFF);
                        canvas.drawBitmap(bitmap,startX+mBigPointRadius-2*mNormalPointRadius-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                    }else if (STAT_PASSED == data[i]){
                        canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY, mBigPointRadius, mCurrentPointsPaint);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                        canvas.drawBitmap(bitmap,startX+mBigPointRadius-2*mNormalPointRadius-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                    }else {
                        canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY, mBigPointRadius, mCurrentPointsPaint);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                        canvas.drawBitmap(bitmap,startX+mBigPointRadius-2*mNormalPointRadius-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                    }

                }else {
                    if (STATE_TOTAL == data[i]){
                        canvas.drawLine(startX,centerY, stopX,centerY,mTotalLinesPaint);
                        canvas.drawCircle(startX-mNormalPointRadius, centerY, mNormalPointRadius, mTotalPointsPaint);
                        canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+ mTextSize / 3,mTotalTextPaint);
                    }else if (STAT_PASSED == data[i]){
                        canvas.drawLine(startX,centerY, stopX,centerY,mCurrentLinesPaint);
                        canvas.drawCircle(startX-mNormalPointRadius, centerY, mNormalPointRadius, mCurrentPointsPaint);
                        canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+ mTextSize / 3,mCurrentTextPaint);
                    }else {
                        canvas.drawLine(startX,centerY, stopX,centerY,mTotalLinesPaint);
                        canvas.drawCircle(startX-mNormalPointRadius,centerY, mNormalPointRadius, mCurrentRingPaint);
                        canvas.drawCircle(startX-mNormalPointRadius,centerY, mNormalPointRadius, mCurrentCirclePaint);
                        canvas.drawCircle(startX-mNormalPointRadius, centerY, mNormalPointRadius, mCurrentPointsPaint);
                        canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+ mTextSize / 3,mCurrentTextPaint);
                    }
                }
            }else { //大于1行的情况

                //当前第几行
                int currentLine = (int)Math.ceil((float)(i+1)/mMaxStep);

                float startX = centerX + mBigPointRadius + mNormalLineSpace * (i%mMaxStep) + (2 * (i%mMaxStep)) * mNormalPointRadius;
                float stopX = startX+mNormalLineSpace;
                if (currentLine%2 != 0){ //奇数行
                    //奇数行是按照顺序的步骤画进度 并且右侧有转弯弧
                    if (i == 0){
                        if (STATE_TOTAL == data[i]){
                            canvas.drawCircle(centerX, centerY, mBigPointRadius, mTotalPointsPaint);
                            canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mTotalLinesPaint);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconStartOFF);
                            canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);
                        }else if (STAT_PASSED == data[i]){
                            canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);
                            canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mCurrentLinesPaint);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconStartON);
                            canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);

                        }else {
                            canvas.drawLine(centerX+mBigPointRadius,centerY, centerX+mBigPointRadius+mNormalLineSpace,centerY,mTotalLinesPaint);
                            canvas.drawCircle(centerX, centerY, mBigPointRadius, mCurrentPointsPaint);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconStartON);
                            canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY-bitmap.getHeight()/2,mTotalLinesPaint);

                        }
                    }else if(i == mTotalStep-1){
                        if (STATE_TOTAL == data[i]){
                            canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mTotalPointsPaint);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndOFF);
                            canvas.drawBitmap(bitmap,startX+mBigPointRadius-2*mNormalPointRadius-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);
                        }else if (STAT_PASSED == data[i]){
                            canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mCurrentPointsPaint);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                            canvas.drawBitmap(bitmap,startX+mBigPointRadius-2*mNormalPointRadius-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);
                        }else {
                            canvas.drawCircle(startX+mBigPointRadius-2*mNormalPointRadius, centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mCurrentPointsPaint);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                            canvas.drawBitmap(bitmap,startX+mBigPointRadius-2*mNormalPointRadius-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                        }
                    }else {
                        RectF rightRect = new RectF();
                        rightRect.left = startX-mNormalPointRadius-mHorizontalSpace/2;
                        rightRect.top = centerY+mHorizontalSpace*(currentLine-1);
                        rightRect.right = rightRect.left+mHorizontalSpace;
                        rightRect.bottom = rightRect.top+mHorizontalSpace;
                        if (STATE_TOTAL == data[i]){
                            if((i+1)%mMaxStep != 0){
                                canvas.drawLine(startX,centerY+mHorizontalSpace*(currentLine-1), stopX,centerY+mHorizontalSpace*(currentLine-1),mTotalLinesPaint);
                            }else {
                                canvas.drawArc(rightRect,-90,180,false,mTotalLinesPaint);
                            }
                            canvas.drawCircle(startX-mNormalPointRadius, centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mTotalPointsPaint);
                            canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mTotalTextPaint);
                        }else if (STAT_PASSED == data[i]){
                            if((i+1)%mMaxStep != 0){
                                canvas.drawLine(startX,centerY+mHorizontalSpace*(currentLine-1), stopX,centerY+mHorizontalSpace*(currentLine-1),mCurrentLinesPaint);
                            }else {
                                canvas.drawArc(rightRect,-90,180,false,mCurrentLinesPaint);
                            }
                            canvas.drawCircle(startX-mNormalPointRadius, centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentPointsPaint);
                            canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mCurrentTextPaint);
                        }else {
                            if((i+1)%mMaxStep != 0){
                                canvas.drawLine(startX,centerY+mHorizontalSpace*(currentLine-1), stopX,centerY+mHorizontalSpace*(currentLine-1),mTotalLinesPaint);
                            }else {
                                canvas.drawArc(rightRect,-90,180,false,mTotalLinesPaint);
                            }
                            canvas.drawCircle(startX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentRingPaint);
                            canvas.drawCircle(startX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentCirclePaint);
                            canvas.drawCircle(startX-mNormalPointRadius, centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentPointsPaint);
                            canvas.drawText((i+1)+"", startX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mCurrentTextPaint);
                        }
                    }

                }else { //偶数行

                    //等差数列： an = a1+(n-1)*d
                    //a1 = centerX+mBigPointRadius+(mMaxStep-1)*mNormalLineSpace + (2*mMaxStep-3)*mNormalPointRadius
                    //d = -mNormalLineSpace-2*mNormalPointRadius
                    //n = (i+1)%mMaxStep
                    float cX = centerX+mBigPointRadius+(mMaxStep-1)*mNormalLineSpace + (2*mMaxStep-3)*mNormalPointRadius+((i+1)%mMaxStep-1)*(-mNormalLineSpace-2*mNormalPointRadius);

                    if (i == 0){
                        //不存在这种情况
                    }else if(i == mTotalStep-1){
                        if (STATE_TOTAL == data[i]){
                            if ((i+1)%mMaxStep == 0){
                                canvas.drawCircle(centerX, centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mTotalPointsPaint);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndOFF);
                                canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                            }else {
                                canvas.drawCircle(cX-(mBigPointRadius-mNormalPointRadius), centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mTotalPointsPaint);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndOFF);
                                canvas.drawBitmap(bitmap,cX-(mBigPointRadius-mNormalPointRadius)-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                            }
                        }else if (STAT_PASSED == data[i]){
                            if ((i+1)%mMaxStep == 0){
                                canvas.drawCircle(centerX, centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mCurrentPointsPaint);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                                canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                            }else {
                                canvas.drawCircle(cX-(mBigPointRadius-mNormalPointRadius), centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mCurrentPointsPaint);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                                canvas.drawBitmap(bitmap,cX-(mBigPointRadius-mNormalPointRadius)-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                            }
                        }else {
                            if ((i+1)%mMaxStep == 0){
                                canvas.drawCircle(centerX, centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mCurrentPointsPaint);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                                canvas.drawBitmap(bitmap,centerX-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                            }else {
                                canvas.drawCircle(cX-(mBigPointRadius-mNormalPointRadius), centerY+mHorizontalSpace*(currentLine-1), mBigPointRadius, mCurrentPointsPaint);
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mIconEndON);
                                canvas.drawBitmap(bitmap,cX-(mBigPointRadius-mNormalPointRadius)-bitmap.getWidth()/2,centerY+mHorizontalSpace*(currentLine-1)-bitmap.getHeight()/2,mTotalLinesPaint);

                            }

                        }
                    }else {
                        RectF leftRect = new RectF();
                        leftRect.right = centerX+(mBigPointRadius-mNormalPointRadius)+mHorizontalSpace/2;
                        leftRect.left = leftRect.right-mHorizontalSpace;
                        leftRect.top = centerY+mHorizontalSpace*(currentLine-1);
                        leftRect.bottom = leftRect.top+mHorizontalSpace;
                        if (STATE_TOTAL == data[i]){
                            if ((i+1)%mMaxStep == 0){
                                canvas.drawArc(leftRect,90,180,false,mTotalLinesPaint);
                                canvas.drawCircle(centerX+(mBigPointRadius-mNormalPointRadius), centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mTotalPointsPaint);
                                canvas.drawText((i+1)+"", centerX+(mBigPointRadius-mNormalPointRadius),centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mTotalTextPaint);

                            }else {
                                canvas.drawLine(cX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1), cX-mNormalPointRadius-mNormalLineSpace,centerY+mHorizontalSpace*(currentLine-1),mTotalLinesPaint);
                                canvas.drawCircle(cX, centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mTotalPointsPaint);
                                canvas.drawText((i+1)+"", cX,centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mTotalTextPaint);
                            }

                        }else if (STAT_PASSED == data[i]){
                            if ((i+1)%mMaxStep == 0){
                                canvas.drawArc(leftRect,90,180,false,mCurrentLinesPaint);
                                canvas.drawCircle(centerX+(mBigPointRadius-mNormalPointRadius), centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentPointsPaint);
                                canvas.drawText((i+1)+"", centerX+(mBigPointRadius-mNormalPointRadius),centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mCurrentTextPaint);
                            }else {
                                canvas.drawLine(cX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1), cX-mNormalPointRadius-mNormalLineSpace,centerY+mHorizontalSpace*(currentLine-1),mCurrentLinesPaint);
                                canvas.drawCircle(cX, centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentPointsPaint);
                                canvas.drawText((i+1)+"", cX,centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mCurrentTextPaint);
                            }

                        }else {

                            if ((i+1)%mMaxStep == 0){
                                canvas.drawArc(leftRect,90,180,false,mTotalLinesPaint);
                                canvas.drawCircle(centerX+(mBigPointRadius-mNormalPointRadius),centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentRingPaint);
                                canvas.drawCircle(centerX+(mBigPointRadius-mNormalPointRadius),centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentCirclePaint);
                                canvas.drawCircle(centerX+(mBigPointRadius-mNormalPointRadius), centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentPointsPaint);
                                canvas.drawText((i+1)+"", centerX+(mBigPointRadius-mNormalPointRadius),centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mCurrentTextPaint);

                            }else {
                                canvas.drawLine(cX-mNormalPointRadius,centerY+mHorizontalSpace*(currentLine-1), cX-mNormalPointRadius-mNormalLineSpace,centerY+mHorizontalSpace*(currentLine-1),mTotalLinesPaint);
                                canvas.drawCircle(cX,centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentRingPaint);
                                canvas.drawCircle(cX,centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentCirclePaint);
                                canvas.drawCircle(cX, centerY+mHorizontalSpace*(currentLine-1), mNormalPointRadius, mCurrentPointsPaint);
                                canvas.drawText((i+1)+"", cX,centerY+mHorizontalSpace*(currentLine-1)+ mTextSize / 3,mCurrentTextPaint);

                            }

                        }
                    }
                }
            }

        }

    }

    public void setTotalStep(int step){
        this.mTotalStep = step;
        invalidate();
    }

    public void setCurrentStep(int currentStep){
        this.mCurrentStep = currentStep;
        invalidate();
    }

    public void setMaxStep(int maxStep){
        this.mMaxStep = maxStep;
        invalidate();
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
