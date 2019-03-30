package com.eshel.jump.log;

import android.util.Log;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:04
 * desc: TODO
 */
public class AndroidLogImpl implements ILog{

	@Override
	public void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	@Override
	public void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	@Override
	public void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	@Override
	public void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	@Override
	public void e(String tag, String msg) {
		Log.e(tag, msg);
	}
}
