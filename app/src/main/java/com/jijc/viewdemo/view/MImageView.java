package com.jijc.viewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
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
public class MImageView extends View {

    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
    private TypedArray typedArray;
    private String text;
    private int textColor;
    private float textSize;
    private int scaleType;
    private Bitmap imageSrc;
    private Paint mPaint;
    private Rect mTextBounds;
    private Rect rect;

    private int mWidth;
    private int mHeight;


    public MImageView(Context context) {
        this(context,null);
    }

    public MImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context,attrs);
    }

    /**
     * 获取自定义的属性
     * @param context
     * @param attrs
     */
    private void initRes(Context context, AttributeSet attrs) {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.MImageView);
        text = typedArray.getString(R.styleable.MImageView_text);
        textColor = typedArray.getColor(R.styleable.MImageView_textColor, Color.BLACK);
        textSize = typedArray.getDimension(R.styleable.MImageView_textSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()));
        scaleType = typedArray.getInt(R.styleable.MImageView_scaleType, 0);
        imageSrc = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.MImageView_imageSrc, 0));
        typedArray.recycle();

        mPaint = new Paint();
        //文字的区域
        mTextBounds = new Rect();
        mPaint.setTextSize(textSize);
        //获取文本的区域
        mPaint.getTextBounds(text,0,text.length(),mTextBounds);
        //图片的区域
        rect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 设置宽度
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        //match_parent or 固定大小
        if (MeasureSpec.EXACTLY == widthMode){
            Log.w("jijinchao","-----------------width:MeasureSpec.EXACTLY");
            mWidth = widthSize;
            Log.w("jijinchao","widthSize="+widthSize+"---parent="+getParent());
        }else if (MeasureSpec.AT_MOST == widthMode){ //wrap_content
            Log.w("jijinchao","-----------------width:MeasureSpec.AT_MOST");
            int imgWidth = getPaddingLeft()+getPaddingRight()+imageSrc.getWidth();
            int textWidth = getPaddingLeft()+getPaddingRight()+mTextBounds.width();
            int width = Math.max(imgWidth,textWidth);
            mWidth = Math.min(width,widthSize);//这里取最小值是因为wrap_content 宽度不能超过父控件给定的值即widthSize
            Log.w("jijinchao","imgWidth="+imgWidth+"---textWidth="+textWidth+"---widthSize="+widthSize+"---parent="+getParent());
        }

        /**
         * 设置高度
         */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //match_parent or 固定大小
        if (MeasureSpec.EXACTLY == heightMode){
            Log.w("jijinchao","-----------------height:MeasureSpec.EXACTLY");
            mHeight = heightSize;
            Log.w("jijinchao","widthSize="+widthSize+"---parent="+getParent());
        }else if (MeasureSpec.AT_MOST == heightMode){
            Log.w("jijinchao","-----------------height:MeasureSpec.AT_MOST");
            int height = getPaddingTop()+mTextBounds.height()+imageSrc.getHeight()+getPaddingBottom();
            mHeight = Math.min(height,heightSize);
        }
        //重新设置大小
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        //绘制边框
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        Log.w("jijinchao","onDraw:width="+getMeasuredWidth()+"---------height="+getMeasuredHeight());

        rect.left = getPaddingLeft();
        rect.top=getPaddingTop();
        rect.right=mWidth-getPaddingRight();
        rect.bottom = mHeight - getPaddingBottom();

        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();

        //绘制文本开始的纵坐标，方式1  比较接近textview的方式，垂直方向不一定居中
        int startY = getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2;
        //绘制文本开始的纵坐标，方式2  垂直方向居中
        int startY1 = (getHeight()) / 2 - fm.descent + (fm.descent - fm.ascent)/ 2;


        //设置文字样式
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        //当字体需要的宽度大于设置的字体的宽度，显式为XXX...
        if (mTextBounds.width()>mWidth){
            TextPaint paint = new TextPaint(mPaint);
            String strText = TextUtils.ellipsize(text,paint,mWidth-getPaddingRight()-getPaddingLeft(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(strText,getPaddingLeft(),mHeight - getPaddingBottom(),mPaint);
        }else {
            //一般情况下字体居中
            canvas.drawText(text,mWidth / 2 - mPaint.measureText(text) / 2,mHeight - getPaddingBottom(),mPaint);
        }

        //取消使用掉的快
        rect.bottom -= mTextBounds.height();
        if (scaleType == IMAGE_SCALE_FITXY){
            canvas.drawBitmap(imageSrc, null, rect, mPaint);
        }else {
            rect.left = mWidth/2-imageSrc.getWidth()/2;
            rect.right=mWidth/2+imageSrc.getWidth()/2;
            rect.top = (mHeight-mTextBounds.height())/2-imageSrc.getHeight()/2;
            rect.bottom = (mHeight-mTextBounds.height())/2+imageSrc.getHeight()/2;
            canvas.drawBitmap(imageSrc,null,rect,mPaint);
        }

    }
}
