package com.jackie.bookpagerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jackie.bookpagerview.view.LoveLayout;

/**
 * Created by Jackie on 2018/4/11.
 */

public class WeatherGlassActivity extends BaseActivity {
    private LoveLayout mLoveLayout;
    private WindowManager.LayoutParams mWindowParams;  //item镜像的布局参数
    private WindowManager mWindowManager;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123){
                mLoveLayout.addLove();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_glass);
        mLoveLayout=(LoveLayout)findViewById(R.id.id_love_layout);
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(0x123);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
//        createDragImg();
    }

    //创建拖动的镜像
    private void createDragImg(){
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.format = PixelFormat.TRANSLUCENT;  //图片之外其他地方透明
        mWindowParams.gravity = Gravity.TOP|Gravity.RIGHT;
//        mWindowParams.alpha = 0.55f;
        mWindowParams.x = 200;
        mWindowParams.y = 10;
        mWindowParams.width = 100;
        mWindowParams.height = 200;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        View view = LayoutInflater.from(this).inflate(R.layout.activity_weather_glass,null);

        mWindowManager.addView(view,mWindowParams);
    }
}
