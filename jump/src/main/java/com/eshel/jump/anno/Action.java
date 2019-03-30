package com.eshel.jump.anno;

import com.eshel.jump.configs.JumpConst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoshiwen on 2019/3/28.
 * 用于指定Action参数
 * interface Jump{
 *     @Action(Intent.ACTION_VIEW)
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context);
 *
 *     @Intent(targetClass = LoginActivity.class, action = Intent.ACTION_VIEW)
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(@AContext(ContextType.Context)Context context, @Action String action);
 * }
 * 三种都可以指定 Action
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String value() default JumpConst.NULL;
}
