package com.eshel.jump.anno;

import android.support.annotation.NonNull;

import com.eshel.jump.configs.JumpConst;
import com.eshel.jump.enums.JumpType;
import com.eshel.jump.enums.IntentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.eshel.jump.configs.JumpConst.NULL;
import static com.eshel.jump.configs.JumpConst.NULL_C;
import static com.eshel.jump.configs.JumpConst.NULL_I;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Intent {
    String PARSE_ID = "JUMP_PARSEID";
    /**
     * 使用 MemoryIntent 或者 Intent
     * @see IntentType
     */
    IntentType intentType() default IntentType.Intent;

    /**
     * @see Action
     * 用于指定 Action
     * @return Intent.ACTION_XXXXXX
     */
    String action() default "";

    /**
     * @see Category
     * 顾名思义, 请看 {@link Category}
     */
    String[] category() default {JumpConst.NULL};

    /**
     * @see Flag
     * 用于配置 flag 参数
     */
    int[] flag() default {0};

    /**
     * @see Type
     */
    String type() default NULL;
    /**
     * 目标 Activity 目标服务, 目标广播 的 Class
     */
    @NonNull Class<? extends AContext> target() default NULL_C;

    /**
     * 目标的包名加类名
     */
    String targetName() default NULL;//包名+类名

    /**
     * 跳转方式, startActivity() 或者 startActivityForResult()
     */
    JumpType jumpType() default JumpType.StartAct;

    /**
     * 如果跳转方式为 startActivityForResult 则需要 requestCode
     */
    int requestCode() default NULL_I;

    /**
     * todo v2.0 待实现
     * 此处如果指定ID, 则在调用 JumpUtil.parseIntent() 时会执行 带注解 IntentParser(id = 1)并对应ID的方法
     */
    int parseId() default NULL_I;
}
