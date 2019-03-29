package com.eshel.jump.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoshiwen on 2019/3/29.
 * 用于指定参数 :
 * interface Jump{
 *     @ExtraParams(key = "token", value = "121212222eee")
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(Context context);
 * }
 * 等同于 new Intent(LoginActivity.class).putExtra("token", "12121222eee");
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtraParams {

    String[] key();
    String[] value();
}
