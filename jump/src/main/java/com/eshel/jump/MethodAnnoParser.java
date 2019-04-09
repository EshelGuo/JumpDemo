package com.eshel.jump;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.eshel.jump.anno.AContext;
import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.Data;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.TargetClass;
import com.eshel.jump.anno.TargetName;
import com.eshel.jump.anno.Type;
import com.eshel.jump.enums.Null;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static com.eshel.jump.configs.JumpConst.NULL;
import static com.eshel.jump.configs.JumpConst.NULL_I;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:44
 * desc: TODO
 */
public class MethodAnnoParser {

	private Intent intentAnno;

	MethodAnnoParser(Intent intentAnno) {
		this.intentAnno = intentAnno;
	}

	public int parseFlag(Flag flagAnno){
		int flag = 0;

		if(intentAnno != null)
		{
			int[] value = intentAnno.flag();
			if(value.length == 0 || value[0] == NULL_I)
			{
				flag = 0;
			}
			else
			{
				for (int v : value)
				{
					flag |= v;
				}
			}
		}

		if(flagAnno != null)
		{
			int[] value = flagAnno.value();
			if(value.length == 0 || value[0] == NULL_I)
			{
				flag = 0;
			}
			else
			{
				for (int v : value)
				{
					flag |= v;
				}
			}
		}
		return flag;
	}

	public String parseAction(Action actionAnno) {
		if(actionAnno != null && actionAnno.value() != NULL)
			return actionAnno.value();
		else if(intentAnno != null && intentAnno.action() != NULL)
			return intentAnno.action();
		return null;
	}

	public String[] parseCategory(Category categoryAnno) {
		String[] category = null;
		String[] category2 = null;
		if(categoryAnno != null){
			category = categoryAnno.category();
			if(category.length == 0 || category[0] == NULL)
				category = null;
		}

		if(intentAnno != null){
			category2 = intentAnno.category();
			if(category2.length == 0 || category2[0] == NULL)
				category2 = null;
		}
		return JUtils.subStrings(category2, category);
	}

	public String parseType(Type typeAnno) {
		String type = null;
		if(typeAnno != null){
			type = typeAnno.value();
		}

		if(intentAnno != null && type == NULL){
			String temp = intentAnno.type();
			if(temp != NULL)
				type = temp;
		}

		if(type == NULL)
			type = null;

		return type;
	}

	public Class parseTargetClass(TargetClass targetClassAnno) {
		Class target = null;
		if(intentAnno != null){
			target = intentAnno.target();
		}

		if(targetClassAnno != null){
			Class temp = targetClassAnno.value();
			if(temp != Null.class)
				target = temp;
		}

		if(target == Null.class)
			target = null;
		return target;
	}

	public String parseTargetname(TargetName targetNameAnno) {
		String targetName = null;
		if(intentAnno != null){
			String temp = intentAnno.targetName();
			if(temp != NULL)
				targetName = temp;
		}

		if(targetNameAnno != null){
			String temp = targetNameAnno.value();
			if(temp != NULL)
				targetName = temp;
		}
		return targetName;
	}

	public Bundle parseExtraParams(ExtraParams extraParamsAnno) {
		Bundle bundle = null;
		if(extraParamsAnno != null){
			String[] keys = extraParamsAnno.key();
			String[] values = extraParamsAnno.value();
			int length = Math.min(keys.length, values.length);
			bundle = new Bundle(length);
			for (int i = 0; i < length; i++) {
				bundle.putString(keys[i], values[i]);
			}
		}
		return bundle;
	}

	public int parseParamsFlag(Flag flagAnno1, Class<?> paramsType, Object arg) {
		if(flagAnno1 == null || arg == null)
			return 0;
		if(arg instanceof Integer || int.class.isInstance(arg)){
			return (int) arg;
		}

		int flag = 0;
		if(int[].class.isInstance(arg)){
			int[] flagArray = (int[]) arg;
			for (int temp : flagArray) {
				flag |= temp;
			}
			return flag;
		}

		if(arg instanceof Collection){
			Collection flags = (Collection) arg;
			Iterator it = flags.iterator();
			int temp;
			while (it.hasNext()){
				Object next = it.next();
				if(next instanceof Integer || int.class.isInstance(next)){
					temp = (int) next;
					flag |= temp;
				}else {
					return 0;
				}
			}
			return flag;
		}
		return 0;
	}

	public Context parseParamsContext(IntentBuilder builder, AContext contextAnno, Class<?> type, Object arg) {
		if(contextAnno == null){
			if(arg instanceof Context){
				if(builder.getContext() == null){
					return (Context) arg;
				}
			}
		}else {
			if(arg instanceof Context)
				return (Context) arg;
		}
		return null;
	}

	public String parseParamsAction(Action actionAnno, Class<?> type, Object arg) {
		if(actionAnno == null)
			return null;

		if(arg instanceof String){
			return (String) arg;
		}

		return null;
	}

	public String parseParamsType(Type typeAnno, Class<?> type, Object arg) {
		if(typeAnno == null)
			return null;

		if(arg instanceof String)
			return (String) arg;

		return null;
	}

	public Class parseParamsTargetClass(TargetClass targetClassAnno, Class<?> type, Object arg) {
		if(targetClassAnno == null)
			return null;

		if(arg instanceof Class)
			return (Class) arg;

		return null;
	}

	public String parseParamsTargetName(TargetName targetNameAnno, Class<?> type, Object arg) {
		if(targetNameAnno == null)
			return null;

		if(arg instanceof String)
			return (String) arg;

		return null;
	}

	public String[] parseParamsCategory(Category categoryAnno, Class<?> type, Object arg) {
		if(categoryAnno == null)
			return null;

		if(arg instanceof String)
			return new String[]{(String) arg};

		if(String[].class.isInstance(arg))
			return (String[]) arg;

		if(arg instanceof Collection){
			Collection list = (Collection) arg;
			try{
				return (String[]) list.toArray(new String[list.size()]);
			} catch (Exception e){
				Iterator it = list.iterator();
				List<String> categorys = new ArrayList<>(list.size());
				while (it.hasNext()){
					Object next = it.next();
					if(next instanceof String)
						categorys.add((String) next);
				}
				return categorys.toArray(new String[categorys.size()]);
			}
		}
		return null;
	}

	/**
	 * 解析方法上和 Intent 注解中 的 @Data 主机的值并转换为 Uri
	 */
	public Uri parseData(Data dataAnno) {
		if(dataAnno != null && dataAnno.value() != NULL){
			return Uri.parse(dataAnno.value());
		}else if(intentAnno != null && intentAnno.data() != NULL){
			return Uri.parse(intentAnno.data());
		}
		return null;
	}

	public Uri parseParamsData(Data dataAnno, Class<?> type, Object arg) {
		if(dataAnno == null)
			return null;
		if(arg instanceof Uri)
			return (Uri) arg;
		String dataFirst = dataAnno.value();
		if(NULL == dataFirst || dataFirst == null){
			if(arg instanceof String)
				return Uri.parse((String) arg);
		}else {
			if(!dataFirst.contains("%") && arg instanceof String)
				return Uri.parse(dataFirst + arg);
			return Uri.parse(String.format(Locale.getDefault(), dataFirst, arg));
		}
		return null;
	}
}
