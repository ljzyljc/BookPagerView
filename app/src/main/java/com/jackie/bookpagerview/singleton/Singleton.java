package com.jackie.bookpagerview.singleton;

import android.util.Log;

/**
 * Created by Jackie on 2018/3/30.
 */
 //饿汉模式
public class Singleton {
    private static final String TAG = "Singleton";
    private Singleton(){
        Log.i(TAG, "Singleton: ------");
        if (instance != null){
            throw new IllegalArgumentException("不可通过反射来调用构造器");
        }
    }
    public static Singleton getInstance(){
        return instance;
    }

    private static Singleton instance = new Singleton();
    
    public void doSomething(){
        Log.i(TAG, "doSomething: ----------");
    }
}
