package com.jijc.viewdemo.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.jijc.viewdemo.R;


/**
 * Created by zhou on 14-6-6.
 */
public class RoundBitmapUtils {
    /**
     * 刷新当前imageView drawable
     *
     * @param url
     * @param img
     */
    public static void roundImageView(String url, final ImageView img) {
       roundImageView(url, img, R.drawable.default_avatar);
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//        ImageLoader.getInstance().displayImage(url, img,
//                DisplayImageOptionsCfg.getInstance().getOptions(R.drawable.default_avatar, new CircleRoundedBitmapDisplayer(0)));
//        ImageLoader.getInstance().displayImage(url, img, DisplayImageOptionsCfg.getInstance().getOptions(new MyRoundedBitmapDisplayer(img.getWidth())));
//        ImageLoader.getInstance().displayImage(url, img, DisplayImageOptionsCfg.getInstance().getOptions(R.drawable.default_avatar), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//                img.setImageResource(R.drawable.default_avatar);
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                img.setImageResource(R.drawable.default_avatar);
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                if (bitmap == null) {
//                    img.setImageResource(R.drawable.default_avatar);
//                    return;
//                }
//                Bitmap bmp = BitmapUtils.toRoundBitmap(bitmap);
//                img.setImageBitmap(bmp);
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//            }
//        });
    }

    public static void roundImageView(String url, final ImageView img,int defResId) {
        ImageLoader.loadImageAsync(img,url,
                DisplayImageOptionsCfg.getInstance().getOptions(defResId, new CircleRoundedBitmapDisplayer(0)));

    }

    /**
     * 直接通过tag判断是否当前图片已加载
     * @param url
     * @param img
     * @param defResId
     */
    public static void roundImageViewWithTag(String url, final ImageView img,int defResId) {
        Object object=img.getTag();
        if(object==null||!TextUtils.equals(url, (CharSequence) object)){
            img.setTag(url);
            if (TextUtils.isEmpty(url)||url.lastIndexOf("/")==url.length()-1) {
                if(defResId==0)
                    return;
                try {
                    img.setImageResource(defResId);
                } catch (Throwable e) {
                }
                return;
            }
            ImageLoader.loadImageAsync(img,url,
                    DisplayImageOptionsCfg.getInstance().getOptions(defResId, new CircleRoundedBitmapDisplayer(0)));
        }

    }
}
