package com.eshel.jump.configs;

import android.support.annotation.NonNull;

import com.eshel.jump.IntentBuilderProvider;
import com.eshel.jump.JumpHelper;
import com.eshel.jump.ParamsAdapter;
import com.eshel.jump.configs.interceptors.Interceptor;
import com.eshel.jump.log.AndroidLogImpl;
import com.eshel.jump.log.ILog;
import com.eshel.jump.log.JLog;

import java.util.ArrayList;
import java.util.List;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:07
 * desc: JumpLib 全局配置
 */
public class JConfig {

	public static JConfig getInstance(){
		if(JumpHelper.getConfig() == null){
			synchronized (JConfig.class){
				if(JumpHelper.getConfig() == null)
					JumpHelper.init(new JConfig());
			}
		}
		return JumpHelper.getConfig();
	}

	private ILog mLog;
	private List<ParamsAdapter> mParamsAdapters = new ArrayList<>(3);
	private IntentBuilderProvider mIntentBuilderProvider;
	private List<Interceptor> mInterceptorList;

	public void addInterceptor(@NonNull Interceptor interceptor){
		getInterceptorList().add(interceptor);
	}

	public void removeInterceptor(@NonNull Interceptor interceptor){
		getInterceptorList().remove(interceptor);
	}

	/**
	 * @hide
	 */
	public List<Interceptor> getInterceptorList(){
		if(mInterceptorList == null)
			mInterceptorList = new ArrayList<>();
		return mInterceptorList;
	}

	public void setIntentBuilderProvider(@NonNull IntentBuilderProvider intentBuilderProvider){
		mIntentBuilderProvider = intentBuilderProvider;
	}

	public IntentBuilderProvider getIntentBuilderProvider(){
		if(mIntentBuilderProvider == null){
			mIntentBuilderProvider = new DefaultIntentBuilderProvider();
		}
		return mIntentBuilderProvider;
	}

	public JConfig addParamsAdapter(ParamsAdapter adapter){
		mParamsAdapters.add(adapter);
		return this;
	}

	public List<ParamsAdapter> getParamsAdapters(){
		return mParamsAdapters;
	}

	public JConfig setLogAdapter(ILog log){
		if(log != null)
			mLog = log;
		return this;
	}

	public JConfig openLog(){
		JLog.openLog();
		return this;
	}

	public ILog getLogImpl(){
		if(mLog == null)
			mLog = new AndroidLogImpl();
		return mLog;
	}
}
