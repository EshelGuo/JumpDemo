package com.eshel.jump.anno;

import com.eshel.jump.configs.JumpConst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guoshiwen on 2019/3/28.
 * 用于配置 category:
 * interface Jump{
 *     @Category({Intent.CATEGORY_BROWSABLE, Intent.CATEGORY_ALTERNATIVE})
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(@AContext(ContextType.Context)Context context, @Category String category, @Category List<String> category2, @Category String[] category3);
 * }
 * 等同于:
 *      intent.addCategory(Intent.CATEGORY_BROWSABLE);
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {
    String[] category() default {JumpConst.NULL};
}
