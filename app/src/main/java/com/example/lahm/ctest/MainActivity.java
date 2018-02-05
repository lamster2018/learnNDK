package com.example.lahm.ctest;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("ctest");
    }

    public native String hello();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkDebug();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.content);
        textView.setText(getMetaValue("shit"));
        textView.setText(String.valueOf(checkDebug()));
    }

    public native String getMetaValue(String name);

    private String getApplicationMetaValue(String name) {
        String value = "";
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public native int checkDebug();

    private boolean checkIsDebug() {
        try {
            PackageManager packageManager = getPackageManager();

            String packageName = getPackageName();

            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Process.killProcess(Process.myPid());
            return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
