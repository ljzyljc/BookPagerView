package com.jackie.bookpagerview;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

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
        requeseRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                PhotoUtils.loadImageForSDCard(DragTestActivity.this, new PhotoUtils.DataCallback() {
                    @Override
                    public void onSuccess(ArrayList<Folder> folders) {
                        dragAdapter = new DragAdapter(DragTestActivity.this,folders.get(0).getImages());
                        drag_grid.setAdapter(dragAdapter);
                    }
                });
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });

    }
}
