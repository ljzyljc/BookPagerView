package com.jackie.bookpagerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackie.bookpagerview.view.StrategyAxisView;

/**
 * Created by Jackie on 2018/9/11.
 */
public class TestAxisActivity extends Activity {
    private StrategyAxisView strategy_axis_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axis);
        strategy_axis_view = findViewById(R.id.strategy_axis_view);

//        strategy_axis_view.post(new Runnable() {
//            @Override
//            public void run() {
//                strategy_axis_view.startAnimation();
//            }
//        });
    }
}
