package com.jijc.viewdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.view.SnakeProgressBar;
import com.jijc.viewdemo.view.SnakeProgressBar2;

public class Demo9Activity extends AppCompatActivity {

    private SnakeProgressBar2 spb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo9);
        spb = (SnakeProgressBar2) findViewById(R.id.spb);
        spb.setTotalStep(21);
        spb.setCurrentStep(13);
        spb.setMaxStep(7);
    }
}
