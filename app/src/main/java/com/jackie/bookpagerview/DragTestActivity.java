package com.jackie.bookpagerview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;
import com.jackie.bookpagerview.adapter.DragAdapter;
import com.jackie.bookpagerview.adapter.MyGridAdapter;
import com.jackie.bookpagerview.camera.CameraGridView;
import com.jackie.bookpagerview.camera.Folder;
import com.jackie.bookpagerview.camera.ImageBean;
import com.jackie.bookpagerview.camera.PhotoUtils;
import com.jackie.bookpagerview.view.DragGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jackie on 2018/4/3.
 */

public class DragTestActivity extends BaseActivity {
    private static final String TAG = "DragTestActivity";
    @ViewInject(R.id.drag_grid)
    DragGridView drag_grid;
    DragAdapter dragAdapter;
    private ArrayList<ImageBean> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_drag);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());
        arrayList = new ArrayList<>();
        dragAdapter = new DragAdapter(DragTestActivity.this,arrayList);
        drag_grid.setAdapter(dragAdapter);
//        requeseRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ,Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
//            @Override
//            public void onGranted() {
//                PhotoUtils.loadImageForSDCard(DragTestActivity.this, new PhotoUtils.DataCallback() {
//                    @Override
//                    public void onSuccess(ArrayList<Folder> folders) {
//                        arrayList.addAll(folders.get(0).getImages());
//                        dragAdapter = new DragAdapter(DragTestActivity.this,arrayList);
//                        drag_grid.setAdapter(dragAdapter);
//                    }
//                });
//            }
//
//            @Override
//            public void onDenied(List<String> deniedPermission) {
//
//            }
//        });
        drag_grid.setOnChangeListener(new DragGridView.OnChangeListener() {
            //交换顺序
            @Override
            public boolean onChange(int from, int to) {
                if (from == arrayList.size() || to == arrayList.size()){
                    return true;
                }
                //一个个往前推移，或者往后
                if (from < to){
                    for (int i= from;i<to;i++) {
                        Collections.swap(arrayList, i, i + 1);
                    }
                }else if (from > to){
                    for (int i = from;i>to;i--){
                        Collections.swap(arrayList,i,i-1);
                    }
                }
                dragAdapter.notifyDataSetChanged();
                return false;
            }
            //调整完位置之后
            @Override
            public void onAfterChange() {
                dragAdapter.notifyDataSetChanged();
                if (popWindow != null) {
                    popWindow.dismiss();
                }
            }
            //拖动的时候显示删除按钮
            @Override
            public void onShowDeleteButttom() {
                if (popWindow == null || !popWindow.isShowing()) {
                    showPopwindow();
                }
            }
            //删除后
            @Override
            public void onDelete(int position) {
                if (popWindow != null) {
                    popWindow.dismiss();
                }
                Log.i(TAG, "onDelete: ----------"+position);
                arrayList.remove(position);
                dragAdapter.notifyDataSetChanged();
            }
            //是否在删除按钮上面，用于改变popupwindow的文字
            @Override
            public void aboveDeleteButton(boolean flag) {
                if (popWindow!=null){
                    if (flag){
                        btn_camera_pop_cancel.setText("松开即可删除");
                    }else{
                        btn_camera_pop_cancel.setText("拖动到此处删除");
                    }
                }
            }
        });


    }
    PopupWindow popWindow;
    private Button btn_camera_pop_cancel;
    private void showPopwindow() {
//        View parent = this.findViewById(R.id.linear_main);
        View popView = LayoutInflater.from(DragTestActivity.this).inflate(R.layout.activity_popup, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        btn_camera_pop_cancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);
        popWindow = new PopupWindow(popView,width,height);
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(false);// 设置同意在外点击消失
        popWindow.setHeight(height/8);
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popWindow.setBackgroundDrawable(dw);

        popWindow.showAtLocation(this.findViewById(R.id.linear_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null){
            return;
        }
        if (requestCode == 1000){
            Bundle bundle = data.getExtras();
            Log.i(TAG, "onActivityResult: -----------100-----------"+bundle);
            arrayList.addAll((ArrayList<ImageBean>)bundle.getSerializable("gridlist"));
            dragAdapter.notifyDataSetChanged();
        }


    }
}
