package com.eshel.jump;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.eshel.jump.configs.JConfig;
import com.eshel.jump.configs.JumpException;
import com.eshel.jump.impl.IntentInjectImpl;
import com.eshel.jump.impl.IntentParserImpl;

import java.lang.reflect.Proxy;

/**
 * Created by guoshiwen on 2019/3/29.
 * 替代原来的 JumpUtil
 */

public class JumpHelper {
    private static JConfig sConfig;

    @SuppressWarnings("unchecked")
    public static<T> T create(Class<T> clazz){
        JUtils.checkNull(clazz, JumpException.MSG_INTERFACE_IS_NULL);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new JumpInvokeHandlerV2());
    }

    public static void init(JConfig config){
        sConfig = config;
    }

    public static JConfig getConfig(){
        return sConfig;
    }

    /**
     * @see #parseIntent(int, Object, Intent, boolean)
     */
    public static void parseIntent(@NonNull Object target, @NonNull Intent intent, boolean needRecycleMemoryIntent){
        int id = intent.getIntExtra(com.eshel.jump.anno.Intent.PARSE_ID, 0);
        parseIntent(id, target, intent, needRecycleMemoryIntent);
    }

    /**
     * 注入目标类, 反射实现
     * @param target
     * @param intent
     */
    public static void inject(@NonNull Object target, @NonNull Intent intent){
        IntentInjectImpl.injectInternal(target, intent, IntentInjectImpl.AUTO);
    }

    /**
     * @param usedMemoryIntent 是否是使用 MemoryIntent 来传递的数据
     */
    public static void inject(@NonNull Object target, @NonNull Intent intent, boolean usedMemoryIntent){
        IntentInjectImpl.injectInternal(target, intent, usedMemoryIntent ? IntentInjectImpl.USE_MEMORY_INTENT : IntentInjectImpl.NO_USE_MEMORY_INTENT);
    }

    /**
     * @IntentParser(intentType = IntentType.MemoryIntent, id = 2)
     * @param id id为 @IntentParser 中的id
     * @param target 即 @IntentParser 注解的方法所在类
     * @param intent 需要 activity.getIntent(), 不能为空
     * @param needRecycleMemoryIntent 如果使用MemoryIntent该参数有效(使用Intent可以忽略该参数) , 即解析完MemoryIntent后是否回收MemoryIntent, 如果被回收则无法进行二次解析
     *                    例如:
     *                          parseIntent(1, this, getIntent(), true);//第一次被回收
     *                          parseIntent(1, this, getIntent(), true);//此次解析不到MemoryIntent, 因为他已经被回收释放.
     *                    正确写法:
     *                          parseIntent(1, this, getIntent(), false);//第一次不回收
     *                          parseIntent(1, this, getIntent(), true);//第二次用完回收 MemoryIntent.
     *
     */
    public static void parseIntent(int id,@NonNull Object target, @NonNull Intent intent, boolean needRecycleMemoryIntent){
        JUtils.checkNull(target, "解析 Intent 时 target 不能为 null");
        JUtils.checkNull(intent, "解析 Intent 时 intent 不能为 null");
        IntentParserImpl.parseIntentInternal(id, target, intent, needRecycleMemoryIntent);
    }

    /**
     * 调用后执行使用注解 @IntentParser 的对应 id 的方法
     * 如果 IntentType 为 MemoryIntent, 则回收MemoryIntent对象
     * @param target 带@IntentParser注解的方法所在的类的对象
     * @param intent 使用 activity.getIntent();
     */
    public static void parseIntent(Object target, @NonNull Intent intent){
        int id = intent.getIntExtra(com.eshel.jump.anno.Intent.PARSE_ID, 0);
        parseIntent(id, target, intent);
    }

    public static void parseIntent(int id, Object target, @NonNull Intent intent){
        parseIntent(id, target, intent, true);
    }

    /**
     * 判断 flags 是否包含 flag
     * @param flags {@link Intent#getFlags() }
     * @param flag  {@link Intent#FLAG_ACTIVITY_CLEAR_TOP ...}
     * @return flags 包含 flag 返回 true ,否则返回 false
     */
    public static boolean hasFlag(int flags, int flag){
        return (flags & flag) != 0;
    }
}
