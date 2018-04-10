package com.jackie.bookpagerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.jackie.bookpagerview.view.MagicBezierCircle;

/**
 * Created by Jackie on 2018/4/9.
 */

public class MagicBezicerActivity extends BaseActivity {
    private Button btn;
    private MagicBezierCircle bezier;
    private ImageView img;
    private long lastClickTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_bezier);
        btn = (Button) findViewById(R.id.btn);
        bezier = (MagicBezierCircle)findViewById(R.id.bezier);
        img = (ImageView) findViewById(R.id.img);
//        bezier.bringToFront();
//        img.bringToFront();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezier.mCurrTime = 0;
                bezier.invalidate();
            }
        });
    }

}
