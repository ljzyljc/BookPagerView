package com.jackie.bookpagerview.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Jackie on 2018/4/19.
 */
public class DragListView extends ListView {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;  //item镜像的布局参数
    private int mDragPosition;
    private int mDownX;
    private int mDownY;
    private int mWidth;//整个布局的宽度
    private int mHeight;//整个布局的高度
    private static final String TAG = "DragListView";
    private boolean isDrag;
    private View mStartDragItemView;  //刚开始拖拽的item对应的view
    private Bitmap mDragBitmap;
    private ImageView mDragView;  //用于拖拽的镜像
    private int mDonwBorder; //向下滚动的边界值
    private int mUpBorder;   //向上滚动的边界值
    private int statusHeight;
    private static final int speed = 20;
    private Handler handler = new Handler();
    public DragListView(Context context) {
        super(context);
    }

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();
        statusHeight = getStatusHeight(context);
    }

    public DragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    
    //移动的Runnable
    private Runnable mMoveRunnable = new Runnable() {
        @Override
        public void run() {
            isDrag = true;
//            mStartDragItemView.setVisibility(View.INVISIBLE);
            //创建镜像
            createDragItem(mDragBitmap,mDownX,mDownY);
        }
    };
    //滚动的Runnable
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY;
               if (mMoveY > mUpBorder){   //手指在最下面的时候，开始向上滑动
                   scrollY = 20;
                   handler.postDelayed(mScrollRunnable,25);
               }else if (mMoveY < mDonwBorder){ //手指在最上面的时候向下滚动
                   scrollY = -20;
                   handler.postDelayed(mScrollRunnable,25);
               }else{
                   scrollY = 0;
                   handler.removeCallbacks(mScrollRunnable);
               }
               onSwapItem(mMoveX,mMoveY);
               smoothScrollBy(scrollY,10);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                if (mDownX > mWidth / 10 * 9) {
                    mDragPosition = pointToPosition(mDownX, mDownY);
                    Log.i(TAG, "onTouchEvent: " + mDragPosition);
                    handler.post(mMoveRunnable);
                    mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                    if (mStartDragItemView != null) {
                        mStartDragItemView.setDrawingCacheEnabled(true);
                        mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                        mStartDragItemView.destroyDrawingCache();
                        mUpBorder = getHeight() * 3 / 4;
                        mDonwBorder = getHeight() / 4;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(mMoveRunnable);
                handler.removeCallbacks(mScrollRunnable);
                break;

        }

        return super.dispatchTouchEvent(event);
    }

    boolean isFirstMove = true;
    private int mMoveX;
    private int mMoveY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragView!=null) {
            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mDownX = (int) ev.getX();
//                    mDownY = (int) ev.getY();
//                    if (mDownX > mWidth / 10 * 9) {
//                        mDragPosition = pointToPosition(mDownX, mDownY);
//                        Log.i(TAG, "onTouchEvent: " + mDragPosition);
//                        handler.post(mMoveRunnable);
//                        mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
//                        if (mStartDragItemView != null) {
//                            mStartDragItemView.setDrawingCacheEnabled(true);
//                            mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
//                            mStartDragItemView.destroyDrawingCache();
//                            mUpBorder = mHeight / 8 * 7;
//                            mDonwBorder = mHeight / 8 * 1;
//                        }
//                    }
//
//                    break;
                case MotionEvent.ACTION_MOVE:
                    mMoveX = (int) ev.getX();
                    mMoveY = (int) ev.getY();
                    Log.i(TAG, "onTouchEvent: ===============");
                    if (isFirstMove && mStartDragItemView != null) {
                        Log.i(TAG, "onTouchEvent: --------invisivle");
                        mStartDragItemView.setVisibility(INVISIBLE);
                        isFirstMove = false;
                    }
                    //拖动镜像
                    if (mStartDragItemView != null) {
                        onDragItem(mMoveX, mMoveY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isFirstMove = true;
                    mDownX = 0;
                    mDownY = 0;
//                    handler.removeCallbacks(mMoveRunnable);
                    mStartDragItemView = null;
                    onStopDrag();
//                    handler.removeCallbacks(mScrollRunnable);
                    isDrag = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isFirstMove = true;
                    mDownX = 0;
                    mDownY = 0;
//                    handler.removeCallbacks(mMoveRunnable);
//                    handler.removeCallbacks(mScrollRunnable);
                    mStartDragItemView = null;
                    onStopDrag();
                    isDrag = false;
                    break;

            }
            return true;
        }

        return super.onTouchEvent(ev);
    }
    //开始拖拽
    private void onDragItem(int moveX,int moveY){
        //移动的时候更新镜像
        mWindowParams.y = moveY-40;
        mWindowManager.updateViewLayout(mDragView,mWindowParams);
        onSwapItem(moveX,moveY);
        handler.post(mScrollRunnable);
    }
    //停止拖拽
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void onStopDrag(){
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null){
            view.setVisibility(View.VISIBLE);
        }
        //移除镜像
        if (mDragView != null && mDragView.isAttachedToWindow()){
            mWindowManager.removeView(mDragView);
        }
        if (onChangeListener != null){
            onChangeListener.onAfterChange();
        }

    }
    //交换顺序
    private void onSwapItem(int postionX,int postionY){
        int swapPosition = pointToPosition(postionX,postionY);
        if (swapPosition != mDragPosition && swapPosition != INVALID_POSITION){
            if (onChangeListener!=null){
                onChangeListener.onChange(mDragPosition,swapPosition);
            }
            //拖动到哪个item,哪个item隐藏，原先的item显示
            getChildAt(swapPosition - getFirstVisiblePosition()).setVisibility(INVISIBLE);

            getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(VISIBLE);
            mDragPosition = swapPosition;
        }
    }


    //创建拖动的镜像
    private void createDragItem(Bitmap bitmap,int x,int y){
        mWindowParams = new WindowManager.LayoutParams();
//        mWindowParams.format = PixelFormat.;
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowParams.alpha = 1.0f;
        int mY = 0;
        if (getChildAt(pointToPosition(x,y)-getFirstVisiblePosition())!=null) {
            mY = getChildAt(pointToPosition(x, y) - getFirstVisiblePosition()).getTop();
        }
        mWindowParams.x = 0;
        mWindowParams.y = mY ;
        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        mDragView = new ImageView(getContext());
        mDragView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragView,mWindowParams);
    }

    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public interface OnChangeListener{
        void onChange(int from,int to);
        void onAfterChange();
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
}
