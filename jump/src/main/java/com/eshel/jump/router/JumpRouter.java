package com.eshel.jump.router;

import android.content.Context;

import com.eshel.jump.Call;
import com.eshel.jump.JUtils;

import java.net.URI;
import java.util.zip.GZIPInputStream;

/**
 * createBy Eshel
 * createTime: 2019/4/6 18:07
 * desc: 使用短链接进行Activity跳转
 */
public class JumpRouter {

	/**
	 * 解析链接
	 */
	public static Call fromLink(Context context, String link){
		// jump://com.eshel.MainActivity?title=你好&id=2&id=S_2
		JumpURI uri = JumpURI.create(link);
		if(uri.isValid()){
			return new Call(context, uri);
		}
		return null;
	}

	public static String generateLink(Call call){
		if(JUtils.isNull(call))
			return null;
		return call.toUri().toString();
	}
}
