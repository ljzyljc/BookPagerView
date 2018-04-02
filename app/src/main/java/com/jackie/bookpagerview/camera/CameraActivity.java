package com.jackie.bookpagerview.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;
import com.jackie.bookpagerview.BaseActivity;
import com.jackie.bookpagerview.R;

import java.io.File;
import java.util.List;

/**
 * Created by Jackie on 2018/4/2.
 */

public class CameraActivity extends BaseActivity {
    private static int CHOOSE_PHOTO = 66;
    private static int TAKE_PHOTO = 88;
    private static final String TAG = "CameraActivity";
    private File chooseFile;
    @ViewInject(R.id.start_camera)
    Button start_camera;
    @ViewInject(R.id.pick_photo)
    Button pick_photo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());


        start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this,CameraGridView.class);
                startActivity(intent);
            }
        });
    }



    //选择相册
    public void choosePhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("return-data",true);  //是否需要返回值
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CHOOSE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK ){
            return;
        }
        //选择本地相册
        if (requestCode == CHOOSE_PHOTO){
            Uri uri = data.getData();
            final String picPath = UriUtil.getPathByUri4kitkat(this,uri);
            if (!TextUtils.isEmpty(picPath)){
                Log.i(TAG, "onActivityResult: ---"+picPath);
            }
            //6.0以上的手机要手动申请权限,写的权限用于创建文件，读的权限用于BitmapFactory.decodeFile
            requeseRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                    //压缩图片,同时创建压缩后的文件，用于上传
                    Bitmap bitmap = PhotoUtils.compressImageToBitmap(picPath,chooseFile);
//                    imageview.setBitmap(bitmap)
                }

                @Override
                public void onDenied(List<String> deniedPermission) {

                }
            });
         //选择拍照
        }else if (requestCode == TAKE_PHOTO){

        }



    }
}
