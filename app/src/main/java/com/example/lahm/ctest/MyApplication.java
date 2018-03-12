package com.example.lahm.ctest;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Field;

/**
 * Project Name:learnNDK
 * Package Name:com.example.lahm.ctest
 * Created by lahm on 2018/2/25 下午1:32 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */

public class MyApplication extends Application {
    private static String TAG = "ceshi";
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public native boolean isEquals(String fck);

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
//        System.loadLibrary("antitrace");
        System.loadLibrary("ccheck");
        antiXposedInject();
    }

    /**
     * 没有类，没有参说明没有xposed注入，但是如果setAccess失败，说明xposed注入了，但我们修改失败了
     */
    private void antiXposedInject() {
        Field xpdisableHooks = null;
        try {
            xpdisableHooks = ClassLoader.getSystemClassLoader()
                    .loadClass("de.robv.android.xposed.XposedBridge")
                    .getDeclaredField("disableHooks");
            xpdisableHooks.setAccessible(true);
            xpdisableHooks.set(null, Boolean.TRUE);
        } catch (NoSuchFieldException e) {
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
            System.exit(1);
        }
    }
}
