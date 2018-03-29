package com.jackie.booklibrary.uitls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Jackie on 2018/3/29.
 */
//  RetentionPolicy的类型有以下几种
//
//            1.SOURCE:在源文件中有效（即源文件保留）
//          　2.CLASS:在class文件中有效（即class保留）
//          　3.RUNTIME:在运行时有效（即运行时保留）

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
//    因为value这个名字是默认的，如果我们定义为value，那么注解的时候可以省略
    int value();
}
