package com.cl.apiprotect.demo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/***
 * @ClassName: AccessLimit
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/13 21:50
 * @version : V1.0.0
 */

@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    int seconds();

    int maxCount();

    boolean needLogin() default true;

}
