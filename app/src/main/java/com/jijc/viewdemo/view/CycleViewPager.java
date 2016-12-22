package com.jijc.viewdemo.view;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jijc.viewdemo.activity.Demo7Activity;
import com.jijc.viewdemo.utils.UIUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:可以无限轮播的ViewPager
 * Created by jijc on 2016/12/21.
 * PackageName: com.jijc.viewdemo.view
 */
public class CycleViewPager extends ViewPager {
    private LinearLayout ll_pointer;
    private Timer timer;
    private Context mContext;
    private OnItemInitLisenter mOnItemInitLisenter;
    private float downX;
    private float downY;
    private long downTime;
    private long millisecond; //viewpager滚动间隔时间
    private boolean isRunnin;

    public CycleViewPager(Context context) {
        super(context);
        mContext = context;
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /**
     * 矫正adapter
     *
     * @param adapter
     */
    @Override
    public void setAdapter(PagerAdapter adapter) {
        //假的监听 解决 cycleviewpager 不设置 OnPageChangeListener 时不修正 listener 的问题
        addOnPageChangeListener(new SimpleOnPageChangeListener());
        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter(adapter);
        super.setAdapter(innerPagerAdapter);
        //添加首位后将viewpager定位到position=1的位置【ABCD】-->【DABCDA】
        setCurrentItem(1, false);

        /**
         * viewpager 切换的动画 更多动画参考：https://github.com/jijinchao2014/ViewPagerAnimation
         * 该库来源于AndroidMsky  对其表示感谢
         */
        setPageTransformer(true, new DepthPageTransformer());
//        setPageTransformer(true, new ZoomOutPageTransformer());
    }

    /**
     * 矫正listener
     *
     * @param listener
     */
    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        InnerPageChangeListener innerPageChangeListener = new InnerPageChangeListener(listener);
        super.addOnPageChangeListener(innerPageChangeListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //防止多指误操作
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action){
            case MotionEvent.ACTION_DOWN:
//                Log.w("jijinchao","action--------MotionEvent.ACTION_DOWN");
                downX = ev.getX();
                downY = ev.getY();
                downTime = SystemClock.uptimeMillis();
                stopRoll();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.w("jijinchao","action--------MotionEvent.ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
//                Log.w("jijinchao","action--------MotionEvent.ACTION_UP:"+getCurrentItem());
                if (isRunnin){
                    startRoll(millisecond);
                }
                float upX = ev.getX();
                float upY = ev.getY();
                long upTime = SystemClock.uptimeMillis();
                if (Math.abs(upX-downX) < 15 && Math.abs(upY - downY) < 15 && upTime-downTime<500){
                    if (mOnItemInitLisenter!=null){
                        mOnItemInitLisenter.onItemClick(getCurrentItem()-1);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
//                Log.w("jijinchao","action--------MotionEvent.ACTION_CANCEL");
                if (isRunnin){
                    startRoll(millisecond);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 设置图片和adapter
     *
     * @param imgList
     * @param mContext
     */
    public void setImages(final Context mContext, final ArrayList<String> imgList, final int layoutResId, final OnItemInitLisenter lisenter) {
        this.mOnItemInitLisenter = lisenter;
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                View view = View.inflate(mContext, layoutResId, null);
                if (lisenter != null) {
                    lisenter.initItemView(view, position);
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }

    /**
     * 动态添加指示点
     *
     * @param mContext 上下文
     * @param ll_point 放置指示点的布局
     * @param drawable 指示点的样式 是一个selector，并且通过“state_enabled”来区分选中与非选中
     * @param size 列表里的个数
     */
    public void addPoints(Context mContext,int drawable, LinearLayout ll_point, int size) {
        ll_pointer = ll_point;
        //只有一张图片时不显示指示点
        if (size < 2) {
            ll_point.setVisibility(GONE);
        } else {
            ll_point.setVisibility(VISIBLE);
            for (int i = 0; i < size; i++) {
                ImageView pointer = new ImageView(mContext);
                pointer.setBackgroundResource(drawable);

                //第一个点选中
                if (i == 0) {
                    pointer.setEnabled(true);
                } else {
                    pointer.setEnabled(false);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = UIUtils.dip2px(8);
                ll_point.addView(pointer, params);
            }
        }
    }

    private int lastPos;

    /**
     * 修正listener
     */
    public class InnerPageChangeListener implements OnPageChangeListener {

        private OnPageChangeListener listener;
        private int position;

        public InnerPageChangeListener(OnPageChangeListener listener) {

            this.listener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (listener != null) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

        }

        @Override
        public void onPageSelected(int position) {
            if (listener != null) {
                listener.onPageSelected(position);
            }
            this.position = position;
            //修正position 解决指示点在首位切换时延时问题
            if (position == getAdapter().getCount() - 1) {
                position = 1;
            } else if (position == 0) {
                position = getAdapter().getCount() - 2;
            }
//            Log.w("jijinchao", "-----pos:" + position + "------count:" + getAdapter().getCount());
            View lastChild = ll_pointer.getChildAt(lastPos);

            //选中的指示点和非选中的指示点如果大小不同时需要重新设置LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.rightMargin = UIUtils.dip2px(8);
            if (lastChild != null) {
                lastChild.setLayoutParams(params);
                lastChild.setEnabled(false);
            }
            View curChild = ll_pointer.getChildAt(position - 1);
            if (curChild != null) {
                curChild.setLayoutParams(params);
                curChild.setEnabled(true);
            }
            lastPos = position - 1;
            Log.w("jijinchao","postion******"+position);
            if (mOnItemInitLisenter!=null){
              mOnItemInitLisenter.onItemVisible(position-1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (position == getAdapter().getCount() - 1) {
                    CycleViewPager.this.setCurrentItem(1, false);
                } else if (position == 0) {
                    CycleViewPager.this.setCurrentItem(getAdapter().getCount() - 2, false);
                }
            }
            if (listener != null) {
                listener.onPageScrollStateChanged(state);
            }
        }
    }

    /**
     * 修正adapter
     */
    public class InnerPagerAdapter extends PagerAdapter {

        private PagerAdapter adapter;

        public InnerPagerAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getCount() {
            return adapter.getCount() + 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == getCount() - 1) {
                position = 0;
            } else if (position == 0) {
                position = adapter.getCount() - 1;
            } else {
                position -= 1;
            }
            return adapter.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            adapter.destroyItem(container, position, object);
        }
    }

    public interface OnItemInitLisenter {
        /**
         * 初始化CycleViewPager的item布局，将主动权交给开发者，开发者可以通过传入的布局资源初始化成view<br/>
         * 这个方法将完成加载的view返回给开发者，从而可以方便的设置ViewPager的item的图片文字等
         * @param view     通过传入的资源文件生成的view
         * @param position 当前的位置
         */
        void initItemView(View view, int position);

        /**
         * CycleViewPager的item点击事件
         * @param position
         */
        void onItemClick(int position);

        /**
         * viewpager条目可见时调用，处理广告的曝光建议在此回调中进行
         * @param position
         */
        void onItemVisible(int position);
    }

    /**
     * 开启自动轮播
     * @param millisecond 轮播间隔
     */
    public void startRoll(long millisecond) {
        this.millisecond = millisecond;
        isRunnin = true;
        //只有一张图片时不滚动
        if (getAdapter().getCount() - 2 < 2) {
            return;
        }
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Demo7Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCurrentItem(getCurrentItem() + 1);
                    }
                });
            }
        };
        timer.schedule(timerTask, millisecond, millisecond);
    }

    /**
     * 轮播间隔
     */
    public void stopRoll() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }
}
