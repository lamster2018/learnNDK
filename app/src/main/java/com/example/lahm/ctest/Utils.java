package com.example.lahm.ctest;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/**
 * Project Name:learnNDK
 * Package Name:com.example.lahm.ctest
 * Created by lahm on 2018/2/25 下午1:44 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */

public class Utils {
    private static final String APP_SIGN = "aaa";

    public static boolean isOwnApp() {
        String signStr = getSignature();
        return APP_SIGN.equals(signStr);
    }

    public static String getSignature() {
        Context ctx = MyApplication.getContext();
        try {
            //通过包管理器获得指定包名包含签名的包信息
            PackageInfo packageInfo = ctx.
                    getPackageManager()
                    .getPackageInfo(ctx.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            // 通过返回的包信息获得签名数组
            Signature[] signatures = packageInfo.signatures;
            // 循环遍历签名数组拼接应用签名
            StringBuilder builder = new StringBuilder();
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            // 得到应用签名
            return builder.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 拿appInfo的值去判断
     *
     * @return
     */
    public static boolean checkIsDebugA() {
        return (MyApplication.getContext().getApplicationInfo().flags
                & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 直接调用debug
     *
     * @return
     */
    public static boolean checkIsDebugB() {
        return android.os.Debug.isDebuggerConnected();
    }

    private String getApplicationMetaValue(String name) {
        ApplicationInfo appInfo = MyApplication.getContext().getApplicationInfo();
        return appInfo.metaData.getString(name);
    }
}
