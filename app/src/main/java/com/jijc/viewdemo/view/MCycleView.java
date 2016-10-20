package com.jijc.viewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.jijc.viewdemo.R;

/**
 * Description:
 * Created by jijc on 2016/10/19.
 * PackageName: com.jijc.viewdemo.view
 */
public class MCycleView extends View {

    private static final int FILL = 0;
    private static final int STROKE = 1;
    private static final int FILL_AND_STROKE = 2;
    private int style;
    private int cycleStrokeWidth;
    private int textStrokeWidth;
    private int textSize;
    private int textColor;
    private int firstColor;
    private int secondColor;
    private int cycleWidth;
    private int speed;
    private int progress;

    private int mProgress;
    private Paint mPaint;
    private boolean isNext; //是否应该开始下一个

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mProgress++;
            if (mProgress==360) {
                mProgress = 0;
               isNext=!isNext;
            }
            postInvalidate();
            sendEmptyMessageDelayed(100,speed);
        }
    };

    public MCycleView(Context context) {
        this(context,null);
    }

    public MCycleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MCycleView);
        firstColor = typedArray.getColor(R.styleable.MCycleView_firstColor, Color.BLACK);
        secondColor = typedArray.getColor(R.styleable.MCycleView_secondColor, Color.BLACK);
        textColor = typedArray.getColor(R.styleable.MCycleView_textColor, Color.BLACK);
        cycleWidth = typedArray.getDimensionPixelSize(R.styleable.MCycleView_cycleWidth, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()));
        textSize = typedArray.getDimensionPixelSize(R.styleable.MCycleView_textSize, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, getResources().getDisplayMetrics()));
        speed = typedArray.getInt(R.styleable.MCycleView_speed, 20);
        progress = typedArray.getInt(R.styleable.MCycleView_progress, 0);
        cycleStrokeWidth =typedArray.getDimensionPixelSize(R.styleable.MCycleView_cycleStrokeWidth, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics()));
        textStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.MCycleView_textStrokeWidth, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics()));
        style = typedArray.getInt(R.styleable.MCycleView_style, 0);
        typedArray.recycle();

        mPaint = new Paint();

        handler.sendEmptyMessageDelayed(100,speed);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (true){
//                    mProgress++;
//                    if (mProgress==360){
//                        mProgress=0;
//                        if (!isNext){
//                            isNext=true;
//                        }else {
//                            isNext=false;
//                        }
//                    }
//                    postInvalidate();
//
//                    SystemClock.sleep(speed);
//                }
//            }
//        }).start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //圆心的横坐标
        int x = getWidth()/2;
        //半径
        int radius = x-cycleWidth;
        //抗锯齿
        mPaint.setAntiAlias(true);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        //设置圆环的宽度
        mPaint.setStrokeWidth(cycleStrokeWidth);

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        if (style==FILL){
            textPaint.setStyle(Paint.Style.FILL);
        }else if(style==STROKE){
            textPaint.setStyle(Paint.Style.STROKE);
        }else if(style==FILL_AND_STROKE){
            textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        textPaint.setStrokeWidth(textStrokeWidth);
        RectF oval = new RectF(x-radius,x-radius,x+radius,x+radius);
        String str="";
        if (!isNext){
            //设置圆环的颜色
            mPaint.setColor(firstColor);
            //画出圆环
            canvas.drawCircle(x,x,radius,mPaint);
            //设置圆弧的颜色
            mPaint.setColor(secondColor);
            //绘制圆弧
            canvas.drawArc(oval,-90,mProgress,false,mPaint);

            str =  (mProgress*100/360) + "";
            Rect  bounds = new Rect();
            textPaint.getTextBounds(str,0,str.length(),bounds);
            canvas.drawText(str,x-textPaint.measureText(str,0,str.length())/2,x+bounds.height()/2,textPaint);
//            canvas.drawText("======",x-textPaint.measureText("======",0,"======".length())/2,x+40, textPaint);
            Log.w("jijinchao","mprogress:"+mProgress+"------Progress:"+progress);
            if (360*progress<=mProgress*100){

                handler.removeCallbacksAndMessages(null);
            }

        }else {
            mPaint.setColor(secondColor); // 设置圆环的颜色

            canvas.drawCircle(x, x, radius, mPaint); // 画出圆环
            textPaint.setColor(Color.GREEN);
            Rect  bounds = new Rect();
            str="100";
            textPaint.getTextBounds(str,0,str.length(),bounds);
            canvas.drawText(str,x-textPaint.measureText(str,0,str.length())/2,x+bounds.height()/2, textPaint);
//            canvas.drawText("======",x-textPaint.measureText("======",0,"======".length())/2,x+40, textPaint);
//            mPaint.setColor(firstColor); // 设置圆环的颜色
//            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
            handler.removeCallbacksAndMessages(null);
        }
    }
}
