package com.jijc.viewdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.view.SpringProgressBar;

public class Demo6Activity extends AppCompatActivity {

    private SpringProgressBar spb;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo6);
        spb = (SpringProgressBar) findViewById(R.id.spb);
        tv = (TextView) findViewById(R.id.tv);
        spb.setMaxCount(15);
        spb.setCurrentCount(12);
        tv.setText(12+"/"+15);
    }
}
