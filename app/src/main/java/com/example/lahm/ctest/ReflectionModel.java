package com.example.lahm.ctest;

import android.util.Log;

/**
 * Project Name:learnNDK
 * Package Name:com.example.lahm.ctest
 * Created by lahm on 2018/2/6 下午10:19 .
 */

public class ReflectionModel {
    private long id;
    private int age;
    private String name;

    public ReflectionModel() {

    }

    public ReflectionModel(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {

        return "Name=" + getName() + "  Age=" + getAge() + "  Id=" + getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void privateFunc(String s) {
        Log.i("xxxxxxxxxx", "privateFunc: " + s + privateParams);
    }

    private String privateParams = "u ?? me";

}
