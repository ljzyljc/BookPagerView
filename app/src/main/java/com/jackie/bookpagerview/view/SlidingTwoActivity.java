package com.jackie.bookpagerview.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.SlideActivity;

/**
 * Created by Jackie on 2018/4/24.
 */
public class SlidingTwoActivity extends SlideActivity {
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_two);
        button = (Button) findViewById(R.id.tow_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
