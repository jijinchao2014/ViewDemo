package com.jijc.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.jijc.viewdemo.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> functions;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initData();
        initView();
        listView.setOnItemClickListener(this);
    }

    private void initData() {
        functions = new ArrayList<>();
        functions.add("Android 自定义View (一) ");
        functions.add("Android 自定义View (二) 进阶 ");
        functions.add("Android 自定义View (三) 圆环交替 等待效果 ");
        functions.add("Android 自定义View (四) 标签 ");
        functions.add("Android 自定义View (五) 标签 ");
        functions.add("Android 自定义View (六) 进度条 ");
        functions.add("Android 自定义View (七) 轮播图 ");
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,functions));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                startActivity(new Intent(mContext,Demo1Activity.class));
                break;
            case 1:
                startActivity(new Intent(mContext,Demo2Activity.class));
                break;
            case 2:
                startActivity(new Intent(mContext,Demo3Activity.class));
                break;
            case 3:
                startActivity(new Intent(mContext,Demo4Activity.class));
                break;
            case 4:
                startActivity(new Intent(mContext,Demo5Activity.class));
                break;
            case 5:
                startActivity(new Intent(mContext,Demo6Activity.class));
                break;
            case 6:
                startActivity(new Intent(mContext,Demo7Activity.class));
                break;
        }
    }
}
