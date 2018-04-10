package com.jackie.bookpagerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackie.bookpagerview.utils.AppShortCutUtil;

/**
 * Created by Jackie on 2018/4/10.
 */

public class IconTestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_test);

//        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//        intent.putExtra("badge_count", 2);
//        intent.putExtra("badge_count_package_name", this.getPackageName());
//        intent.putExtra("badge_count_class_name", "com.jackie.bookpagerview.IconTestActivity");
//        this.sendBroadcast(intent);

//        AppShortCutUtil.addNumShortCut(this,MainActivity.class,true,"10",true);
    }
}
