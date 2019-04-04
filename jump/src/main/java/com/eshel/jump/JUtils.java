package com.eshel.jump;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import java.util.ArrayList;

/**
 * Created by guoshiwen on 2019/3/29.
 */

public class JUtils {

    public static void checkNull(Object obj){
        checkNull(obj, "object is null!!!");
    }

    public static void checkNull(Object obj, String msg){
        if(isNull(obj)){
            throw new NullPointerException(msg);
        }
    }

    public static boolean isNull(Object obj){
        return obj == null;
    }

    /**
     * 判断数组是否为null
     */
    public static boolean isEmpty(Object[] objs){
        if(objs == null || objs.length == 0)
            return true;
        for (Object obj : objs) {
            if(obj != null)
                return false;
        }
        return true;
    }

    public static String[] subStrings(String[] ... arrayss){
        if(arrayss == null)
            return null;
        ArrayList<String> result = new ArrayList<>(arrayss.length * 2);
        for (String[] strings : arrayss) {
            if(strings == null)
                continue;
            for (String string : strings) {
                if(string != null)
                    result.add(string);
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public static Activity getActivityFromContext(Context context){
        if(context instanceof Application)
            return null;
        while(true) {
            if(context instanceof Activity)
                return (Activity) context;
            if(context instanceof ContextWrapper){
                ContextWrapper cw = (ContextWrapper) context;
                context = cw.getBaseContext();
            }else {
                break;
            }
        }
        return null;
    }
}
