package com.eshel.jump.configs.interceptors;

import android.content.Context;
import android.content.Intent;

import com.eshel.jump.Call;
import com.eshel.jump.IntentBuilder;
import com.eshel.jump.JUtils;
import com.eshel.jump.JumpHelper;
import com.eshel.jump.configs.JConfig;

/**
 * createBy Eshel
 * createTime: 2019/4/24 09:51
 * desc: 执行 Interceptor 工具
 */
public class InterceptorUtil {

	public static void invokeOnCallCreated(Call call){
		for (Interceptor interceptor : JConfig.getInstance().getInterceptorList()) {
			if(JUtils.notNull(interceptor)){
				interceptor.onCallCreated(call);
			}
		}
	}

	public static void invokeOnJumpExecute(Context context, Call call, Intent intent){
		for (Interceptor interceptor : JConfig.getInstance().getInterceptorList()) {
			if(JUtils.notNull(interceptor)){
				interceptor.onJumpExecute(context, call, intent);
			}
		}
	}

	public static void invokeBeforeBuildIntent(IntentBuilder builder){
		for (Interceptor interceptor : JConfig.getInstance().getInterceptorList()) {
			if(JUtils.notNull(interceptor)){
				interceptor.beforeBuildIntent(builder);
			}
		}
	}
}
