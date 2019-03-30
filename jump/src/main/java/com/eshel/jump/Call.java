package com.eshel.jump;

import android.os.Bundle;

import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.TargetClass;
import com.eshel.jump.anno.TargetName;
import com.eshel.jump.anno.Type;
import com.eshel.jump.configs.JConfig;

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

	Call(Object proxy, Method method, Object[] args, Annotation[] methodAnnos) {
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.methodAnnos = methodAnnos;
	}

	public void execute(){
		AnnoProvider ap = new AnnoProvider();
		ap.initParser(methodAnnos);

		Intent intentAnno = ap.getIntentAnno();
		MethodAnnoParser parser = new MethodAnnoParser(intentAnno);
		IntentBuilder builder = JConfig.getInstance().getIntentBuilderProvider().provideIntentBuilder();

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
	}
}
