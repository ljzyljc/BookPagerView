package com.jackie.bookpagerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.jackie.bookpagerview.R;

import java.lang.reflect.TypeVariable;

/**
 * Created by Jackie on 2018/4/8.
 */

public class SatelliteMenu extends ViewGroup implements View.OnClickListener{
    private static final String TAG = "SatelliteMenu";
    private Position mPosition = Position.RIGHT_BOTTOM;   //菜单的位置，枚举类
    private int mRadious;
    private Status mCurrentStatus = Status.CLOSE;        //菜单的状态，打开或者关闭
    private View mCButton;                               //菜单主按钮
    private onMenuItemClickListener onMenuItemClickListener;



    public interface  onMenuItemClickListener{
        void onClick(View view,int pos);
    }

    public void setOnMenuItemClickListener(SatelliteMenu.onMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public enum Status{
        OPEN,CLOSE;
    }
    public enum Position{
        LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM;
    }
    public SatelliteMenu(Context context) {
        this(context, null);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //半径默认值是100
        mRadious = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,
                getResources().getDisplayMetrics());
        //获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SatelliteMenu,defStyleAttr,0);
        int pos = a.getInt(R.styleable.SatelliteMenu_position,3);
        switch (pos){
            case 0:   //左上
                break;
            case 1:  //左下
                break;
            case 2: //右上
                break;
            case 3:  //右下
                break;
            default:
                break;
        }
        mRadious = (int)a.getDimension(R.styleable.SatelliteMenu_staellite_radius
                , TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100
                ,getResources().getDisplayMetrics()));
        Log.i(TAG, "SatelliteMenu: "+mRadious+"-----"+pos);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i=0;i<count;i++){
            //测量child
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            layoutCenterButton();
            int count = getChildCount();
            Log.i(TAG, "onLayout: ---6个--"+count);
            for (int i=0;i<count-1;i++){
                View child = getChildAt(i+1);
                //一开始设置不可见
                child.setVisibility(GONE);
                int cl = (int) (mRadious * Math.sin(Math.PI/2 /(count-2)*i));
                int ct = (int)(mRadious * Math.cos(Math.PI/2 /(count-2)*i));
                int cWith = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                //在左下或者右下
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM){
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM){
                    cl = getMeasuredWidth() - cWith - cl;
                }


                child.layout(cl,ct,cl+cWith,ct+cHeight);
            }

        }
    }
    //定位主菜单按钮
    private void layoutCenterButton(){
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);
        int l = 0;
        int t = 0;
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();
        switch (mPosition){
            case LEFT_TOP:
                l = 0;
                t = 0;

                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }

        mCButton.layout(l,t,l+width,t+width);


    }
    private MagicBezierCircle magicBezierCircle;
    @Override
    public void onClick(View v) {
        //主按钮旋转
        if (mCurrentStatus == Status.CLOSE){
            rotateCButton(findViewById(R.id.img),0f,45f,600);
        }else{
            rotateCButton(findViewById(R.id.img),45f,0f,600);
        }
        magicBezierCircle = (MagicBezierCircle)findViewById(R.id.bezier);
        magicBezierCircle.mCurrTime = 0;  //点击的时候刷新
        magicBezierCircle.invalidate();
        toggleMenu(600);
    }
    //切换菜单
    public void toggleMenu(int duration){
        //为menu item添加平移动画，旋转动画
        int count = getChildCount();
        for (int i=0;i<count -1;i++){
            final View childView = getChildAt(i+1);
            childView.setVisibility(VISIBLE);
            //end 0,0
            //start
            int cl = (int) (mRadious * Math.sin(Math.PI/2 /(count-2)*i));
            int ct = (int)(mRadious * Math.cos(Math.PI/2 /(count-2)*i));
            int xflag =1;
            int yflag =1;
            if (mPosition == Position.LEFT_TOP || mPosition ==Position.LEFT_BOTTOM){
                xflag = -1;
            }
            if (mPosition ==Position.LEFT_TOP || mPosition ==Position.RIGHT_TOP){
                yflag = -1;
            }

            AnimationSet animset = new AnimationSet(true);

            Animation transAnim = null;

            //to open
            if (mCurrentStatus == Status.CLOSE){
                transAnim = new TranslateAnimation(xflag*cl,0,yflag*ct,0);

                childView.setClickable(true);
                childView.setFocusable(true);
            }else{//to close
                transAnim = new TranslateAnimation(0,xflag*cl,0,yflag*ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            transAnim.setFillAfter(true);
            transAnim.setDuration(duration);
            transAnim.setStartOffset(i*100/getChildCount());
            transAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE){
                        childView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0,720,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);
            animset.addAnimation(rotateAnim);
            animset.addAnimation(transAnim);
            childView.startAnimation(animset);
            final int finalI = i+1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null){
                        onMenuItemClickListener.onClick(v, finalI);
                    }
                    //item的动画
                    menuItemAnim(finalI-1);
                    changeStatus();

                }
            });

        }
        //切换菜单状态
        changeStatus();

    }
    //
    private void menuItemAnim(int position){
        for (int i=0;i<getChildCount() -1;i++){
            View childView = getChildAt(i+1);
            if (i == position){
                childView.startAnimation(scaleBigAnim(300));
            }else{
                childView.startAnimation(scaleSmallAnim(300));
            }

            childView.setClickable(true);
            childView.setFocusable(true);
        }
    }

    //切换菜单状态
    private void changeStatus(){
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }
    //变大和透明度减低的动画
    private Animation scaleBigAnim(int duration){
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f,4.0f,1.0f,4.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;

    }
    private Animation scaleSmallAnim(int duration){
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    private void rotateCButton(View v,float start,float end,int durtion){
        RotateAnimation anim = new RotateAnimation(start,end,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(durtion);
        anim.setFillAfter(true);
        v.startAnimation(anim);

    }
}
