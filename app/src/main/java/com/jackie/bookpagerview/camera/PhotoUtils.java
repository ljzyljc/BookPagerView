package com.jackie.bookpagerview.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jackie on 2018/4/2.
 */

public class PhotoUtils {
    private static final String TAG = "PhotoUtils";

    //通过采样率压缩图片
    public static Bitmap compressImageToBitmap(String imagePath,File myFile){
        Log.i(TAG, "compressImageToBitmap: -----"+imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  //不加载到内存中,获取图片的宽高等数据
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);

        options.inJustDecodeBounds = false;

        options.inSampleSize = 5;
        Log.i(TAG, "compressImageToBitmap: --采样率--"+options.inSampleSize+"-------高度"+options.outHeight);
        bitmap = BitmapFactory.decodeFile(imagePath,options);
        if (bitmap != null){
            Log.i(TAG, "compressImageToBitmap: -----"+bitmap.getByteCount());
        }else{
            Log.i(TAG, "compressImageToBitmap: ------图片为空");
        }
        //把压缩后的文件保存到SD卡中用于上传
        myFile = FileManagerUtils.createNewFile();
        try {
            FileOutputStream out = new FileOutputStream(myFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
