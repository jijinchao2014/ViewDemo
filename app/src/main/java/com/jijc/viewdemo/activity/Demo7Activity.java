package com.jijc.viewdemo.activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.utils.DisplayImageOptionsCfg;
import com.jijc.viewdemo.utils.ImageLoader;
import com.jijc.viewdemo.view.CycleViewPager;

import java.util.ArrayList;

public class Demo7Activity extends AppCompatActivity {

    private CycleViewPager viewpager;
    private ArrayList<String> imgList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_demo7);
        imgList.add("http://ad5.bbtree.com/ad-test/R4D5wwbpnfC_1482127068443.jpg");
        imgList.add("http://ad5.bbtree.com/ad-test/vAV1qGjk5GB_1481875269227.png");
        imgList.add("http://ad5.bbtree.com/ad-test/GBn8Yvuz2kG_1482286543893.jpg");
        imgList.add("http://ad5.bbtree.com/ad-test/181KXS7wJRm_1482288039170.jpg");
        viewpager = (CycleViewPager) findViewById(R.id.viewpager);
        viewpager.setImages(imgList, mContext,R.layout.item_cycle_pager ,new CycleViewPager.OnItemInitLisenter() {
            @Override
            public void onItemInitView(View view, int position) {
                ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
                TextView tv_play_count = (TextView) view.findViewById(R.id.tv_play_count);
                tv_play_count.setText("这是第" + (position + 1) + "个图片");
                ImageLoader.loadImageAsync(iv_img, imgList.get(position), DisplayImageOptionsCfg.getInstance().getOptions(R.drawable.item_live_bg));
            }
        });

    }
}
