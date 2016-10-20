package com.jijc.viewdemo.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.bbtree.tts.TTS;

/**
 * Created by dev on 2015/8/19.
 */
public class VoiceConnectUtils {
    private final static String TAG = VoiceConnectUtils.class.getSimpleName();
    private final static boolean Debug = true;
    private static VoiceConnectUtils voiceConnectUtils = new VoiceConnectUtils();
    public TTS mRemoteService = null;
    public ServiceConnection mRemoteConnection = null;
    private static Context mContext;

    private VoiceConnectUtils(){

    }

    public static VoiceConnectUtils getInstance(Context context){
        mContext = context;
        return voiceConnectUtils;
    }

    public void getVoiceConnect(){
        if (CheckPackageUtils.checkPackage("com.bbtree.tts", mContext) && mRemoteConnection == null && mRemoteService == null) {
            mRemoteConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder service) {
                    mRemoteService = TTS.Stub.asInterface(service);
                    Log.i("jijinchao", "人工语音插件连接成功");
                }

                public void onServiceDisconnected(ComponentName className) {
                    Log.i("jijinchao","人工语音插件断开连接");
                    mRemoteService = null;
                    mRemoteConnection = null;
                    getVoiceConnect();
                }
            };

            Intent tts_intent = new Intent("com.bbtree.tts.action.read");
            tts_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            tts_intent.setComponent(new ComponentName("com.bbtree.tts", "com.bbtree.tts.TTSService"));
            try {
                mContext.bindService(tts_intent, mRemoteConnection, mContext.BIND_AUTO_CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void voiceDisconnect(){
        if (CheckPackageUtils.checkPackage("com.bbtree.tts", mContext) && mRemoteConnection != null) {
            mContext.unbindService(mRemoteConnection);
            mRemoteService = null;
            mRemoteConnection = null;
            Log.i("jijinchao", "智慧树关闭，人工语音插件断开连接");
        }
    }

}
