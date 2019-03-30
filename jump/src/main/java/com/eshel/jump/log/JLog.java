package com.eshel.jump.log;

import com.eshel.jump.configs.JConfig;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:03
 * desc: 日志工具
 */
public class JLog {

	public static void v(String tag, String msg) {
		JConfig.getInstance().getLogImpl().v(tag, msg);
	}

	public static void d(String tag, String msg) {
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
}
