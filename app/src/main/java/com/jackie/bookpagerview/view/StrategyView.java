package com.jackie.bookpagerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jackie on 2018/9/3.
 * 策略坐标轴view
 */
public class StrategyView extends View {
    private Context context;

    public StrategyView(Context context) {
        super(context);
        init(context);
    }

    public StrategyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StrategyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){

    }
}
