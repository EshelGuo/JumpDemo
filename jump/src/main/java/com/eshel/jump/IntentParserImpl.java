package com.eshel.jump;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.anno.Type;
import com.eshel.jump.configs.JumpConst;
import com.eshel.jump.configs.JumpException;
import com.eshel.jump.enums.IntentType;
import com.eshel.jump.log.JLog;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * createBy Eshel
 * createTime: 2019/4/6 06:17
 * desc: TODO
 */
public class IntentParserImpl {

	public static void parseIntentInternal(int id, @NonNull Object target, @NonNull Intent intent, boolean needRecycleMemoryIntent){
		Class<?> clazz = target.getClass();
		Method[] methods = clazz.getDeclaredMethods();

		Method targetMethod = null;
		IntentParser intentParserAnno = null;
		for (Method method : methods) {
//            if(!Modifier.isPublic(method))
			IntentParser annotation = method.getAnnotation(IntentParser.class);
			if(annotation == null)
				continue;
			if(id != annotation.id())
				continue;
			targetMethod = method;
			intentParserAnno = annotation;
			break;
		}

		if(targetMethod == null){
			JLog.printStackTrace(new JumpException(JumpException.JumpExpType.NoneIntentParser));
			return;
		}

		if(!Modifier.isPublic(targetMethod.getModifiers()))
			targetMethod.setAccessible(true);

		Annotation[][] annos = targetMethod.getParameterAnnotations();
		Class<?>[] types = targetMethod.getParameterTypes();

		if(annos == null || types == null || types.length == 0) {
			try {
				targetMethod.invoke(target);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return;
		}

		Object[] params = new Object[types.length];
		MemoryIntent intentM = null;

		if(intentParserAnno.intentType() == IntentType.MemoryIntent) {
			intentM = MemoryIntent.getIntent(intent.getStringExtra(IntentBuilder.KEY_MEMORY_INTENT_HASH_CODE));
			if(intentM == null) {
				JLog.e(JumpConst.TAG, "error!!! parse intent failed: MemoryIntent is null");
				return;
			}
		}

		AnnoProvider provider = new AnnoProvider();

		for (int i = 0; i < types.length; i++) {
			provider.initParser(annos[i]);
			Params paramsAnno;
			Object result = null;

			if((paramsAnno = provider.getParamsAnno()) != null){
				String paramsKey = paramsAnno.value();
				if(intentParserAnno.intentType() == IntentType.Intent){
					result = intent.getSerializableExtra(paramsKey);
				}else if(intentParserAnno.intentType() == IntentType.MemoryIntent){
					result = intentM.load(paramsKey, Object.class);
				}
			}else if(provider.getActionAnno() != null){
				result = intent.getAction();
			}else if(provider.getTypeAnno() != null){
				result = intent.getType();
			}else if(provider.getCategoryAnno() != null){
				Class<?> type = types[i];
				Set<String> categories = intent.getCategories();
				if(categories == null || categories.size() == 0){
					continue;
				}
				if(type.isInstance(categories)){
					result = categories;
				}else if(type.isArray() && (type == Object[].class || type == String[].class)){
					result = categories.toArray(new String[categories.size()]);
				}else if(type == String.class){
					result = categories.iterator().next();
				}else if(type.isAssignableFrom(ArrayList.class)){
					ArrayList<String> temp = new ArrayList<>(categories.size());
					temp.addAll(categories);
					result = temp;
				}else if(type.isAssignableFrom(LinkedList.class)){
					LinkedList<String> temp = new LinkedList<>();
					temp.addAll(categories);
					result = temp;
				}else {
					throw new JumpException(new ProxyInfo(target.getClass().getName(), targetMethod.getName()), "暂不支持该数据类型: " + type.getName());
				}
			}else if(provider.getFlagAnno() != null){
				int flags = intent.getFlags();
				Class<?> type = types[i];
				if(type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)){
					result = flags;
				}else {
					throw new JumpException(new ProxyInfo(target.getClass().getName(), targetMethod.getName()), "flags(@Flag) 仅支持 int 类型");
				}
			}
			params[i] = result;
		}

		//回收MemoryIntent对象
		if(needRecycleMemoryIntent && intentM != null){
			MemoryIntent.recycleIntent(intentM);
		}

		try {
			targetMethod.invoke(target, params);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
