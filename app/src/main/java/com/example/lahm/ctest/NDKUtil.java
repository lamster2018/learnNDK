package com.example.lahm.ctest;

/**
 * Project Name:learnNDK
 * Package Name:com.example.lahm.ctest
 * Created by lahm on 2018/3/7 下午3:51 .
 */

public class NDKUtil {
    private static volatile boolean mIsLibLoaded = false;
    private static volatile boolean mIsNativeInited = false;

    private static LibLoader localLibLoader = new LibLoader() {
        @Override
        public void loadLibrary(String libName) throws UnsatisfiedLinkError, SecurityException {
            System.loadLibrary(libName);
        }
    };

    public static void loadLibrariesOnce(LibLoader libLoader) {
        synchronized (NDKUtil.class) {
            if (!mIsLibLoaded) {
                if (libLoader == null) {
                    libLoader = localLibLoader;
                }
                libLoader.loadLibrary("antitrace");
                mIsLibLoaded = true;
            }
        }
    }

    public NDKUtil() {
        this(localLibLoader);
    }

    public NDKUtil(LibLoader libLoader) {
        initNDK(libLoader);
    }

    private void initNDK(LibLoader libLoader) {
        loadLibrariesOnce(libLoader);
        initNativeOnce();
    }

    private void initNativeOnce() {
        synchronized (NDKUtil.class) {
            if (!mIsNativeInited) {
//                native_init();
                mIsNativeInited = true;
            }
        }
    }

//    private static native void native_init();
}
