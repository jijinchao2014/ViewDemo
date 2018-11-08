package com.jijc.viewdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.jijc.viewdemo.BaseApp;

import static android.content.Context.WINDOW_SERVICE;

public class UIUtils {

	private static Toast toast;

	/**
	 * 静态吐司
	 * 
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}
/**
 * 不需要上下文对象的  静态toast
 */
	public static void showToast(String text) {
		showToast(getContext(), text);
	}

	/**
	 * 通过id 获取string-array
	 * 
	 * @param tabNames
	 */
	public static String[] getStringArray(int tabNames) {
		return getResources().getStringArray(tabNames);
	}

	private static Resources getResources() {
		return getContext().getResources();
	}

	/**
	 * 获取上下文对象
	 * 
	 * @return
	 */
	public static Context getContext() {
		return BaseApp.getInstance();
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** px转换dip */

	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 根据布局id 创建view对象
	 * 
	 * @param id
	 * @return
	 */
	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	/**
	 * R.drawable.id---->drawable
	 * 
	 * @param nothing
	 * @return
	 */
	public static Drawable getDrawable(int nothing) {
		return getContext().getResources().getDrawable(nothing);
	}

	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
	/**
	 * 通过id  获取string
	 * @param id
	 * @return
	 */
	public static String getString(int id) {
		return getResources().getString(id);
	}
//	public static void startActivity(Intent intent) {
//		// 如果有Activity  可以直接打开 如果 当前不是一个Activity  应该  指定任务栈
//		if (BaseActivity.activity == null) {
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//指定一个新的任务栈
//			getContext().startActivity(intent);
//		}else{
//			BaseActivity.activity .startActivity(intent);
//		}
//
//	}
}
