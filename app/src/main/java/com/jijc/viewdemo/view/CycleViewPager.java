package com.jijc.viewdemo.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.utils.DisplayImageOptionsCfg;
import com.jijc.viewdemo.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Description:可以无限轮播的ViewPager
 * Created by jijc on 2016/12/21.
 * PackageName: com.jijc.viewdemo.view
 */
public class CycleViewPager extends ViewPager {
    private OnItemInitLisenter onItemInitListener;

    public CycleViewPager(Context context) {
        super(context);
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 矫正adapter
     * @param adapter
     */
    @Override
    public void setAdapter(PagerAdapter adapter) {
        //假的监听 解决 cycleviewpager 不设置 OnPageChangeListener 时不修正 listener 的问题
        addOnPageChangeListener(new SimpleOnPageChangeListener());
        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter(adapter);
        super.setAdapter(innerPagerAdapter);
        //添加首位后将viewpager定位到position=1的位置【ABCD】-->【DABCDA】
        setCurrentItem(1,false);
    }

    /**
     * 矫正listener
     * @param listener
     */
    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        InnerPageChangeListener innerPageChangeListener = new InnerPageChangeListener(listener);
        super.addOnPageChangeListener(innerPageChangeListener);
    }

    /**
     * 设置图片和adapter
     * @param imgList
     * @param mContext
     */
    public void setImages(final ArrayList<String> imgList, final Context mContext, final int layoutResId, final OnItemInitLisenter lisenter) {
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
            public Object instantiateItem(ViewGroup container, int position) {

                View view = View.inflate(mContext, layoutResId, null);
                if(lisenter!=null){
                    lisenter.onItemInitView(view,position);
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
     * 修正listener
     */
    public class InnerPageChangeListener implements OnPageChangeListener{

        private OnPageChangeListener listener;
        private int position;

        public InnerPageChangeListener(OnPageChangeListener listener){

            this.listener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (listener!=null){
                listener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }

        }

        @Override
        public void onPageSelected(int position) {
            if (listener != null){
                listener.onPageSelected(position);
            }
            this.position = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE){
                if (position == getAdapter().getCount()-1){
                    CycleViewPager.this.setCurrentItem(1,false);
                }else if(position == 0){
                    CycleViewPager.this.setCurrentItem(getAdapter().getCount()-2,false);
                }
            }
            if (listener!=null){
                listener.onPageScrollStateChanged(state);
            }
        }
    }

    /**
     * 修正adapter
     */
    public class InnerPagerAdapter extends PagerAdapter{

        private PagerAdapter adapter;

        public InnerPagerAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getCount() {
            return adapter.getCount()+2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(position == getCount()-1){
                position = 0;
            }else if (position == 0){
                position = adapter.getCount()-1;
            }else {
                position -=1;
            }
            return adapter.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           adapter.destroyItem(container,position,object);
        }
    }

    public interface OnItemInitLisenter{
        void onItemInitView(View view,int position);
    }
}
