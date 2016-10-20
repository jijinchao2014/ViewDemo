package com.jijc.viewdemo.activity;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
