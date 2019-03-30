package com.eshel.jump;

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
}
