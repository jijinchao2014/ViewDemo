package com.jijc.viewdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.utils.UIUtils;
import com.jijc.viewdemo.view.MFlowLayout;

import java.util.Random;

/**
 * Description:
 * Created by jijc on 2016/11/16.
 * PackageName: com.jijc.viewdemo.activity
 */
public class Demo5Activity extends AppCompatActivity {
    private String[] datas = new String[]{ "安全必备", "音乐", "父母学", "上班族必备",
            "360手机卫士", "QQ","输入法", "微信", "最美应用", "AndevUII"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo5);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        // LinearLayout layout = new LinearLayout(UIUtils.getContext());
        // layout.setOrientation(LinearLayout.VERTICAL);
        MFlowLayout layout = new MFlowLayout(UIUtils.getContext());
        int padding = UIUtils.dip2px(13);
        layout.setPadding(padding, padding, padding, padding);
        int backColor = 0xffcecece; // 按下的颜色
        Random random = new Random();
        for (int i = 0; i < datas.length; i++) {
            final String text = datas[i];
            TextView textView = new TextView(UIUtils.getContext());
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textView.setTextColor(Color.WHITE);
            int textPaddingV = UIUtils.dip2px(5);
            int textPaddingH = UIUtils.dip2px(13);
            textView.setPadding(textPaddingH, textPaddingV, textPaddingH,
                    textPaddingV);
            int red = random.nextInt(200) + 20; // 0- 255 --- 20 --->219
            int green = random.nextInt(200) + 22; // 0- 255
            int blue = random.nextInt(200) + 22; // 0- 255
            int color = Color.rgb(red, green, blue);
            textView.setBackgroundColor(Color.GRAY);
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    UIUtils.showToast(text);
                }
            });
            layout.addView(textView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, -2));
        }
        scrollView.addView(layout);
    }
}
