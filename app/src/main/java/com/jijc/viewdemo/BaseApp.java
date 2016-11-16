package com.jijc.viewdemo;

import android.app.Application;
import android.content.Context;

/**
 * application
 * 
 * @author wxj
 * 
 */
public class BaseApp extends Application {
	private static BaseApp instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		//初始化Picasso
//		ImageUtil.init();
	}
	public static Context getInstance() {
		return instance;
	}
}
