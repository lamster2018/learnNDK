package com.example.lahm.ctest;

import android.app.Application;
import android.content.Context;
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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        boolean isOwnApp = Utils.isOwnApp();
        Log.i(TAG, "isownapp:" + isOwnApp);
        if (!isOwnApp) {
            Log.i(TAG, "is not own app...exit app");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
