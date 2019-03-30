package com.eshel.jump;

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
}
