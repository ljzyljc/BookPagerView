package com.jackie.bookpagerview.left_right_menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.reflect.TypeVariable;

/**
 * Created by Jackie on 2018/4/17.
 */

public class MySlidingMenu extends HorizontalScrollView {

    int mWidth;
    int mHeight;
    private int menuWidth;
    private int menuRightPadding = 30;
    private boolean once;

    public MySlidingMenu(Context context) {
        super(context);
    }

    public MySlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();



    }

    public MySlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once){
            LinearLayout linearLayout = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) linearLayout.getChildAt(0);
            ViewGroup content = (ViewGroup) linearLayout.getChildAt(1);

            menuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,menuRightPadding,content.getResources().getDisplayMetrics());

            menuWidth = mWidth - menuRightPadding;
            menu.getLayoutParams().width = menuWidth;
            content.getLayoutParams().width = mWidth;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            this.scrollTo(menuWidth,0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                if (getScrollX()>menuWidth/2){
                    smoothScrollTo(menuWidth,0);
                }else{
                    smoothScrollTo(0,0);
                }
                return true;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }
}
