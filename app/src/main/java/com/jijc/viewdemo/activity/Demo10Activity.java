package com.jijc.viewdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.view.PileLayout;

import java.util.ArrayList;

public class Demo10Activity extends AppCompatActivity {

    private PileLayout pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo10);
        pl = (PileLayout) findViewById(R.id.pl_ava);
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        pl.initView(this,arrayList,R.layout.item_ava);
    }
}
