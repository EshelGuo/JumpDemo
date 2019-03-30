package com.eshel.jump.configs;

import com.eshel.jump.IntentBuilder;
import com.eshel.jump.IntentBuilderProvider;

/**
 * createBy Eshel
 * createTime: 2019/3/30 15:55
 * desc: TODO
 */
public class DefaultIntentBuilderProvider implements IntentBuilderProvider{
	@Override
	public IntentBuilder provideIntentBuilder() {
		return new IntentBuilder();
	}
}
