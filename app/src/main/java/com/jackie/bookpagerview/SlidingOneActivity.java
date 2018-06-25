package com.jackie.bookpagerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.jackie.bookpagerview.view.SlidingTwoActivity;

/**
 * Created by Jackie on 2018/4/24.
 */
public class SlidingOneActivity extends SlideActivity {
    Button button;
    private static final String TAG = "SlidingOneActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_one);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SlidingOneActivity.this, SlidingTwoActivity.class);
//                SlidingOneActivity.this.startActivity(intent);
//                showUpdateSuccessDialog();
            }
        });
    }

//    private void showUpdateSuccessDialog(){
//        Log.i(TAG, "showUpdateSuccessDialog: ");
//        WindowManager wm = (WindowManager) SlidingOneActivity.this.getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
////        layoutParams.height = -2;
////        layoutParams.width = -2;
////        layoutParams.format = 1;
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//        layoutParams.t
//
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_dialog,null);
//        wm.addView(view,layoutParams);
//    }
}
