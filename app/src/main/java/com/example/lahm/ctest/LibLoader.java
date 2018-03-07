package com.example.lahm.ctest;

/**
 * Project Name:learnNDK
 * Package Name:com.example.lahm.ctest
 * Created by lahm on 2018/3/7 下午3:52 .
 */

public interface LibLoader {
    void loadLibrary(String libName) throws UnsatisfiedLinkError, SecurityException;
}
