package com.jackie.bookpagerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Jackie on 2018/4/17.
 */

public class LeftDrawerLayout extends ViewGroup {
    private View mLeftMenu;
    private View mRightMenu;
    private int mMinDrawerMargin;  //drawer离父容器右边的最小外边距

    private ViewDragHelper viewDragHelper;
    //drawer显示出来占自身的百分比
    private float mLeftMenuOnScreenPersent;
    private static final int MIN_DRAWER_MARGIN = 64; // dp
    private static final String TAG = "LeftDrawerLayout";
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    public LeftDrawerLayout(Context context) {
        super(context);
    }

    public LeftDrawerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LeftDrawerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        float density = getResources().getDisplayMetrics().density;
        float minVel = MIN_FLING_VELOCITY * density;
        //设置最小右边距
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density+0.5f);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //只捕获左边的
                return child == mLeftMenu;
            }

            //clampViewPositionHorizontal,clampViewPositionVertical可以在该方法中对child移动的边界进行控制
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int newLeft = Math.max(-child.getWidth(),Math.min(left,0));
                Log.i(TAG, "clampViewPositionHorizontal: ----"+newLeft+"-------left:"+left);
                return newLeft;
            }


            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                viewDragHelper.captureChildView(mLeftMenu,pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                int childWidth = releasedChild.getWidth();
//                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth, releasedChild.getTop());
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                Log.i(TAG, "onViewReleased: offset:"+offset+"-------releasedChild.getLeft():"+releasedChild.getLeft()+"----xvel:"+xvel);
                //回到左边或者右边
                viewDragHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth
                        ,releasedChild.getTop());
                invalidate();
            }

            //当captureview的位置发生改变时回调
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                int childWidth = changedView.getWidth();
                float offset = (childWidth + changedView.getLeft()) * 1.0f / childWidth;
                mLeftMenuOnScreenPersent = offset; //左边占的百分比
                changedView.setVisibility(offset == 0 ? View.INVISIBLE :View.VISIBLE);
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mLeftMenu == child ? child.getWidth() : 0;
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        viewDragHelper.setMinVelocity(minVel);   //设置最小的速度


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

        View leftMenuView = getChildAt(1);   //第二个，即Framelayout的内容为可左滑右滑的leftmenu
        MarginLayoutParams lp = (MarginLayoutParams) leftMenuView.getLayoutParams();
        //第二个参数为 padding的所有距离
        int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,mMinDrawerMargin+lp.leftMargin+lp.rightMargin,lp.width);
        int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,lp.topMargin+lp.bottomMargin,lp.height);
        leftMenuView.measure(drawerWidthSpec,drawerHeightSpec);

        View contentMenuView = getChildAt(0);
        lp = (MarginLayoutParams) contentMenuView.getLayoutParams();
        int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin,MeasureSpec.EXACTLY);
        int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin,MeasureSpec.EXACTLY);
        Log.i(TAG, "onMeasure: ----"+contentWidthSpec+"   "+contentHeightSpec);
        contentMenuView.measure(contentWidthSpec,contentHeightSpec);

        mLeftMenu = leftMenuView;
        mRightMenu = contentMenuView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menuView = mLeftMenu;
        View contentView = mRightMenu;
        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin,lp.topMargin,lp.leftMargin+contentView.getMeasuredWidth(),lp.topMargin+contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();
        int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth+(int)(menuWidth*mLeftMenuOnScreenPersent);
        menuView.layout(childLeft,lp.topMargin,childLeft+menuWidth,
                lp.topMargin+menuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public void closeDrawer(){
        View menuView = mLeftMenu;
        mLeftMenuOnScreenPersent = 0.0f;
        viewDragHelper.smoothSlideViewTo(menuView,-menuView.getWidth(),menuView.getTop());
    }

    public void openDrawer(){
        View menuView = mLeftMenu;
        mLeftMenuOnScreenPersent = 1.0f;
        viewDragHelper.smoothSlideViewTo(menuView,0,menuView.getTop());
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new MarginLayoutParams(lp);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }
}
