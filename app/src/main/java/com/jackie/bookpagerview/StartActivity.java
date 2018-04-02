package com.jackie.bookpagerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackie.bookpagerview.singleton.Singleton;
import com.jackie.bookpagerview.singleton.SingletonBest;
import com.jackie.bookpagerview.singleton.SingletonDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Jackie on 2018/3/29.
 */

public class StartActivity extends Activity {
    Singleton singleton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---------------1--------------饿汉模式----------------------------
//        //只调用一次
//        singleton = Singleton.getInstance();
//        singleton.doSomething();
//        singleton = Singleton.getInstance();
//        singleton.doSomething();
//
//        //通过放射调用
//        Class<Singleton> clazz = Singleton.class;
//        //拿到构造器
//        try {
//            Constructor<Singleton> constructor = clazz.getDeclaredConstructor();
//            constructor.setAccessible(true);
//            constructor.newInstance().doSomething();   //这样单例私有构造器又被重新调用了一次
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        //-----------------2-----------静态内部类，同时又是安全的，类似懒汉模式的效果
//        SingletonDemo singletonDemo;
//        singletonDemo = SingletonDemo.getInstance();

        //-----------------3----------枚举实现方式-------------------------------
//        SingletonBest.INSTANCE.otherMethod();







    }
}
