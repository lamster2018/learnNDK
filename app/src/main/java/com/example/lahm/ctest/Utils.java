package com.example.lahm.ctest;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Project Name:learnNDK
 * Package Name:com.example.lahm.ctest
 * Created by lahm on 2018/2/25 下午1:44 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */

public class Utils {
    private static final String TAG = "ceshi";
    private static final String APP_SIGN = "308202b9308201a1a003020102020405af33a4300d06092a864886f70d01010b0500300d310b3009060355040613023836301e170d3137313131313032313633335a170d3432313130353032313633335a300d310b300906035504061302383630820122300d06092a864886f70d01010105000382010f003082010a0282010100c98bea8924c6c3e5258cc61068dcf9c239056086aede80830f76bd120feb7004b0d524a288653b5845fcfe8a0dd562cdb388ef0aa3bff98d8124953b76afcb3113e04abbcdbf4c505f42739d2a37118be0be3195ab811be072e18faa6d7350e1be62049f82b02bdfe18ff2ebafeb06b67d7ffebe2a1d1e59faa9544a2c652fdff18bb0933c197350c310c25eb7a66b1501ac4f321c3311bfd130c456177abf3d5df254f12db499e719ead6cecebf85f3a3a1ae31e43f2c46f6ef5a509908041a67863f1e2587264715947ca01bd7aa68568d7ef4709604ad4e999e5383004f5d76426ce4e05216153cb4972fc35d0a7f124f1376038077b0be2359f660f534e50203010001a321301f301d0603551d0e04160414038bd8206cc6e761f8fb60eec7194ce77b91b9af300d06092a864886f70d01010b0500038201010064f665f0c3f72f0139fc963bc47510b9b9ce1d2ff90c0ee5689bbfaa34f063a0996a06096e58a0fd939ef0b61b8924936fee57ba2af8f5f48a5e5e98302a3700d381b33d375878e8d09f4eb3698e30e4086900165b050d21bd5fdb92d9a096cab0b27e139a8dfa1a1cde592f811da26997c90e153ea12ab8b85c721678dc5728b140683dd13191d0db21e43a82531c229e2c183ae900cc33875c63b64a59bc34862f3dabb88701198982325afc92fe0011825bc055627ca5e3cbea98243d24b72634a3153dff52dc63ed5fbdeb473413552cf96bc86fc684333a24ef486d143f54cbfa9386e7c038eb177727dcf9891538871376f2f6f267d371a3c110069664";

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

    /***
     *  true:already in using  false:not using
     * @param port
     */
    public static boolean isLoclePortUsing(int port) {
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
        }
        return flag;
    }

    /***
     *  true:already in using  false:not using
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (IOException e) {
        }
        return flag;
    }
}
