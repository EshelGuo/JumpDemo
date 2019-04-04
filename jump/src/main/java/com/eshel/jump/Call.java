package com.eshel.jump;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.eshel.jump.anno.AContext;
import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.Params;
import com.eshel.jump.anno.TargetClass;
import com.eshel.jump.anno.TargetName;
import com.eshel.jump.anno.Type;
import com.eshel.jump.configs.JConfig;
import com.eshel.jump.configs.JumpException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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

	Call(Object proxy, Method method, Object[] args, Annotation[] methodAnnos) {
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.methodAnnos = methodAnnos;
		Class<?>[] proxyInterfaces = proxy.getClass().getInterfaces();
		mProxyInfo = new ProxyInfo(getInterfacesName(proxyInterfaces), method.getName());
	}

	private String getInterfacesName(Class<?>[] proxyInterfaces) {
		if(proxyInterfaces == null || proxyInterfaces.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < proxyInterfaces.length; i++) {
			if(i != 0)
				sb.append(", ");
			sb.append(proxyInterfaces[i].getName());
		}
		return sb.toString();
	}

	public void cancel(){
		isCancel = true;
	}

	public void execute(){
		if(args == null || args.length == 0)
			return;
		if(isCancel)
			return;

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

		parseMethodParams(ap, parser, builder);

		invokeIntent(builder);
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

			Params paramsAnno = ap.getParamsAnno();
			builder.setParams(paramsAnno.value(), arg);
		}
	}
}
