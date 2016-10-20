package com.jijc.viewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.jijc.viewdemo.R;

/**
 * Description:
 * Created by jijc on 2016/9/28.
 * PackageName: com.jijc.viewdemo.view
 */
public class MTextView extends View {

    private String mText;
    private int mTextColor;
    private float mTextSize;
    private Paint mPaint;
    private Rect mBounds;
    private Paint.FontMetrics fontMetrics;

    public MTextView(Context context) {
        this(context,null);
    }

    public MTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MTextView);
        mText = typedArray.getString(R.styleable.MTextView_mText);
        mTextColor = typedArray.getColor(R.styleable.MTextView_mTextColor, Color.BLACK);
        //设置textview的默认size为12dp
        mTextSize = typedArray.getDimension(R.styleable.MTextView_mTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mBounds = new Rect();
        mPaint.getTextBounds(mText,0,mText.length(), mBounds);
    }

    /**
     * 在xml中设置wrap_content的会填充整个宽或高，这是系统测出来的结果，因此需要手动测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 三种测量模式
         * MeasureSpec.EXACTLY 表示设置了精确值或者match_parent
         * MeasureSpec.AT_MOST 表示子布局限制在一个最大的范围内，一般为wrap_content
         * MeasureSpec.UNSPECIFIED 表示子布局想要多大就有多大，很少使用
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width,height;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBounds);
            width=(int)mPaint.measureText(mText)+getPaddingLeft()+getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBounds);
            fontMetrics = mPaint.getFontMetrics();
            height = (int)Math.abs(fontMetrics.top-fontMetrics.bottom)+getPaddingTop()+getPaddingBottom();
        }
        Log.w("jijinchao","onMeasure==measureWidth:"+mPaint.measureText(mText)+",mBoundsWidth:"+mBounds.width());
        Log.w("jijinchao","onMeasure==top:"+fontMetrics.top+"-------leading:"+fontMetrics.leading+"-----ascent:"+fontMetrics.ascent+"------descent:"+fontMetrics.descent+"--------bottom:"+fontMetrics.bottom);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();

        //绘制文本开始的纵坐标，方式1  比较接近textview的方式，垂直方向不一定居中
        int startY = getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2;
        //绘制文本开始的纵坐标，方式2  垂直方向居中
        int startY1 = getHeight() / 2 - fm.descent + (fm.descent - fm.ascent)/ 2;

        //绘制边框
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        canvas.drawRoundRect(0f,0f,(float) getMeasuredWidth(),(float) getMeasuredHeight(),2f,2f,mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(mText,getWidth()/2-mPaint.measureText(mText)/2,startY1,mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);  //横向的中线
//        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),mPaint); //竖向的中线
        Log.w("jijinchao","onDraw==measureWidth:"+mPaint.measureText(mText)+",mBoundsWidth:"+mBounds.width());
        Log.w("jijinchao","onDraw==top:"+fontMetrics.top+"-------leading:"+fontMetrics.leading+"-----ascent:"+fontMetrics.ascent+"------descent:"+fontMetrics.descent+"--------bottom:"+fontMetrics.bottom);
    }
}
