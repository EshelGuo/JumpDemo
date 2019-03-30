package com.eshel.jump.log;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:03
 * desc: TODO
 */
public interface ILog {
	void v(String tag, String msg);
	void d(String tag, String msg);
	void i(String tag, String msg);
	void w(String tag, String msg);
	void e(String tag, String msg);
}
