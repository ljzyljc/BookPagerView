package com.jackie.bookpagerview.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.animator.Beizerevalutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jackie on 2018/4/12.
 */

public class LoveLayout extends RelativeLayout {
    //用于存放不同图片的爱心
    private Drawable firstDrawable;
    private Drawable secondDrawable;
    private Drawable threeDrawable;
    private int dHeight;//爱心的高度
    private int dWidth;//爱心的宽度
    private int mWidth;//整个布局的宽度
    private int mHeight;//整个布局的高度
    List<Drawable> mDrawablesList=new ArrayList<Drawable>();
    private LayoutParams params;
    private Random random=new Random();//定义一个随机数对象，用于表示P1，P2,P3点的X，Y坐标的在某个范围随机变化
    public LoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        firstDrawable=getResources().getDrawable(R.mipmap.love6);
        mDrawablesList.add(firstDrawable);
        secondDrawable=getResources().getDrawable(R.mipmap.love6);
        mDrawablesList.add(secondDrawable);
        threeDrawable=getResources().getDrawable(R.mipmap.love6);
        mDrawablesList.add(threeDrawable);
        //得到爱心图片的宽高
        dHeight=firstDrawable.getIntrinsicHeight();
        dWidth=firstDrawable.getIntrinsicWidth();
        params=new LayoutParams(dWidth, dHeight);
        //给爱心控件动态布局，使得爱心始终在布局最底部的中间位置
        params.addRule(CENTER_HORIZONTAL,TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到本布局的宽高
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
    }
    public void addLove(){//添加心
        ImageView mImageView=new ImageView(getContext());
        mImageView.setImageDrawable(mDrawablesList.get(random.nextInt(3)));//通过随机对象，随机在这三张爱心图片产生任意一张图片
        mImageView.setLayoutParams(params);
        addView(mImageView);
        //属性动画控制坐标
        AnimatorSet set= getAnimator(mImageView);//通过getAnimator得到整个爱心所有动画集合
        set.start();
    }
    //构造3个属性动画
    private AnimatorSet getAnimator(ImageView mImageView) {
        //1,alpha动画;2,
        ObjectAnimator alphaAnimator= ObjectAnimator.ofFloat(mImageView, "alpha", 0.3f,1.0f);
        ObjectAnimator scaleXAnimator= ObjectAnimator.ofFloat(mImageView, "scaleX", 0.2f,1.0f);
        ObjectAnimator scaleYAnimator= ObjectAnimator.ofFloat(mImageView, "scaleY", 0.2f,1.0f);
        AnimatorSet mAnimatorSet=new AnimatorSet();
        mAnimatorSet.setDuration(500);
        //三个动画同时集合
        mAnimatorSet.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator);
        mAnimatorSet.setTarget(mImageView);
        //贝塞尔曲线动画,不断修改ImageView的坐标,PointF(x,y)
        ValueAnimator bezierValueAnimator=getBeziValueAnimator(mImageView);//getBeziValueAnimator得到贝赛尔曲线轨迹位移动画
        AnimatorSet bezierAnimatorSet =new AnimatorSet();
        //按顺序播放动画
        bezierAnimatorSet.playSequentially(mAnimatorSet,bezierValueAnimator);//然后按顺序播放这些动画集合
        //bezierAnimatorSet.setDuration(3000);
        bezierAnimatorSet.setTarget(mImageView);

        return bezierAnimatorSet;//返回一个整体爱心所有动画的集合
    }
    /**
     * @author mikyou
     * getBeziValueAnimator
     * 构造一个贝塞尔曲线动画
     * */
    private ValueAnimator getBeziValueAnimator( final ImageView mImageView) {
        //贝塞尔曲线动画,不断修改ImageView的坐标,PointF(x,y)
        PointF pointF2=getPointF(2);//getPointF方法根据传进来的数字来标记四个点,P0,P1,P2,P3
        PointF pointF1=getPointF(1);
        PointF pointF0=new PointF(mWidth/2-dWidth/2, mHeight-dHeight);//创建P0点，起点
        PointF pointF3=new PointF(random.nextInt(mWidth), 0);//创建P3点，终点
        Beizerevalutor mBezierEvalutor=new Beizerevalutor(pointF1, pointF2);//创建一个估值器，然后并把P1，P2点传入
        /**
         * @author zhongqihong
         * 创建一个ValueAnimator，并把起点P0和终点P3传入,然后在BezierEvalutor重写的方法evalute中得到P0，P3
         * 然后通过上一步利用BezierEvalutor构造器将P1,P2两个点传入，所以这就是说
         * 在BezierEvalutor重写的方法evalute可以得到P0，P1,P2,P3点对象，然后通过贝塞尔的公式
         * 即可计算出该轨迹上的任意一点的坐标，并实时返回一个PontF点的对象，然后通过addUpdateListener
         * 监听事件实时获得最新点的坐标然后将这些最新点坐标去更新爱心的ImageVIew控件的X，Y坐标
         * */
        ValueAnimator animator=ValueAnimator.ofObject(mBezierEvalutor, pointF0,pointF3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF=(PointF) animation.getAnimatedValue();    //通过addUpdateListener监听事件实时获得从mBezierEvalutor估值器对象evalute方法实时计算出最新点的坐标  。
                mImageView.setX(pointF.x);//然后去更新该爱心ImageView的X,Y坐标
                mImageView.setY(pointF.y);
                mImageView.setAlpha(1-animation.getAnimatedFraction());//getAnimatedFraction()就是mBezierEvalutor估值器对象中evaluate方法t即时间因子,从0~1变化,所以爱心透明度应该是从1~0变化正好到了顶部，t变为1，透明度变为0，即爱心消失
            }
        });
        animator.setTarget(mImageView);
        animator.setDuration(3000);
        return animator;
    }

    private PointF getPointF(int i) {
        PointF pointF=new PointF();
        pointF.x=random.nextInt(mWidth);//0~loveLayout.Width
        //为了美观,建议尽量保证P2在P1上面,那怎么做呢??
        //只需要将该布局的高度分为上下两部分,让p1只能在下面部分范围内变化(1/2height~height),让p2只能在上面部分范围内变化(0~1/2height),因为坐标系是倒着的;

        //0~loveLayout.Height/2
        if (i==1) {
            pointF.y=random.nextInt(mHeight/2)+mHeight/2;//P1点Y轴坐标变化
        }else if(i==2){//P2点Y轴坐标变化
            pointF.y=random.nextInt(mHeight/2);
        }

        return pointF;
    }
}
