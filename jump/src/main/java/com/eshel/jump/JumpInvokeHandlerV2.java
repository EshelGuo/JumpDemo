package com.eshel.jump;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by guoshiwen on 2019/3/29.
 */

public class JumpInvokeHandlerV2 implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation[] annotations = method.getAnnotations();
        if(JUtils.isEmpty(annotations))
            return method.invoke(proxy, args);
        Call call = new Call(proxy, method, args, annotations);
        Class<?> returnType = method.getReturnType();
        if(returnType == Call.class)
            return call;
        else {
            call.execute();
            return null;
        }
    }
}
