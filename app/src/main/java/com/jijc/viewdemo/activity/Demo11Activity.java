package com.jijc.viewdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.view.SnakeProgressBar;
import com.jijc.viewdemo.view.SnakeProgressBar2;

import static com.jijc.viewdemo.R.id.spb;

public class Demo11Activity extends AppCompatActivity {

    private SnakeProgressBar spb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo11);
        spb = (SnakeProgressBar) findViewById(R.id.spb);
        spb.setTotalStep(14);
        spb.setCurrentStep(13);
        spb.setMaxStep(7);
    }
}
