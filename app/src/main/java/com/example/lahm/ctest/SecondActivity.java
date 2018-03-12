package com.example.lahm.ctest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    //文件拆分
//    public static native void diff(String path, String pattern, int count);

    //文件合并
//    public static native void patch(String path, String pattern, int count);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        NDKUtil.loadLibraryByName("cfile");

    }
}
