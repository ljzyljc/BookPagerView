package com.jackie.bookpagerview;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.jackie.bookpagerview.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/4/2.
 */

public class BaseActivity extends Activity {
    private static PermissionListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    List<String> deniedPermissionList=new ArrayList<>();
                    for (int i=0;i<grantResults.length;i++){
                        int grantResult=grantResults[i];
                        String permission=permissions[i];
                        if (grantResult!= PackageManager.PERMISSION_GRANTED){
                            deniedPermissionList.add(permission);
                        }
                    }
                    if (deniedPermissionList.isEmpty()){
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissionList);
                    }
                }
                break;
            default:
                break;
        }
    }
    //运行时权限调用方法
    public static void requeseRunTimePermission(String []permissions,PermissionListener permissionListener){
        Activity topActivity=ActivityCollector.getTopActivity();
        if (topActivity==null){
            return;
        }
        mListener=permissionListener;
        List<String> permissionList=new ArrayList<>();
        for (String permission:permissions) {
            //如果没有允许该权限，就添加到该List中
            if (ActivityCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(topActivity,permissionList.toArray(new String[permissionList.size()]),1);
        }else {
            mListener.onGranted();
        }
    }
    //    grantResults (授权的结果)
    //运行时权限监听接口
    public interface PermissionListener {
        void onGranted();
        void onDenied(List<String> deniedPermission);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
