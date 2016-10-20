package com.jijc.viewdemo.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by dev on 2015/8/18.
 */
public class CheckPackageUtils {

    /**
     * 检测语音包所对应的应用是否存在
     * @param packageName
     * @return
     */
    public static boolean checkPackage(String packageName,Context mContext)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try
        {
            mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
