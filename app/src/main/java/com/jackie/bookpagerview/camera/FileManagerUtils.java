package com.jackie.bookpagerview.camera;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Jackie on 2017/2/19.
 */

public class FileManagerUtils {

    /**
     * 创建文件(照片)
     * /DingYin/photo目录下
     * @return
     */
    public static File createNewFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String path = null;
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File dir = new File(path + "/DingYin/photo/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File f1 = new File(path + "/DingYin/photo/", System.currentTimeMillis() + ".jpg");
                f1.createNewFile();
                return f1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 创建文件(音频)
     * /DingYin/audio目录下
     * @return
     */
    public static File createAudioFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String path = null;
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File dir = new File(path + "/DingYin/audio/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File f1 = new File(path + "/DingYin/audio/", System.currentTimeMillis() + ".wav");
                f1.createNewFile();
                return f1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据已有的文件（路径）创建一个新的文件（该文件用于压缩，这样可以防止把手机中的原图片给压缩了）
     * @param path
     * @return
     */
    public static File createNewFile(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file=createNewFile();
                copyFile(path,file.getAbsolutePath());
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPath
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }
}
