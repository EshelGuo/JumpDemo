package com.eshel.jump;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:52
 * desc: TODO
 */
public class IntentBuilder {
	protected Context mContext;
	protected Intent mIntent;
	protected Class targetClass;
	protected String targetClassName;

	public IntentBuilder() {
		mIntent = new Intent();
	}

	public Intent build(){
		return mIntent;
	}

	public void addFlag(int flag){
		mIntent.addFlags(flag);
	}

	public void setAction(String action) {
		mIntent.setAction(action);
	}

	public void addCategory(String[] categorys) {
		if(categorys != null){
			for (String category : categorys) {
				if(category != null)
					mIntent.addCategory(category);
			}
		}
	}

	public void setType(String type) {
		mIntent.setType(type);
	}

	public void setClass(Class targetClass) {
		this.targetClass = targetClass;
		if(mContext != null)
			mIntent.setClass(mContext, this.targetClass);
	}

	public void setTargetName(String targetName) {
		this.targetClassName = targetName;
		if(mContext != null)
			mIntent.setClassName(mContext, targetClassName);
	}

	public void setContext(@NonNull Context context){
		this.mContext = context;
		if(targetClass != null)
			mIntent.setClass(mContext, targetClass);
		else if(targetClassName != null){
			mIntent.setClassName(mContext, targetClassName);
		}
	}

	public void setMethodExtra(Bundle params) {
		if(params == null)
			return;
		for (String key : params.keySet()) {
			mIntent.putExtra(key, params.getString(key));
		}
	}
}
