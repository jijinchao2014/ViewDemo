package com.jijc.viewdemo.activity;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jijc.viewdemo.R;
import com.jijc.viewdemo.utils.CheckPackageUtils;
import com.jijc.viewdemo.utils.VoiceConnectUtils;
import com.jijc.viewdemo.view.MTextView;

public class Demo1Activity extends AppCompatActivity implements View.OnClickListener {

    private VoiceConnectUtils voiceConnectUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        MTextView tv = (MTextView) findViewById(R.id.tv);

        String str="{'data':[{'compTime':'17:17','jdr':'导购1','method':'2','suegue':'2'},{'compTime':'22:17','jdr':'导购2','method':'2','suegue':'2'},{'compTime':'3:17','jdr':'导购3','method':'2','suegue':'2'}]}";
        Gson gson = new Gson();
        TestBean testBean = gson.fromJson(str, TestBean.class);

        tv.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Demo1Activity.this, "dddddd", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        voiceConnectUtils = VoiceConnectUtils.getInstance(getApplicationContext());
        voiceConnectUtils.getVoiceConnect();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(Demo1Activity.this, "aaa", Toast.LENGTH_SHORT).show();
        if (CheckPackageUtils.checkPackage("com.bbtree.tts", this)){
            try {
                if (voiceConnectUtils!= null && voiceConnectUtils.mRemoteConnection != null && voiceConnectUtils.mRemoteService != null) {
                    voiceConnectUtils.mRemoteService.readText("welcome to china");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceConnectUtils != null) {
            voiceConnectUtils.voiceDisconnect();
        }
    }
}
