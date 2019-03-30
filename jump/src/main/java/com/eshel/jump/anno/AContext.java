package com.eshel.jump.anno;


import com.eshel.jump.enums.ContextType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoshiwen on 2019/3/29.
 * 为避免跟 android.content.Context 出现导包冲突, 将 @Context 注解 更名为 @AContext
 * 上下文, 用于跳转的上下文
 * 如果不加Context 注解, 则以参数中的第一个 Context 参数来作为上下文进行跳转
 * interface Jump{
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(@AContext(ContextType.Context)Context context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin2(@AContext(ContextType.Activity)Context context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin3(@AContext(ContextType.Activity)Activity context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin4(@AContext(ContextType.Fragment)Fragment context);
 * }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AContext {
    ContextType value() default ContextType.Context;
}
