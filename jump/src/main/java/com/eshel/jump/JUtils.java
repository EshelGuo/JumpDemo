package com.eshel.jump;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Base64;

import com.eshel.jump.log.JLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

    public static boolean isEmpty(String text){
        return isNull(text) || "".equals(text);
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

    // 字符串编码
    private static final String UTF_8 = "UTF-8";

    /**
     * 解密字符串
     */
    public static String base64Decode(String text) {
        try {
            if (isNull(text)) {
                return null;
            }
            return new String(Base64.decode(text.getBytes(UTF_8), Base64.DEFAULT));
        } catch (Exception e) {
            JLog.printStackTrace(e);
        }
        return null;
    }

    /**
     * 加密的字符串
     */
    public static String base64Encode(String text) {
        try {
            if (isNull(text)) {
                return null;
            }
            return Base64.encodeToString(text.getBytes(UTF_8), android.util.Base64.DEFAULT);
        } catch (Exception e) {
            JLog.printStackTrace(e);
        }
        return null;
    }

    public static boolean isEmpty(Map map) {
        return isNull(map) || map.size() == 0;
    }

    /**
     * 判断String是否为boolean类型
     */
    public static boolean checkStringIsBoolean(String str){
        return "true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str);
    }
}
