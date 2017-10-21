package com.jijc.viewdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.jijc.viewdemo.R;

/**
 * Description:
 * Created by JIJC on 2017/10/18.
 */

public class CircleProgressBar extends View {

    private float diameter;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;
    private Paint pointPaint;
    private Paint bgPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = -90;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(60);
    private float hintSize = dipToPx(15);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);
    private float point_radius = dipToPx(0);

    private String hintColor = "#676767";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";

    private String titleString;
    private String hintString;

    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    // sweepAngle / maxValues 的值
    private float k;

    /**
     * 总进度背景颜色
     */
    private int bgArcColor;
    /**
     * 进度条内部颜色
     */
    private int inner_color;
    private int gradient_start;
    private int gradient_middle1;
    private int gradient_middle2;
    private int gradient_end;
    /**
     * 进度条样式：0 百分比 1 计步器 2时速表
     */
    private int style;

    public CircleProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        style = a.getInt(R.styleable.CircleProgressBar_progress_style,0);
        inner_color = a.getColor(R.styleable.CircleProgressBar_inner_color, Color.TRANSPARENT);
        gradient_start = a.getColor(R.styleable.CircleProgressBar_gradient_start, Color.GREEN);
        gradient_middle1 = a.getColor(R.styleable.CircleProgressBar_gradient_middle1, gradient_start);
        gradient_middle2 = a.getColor(R.styleable.CircleProgressBar_gradient_middle2, gradient_start);
        gradient_end = a.getColor(R.styleable.CircleProgressBar_gradient_end, gradient_start);
        bgArcColor = a.getColor(R.styleable.CircleProgressBar_back_color, Color.LTGRAY);
        diameter = a.getDimension(R.styleable.CircleProgressBar_diameter, dipToPx(150));
        bgArcWidth = a.getDimension(R.styleable.CircleProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.CircleProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.CircleProgressBar_is_need_title, false);
        point_radius = a.getDimension(R.styleable.CircleProgressBar_point_radius, dipToPx(0));
        isNeedContent = a.getBoolean(R.styleable.CircleProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.CircleProgressBar_is_need_unit, false);
        hintString = a.getString(R.styleable.CircleProgressBar_string_unit);
        titleString = a.getString(R.styleable.CircleProgressBar_string_title);
        curValues = a.getFloat(R.styleable.CircleProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.CircleProgressBar_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height= (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {

        if (style == 0){
            sweepAngle = 360;
            startAngle = -90;
            isNeedDial = false;
            longdegree = dipToPx(0);
            colors = new int[50];
            for (int i = 0;i<50;i++){
                if (i<10){
                    colors[i] = gradient_start;
                }else if (i<15){
                    colors[i] = gradient_middle1;
                }else if (i<25){
                    colors[i] = gradient_middle2;
                }else if (i<49){
                    colors[i] = gradient_end;
                }else {
                    colors[i] = gradient_start;
                }
            }
        }else if (style == 1){
            sweepAngle = 270;
            startAngle = 135;
            isNeedDial = false;
            longdegree = dipToPx(0);
            colors = new int[]{gradient_start,gradient_middle1,gradient_end,gradient_start};
        }else {
            sweepAngle = 270;
            startAngle = 135;
            isNeedDial = true;
            longdegree = dipToPx(13);
            colors = new int[]{gradient_start,gradient_middle1,gradient_end,gradient_start};
        }

//        diameter = 3 * getScreenWidth() / 5;

        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.left = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.top = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(bgArcColor);
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
//        progressPaint.setStrokeJoin(Paint.Join.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        //进度指示点
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setDither(true);
        pointPaint.setStyle(Paint.Style.FILL);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(inner_color);

        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();
        rotateMatrix.setRotate(startAngle, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        //设置渐变色
        progressPaint.setShader(sweepGradient);
        pointPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);
        //圆环内透明遮罩
        canvas.drawCircle(centerX, centerY, diameter/2-progressWidth/2, bgPaint);
        //进度指示点
        if (curValues > 0 && point_radius > 0){
            if (style == 0){
                if (curValues < 3 ){
                    pointPaint.setShader(null);
                    pointPaint.setColor(gradient_start);
                }
                if (curValues > 100-point_radius){
                    pointPaint.setShader(null);
                    pointPaint.setColor(gradient_end);
                }
            }
            float smallCircleX,smallCircleY;
            smallCircleX = centerX + diameter/2 * (float)Math.cos(Math.PI*(currentAngle+startAngle)/180);
            smallCircleY = centerY + diameter/2 * (float)Math.sin(Math.PI*(currentAngle+startAngle)/180);
            canvas.drawCircle(smallCircleX, smallCircleY, point_radius, pointPaint);

        }

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3, hintPaint);
        }
        Log.i("jijc","------length:"+String.valueOf(curValues).length()+"--value:"+String.format("%.0f", curValues));
        canvas.drawText("第", centerX-vTextPaint.measureText(String.format("%.0f", curValues))/2-dipToPx(10), centerY+1*textSize/3, hintPaint);
        canvas.drawText("天", centerX+vTextPaint.measureText(String.format("%.0f", curValues))/2+dipToPx(10), centerY+1*textSize/3, hintPaint);
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3, curSpeedPaint);
        }

        invalidate();
        canvas.save();
        canvas.restore();
    }

    /**
     * 设置最大值
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle/maxValues;
    }

    /**
     * 设置当前值
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        this.titleString = title;
//        invalidate();
    }


    /**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle= (float) animation.getAnimatedValue();
                curValues = currentAngle/k;
            }
        });
        progressAnimator.start();
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
