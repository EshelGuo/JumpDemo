package com.eshel.jump;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.Params;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JumpInvokeHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Context context = null;
        Class<? extends Activity> targetAct;

        Intent intentAnno = method.getAnnotation(Intent.class);
        //跳过接口中没有被 Intent 注解标注的类
        if(intentAnno == null){
            Log.w(Const.TAG, JumpException.JumpExpType.HaveMethodNoIntentAnno.getAllMessage(proxy, method));
            return null;
        }

        targetAct = intentAnno.target();

        if(targetAct == null){
            throw new JumpException(JumpException.JumpExpType.TargetIsNull, proxy, method);
        }

        if(args == null || args.length == 0){
            throw new JumpException(JumpException.JumpExpType.MethodNoneParams, proxy, method);
        }

        Object intent = null;
        if(intentAnno.intentType() == IntentType.Intent){
            intent = new android.content.Intent();
        }else if(intentAnno.intentType() == IntentType.MemoryIntent){
            intent = new MemoryIntent();
        }

        Annotation[][] paramsAnnos = method.getParameterAnnotations();
        Log.i(Const.TAG, paramsAnnos.length+" paramsAnnos length");

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Params annotation = JumpUtil.getParamsAnno(paramsAnnos[i]);
            if(arg instanceof Context && context == null && annotation == null){
                context = (Context) arg;
                continue;
            }

            String key;
            if(annotation != null){
                key = annotation.value();
            }else {
                throw new JumpException(JumpException.JumpExpType.NoneParamsKey, proxy, method);
            }
            putParamsToIntent(intent, key, arg, proxy, method);
        }

        if(context == null){
            throw new JumpException(JumpException.JumpExpType.NoneContext, proxy, method);
        }
        //final: 最终的
        android.content.Intent finalIntent = putContextTargetToIntent(intent, context, targetAct);

        if(intentAnno.jumpType() == JumpType.StartAct){
            context.startActivity(finalIntent);
        }else if(intentAnno.jumpType() == JumpType.StartActForResult){
            Activity activity = JumpUtil.getActivity(context);
            if(activity == null)
                throw new JumpException(JumpException.JumpExpType.ContextNotActivity, proxy, method);
            int requestCode = intentAnno.requestCode();
            if(requestCode == 0){
                Log.w(Const.TAG, JumpException.JumpExpType.NoneRequestCode.getAllMessage(proxy, method));
            }
            activity.startActivityForResult(finalIntent, requestCode);
        }
        return null;
    }

    private android.content.Intent putContextTargetToIntent(Object intentO, Context context, Class<? extends Activity> targetAct) {
        android.content.Intent intent = null;
        if(intentO instanceof MemoryIntent){
            intent = new android.content.Intent(context, targetAct);
            MemoryIntent.sendIntent(targetAct, (MemoryIntent) intentO);
        }else if(intentO instanceof android.content.Intent){
            intent = (android.content.Intent) intentO;
            intent.setClass(context, targetAct);
        }
        return intent;
    }

    private void putParamsToIntent(Object intent, String key, Object arg, Object proxy, Method method) throws JumpException {
        if(intent instanceof MemoryIntent){
            ((MemoryIntent) intent).save(key, arg);
        }else if(intent instanceof android.content.Intent){
           if(arg == null)
               ((android.content.Intent) intent).putExtra(key, (Serializable) arg);
           else if(arg instanceof Serializable)
               ((android.content.Intent) intent).putExtra(key, (Serializable) arg);
           else {
               Log.e(Const.TAG, arg.getClass().getSimpleName());
               throw new JumpException(JumpException.JumpExpType.ParamsNeedImplSerializable, proxy, method);
           }
        }
    }
}
