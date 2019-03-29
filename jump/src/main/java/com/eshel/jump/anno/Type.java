package com.eshel.jump.anno;

import com.eshel.jump.configs.JumpConst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoshiwen on 2019/3/28.
 * 用于设置 type 参数
 * interface Jump{
 *     @Type("text/plain")
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *
 *      //必须为String类型
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(@AContext(ContextType.Context)Context context, @Type String type);
 *
 *     @Intent(targetClass = LoginActivity.class, type = "text/plain")
 *     void jumpLogin(@AContext(ContextType.Context)Context context);
 * }
 * 等同于:
 *      intent.setType("text/plain");
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    String value() default JumpConst.NULL;
}
