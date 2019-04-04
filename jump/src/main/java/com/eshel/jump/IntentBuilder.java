package com.eshel.jump;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eshel.jump.configs.JumpException;
import com.eshel.jump.enums.IntentType;
import com.eshel.jump.enums.JumpType;

import java.io.Serializable;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:52
 */
public class IntentBuilder {

	public static final String KEY_MEMORY_INTENT_HASH_CODE = "KEY_MEMORY_INTENT_HASH_CODE";
	protected ProxyInfo mProxyInfo;

	protected Intent mIntent;
	protected MemoryIntent mMemoryIntent;

	protected Context mContext;
	protected Class targetClass;
	protected String targetClassName;

	protected JumpType mJumpType;
	protected int mRequestCode;
	protected int mParseId;
	private IntentType mIntentType;

	final void setProxyInfo(ProxyInfo proxyInfo){
		mProxyInfo = proxyInfo;
	}

	private MemoryIntent getMemoryIntent(){
		if(mMemoryIntent == null)
			mMemoryIntent = new MemoryIntent();
		return mMemoryIntent;
	}

	public IntentBuilder() {
		mIntent = new Intent();
	}

	public Intent build(){
		if (mMemoryIntent != null){
			int hashCode = mMemoryIntent.hashCode();
			String hashCodeS = String.valueOf(hashCode);
			MemoryIntent.sendIntent(hashCodeS, mMemoryIntent);
			mIntent.putExtra(KEY_MEMORY_INTENT_HASH_CODE, hashCodeS);
		}
		return mIntent;
	}

	public void addFlag(int flag){
		mIntent.addFlags(flag);
	}

	public void setAction(String action) {
		if(action == null)
			return;
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
		if("".equals(type) || type == null)
			return;
		mIntent.setType(type);
	}

	public void setClass(Class targetClass) {
		if(targetClass == null)
			return;
		checkClass(targetClass);

		this.targetClass = targetClass;
		if(mContext != null)
			mIntent.setClass(mContext, this.targetClass);
	}

	private void checkClass(Class targetClass) {
		if(mJumpType == JumpType.StartAct || mJumpType == JumpType.StartActForResult){
			if(!Activity.class.isAssignableFrom(targetClass)){
				throw new JumpException(mProxyInfo, "jumpType 为 startActivity, 但提供的Class却不是Activity-- "+targetClass.getSimpleName());
			}
		}else if(mJumpType == JumpType.SendBroadcastReceiver){
			if(!BroadcastReceiver.class.isAssignableFrom(targetClass))
				throw new JumpException(mProxyInfo, "jumpType 为 sendBroadcastReceiver, 但提供的Class却不是BroadcastReceiver-- "+targetClass.getSimpleName());
		}else if(mJumpType == JumpType.StartService){
			throw new JumpException(mProxyInfo, "jumpType 为 StartService, 但提供的Class却不是Service-- "+targetClass.getSimpleName());
		}
	}

	public void setTargetName(String targetName) {
		if("".equals(targetName) || targetName == null)
			return;
		this.targetClassName = targetName;
		if(mContext != null)
			mIntent.setClassName(mContext, targetClassName);
	}

	public void setContext(Context context){
		if(context == null)
			return;
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
			if(mIntentType == IntentType.Intent) {
				mIntent.putExtra(key, params.getString(key));
			} else if(mIntentType == IntentType.MemoryIntent){
				getMemoryIntent().save(key, params.getString(key));
			}
		}
	}

	public void setJumpType(JumpType jumpType) {
		mJumpType = jumpType;
	}

	public void setRequestCode(int requestCode) {
		this.mRequestCode = requestCode;
	}

	public void setParseId(int parseId) {
		mParseId = parseId;
	}

	public Context getContext() {
		return mContext;
	}

	public void setIntentType(IntentType intentType) {
		mIntentType = intentType;
	}

	public void setParams(String key, Object value) {
		if(key == null || value == null)
			return;
		if(mIntentType == IntentType.Intent){
			if(value instanceof Serializable)
				mIntent.putExtra(key, (Serializable) value);
			else {
				throw new JumpException(mProxyInfo, "使用 Intent 传递的数据必须是基本数据类型或者实现 Serializable 接口." +
						"\n\t 如不想实现 Serializable 接口可尝试将 @Intent 注解中 intentType 改为 IntentType.MemoryIntent");
			}
		}else if(mIntentType == IntentType.MemoryIntent){
			getMemoryIntent().save(key, value);
		}
	}
}
