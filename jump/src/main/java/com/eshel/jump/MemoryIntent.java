package com.eshel.jump;

import android.support.v4.util.ArrayMap;

import java.util.Map;

/**
 * Intent 的替代品, 参数将存储在内存
 */
public class MemoryIntent {
    private static Map<Class, MemoryIntent> intents;
    static {
        intents = new ArrayMap<>();
    }
    public MemoryIntent(){
        mDatas = new ArrayMap<>();
    }
    public static MemoryIntent getIntent(Class key){
        return intents.get(key);
    }

    public static synchronized void sendIntent(Class key, MemoryIntent intent){
        intents.put(key, intent);
    }

    public static void recycleIntent(Class clazz){
        intents.remove(clazz);
    }

    public static void recycleIntent(MemoryIntent intent){
        Class key = null;
        for (Map.Entry<Class, MemoryIntent> entry : intents.entrySet()) {
            if(entry.getValue() == intent)
                key = entry.getKey();
        }
        if(key != null)
            intents.remove(key);
    }

    private Map<String,Object> mDatas;

    public void save(String key, Object value){
        if(mDatas != null && key != null){
            mDatas.put(key,value);
        }
    }

    public<T> T load(String key, Class<T> type){
        try {
            if(key != null){
                Object value = mDatas.get(key);
                if(value != null) {
                    return (T) value;
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载并移除
     */
    public <T> T loadOnce(String key, Class<T> type){
        try {
            if(key != null){
                Object value = mDatas.remove(key);
                if(value != null)
                    return (T) value;
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }
}
