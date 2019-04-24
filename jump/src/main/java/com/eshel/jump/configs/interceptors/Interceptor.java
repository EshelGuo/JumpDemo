package com.eshel.jump.configs.interceptors;

import android.content.Context;
import android.content.Intent;

import com.eshel.jump.Call;
import com.eshel.jump.IntentBuilder;

/**
 * createBy Eshel
 * createTime: 2019/4/24 09:41
 * desc: 拦截器
 */
public interface Interceptor {

	/**
	 * @param call call 对象创建时调用
	 */
	void onCallCreated(Call call);

	/**
	 * 准备跳转 Activity 或者要开启服务发送广播时调用此方法
	 * @param context 上下文对象
	 * @param call 可以使用 call 对象取消跳转
	 * @param intent 跳转使用 intent 对象
	 */
	void onJumpExecute(Context context, Call call, Intent intent);

	/**
	 * 取得 Intent 之前调用
	 * @param builder Intent 构建器 , 可用来往 Intent 中增加参数
	 */
	void beforeBuildIntent(IntentBuilder builder);
}
