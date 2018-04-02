package com.jackie.bookpagerview.singleton;

import android.util.Log;

/**
 * Created by Jackie on 2018/3/30.
 */
//静态内部类加载
public class SingletonDemo {

    private static class SingletonHolder{
        private static SingletonDemo instance = new SingletonDemo();
    }
    private SingletonDemo(){
        Log.i("SingletonDemo","单例类已经加载");
    }
    public static SingletonDemo getInstance(){
        return SingletonHolder.instance;
    }

}
