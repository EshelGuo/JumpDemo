package com.eshel.jump.impl;

import android.content.Intent;

import com.eshel.jump.AnnoProvider;
import com.eshel.jump.IntentBuilder;
import com.eshel.jump.JUtils;
import com.eshel.jump.MemoryIntent;
import com.eshel.jump.ProxyInfo;
import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.configs.JumpException;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

/**
 * createBy Eshel
 * createTime: 2019/4/23 17:57
 * desc: 注入器实现
 */
public class IntentInjectImpl {

	public static final int USE_MEMORY_INTENT = 1;
	public static final int NO_USE_MEMORY_INTENT = 0;
	public static final int AUTO = 2;

	public static void injectInternal(Object target, Intent intent, int usedMemoryIntent) {
		Class<?> clazz = target.getClass();
		Field[] fields = clazz.getDeclaredFields();
		IntentDataProvider dataProvider = new IntentDataProvider(intent, usedMemoryIntent);

		AnnoProvider provider = new AnnoProvider();
		for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			if(JUtils.isEmpty(annotations)){
				continue;
			}

			Object result = null;

			provider.initParser(annotations);

			if(!Modifier.isPublic(field.getModifiers()))
				field.setAccessible(true);

			Params paramsAnno = provider.getParamsAnno();

			//解析 Params 注解
			if(paramsAnno != null){
				String key = paramsAnno.value();
				result = dataProvider.getExtra(key);
			}
			//解析 Action 注解
			else if(provider.getActionAnno() != null){
				result = intent.getAction();
			}
			//解析 Type 注解
			else if(provider.getTypeAnno() != null){
				result = intent.getType();
			}
			//解析 Category 注解
			else if(provider.getCategoryAnno() != null){
				Class<?> type = field.getType();
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
					throw new JumpException(new ProxyInfo(target.getClass().getName(), field.getName()), "暂不支持该数据类型: " + type.getName());
				}
			}
			//解析 flag 注解
			else if(provider.getFlagAnno() != null){
				int flags = intent.getFlags();
				Class<?> type = field.getType();
				if(type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)){
					result = flags;
				}else {
					throw new JumpException(new ProxyInfo(target.getClass().getName(), field.getName()), "flags(@Flag) 仅支持 int 类型");
				}
			}

			if(result != null){
				Class<?> type = field.getType();
				boolean noTypeError = false;
//				boolean
				if(checkType(type, result, Boolean.class, boolean.class))
					noTypeError = true;
//				byte
				else if(checkType(type, result, Byte.class, byte.class))
					noTypeError = true;
//				short
				else if(checkType(type, result, Short.class, short.class))
					noTypeError = true;
//				int
				else if(checkType(type, result, Integer.class, int.class))
					noTypeError = true;
//				long
				else if(checkType(type, result, Long.class, long.class))
					noTypeError = true;
//				float
				else if(checkType(type, result, Float.class, float.class))
					noTypeError = true;
//				double
				else if(checkType(type, result, Double.class, double.class))
					noTypeError = true;
//				char
				else if(checkType(type, result, Character.class, char.class))
					noTypeError = true;

				if(result.getClass().isAssignableFrom(field.getType()) || field.getType().isInstance(result)){
					noTypeError = true;
				}

				if(noTypeError){
					try {
						field.set(target, result);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	private static boolean checkType(Class checkedType, Object result, Class type1, Class type2) {
		return (type1.isInstance(result) || type2.isInstance(result)) && (checkedType == type1 || checkedType == type2);
	}

	private static class IntentDataProvider{
		private int usedMemoryIntent;
		private Intent mIntent;
		private MemoryIntent mMemoryIntent;
		public IntentDataProvider(Intent intent, int usedMemoryIntent) {
			mIntent = intent;
			this.usedMemoryIntent = usedMemoryIntent;
			String memoryIntentKey = intent.getStringExtra(IntentBuilder.KEY_MEMORY_INTENT_HASH_CODE);
			if(usedMemoryIntent == IntentInjectImpl.USE_MEMORY_INTENT) {
				if(memoryIntentKey != null){
					MemoryIntent memoryIntent = MemoryIntent.getIntent(memoryIntentKey);
					if(memoryIntent != null){
						mMemoryIntent = memoryIntent;
						MemoryIntent.recycleIntent(memoryIntent);
					}
				}
			}else if(usedMemoryIntent == IntentInjectImpl.AUTO){
				if(memoryIntentKey == null){
					this.usedMemoryIntent = IntentInjectImpl.NO_USE_MEMORY_INTENT;
					return;
				}

				MemoryIntent memoryIntent = MemoryIntent.getIntent(memoryIntentKey);
				if(memoryIntent == null){
					this.usedMemoryIntent = IntentInjectImpl.NO_USE_MEMORY_INTENT;
					return;
				}

				this.usedMemoryIntent = IntentInjectImpl.USE_MEMORY_INTENT;
				mMemoryIntent = memoryIntent;
			}

		}

		public Object getExtra(String key){
			switch (usedMemoryIntent){
				case IntentInjectImpl.USE_MEMORY_INTENT:
					if(mMemoryIntent == null)
						return null;

					return mMemoryIntent.loadOnce(key, Object.class);
				case IntentInjectImpl.NO_USE_MEMORY_INTENT:
					if(mIntent == null)
						return null;

					return mIntent.getSerializableExtra(key);
			}
			return null;
		}
	}
}
