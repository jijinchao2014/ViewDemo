package com.jijc.viewdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.bean.ContactsBean;

import java.io.Serializable;
import java.util.ArrayList;

public class Demo12Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_single;
    private TextView tv_multi;
    private int mSelectType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo12);
        tv_single = findViewById(R.id.tv_single);
        tv_multi = findViewById(R.id.tv_multi);

        tv_single.setOnClickListener(this);
        tv_multi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_single:
                mSelectType = ContactActivity.SINGLE_SELECTION;
                Intent intent = new Intent(this, ContactActivity.class);
                intent.putExtra(ContactActivity.KEY_SELECT_TYPE,ContactActivity.SINGLE_SELECTION);
                startActivityForResult(intent,1000);
                break;
            case R.id.tv_multi:
                mSelectType = ContactActivity.MULTIPLE_SELECTION;
                Intent intent1 = new Intent(this, ContactActivity.class);
                intent1.putExtra(ContactActivity.KEY_SELECT_TYPE,ContactActivity.MULTIPLE_SELECTION);
                startActivityForResult(intent1,1001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data != null){
                ArrayList<ContactsBean> contacts = (ArrayList<ContactsBean>) data.getSerializableExtra(ContactActivity.KEY_FINISH_SELECT);
                String selects = "";
                for (ContactsBean contact: contacts) {
                    selects += contact.toString() + "\n";
                }
                if (mSelectType == ContactActivity.MULTIPLE_SELECTION){
                    tv_multi.setText(selects);
                }else {
                    tv_single.setText(selects);
                }
            }
        }
    }
}
