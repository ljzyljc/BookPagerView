package com.jackie.bookpagerview;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.jackie.bookpagerview.fragment.LeftMenuFragment;
import com.jackie.bookpagerview.view.LeftDrawerLayout;

/**
 * Created by Jackie on 2018/4/16.
 */

public class DragLayoutActivity extends ActionBarActivity {
    private LeftDrawerLayout left_drawer_layout;
    private TextView content_txt;
    private LeftMenuFragment leftMenuFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movelayout);
        setContentView(R.layout.activity_left_drawlayout);
        left_drawer_layout = (LeftDrawerLayout)findViewById(R.id.left_drawer_layout);
        content_txt = (TextView) findViewById(R.id.content_txt);
        leftMenuFragment = new LeftMenuFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_menu,leftMenuFragment).commit();
        leftMenuFragment.setOnMenuItemSelectedListener(new LeftMenuFragment.OnMenuItemSelectedListener() {
            @Override
            public void menuItemSelected(String title) {
                left_drawer_layout.closeDrawer();
                content_txt.setText(title);
            }
        });

    }
}
