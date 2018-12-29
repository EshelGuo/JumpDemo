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
}
