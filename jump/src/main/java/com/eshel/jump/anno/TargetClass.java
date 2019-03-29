package com.eshel.jump.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoshiwen on 2019/3/28.
 * interface Jump{
 *     @TargetClass(LoginActivity.class)
 *     void jumpLogin0(@AContext Activity context);
 *
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(@AContext(ContextType.Context) Activity context);
 *
 *     @Intent()
 *     void jumpLogin(Context context, @TargetClass Class loginClazz);
 * }
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetClass {
    Class value();
}
