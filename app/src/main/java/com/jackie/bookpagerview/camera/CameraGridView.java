package com.jackie.bookpagerview.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;
import com.jackie.bookpagerview.BaseActivity;
import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.adapter.MyGridAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/4/2.
 */

public class CameraGridView extends BaseActivity{
    private static final String TAG = "CameraGridView";
    @ViewInject(R.id.gridview)
    GridView gridview;
    private List<ImageBean> mListPath;
    MyGridAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_gridview);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());
        mListPath = new ArrayList<>();

        requeseRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                Log.i(TAG, "onGranted: -----同意---");
                PhotoUtils.loadImageForSDCard(CameraGridView.this, new PhotoUtils.DataCallback() {
                    @Override
                    public void onSuccess(ArrayList<Folder> folders) {
                        Log.i(TAG, "onSuccess: "+folders.get(0).getImages().size());
                        adapter = new MyGridAdapter(CameraGridView.this,folders.get(0).getImages());
                        gridview.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                Log.i(TAG, "onDenied: -------不同意");
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (adapter !=null && adapter.getmChooseList() != null && adapter.getmChooseList().size() > 0){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("gridlist",adapter.getmChooseList());
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            Log.i(TAG, "onBackPressed: ---------------调用了setresult"+adapter.getmChooseList().size());
        }
        finish();
    }
}
