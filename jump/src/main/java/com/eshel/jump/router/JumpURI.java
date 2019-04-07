package com.eshel.jump.router;

import android.os.Build;
import android.util.ArrayMap;

import com.eshel.jump.JUtils;
import com.eshel.jump.log.JLog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//s: safe
//? 后拼接参数
//& 拼接多参数
//key = value
//byte: b0
//二进制 int: 0b10
// 0x121 是 十六进制 int 类型
//short 类型: s12
//int: 12
//long  类型: 12L
//float:  12.0f
//double: 12.0
//boolean true false
//string 类型数字(以 "" 开头结尾的参数会强制以string进行解析): "1212" 为 string 类型
//jumps://base64(xxx)
//jump://com.eshel.MainActivity
//jump://com.eshel.MainActivity?key=test&key2=12
//jump://com.eshel.MainActivity?key=test&key2=0x12
//jump://com.eshel.MainActivity?key=test&key2=0b10
//jump://com.eshel.MainActivity?key=test&key2="12"
//jump://com.eshel.MainActivity?key=test&key2=12.1f
//jump://com.eshel.MainActivity?key=test&key2=12L
//jump://com.eshel.MainActivity?key=test&key2=true   //boolean
//jump://com.eshel.MainActivity?key=test&key2="true" //string

/**
 * createBy Eshel
 * createTime: 2019/4/7 16:26
 */
public class JumpURI {

	public static final String SCHEME_JUMP = "jump";
	public static final String SCHEME_JUMPS = "jumps";

	public static final String schemeSeparator = "://";
	public static final String pathSeparator = "?";
	public static final String paramsSeparator = "&";
	public static final String paramsValueSeparator = "=";

	public static final String stringStartFlag = "\"";
	public static final String stringEndFlag = "\"";
	public static final String byteFlag = "b";
	public static final String shortFlag = "s";
	public static final String binaryFlag = "0b";
	public static final String binaryFlag2 = "0B";
	public static final String hexadecimalFlag = "0x";
	public static final String hexadecimalFlag2 = "0X";

	public static final int SPECIAL_TYPE_LONG = 1;
	public static final int SPECIAL_TYPE_INTEGER = 0;

	private String scheme;
	private boolean invalid;
	private String path;
	private Map<String, Serializable> paramsMap;

	public JumpURI(String link) {
		if (checkInvalid(JUtils.isEmpty(link)))
			return;
		String[] splitLink = link.split(schemeSeparator);

		if (checkInvalid(splitLink.length != 2))
			return;

		scheme = splitLink[0];

		if(checkSchemeInvalid())
			return;

		String body = splitLink[1];
		if(scheme.equals(SCHEME_JUMPS)){
			String base64Body = body;
			body = JUtils.base64Decode(base64Body);
		}

		parseBody(body);
	}

	public JumpURI(String scheme, String path, Map<String, Serializable> params) {
		this.scheme = scheme;
		if(checkSchemeInvalid())
			return;
		this.paramsMap = params;
		this.path = path;
		checkInvalid(JUtils.isEmpty(path) || JUtils.isEmpty(paramsMap));
	}

	public String getScheme(){
		return scheme;
	}

	public String getPath(){
		return path;
	}

	public Map<String, Serializable> getParams(){
		return paramsMap;
	}

	/**
	 * 判断 JumpURI 是否有效
	 * @return true 有效, false 无效
	 */
	public boolean isValid(){
		return !invalid;
	}

	private void parseBody(String body) {
		int pathSeparatorIndex = body.indexOf(pathSeparator);
		if(pathSeparatorIndex == -1){
			path = body;
		}else {
			path = body.substring(0, pathSeparatorIndex);
			parseParams(body.substring(pathSeparatorIndex + 1, body.length()));
		}
//		path = URLDecoder.decode(path);
	}

	private void parseParams(String paramsLink) {
		String[] params = paramsLink.split(paramsSeparator);
		if(JUtils.isEmpty(params)){
			initParamsMap(1);
			parseParam(paramsLink);
		}else {
			initParamsMap(params.length);
			for (String param : params) {
				parseParam(param);
			}
		}
	}

	private void parseParam(String param) {
		String[] entry = param.split(paramsValueSeparator);
		if(JUtils.isEmpty(entry) || entry.length != 2)
			return;
		String key = entry[0];
		String valueS = entry[1];

		if(parseForcibleString(key, valueS))
			return;
		if(parseDecimal(key, valueS))
			return;
		if(parseBoolean(key, valueS))
			return;
		if(parseNumber(key, valueS))
			return;
		parseString(key, valueS);
//		paramsMap.en
	}

	private boolean parseNumber(String key, String valueS) {
		if(parseByte(key, valueS))
			return true;
		if(parseShort(key, valueS))
			return true;
		if(parseLong(key, valueS))
			return true;
		if(parseInt(key, valueS))
			return true;
		return false;
	}

	private boolean parseInt(String key, String valueS) {
		Integer value = null;

		try {
			Number numberValue = parseSpecialNumber(valueS, SPECIAL_TYPE_INTEGER);
			if(numberValue instanceof Integer)
				value = (Integer) numberValue;
			else {
				value = Integer.valueOf(valueS);
			}
		}catch (Exception e){
			JLog.printStackTrace(e);
		}

		if(value != null){
			paramsMap.put(key, value);
			return true;
		}

		return false;
	}

	/**
	 * 解析特殊类型
	 * @param numberS 数字字符串 如果 long 类型结尾不能带 L
	 * @param type int 类型 或 long 类型
	 * @return 解析失败返回 null, 否则返回 Integer 或者 Long
	 */
	private Number parseSpecialNumber(String numberS, int type){
		Number value = null;
		try {
			//解析二进制
			if(numberS.startsWith(binaryFlag) || numberS.startsWith(binaryFlag2)){
				String binaryValue = numberS.substring(2, numberS.length());
				switch (type){
					case SPECIAL_TYPE_INTEGER:
						value = Integer.valueOf(binaryValue, 2);
						break;
					case SPECIAL_TYPE_LONG:
						value = Long.valueOf(binaryValue, 2);
						break;
				}
			}else if(numberS.startsWith(hexadecimalFlag) || numberS.startsWith(hexadecimalFlag2)){
				// 解析十六进制
				String hexadecimalValue = numberS.substring(2, numberS.length());
				switch (type){
					case SPECIAL_TYPE_INTEGER:
						value = Integer.valueOf(hexadecimalValue, 16);
						break;
					case SPECIAL_TYPE_LONG:
						value = Long.valueOf(hexadecimalValue, 16);
						break;
				}
			}
		}catch (Exception e){
			JLog.printStackTrace(e);
		}
		return value;
	}

	private boolean parseLong(String key, String valueS) {
		char l = valueS.charAt(valueS.length() - 1);
		Long value = null;
		try {
			if(l == 'l' || l == 'L'){
				Number numberValue = parseSpecialNumber(valueS.substring(0, valueS.length() - 1), SPECIAL_TYPE_LONG);
				if(numberValue instanceof Long)
					value = (Long) numberValue;
				else {
					value = Long.valueOf(valueS);
				}
			}
		}catch (Exception e){
			JLog.printStackTrace(e);
		}

		if(value != null){
			paramsMap.put(key, value);
			return true;
		}

		return false;
	}

	private boolean parseShort(String key, String valueS) {
		Short value;
		if(valueS.startsWith(shortFlag)){
			try {
				value = Short.valueOf(valueS);
				if(value != null){
					paramsMap.put(key, value);
					return true;
				}
			}catch (Exception e){
				JLog.printStackTrace(e);
			}
		}
		return false;
	}

	private boolean parseByte(String key, String valueS) {
		Byte value;
		if(valueS.startsWith(byteFlag)){
			try {
				value = Byte.valueOf(valueS);
				if(value != null){
					paramsMap.put(key, value);
					return true;
				}
			}catch (Exception e){
				JLog.printStackTrace(e);
			}
		}
		return false;
	}

	private boolean parseBoolean(String key, String valueS) {
		Boolean value;
		try {
			value = Boolean.valueOf(valueS);
			if(value != null) {
				paramsMap.put(key, value);
				return true;
			}
		}catch (Exception e){
			JLog.printStackTrace(e);
		}
		return false;
	}

	private boolean parseDecimal(String key, String valueS) {
		return valueS.contains(".") && (parseFloat(key, valueS) || parseDouble(key, valueS));
	}

	private boolean parseDouble(String key, String valueS) {
		Double value;
		try {
			value = Double.valueOf(valueS);
			if(value != null) {
				paramsMap.put(key, value);
				return true;
			}
		}catch (Exception e){
			JLog.printStackTrace(e);
		}
		return false;
	}

	private boolean parseFloat(String key, String valueS) {
		char f = valueS.charAt(valueS.length() - 1);
		if(f == 'f' || f == 'F'){
			Float value;
			try {
				value = Float.valueOf(valueS);
				if(value != null){
					paramsMap.put(key, value);
					return true;
				}
			}catch (Exception e){
				JLog.printStackTrace(e);
			}
		}
		return false;
	}

	private void parseString(String key, String valueS) {
//		valueS = URLDecoder.decode(valueS);
		paramsMap.put(key, valueS);
	}

	private boolean parseForcibleString(String key, String valueS) {
		if(valueS.startsWith(stringStartFlag) && valueS.endsWith(stringEndFlag)){
			String value = valueS.substring(1, valueS.length());
//			value = URLDecoder.decode(value);
			paramsMap.put(key, value);
			return true;
		}
		return false;
	}

	private void initParamsMap(int capacity){
		if(paramsMap == null){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				paramsMap = new ArrayMap<>(capacity);
			}else {
				paramsMap = new HashMap<>(capacity);
			}
		}
	}

	/**
	 * 校验 scheme 是否无效
	 */
	private boolean checkSchemeInvalid(){
		if(checkInvalid(JUtils.isEmpty(scheme)))
			return true;
		return checkInvalid(scheme.startsWith(SCHEME_JUMP));
	}

	/**
	 * 校验是否无效
	 * invalid 无效的
	 */
	private boolean checkInvalid(boolean isInvalid) {
		if (isInvalid) {
			invalid = true;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		if (invalid)
			return null;
		StringBuilder bodyBuilder = new StringBuilder(path);
		if (!JUtils.isEmpty(paramsMap)){
			bodyBuilder.append(pathSeparator);

			boolean isFristEntry = true;
			for (Map.Entry<String, Serializable> entry : paramsMap.entrySet()) {
				String key = entry.getKey();
				Serializable value = entry.getValue();
				String finalValue;

				finalValue = convertToJumpURIParams(value);

				if(isFristEntry) {
					isFristEntry = false;
				}else {
					bodyBuilder.append(paramsSeparator);
				}
				bodyBuilder.append(key);
				bodyBuilder.append(paramsValueSeparator);
//					bodyBuilder.append(URLEncoder.encode(finalValue));
				bodyBuilder.append(finalValue);
			}
		}

		String body = bodyBuilder.toString();
		if(scheme.equals(SCHEME_JUMPS))
			body = JUtils.base64Encode(body);
		return String.format(Locale.getDefault(), "%s%s%s", scheme, schemeSeparator, body);
	}

	private String convertToJumpURIParams(Serializable value) {
		//boolean
		if(value instanceof Boolean || boolean.class.isInstance(value))
			return Boolean.toString((Boolean) value);

		//string
		if(value instanceof String){
			String valueS = (String) value;
			if(needStartEndFlagString(valueS))
				return "\"" + valueS + "\"";
			return valueS;
		}

		//byte
		if(value instanceof Byte || byte.class.isInstance(value)){
			return byteFlag + String.valueOf(value);
		}

		//short
		if(value instanceof Short || short.class.isInstance(value)){
			return shortFlag + String.valueOf(value);
		}

		//int
		if(value instanceof Integer || int.class.isInstance(value)){
			return String.valueOf(value);
		}

		//long
		if(value instanceof Long || long.class.isInstance(value)){
			String valueL = String.valueOf(value);
			char lastChar = valueL.charAt(valueL.length() - 1);
			return valueL + ((lastChar == 'L' || lastChar == 'l') ? "" : "L");
		}

		//float
		if(value instanceof Float || float.class.isInstance(value)){
			String valueF = String.valueOf(value);
			char lastChar = valueF.charAt(valueF.length() - 1);
			return valueF + ((lastChar == 'F' || lastChar == 'f') ? "" : "F");
		}

		//double
		if(value instanceof Double || double.class.isInstance(value)){
			return String.valueOf(value);
		}

		return "null";
	}

	private boolean needStartEndFlagString(String string){
		//判断是否需要开始结束 "" 标记
		//以 " 开始并以 " 结束的需要标记
		if(string.startsWith(stringStartFlag) && string.endsWith(stringEndFlag))
			return true;
		//解析以 0x 开头十六进制
		if(string.startsWith(hexadecimalFlag) || string.startsWith(hexadecimalFlag2)){
			if(string.endsWith("L") || string.endsWith("l"))
				string = string.substring(2, string.length() - 1);
			else
				string = string.substring(2, string.length());
			char[] chars = string.toCharArray();
			for (char c : chars) {
				if(c >= '0' && c <= '9')
					continue;
				if(c >= 'a' && c <= 'f')
					continue;
				if(c >= 'A' && c <= 'F')
					continue;
				return false;
			}
			return true;
		}
		//解析以 0b 开头2进制
		if(string.startsWith(binaryFlag) || string.startsWith(binaryFlag2)){
			if(string.endsWith("L") || string.endsWith("l"))
				string = string.substring(2, string.length() - 1);
			else
				string = string.substring(2, string.length());
			char[] chars = string.toCharArray();
			for (char c : chars) {
				if(c >= '0' && c <= '1')
					continue;
				return false;
			}
			return true;
		}

		try {
			//解析是否为 boolean
			if(string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false"))
				return true;

			//解析以 b 开头 byte 类型
			if(string.startsWith(byteFlag)){
				string = string.substring(1, string.length());
				Byte temp = Byte.valueOf(string);
				return true;
			}

			//解析 short 类型
			if(string.startsWith(shortFlag)){
				string = string.substring(1, string.length());
				Short temp = Short.valueOf(string);
				return true;
			}

			if(!string.contains(".")){
				try {
					//int
					Integer temp = Integer.valueOf(string);
					return true;
				}catch (Exception e){
					//long
					Long temp = Long.valueOf(string);
					return true;
				}
			}else {
				try {
					//float
					Float temp = Float.valueOf(string);
					return true;
				}catch (Exception e){
					//double
					Double temp = Double.valueOf(string);
					return true;
				}
			}
		}catch (Exception e){
			return false;
		}
	}

	public static JumpURI create(String link){
		return new JumpURI(link);
	}

	public static JumpURI create(String scheme, String path, Map<String, Serializable> params){
		return new JumpURI(scheme, path, params);
	}
}
