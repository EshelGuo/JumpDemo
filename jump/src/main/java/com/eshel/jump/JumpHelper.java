package com.eshel.jump;

import com.eshel.jump.configs.JConfig;
import com.eshel.jump.configs.JumpException;

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
}
