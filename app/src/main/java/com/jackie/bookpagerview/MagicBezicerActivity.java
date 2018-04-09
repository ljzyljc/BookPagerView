package com.jackie.bookpagerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jackie.bookpagerview.view.MagicBezierCircle;

/**
 * Created by Jackie on 2018/4/9.
 */

public class MagicBezicerActivity extends BaseActivity {
    private Button btn;
    private MagicBezierCircle bezier;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_bezier);
        btn = (Button) findViewById(R.id.btn);
        bezier = (MagicBezierCircle)findViewById(R.id.bezier);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezier.mCurrTime = 0;
                bezier.invalidate();
            }
        });
    }
}
