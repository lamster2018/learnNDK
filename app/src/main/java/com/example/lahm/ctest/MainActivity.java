package com.example.lahm.ctest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("ctest");
    }

    public native String hello();

    public native String getMetaValue(String name);//jni调java方法拿清单里的shit的值

    public native int checkDebug();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkDebug();//用jni调java的方法检查是否为debug版本，如果是则干掉自己
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.content);
        textView.setText(getMetaValue("shit"));
        textView.setText(String.valueOf(checkDebug()));
        reflection();
    }

    //结合反射学习jni调用java
    private void reflection() {
        try {
            Class<?> clazz = Class.forName("com.example.lahm.ctest.ReflectionModel");
            //ClassNotFoundException，class的静态方法获取ReflectionModel类的class对象
            //必须加上包名全称，不然报ClassNotFoundException,所以用下面这个方法比较好
//            Class<?> clazz = ReflectionModel.class;

            Constructor constructorEmp = clazz.getConstructor(new Class[]{});
            //NoSuchMethodException, ReflectionModel有两个构造器，一个无参，一个 两参
            Constructor constructor = clazz.getConstructor(new Class[]{String.class, int.class});
            //获取构造器

            Object object = constructor.newInstance(new Object[]{"abc", 22});
            //构造器生成实例对象
            //IllegalAccessException,InstantiationException,InvocationTargetException

            Method setId = clazz.getMethod("setId", new Class[]{long.class});//setId方法是有参的
            //拿方法
            setId.invoke(object, new Object[]{10});
            //调用

            Method toString = clazz.getMethod("toString", new Class[]{});//toString方法无参
            String result = (String) toString.invoke(object, new Object[]{});//invoke反射调用

            TextView tv = findViewById(R.id.content);
            tv.setText(result);

            // 以上是public，以下private
            // 如果想用反射修改访问控制检查的话，
            // 获取Method和Field对象的时候一定要用getDeclaredField和getDeclaredMethod,
            // 不要用getField和getMethod。
            // 虽然这两个方法的参数都是相同的，
            // 但不同点在于getMethod和getField只能获得public修饰的属性和方法。
            // 而getDeclared可以获取任何类型的属性和方法，
            // 因为这个例子要调用私有的属性和方法，所以要用getDeclaredXX。
            Field privateParams = clazz.getDeclaredField("privateParams");
            //NoSuchFieldException
            privateParams.setAccessible(true);
            privateParams.set(object, "u change me");

            Method privateFunc = clazz.getDeclaredMethod("privateFunc", new Class[]{String.class});
            privateFunc.setAccessible(true);
            //如果反射调用静态方法，invoke的第一个参数传null---看源码解释
            privateFunc.invoke(object, new Object[]{"invoke private func"});


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * try to solve menu.setIcon not working after android 4.0
     * <p>
     * http://blog.csdn.net/stevenhu_223/article/details/9705173
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            setMenuIconVisible(menu, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMenuInflater().inflate(R.menu.abc, menu);//默认是ifRoom
        return true;
    }

    //一样是三步走，拿到对应class，找到对应的field/method ,修改可见性，修改/调用
    private void setMenuIconVisible(Menu menu, boolean visible) throws Exception {
        Class<?> menuClazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
//        Class<?> menuClazz = MenuBuilder.class;//靠谱

        //因为MenuBuilder是一个MenuBuilder为系统内部的框架类，无法调用
        //通过反射方法实现
//        Method method = menuClazz.getMethod("setOptionalIconsVisible", new Class[]{boolean.class});
//        method.setAccessible(true);
        //反射调用
//        method.invoke(menu, visible);//object 就是传进来要被使用的menu

        //通过反射private的属性mOptionalIconsVisible，
        //默认是false，实际上和反射方法修改思路一样
        //拿field
        Field mOptionalIconsVisible = menuClazz.getDeclaredField("mOptionalIconsVisible");
        //修改可见性
        mOptionalIconsVisible.setAccessible(true);
        //反射更改
        mOptionalIconsVisible.set(menu, visible);
    }
}
