package com.jijc.viewdemo.activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.utils.DisplayImageOptionsCfg;
import com.jijc.viewdemo.utils.ImageLoader;
import com.jijc.viewdemo.utils.ListUtils;
import com.jijc.viewdemo.view.CycleViewPager;

import java.util.ArrayList;

public class Demo7Activity extends AppCompatActivity {

    private CycleViewPager viewpager;
    private ArrayList<String> imgList = new ArrayList<>();
    private Context mContext;
    private LinearLayout ll_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_demo7);
        imgList.add("http://img6.pplive.cn/2012/09/04/18174187070.jpg");
        imgList.add("http://pic.58pic.com/58pic/13/05/48/98K58PICRJT_1024.jpg");
        imgList.add("http://pic22.nipic.com/20120621/10381980_112950399193_2.jpg");
        imgList.add("http://img6.pplive.cn/2012/09/04/16593329013.jpg");
        imgList.add("http://img6.pplive.cn/2012/05/08/15004210091.jpg");
        imgList.add("http://pic2.ooopic.com/12/11/23/11bOOOPIC21_1024.jpg");
        viewpager = (CycleViewPager) findViewById(R.id.viewpager);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
//        viewpager.addPoints(mContext,R.drawable.bg_pointer,ll_point,ListUtils.getSize(imgList));

        viewpager.addPoints(mContext,R.drawable.bg_pointer1,ll_point,ListUtils.getSize(imgList));
        viewpager.setImages(mContext,imgList,R.layout.item_cycle_pager,3000,new CycleViewPager.OnItemInitLisenter() {
            @Override
            public void initItemView(View view, int position) {

                ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
                TextView tv_play_count = (TextView) view.findViewById(R.id.tv_play_count);
                tv_play_count.setText("这是第" + (position + 1) + "个图片");
                Object tag = iv_img.getTag();
                if (tag == null || !TextUtils.equals((String)tag,imgList.get(position))) {
                    ImageLoader.loadImageAsync(iv_img, imgList.get(position), DisplayImageOptionsCfg.getInstance().getOptions(R.drawable.item_live_bg));
                    iv_img.setTag(imgList.get(position));
                }
            }

            @Override
            public void onItemClick(int position) {
                Log.w("jijinchao","onItemClick:postion------------"+position);
                Toast.makeText(Demo7Activity.this, "pos:"+(position+1)+"-url:"+imgList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemVisible(int position) {
                Log.w("jijinchao","onItemVisible:postion------------"+position);
            }
        },7);

//        viewpager.startRoll(3000);

    }

    @Override
    protected void onDestroy() {
        viewpager.stopRoll();
        super.onDestroy();
    }
}
