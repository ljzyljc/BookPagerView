package com.jackie.booklibrary.uitls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by Jackie on 2018/3/29.
 */
@Target(ElementType.FIELD)
public @interface ViewInject {
//    因为value这个名字是默认的，如果我们定义为value，那么注解的时候可以省略
    int value();
}
