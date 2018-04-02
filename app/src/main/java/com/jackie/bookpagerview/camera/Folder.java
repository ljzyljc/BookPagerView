package com.jackie.bookpagerview.camera;

/**
 * Created by Jackie on 2018/4/2.
 */

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 图片文件夹实体类
 */
public class Folder {

    private String name;
    private ArrayList<ImageBean> images;

    public Folder(String name) {
        this.name = name;
    }

    public Folder(String name, ArrayList<ImageBean> images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ImageBean> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageBean> images) {
        this.images = images;
    }

    public void addImage(ImageBean image) {
        if (image != null && !TextUtils.isEmpty(image.getPath())) {
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(image);
        }
    }
}
