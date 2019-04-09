package com.eshel.jump;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.eshel.jump.anno.AContext;
import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.Data;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.Params;
import com.eshel.jump.anno.TargetClass;
import com.eshel.jump.anno.TargetName;
import com.eshel.jump.anno.Type;
import com.eshel.jump.configs.JConfig;
import com.eshel.jump.configs.JumpConst;
import com.eshel.jump.configs.JumpException;
import com.eshel.jump.enums.IntentType;
import com.eshel.jump.enums.JumpType;
import com.eshel.jump.log.JLog;
import com.eshel.jump.router.JumpURI;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:19
 * desc: 用于启动Intent
 */
public final class Call {
	private Object proxy;
	private Method method;
	private Object[] args;
	private Annotation[] methodAnnos;

	private volatile boolean isCancel;

	private ProxyInfo mProxyInfo;

	private JumpURI uri;
	private Context context;

	Call(Object proxy, Method method, Object[] args, Annotation[] methodAnnos) {
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.methodAnnos = methodAnnos;
		Class<?>[] proxyInterfaces = proxy.getClass().getInterfaces();
		mProxyInfo = new ProxyInfo(proxyInterfaces, method.getName());
	}

	public Call(Context context, JumpURI uri){
		this.context = context;
		this.uri = uri;
	}

	public JumpURI toUri(boolean base64Encode){
		try {
			if(uri != null)
				return uri;
			IntentBuilder builder = generateIntentBuilder();
			String scheme = base64Encode ? JumpURI.SCHEME_JUMPS : JumpURI.SCHEME_JUMP;
			String path = null;
			Map<String, Serializable> params = null;

			if(builder.targetClass != null)
				path = builder.targetClass.getName();
			if(path == null && builder.targetClassName != null)
				path = builder.targetClassName;

			if(path == null)
				return null;
			if(!(builder.mJumpType == JumpType.StartActForResult || builder.mJumpType == JumpType.StartAct))
				return null;

			android.content.Intent intent = builder.build();
			if(builder.mIntentType == IntentType.MemoryIntent){
				String memoryIntentKey = intent.getStringExtra(IntentBuilder.KEY_MEMORY_INTENT_HASH_CODE);
				MemoryIntent memoryIntent = MemoryIntent.getIntent(memoryIntentKey);
				Map<String,Object> paramMap = memoryIntent.getAll();
				params = new HashMap<>(paramMap.size());
				for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
					if(entry.getValue() instanceof Serializable)
						params.put(entry.getKey(), (Serializable) entry.getValue());
					else {
						JLog.e(JumpConst.TAG, "Call.toUri() 方法暂时不支持解析 JavaBean 对象(仅支持基础数据类型的解析)");
						return null;
					}
				}
				params.put(JumpConst.INTENT_TYPE, JumpConst.TYPE_MEMORY_INTENT);
			}else {
				Bundle extras = intent.getExtras();
				params = new HashMap<>(extras.size());
				for (String key : extras.keySet()) {
					params.put(key, (Serializable) extras.get(key));
				}
			}

			if(builder.mJumpType == JumpType.StartActForResult){
				params.put(JumpConst.JUMP_TYPE, JumpConst.JUMP_TYPE_START_ACTIVITY_FOR_RESULT);
				params.put(JumpConst.REQUEST_CODE, builder.mRequestCode);
			}

			return new JumpURI(scheme, path, params);
		}catch (Exception e){
			JLog.printStackTraceD(e);
		}
		return null;
	}

	public void cancel(){
		isCancel = true;
	}

	public void execute(){
		if(proxy != null)
			executeFromProxyInternal();
		else if(uri != null)
			executeFromJumpUriInternal();
	}

	private void executeFromJumpUriInternal() {
		IntentBuilder builder = JConfig.getInstance().getIntentBuilderProvider().provideIntentBuilder();
		builder.setContext(context);
		builder.setTargetName(uri.getPath());

		Set<Map.Entry<String, Serializable>> entries = uri.getParams().entrySet();
		Iterator<Map.Entry<String, Serializable>> it = entries.iterator();

		while (it.hasNext()){
			Map.Entry<String, Serializable> entry = it.next();
			String key = entry.getKey();
			Serializable value = entry.getValue();

			if(key.equals(JumpConst.INTENT_TYPE) && value instanceof String){
				String intentType = (String) value;
				IntentType type = null;
				if(intentType.equals(JumpConst.TYPE_INTENT))
					type = IntentType.Intent;
				else if(intentType.equals(JumpConst.TYPE_MEMORY_INTENT))
					type = IntentType.MemoryIntent;
				builder.setIntentType(type);
			}else if(key.equals(JumpConst.FLAG) && (value instanceof Integer || int.class.isInstance(value))){
				builder.addFlag((Integer) value);
			}else if(key.equals(JumpConst.JUMP_TYPE) && value instanceof String){
				JumpType type = null;
				String jumpType = (String) value;
				if(jumpType.equals(JumpConst.JUMP_TYPE_START_ACTIVITY))
					type = JumpType.StartAct;
				else if(jumpType.equals(JumpConst.JUMP_TYPE_START_ACTIVITY_FOR_RESULT))
					type = JumpType.StartActForResult;
				builder.setJumpType(type);
			} else if(key.equals(JumpConst.REQUEST_CODE) && (value instanceof Integer || int.class.isInstance(value))){
				int requestCode = (int) value;
				builder.setRequestCode(requestCode);
			}else if(key.equals(Intent.PARSE_ID) && (value instanceof Integer || int.class.isInstance(value))){
				int parseId = (int) value;
				builder.setParseId(parseId);
			}else {
				continue;
			}
			it.remove();
		}

		if(builder.mIntentType == null)
			builder.mIntentType = IntentType.Intent;
		if(builder.mJumpType == null)
			builder.setJumpType(JumpType.StartAct);


		for (Map.Entry<String, Serializable> entry : entries) {
			builder.setParams(entry.getKey(), entry.getValue());
		}

		invokeIntent(builder);
	}

	private void executeFromProxyInternal() {
		if(args == null || args.length == 0)
			return;
		if(isCancel)
			return;

		IntentBuilder builder = generateIntentBuilder();
		invokeIntent(builder);
	}

	@NonNull
	private IntentBuilder generateIntentBuilder() {
		AnnoProvider ap = new AnnoProvider();
		ap.initParser(methodAnnos);

		Intent intentAnno = ap.getIntentAnno();
		MethodAnnoParser parser = new MethodAnnoParser(intentAnno);
		IntentBuilder builder = JConfig.getInstance().getIntentBuilderProvider().provideIntentBuilder();
		builder.setProxyInfo(mProxyInfo);

		builder.setJumpType(intentAnno.jumpType());
		builder.setRequestCode(intentAnno.requestCode());
		builder.setParseId(intentAnno.parseId());
		builder.setIntentType(intentAnno.intentType());

		Flag flagAnno = ap.getFlagAnno();
		int flag = parser.parseFlag(flagAnno);
		builder.addFlag(flag);

		Action actionAnno = ap.getActionAnno();
		String action = parser.parseAction(actionAnno);
		builder.setAction(action);

		Category categoryAnno = ap.getCategoryAnno();
		String[] categorys = parser.parseCategory(categoryAnno);
		builder.addCategory(categorys);

		Type typeAnno = ap.getTypeAnno();
		String type = parser.parseType(typeAnno);
		builder.setType(type);

		TargetClass targetClassAnno = ap.getTargetClassAnno();
		Class targetClass = parser.parseTargetClass(targetClassAnno);
		builder.setClass(targetClass);

		TargetName targetNameAnno = ap.getTargetNameAnno();
		String targetName = parser.parseTargetname(targetNameAnno);
		builder.setTargetName(targetName);

		ExtraParams extraParamsAnno = ap.getExtraParamsAnno();
		Bundle params = parser.parseExtraParams(extraParamsAnno);
		builder.setMethodExtra(params);

		Data dataAnno = ap.getDataAnno();
		Uri data = parser.parseData(dataAnno);
		builder.setData(data);

		parseMethodParams(ap, parser, builder);
		return builder;
	}

	private void invokeIntent(IntentBuilder builder) {
		android.content.Intent finalIntent = builder.build();
		Context context = builder.getContext();
		if(isCancel)
			return;
		switch (builder.mJumpType){
			case StartAct:
				if(context instanceof Application)
					finalIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(finalIntent);
				break;
			case StartActForResult:
				Activity activity = JUtils.getActivityFromContext(context);
				if(activity == null)
					throw new JumpException(mProxyInfo, "使用 startActivityForResult() 跳转Activity提供的Context必须是Activity");
				activity.startActivityForResult(finalIntent, builder.mRequestCode);
				break;
			case StartService:
				context.startService(finalIntent);
				break;
			case SendBroadcastReceiver:
				context.sendBroadcast(finalIntent);
				break;
		}
	}

	private void parseMethodParams(AnnoProvider ap, MethodAnnoParser parser, IntentBuilder builder) {
		if(isCancel)
			return;
		Annotation[][] paramsAnnos = method.getParameterAnnotations();
		Class<?>[] paramsTypes = method.getParameterTypes();
		for (int i = 0; i < args.length; i++) {
			Class<?> type = paramsTypes[i];
			Object arg = args[i];
			ap.initParser(paramsAnnos[i]);

			AContext contextAnno = ap.getContextAnno();
			Context context = parser.parseParamsContext(builder, contextAnno, type, arg);
			builder.setContext(context);

			Flag flagAnno = ap.getFlagAnno();
			int flag = parser.parseParamsFlag(flagAnno, type, arg);
			builder.addFlag(flag);

			Action actionAnno = ap.getActionAnno();
			String action = parser.parseParamsAction(actionAnno, type, arg);
			builder.setAction(action);

			Category categoryAnno = ap.getCategoryAnno();
			String[] categorys = parser.parseParamsCategory(categoryAnno, type, arg);
			builder.addCategory(categorys);

			Type typeAnno = ap.getTypeAnno();
			String type_ = parser.parseParamsType(typeAnno, type, arg);
			builder.setType(type_);

			TargetClass targetClassAnno = ap.getTargetClassAnno();
			Class target = parser.parseParamsTargetClass(targetClassAnno, type, arg);
			builder.setClass(target);

			TargetName targetNameAnno = ap.getTargetNameAnno();
			String targetName = parser.parseParamsTargetName(targetNameAnno, type, arg);
			builder.setTargetName(targetName);

			Data dataAnno = ap.getDataAnno();
			Uri data = parser.parseParamsData(dataAnno, type, arg);
			builder.setData(data);

			Params paramsAnno = ap.getParamsAnno();
			if(paramsAnno != null)
				builder.setParams(paramsAnno.value(), arg);
		}
	}
}
