package com.eshel.jump;

import android.os.Bundle;

import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.TargetClass;
import com.eshel.jump.anno.TargetName;
import com.eshel.jump.anno.Type;
import com.eshel.jump.enums.Null;

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
		String action = null;

		if(intentAnno != null)
		{
			action = intentAnno.action();
		}

		if(actionAnno != null)
		{
			String temp = actionAnno.value();
			if(temp != NULL) action = temp;
		}

		if(action == NULL)
			action = null;
		return action;
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
}
