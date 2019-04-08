package com.eshel.jump.log;

import android.util.Log;

import com.eshel.jump.configs.JConfig;
import com.eshel.jump.configs.JumpConst;
import com.eshel.jump.jump.BuildConfig;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:03
 * desc: 日志工具
 */
public class JLog {

	private static boolean isLogging = BuildConfig.DEBUG;

	public static void openLog(){
		isLogging = true;
	}

	public static void v(String tag, String msg) {
		if(isLogging)
			JConfig.getInstance().getLogImpl().v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if(isLogging)
			JConfig.getInstance().getLogImpl().d(tag, msg);
	}

	public static void i(String tag, String msg) {
		JConfig.getInstance().getLogImpl().i(tag, msg);
	}

	public static void w(String tag, String msg) {
		JConfig.getInstance().getLogImpl().w(tag, msg);
	}

	public static void e(String tag, String msg) {
		JConfig.getInstance().getLogImpl().e(tag, msg);
	}

	public static void printStackTrace(Throwable throwable){
		String stackTraceString = Log.getStackTraceString(throwable);
		w(JumpConst.TAG, stackTraceString);
	}

	public static void printStackTraceD(Throwable throwable){
		String stackTraceString = Log.getStackTraceString(throwable);
		d(JumpConst.TAG, stackTraceString);
	}
}
