package com.eshel.jump.anno;

import com.eshel.jump.configs.JumpConst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意: 使用该注解的参数实际类型必须是 Uri 或 String 类型
 * 如果是 String , 内部会执行 String data = "tel:12123"; Uri.parse(data);
 * 注意: 仅支持 Uri 类型和 String 类型 (拼接形式支持其他(int float 等 String.format支持的类型)类型)
 * interface Jump{
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context, @Data Uri data);
 *
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context, @Data String data);
 *
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context, @Data("tel:%s") int phone);
 *
 *     @Intent(targetClass = LoginActivity.class, data = "tel:12123")
 *     void jumpLogin0(Context context);
 *
 *     @Data("tel:12123")
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(Context context);
 * }
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Data {
    String value() default JumpConst.NULL;
}
