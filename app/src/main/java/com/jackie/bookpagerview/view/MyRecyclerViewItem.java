package com.jackie.bookpagerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Jackie on 2018/6/25.
 */
public class MyRecyclerViewItem extends HorizontalScrollView {

    private int buttonWidth;
    private DisplayMetrics displayMetrics;
    private boolean isLeft;

    public MyRecyclerViewItem(Context context) {
        super(context);
        init();
    }

    public MyRecyclerViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        displayMetrics = getContext().getResources().getDisplayMetrics();
        buttonWidth = dp2px(200);   //布局的宽度
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //调整布局
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        LinearLayout left = (LinearLayout) linearLayout.getChildAt(0);
        LinearLayout right = (LinearLayout) linearLayout.getChildAt(1);

        linearLayout.layout(linearLayout.getLeft(),linearLayout.getTop(),
                linearLayout.getLeft()+getMeasuredWidth()+buttonWidth,
                linearLayout.getBottom());

        left.layout(left.getLeft(),left.getTop(),
                    left.getLeft()+getMeasuredWidth(),
                        left.getBottom());

        right.layout(left.getLeft()+getMeasuredWidth(),
                        left.getTop(),
                        left.getLeft()+getMeasuredWidth()+buttonWidth,
                        left.getBottom());

    }

    private static final String TAG = "MyRecyclerViewItem";
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            return true;
        }
        if (ev.getAction()==MotionEvent.ACTION_CANCEL || ev.getAction()== MotionEvent.ACTION_UP){
            int range = 70;
            if (isLeft){
                Log.i(TAG, "onTouchEvent: -------isleft-----");
                if (getScrollX()>range){
                    isLeft = false;
                    smoothScrollTo(buttonWidth,0);
                }else{
                    smoothScrollTo(0,0);
                }
            }else{
                if (getScrollX()<(buttonWidth-range)){
                    Log.i(TAG, "onTouchEvent: -----isRight----1--------"+getScrollX());
                    isLeft = true;
                    smoothScrollTo(0,0);
                }else{
                    Log.i(TAG, "onTouchEvent: ------right-----2----------");
                    smoothScrollTo(buttonWidth,0);
                }
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public void reset(){
        isLeft = true;
        scrollTo(0,0);
    }

    private int dp2px(float dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, displayMetrics);
    }

}
