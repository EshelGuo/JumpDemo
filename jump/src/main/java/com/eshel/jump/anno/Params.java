package com.eshel.jump.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * interface Jump{
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context, @Params("PHONE")String phone, @Params("password") String psw);
 * }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
    /**
     * Intent 传参参数名
     */
    String value();
}
