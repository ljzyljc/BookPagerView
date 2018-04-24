package com.jackie.bookpagerview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.jackie.bookpagerview.R;

/**
 * Created by Jackie on 2018/4/24.
 */
public class SlideLayout extends FrameLayout {
    private Activity mActivity;
    private Scroller mScroller;
    //上次action_move时的坐标
    private int mLastMoveX;
    //阴影的宽度
    private int mShadowWidth;
    //阴影宽度默认值
    private static final int SHADOW_WIDTH = 16;
    //可滑动的最小坐标
    private int mMinth;
    //Activity finish的标识符
    private boolean isFinish;
    //页面边缘阴影图
    private Drawable mLeftDrawable;
    private int mWidth;
    private static final String TAG = "SlideLayout";

    public SlideLayout(@NonNull Activity activity) {
        this(activity,null);
    }

    public SlideLayout(@NonNull Activity activity, @Nullable AttributeSet attrs) {
        this(activity, attrs,0);
    }

    public SlideLayout(@NonNull Activity activity, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        init(activity);

    }
    private void init(Activity activity){
        mActivity = activity;
        mScroller = new Scroller(activity);
        mLeftDrawable = getResources().getDrawable(R.drawable.left_shadow);
        int desity = (int) activity.getResources().getDisplayMetrics().density;
        mShadowWidth = SHADOW_WIDTH * desity;
    }

    public void bindActivity(Activity activity){
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        View child = viewGroup.getChildAt(0);
        viewGroup.removeView(child);
        addView(child);
        viewGroup.addView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastMoveX = (int) event.getX();
                mWidth = getWidth();
                mMinth = mWidth/10;  //最小宽度
                break;
            case MotionEvent.ACTION_MOVE:
                int rightMovedX = (int) (mLastMoveX - event.getX());
                Log.i(TAG, "onTouchEvent: ");
                if (getScrollX() + rightMovedX >= 0){  //左侧即将划出屏幕
                    scrollTo(0,0);
                    Log.i(TAG, "onTouchEvent: ---");
                }else if (event.getX() > mMinth){   //手指处于屏幕边缘是不处理滑动
                    scrollBy(rightMovedX,0);
                }
                mLastMoveX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (-getScrollX() < mWidth/2){
                    scrollBack();
                    isFinish = false;
                }else{
                    scrollClose();
                    isFinish = true;
                }
                break;
        }
        return true;
    }

    //滑动返回
    private void scrollBack(){
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX,0,dx,0,300);
        invalidate();
    }
    //滑动关闭
    private void scrollClose(){
        int startX = getScrollX();
        int dx = -getScrollX() - mWidth;
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),0);
            postInvalidate();
        }else if (isFinish){
            mActivity.finish();
        }
        super.computeScroll();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }
    private void drawShadow(Canvas canvas){
        canvas.save();
        mLeftDrawable.setBounds(0,0,mShadowWidth,getHeight());
        canvas.translate(-mShadowWidth,0);
        mLeftDrawable.draw(canvas);
        canvas.restore();
    }


}
