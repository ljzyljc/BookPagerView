package com.jackie.bookpagerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jackie.bookpagerview.view.SlidingTwoActivity;

/**
 * Created by Jackie on 2018/4/24.
 */
public class SlidingOneActivity extends SlideActivity {
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_one);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlidingOneActivity.this, SlidingTwoActivity.class);
                SlidingOneActivity.this.startActivity(intent);
            }
        });
    }
}
