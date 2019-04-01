package com.eshel.jump.anno;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.eshel.jump.IntentType;
import com.eshel.jump.JumpType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Intent {
    String PARSE_ID = "JUMP_PARSEID";
    /**
     * 使用 MemoryIntent 或者 Intent
     */
    IntentType intentType() default IntentType.Intent;

    /**
     * 目标 Activity
     */
    @NonNull Class<? extends Activity> target();

    /**
     * 跳转方式, startActivity() 或者 startActivityForResult()
     */
    JumpType jumpType() default JumpType.StartAct;

    /**
     * 如果跳转方式为 startActivityForResult 则需要 requestCode
     */
    int requestCode() default 0;


    /**
     * 此处如果指定ID, 则在调用 JumpUtil.parseIntent() 时会执行 带注解 IntentParser(id = 1)并对应ID的方法
     */
    int parseId() default 0;
}
