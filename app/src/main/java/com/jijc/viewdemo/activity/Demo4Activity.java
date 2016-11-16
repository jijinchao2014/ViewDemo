package com.jijc.viewdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.view.Tag;
import com.jijc.viewdemo.view.TagListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by jijc on 2016/11/16.
 * PackageName: com.jijc.viewdemo.activity
 */
public class Demo4Activity extends AppCompatActivity {
    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private final String[] titles = { "安全必备", "音乐", "父母学", "上班族必备",
            "360手机卫士", "QQ","输入法", "微信", "最美应用", "AndevUI", "蘑菇街" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4);

        mTagListView = (TagListView) findViewById(R.id.tagview);
        setUpData();
        mTagListView.setTags(mTags);
    }

    private void setUpData() {
        for (int i = 0; i < 10; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
    }
}
