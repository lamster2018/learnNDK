# learnNDK
结合反射一起学习NDK



反射四步（代码位置MainActivity.class &     ReflectionModel.class）

1.拿class

2.拿field/method

3.设置可见性setAccess

4.调用set修改field/invoke调用method




jni调java三步(代码位置ctest.cpp)

1.FindClass拿class

2.拿GetMethodId/GetMethodId

3.调用Get***Field/Get***Field

jni调java最需要注意的就是c++和java的类型变换



jni动态注册/轮询反调试（代码位置ccheck.cpp)

1.复写jni_OnLoad方法

2.创建子线程读取proc/[pid]/status文件下的traceid值



安全检查（代码位置SecurityCheckUtils.class）

1.检查签名；

2.检查debug属性；

3.检查root；

4.检查xp框架；

5.检查端口



详细见

[android ndk开发索引](https://www.jianshu.com/p/61e42e511749)



[jni动态注册/轮询traceid/反调试学习笔记](https://www.jianshu.com/p/082456acf89c)



[反Xposed方案学习笔记](http://www.jianshu.com/p/ee0062468251)