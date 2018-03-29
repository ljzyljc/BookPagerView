package com.jackie.booklibrary.uitls;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Jackie on 2018/3/29.
 */

public class AnnotateUtils {

    public static void injectViews(Object activity,View view){
        Class<? extends Activity> object = (Class<? extends Activity>) activity.getClass();
        Field[] fields = object.getDeclaredFields();  //通过Class获取activity的所有字段
        for (Field field : fields){
            //获取字段的注解，如果没有ViewInject注解，则返回null
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null){
                int viewId = viewInject.value();   //获取字段注解的参数，这就是我们传进去的Id
                if (viewId != -1){
                        //获取类中的findViewById方法，参数为int
                    try {
                        Method method = object.getMethod("findViewById",int.class);
                        //执行该方法，返回一个Object类型的View示例
                        Object resView = method.invoke(activity,viewId);
                        field.setAccessible(true);
                        //把字段的值设置为该view的实例
                        field.set(activity,resView);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
