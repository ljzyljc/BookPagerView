package com.jackie.bookpagerview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by Jackie on 2018/4/8.
 */

public class TestPopActivity extends BaseActivity {
    private Button pop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_wind);
        pop = (Button)findViewById(R.id.pop);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow();
            }
        });
        linear_main=findViewById(R.id.linear_main);

    }

    LinearLayout linear_main;

    PopupWindow popWindow;
    private void showPopwindow() {
//        View parent = this.findViewById(R.id.linear_main);
        View popView = LayoutInflater.from(TestPopActivity.this).inflate(R.layout.activity_popup, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();


        popWindow = new PopupWindow(popView,width,height);
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(false);// 设置同意在外点击消失
        popWindow.setHeight(120);
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popWindow.setBackgroundDrawable(dw);

        popWindow.showAtLocation(pop, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
