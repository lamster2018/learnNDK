package com.example.lahm.ctest;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

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
        System.loadLibrary("ccheck");
        boolean isOwnApp = Utils.isOwnApp();
        if (!isOwnApp) {
            Log.i(TAG, "is not own app...exit app");
            Process.killProcess(Process.myPid());
        }
    }

}
