package com.jackie.bookpagerview.singleton;

import android.util.Log;

/**
 * Created by Jackie on 2018/3/30.
 */

public enum SingletonBest {

    INSTANCE;
    public void otherMethod(){
        Log.i("SingletonBest","枚举的实现方式----------");
    }

}
