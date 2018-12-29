package com.eshel.jump.anno;

import com.eshel.jump.IntentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntentParser {

    /**
     * 使用 MemoryIntent 或者 Intent
     */
    IntentType intentType() default IntentType.Intent;

    /**
     * ID, 同一个 Activity 中可能有多个方法由注解 IntentParser 标记, 用来区分不同方法
     */
    int id() default 0;
}
