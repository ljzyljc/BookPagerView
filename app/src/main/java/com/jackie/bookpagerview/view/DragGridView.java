package com.jackie.bookpagerview.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.jackie.bookpagerview.camera.CameraGridView;

/**
 * Created by Jackie on 2018/4/3.
 */

public class DragGridView extends GridView {
    private static final String TAG = "DragGridView";
    private int mDragPosition;  //拖拽的position
    private int mDownX;
    private int mDownY;
    private int drawResponseSeconds = 500;
    private Handler mHandler = new Handler();
    private Vibrator mVibrator;  //振动器
    private WindowManager mWindowManager;
    private boolean isDrag;     //是否拖拽
    private View mStartDragItemView; //刚开始拖拽的item对应的view
    private WindowManager.LayoutParams mWindowParams;  //item镜像的布局参数
    private Bitmap mDragBitmap;  //拖拽的item对应的Bitmap
    private int statusHeight;
    private ImageView mDragImageView;  //用于拖拽的镜像
    private int mMoveX;
    private int mMoveY;
    /**
     * 按下的点到所在item的上边缘的距离
     */
    private int mPoint2ItemTop ;

    /**
     * 按下的点到所在item的左边缘的距离
     */
    private int mPoint2ItemLeft;

    /**
     * DragGridView距离屏幕顶部的偏移量
     */
    private int mOffset2Top;

    /**
     * DragGridView距离屏幕左边的偏移量
     */
    private int mOffset2Left;
    /**
     * DragGridView自动向下滚动的边界值
     */
    private int mDownScrollBorder;

    /**
     * DragGridView自动向上滚动的边界值
     */
    private int mUpScrollBorder;

    /**
     * DragGridView自动滚动的速度
     */
    private static final int speed = 20;
    public DragGridView(Context context) {
        super(context);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //振动器初始化
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusHeight = getStatusHeight(context);
    }
    //用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            isDrag = true;
            mVibrator.vibrate(50);  //震动50毫秒
            mStartDragItemView.setVisibility(INVISIBLE);
            createDragImg(mDragBitmap,mDownX,mDownY);
        }
    };
    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
     * 当moveY的值小于向下滚动的边界值，触犯GridView自动向下滚动
     * 否则不进行滚动
     */
    //滚动，Runnable
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY;
            if (mMoveY > mUpScrollBorder){
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable,25);
            }else if (mMoveY < mDownScrollBorder){
                scrollY = -20;
                mHandler.postDelayed(mScrollRunnable,25);
            }else{
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }
            onSwapItem(mMoveX,mMoveY);
            smoothScrollBy(scrollY,10);

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                //pointToPostion是AbsListView提供的方法，可以根据坐标查找位置
                mDragPosition = pointToPosition(mDownX,mDownY);
                Log.i(TAG, "dispatchTouchEvent:   点击位置:"+mDragPosition+"---x:"+mDownX+"---y:"+mDownY);
                if (mDragPosition == AdapterView.INVALID_POSITION){
                    Log.i(TAG, "dispatchTouchEvent: -------------点击了最后一个------------");
                    return super.dispatchTouchEvent(ev);
                }
                if (mDragPosition == getCount()-1){
                    Intent intent = new Intent(getContext(),CameraGridView.class);
                    ((Activity)getContext()).startActivityForResult(intent,1000);
                    return super.dispatchTouchEvent(ev);
                }
                //使用handler延时执行mLongClickRunnable，如果手抬上去的话，没超过一秒，在action_up上移除
                mHandler.postDelayed(mLongClickRunnable,drawResponseSeconds);
                //根据position获取该item对应的view
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                if (mStartDragItemView!=null) {
                    mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
                    mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                    mOffset2Left = (int) (ev.getRawX() - mDownX);
                    mOffset2Top = (int) (ev.getRawY() - mDownY);
                    //FIXME:滚动
                    mDownScrollBorder = getHeight() / 4;
                    mUpScrollBorder = getHeight() * 3 / 4;

                    //开启mDragItemView绘图缓存
                    mStartDragItemView.setDrawingCacheEnabled(true);
                    //获取mDragItemView在缓存中的Bitmap对象
                    mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                    //释放绘图缓存，避免出现重复的镜像
                    mStartDragItemView.destroyDrawingCache();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragPosition == INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                //如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable,其实感觉这句没啥用，up也一样移除，只有一个down对应一个up,迟早都移除
                if (!isTouchInItem(mStartDragItemView,moveX,moveY)){
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeCallbacks(mLongClickRunnable);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
            default:
                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    //移除镜像，当长按的时候又放回可能会出现镜像还存在，所以移除镜像
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.i(TAG, "onWindowFocusChanged: ------------------------");
        //移除镜像
        if (mDragImageView != null && mWindowManager!=null){
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
            if (mStartDragItemView!=null) {
                mStartDragItemView.setVisibility(VISIBLE);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragImageView != null){
            Log.i(TAG, "onTouchEvent: ---------1------");
            switch (ev.getAction()){
                case MotionEvent.ACTION_MOVE:
                    mMoveX = (int) ev.getX();
                    mMoveY = (int) ev.getY();
                    //拖动镜像
                    onDragItem(mMoveX,mMoveY);
                    break;
                case MotionEvent.ACTION_UP:
                    //抬起后移除镜像
                    onStopDrag();
                    isDrag = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //抬起后移除镜像
                    onStopDrag();
                    isDrag = false;
                    break;
            }
            return true;
        }
        Log.i(TAG, "onTouchEvent: ---------2------");

        return super.onTouchEvent(ev);
    }

    //gridview的item交换
    private void onSwapItem(int moveX,int moveY){
        int swapPostion = pointToPosition(moveX,moveY);
        //最后一个加号设置一直可见
        if (swapPostion - getFirstVisiblePosition() == getLastVisiblePosition()){
            getChildAt(swapPostion - getFirstVisiblePosition()).setVisibility(VISIBLE);
            return;
        }
        Log.i(TAG, "onSwapItem: ----"+swapPostion+"----moveX："+moveX+"---moveY："+moveY);
        if (swapPostion != mDragPosition && swapPostion != INVALID_POSITION) {
            if (onChangeListener != null){
                onChangeListener.onChange(mDragPosition,swapPostion);
            }
            //拖动到哪个item,哪个item隐藏，原先的item显示
            getChildAt(swapPostion - getFirstVisiblePosition()).setVisibility(INVISIBLE);

            getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(VISIBLE);
            mDragPosition =  swapPostion;
        }

    }


    //拖动item,更新镜像
    private void onDragItem(int moveX,int moveY){
        mWindowParams.x = moveX - mPoint2ItemTop + mOffset2Left;
        mWindowParams.y = moveY - mPoint2ItemTop + mOffset2Top - statusHeight;
        mWindowManager.updateViewLayout(mDragImageView,mWindowParams);   //更新镜像的位置
        onSwapItem(moveX,moveY);
        //GridView自动滚动
        mHandler.post(mScrollRunnable);
    }
    //停止拖拽
    private void onStopDrag(){
        //设置item可见
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());

        if (view != null){
            view.setVisibility(VISIBLE);
        }
        //移除镜像
        if (mDragImageView != null){
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
        if (onChangeListener != null){
            onChangeListener.onAfterChange();
        }

    }

    //创建拖动的镜像
    private void createDragImg(Bitmap bitmap,int x,int y){
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.format = PixelFormat.TRANSLUCENT;  //图片之外其他地方透明
        mWindowParams.gravity = Gravity.TOP|Gravity.LEFT;
        mWindowParams.alpha = 0.55f;
        mWindowParams.x = x - mPoint2ItemLeft +mOffset2Left;
        mWindowParams.y = y - mPoint2ItemTop + mOffset2Top - statusHeight;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView,mWindowParams);
    }


    /**
     * 设置长按的时间，长按多久进行拖拽
     * @param drawResponseSeconds
     */
    public void setDrawResponseSeconds(int drawResponseSeconds) {
        this.drawResponseSeconds = drawResponseSeconds;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public interface OnChangeListener {

        boolean onChange(int from,int to);
        void onAfterChange();

    }
    /**
     * 是否点击在GridView的item上面
     * @param
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(View dragView, int x, int y){
        if(dragView == null){
            return false;
        }
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if(x < leftOffset || x > leftOffset + dragView.getWidth()){
            return false;
        }

        if(y < topOffset || y > topOffset + dragView.getHeight()){
            return false;
        }

        return true;
    }

}
