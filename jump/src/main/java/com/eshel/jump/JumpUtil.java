package com.eshel.jump;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.configs.JumpException;
import com.eshel.jump.enums.IntentType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

@Deprecated
public class JumpUtil {






    public static Params getParamsAnno(Annotation[] annos) {
        if(annos == null)
            return null;
        for (Annotation annotation : annos) {
            if( annotation instanceof Params)
                return (Params) annotation;
        }
        return null;
    }

    public static Activity getActivity(Context context) {
        // Gross way of unwrapping the Activity so we can get the FragmentManager
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }





    private static final int INT = 10;
    private static final int FLOAT = 11;
    private static final int STRING = 12;
    // type: 0 int, 1 float ,2 String
    public static<T> T getFlag(Object target, Intent intent, String key, int type){
        Object flag = null;
        if(intent != null) {
            switch (type){
                case INT:
                    flag = intent.getIntExtra(key, 0);
                    break;
                case FLOAT:
                    flag = intent.getFloatExtra(key, 0);
                    break;
                case STRING:
                    flag = intent.getStringExtra(key);
                    break;
                default:
                    flag = intent.getSerializableExtra(key);
                    break;
            }

            if((flag instanceof Integer || int.class.isInstance(flag)) && (int)flag != 0){
                return (T) flag;
            }else if((flag instanceof Float || float.class.isInstance(flag)) && (float)flag != 0){
                return (T) flag;
            }else if(flag instanceof String)
                return (T) flag;
            else
                return (T) flag;
        }
        MemoryIntent memoryIntent = MemoryIntent.getIntent(target.getClass());
        if(memoryIntent != null)
            flag = memoryIntent.load(key, Object.class);
        return (T) flag;
    }

    public static float getFlagFloat(Object target, Intent intent, String key) {
        Object flag = getFlag(target, intent, key, FLOAT);
        if(flag instanceof Float || float.class.isInstance(flag))
            return (float) flag;
        return 0;
    }

    public static int getFlagInt(Object target, Intent intent, String key) {
        Object flag = getFlag(target, intent, key, INT);
        if(flag instanceof Integer|| int.class.isInstance(flag))
            return (int) flag;
        return 0;
    }

    public static String getFlagString(Object target, Intent intent, String key) {
        Object flag = getFlag(target, intent, key, STRING);
        if(flag instanceof String)
            return (String) flag;
        return null;
    }
}
