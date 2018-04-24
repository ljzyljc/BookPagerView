package com.jackie.bookpagerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackie.bookpagerview.view.SlideLayout;

/**
 * Created by Jackie on 2018/4/24.
 */
public class SlideActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlideLayout slideLayout = new SlideLayout(this);
        slideLayout.bindActivity(this);

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.anim_slide_in,R.anim.anim_slide_out);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in,R.anim.anim_slide_out);
    }
}
