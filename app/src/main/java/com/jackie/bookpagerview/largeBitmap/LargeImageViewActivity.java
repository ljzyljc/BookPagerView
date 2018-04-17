package com.jackie.bookpagerview.largeBitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jackie.bookpagerview.BaseActivity;
import com.jackie.bookpagerview.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jackie on 2018/4/16.
 */

public class LargeImageViewActivity extends BaseActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_img);
        imageView = (ImageView)findViewById(R.id.rect_img);

        try {
            InputStream inputStream = getAssets().open("tangyan.jpg");
            //获得图片的宽高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream,null,options);
            int width = options.outWidth;
            int height = options.outHeight;

            //设着图片的中心区域
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream,false);
            BitmapFactory.Options options1 =new BitmapFactory.Options();
            options1.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(width/2-100,height/2-100,width/2+100,height/2+100),options1);
            imageView.setImageBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
