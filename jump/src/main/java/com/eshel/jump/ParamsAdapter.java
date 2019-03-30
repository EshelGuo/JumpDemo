package com.eshel.jump;

/**
 * createBy Eshel
 * createTime: 2019/3/30 15:04
 * desc: 参数拦截修改
 */
public abstract class ParamsAdapter<TYPE> {
	public Object putToIntent(TYPE obj){
		return null;
	}
	public TYPE parseFromIntent(Object params){
		return null;
	}
}
