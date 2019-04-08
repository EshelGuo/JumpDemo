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
	 * jump://com.eshel.jump.DemoAct?intentType=MemoryIntent 可以通过参数指定跳转特殊参数
	 * intentType 对应使用 JumpHelper 跳转的 {@link com.eshel.jump.enums.IntentType} MemoryIntent 和 Intent
	 * jumpType : startActivity startActivityForResult
	 * requestCode: 使用startActivityForResult条转传入该参数
	 * 参数 string 类型需要对 value 的有效内容进行 URLEncode.encode()编码, 编码 charset 为 UTF-8 (编码为GBK会导致中文乱码)
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
		return call.toUri(true).toString();
	}
}
