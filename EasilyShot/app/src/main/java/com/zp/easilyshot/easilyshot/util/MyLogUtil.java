package com.zp.easilyshot.easilyshot.util;

import android.util.Log;

import com.zp.easilyshot.easilyshot.BuildConfig;


public class MyLogUtil {
    /**
     * 输出log,error级别的
     *
     * @param tag
     * @param info
     */
    public static void showLogE(String tag, String info) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, info);
        }
    }

    public static void showLogI(String tag, String info) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, info);
        }
    }
}
