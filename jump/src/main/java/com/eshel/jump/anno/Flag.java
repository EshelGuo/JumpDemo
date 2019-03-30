package com.eshel.jump.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.eshel.jump.configs.JumpConst.NULL_I;

/**
 * Created by guoshiwen on 2019/3/28.
 * 先解析方法上的注解, 后解析参数上的注解
 * 用于配置 flag 参数:
 * 可用于方法, 一个方法只能有一个 Flag 注解
 * 可用于参数, 一个方法可以有多个参数使用 Flag 注解
 * interface Jump{
 *     @Flag({Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK})
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *
 *     @Intent(targetClass = LoginActivity.class, flag = {Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK})
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *
 *      //List 数组 int 类型都支持
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(@AContext(ContextType.Context)Context context, @Flag int flag, @Flag List<Integer> flag2, @Flag int[] flag3);
 * }
 * 等同于:
 *      intent.addFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {
    int[] value() default {NULL_I};
}
