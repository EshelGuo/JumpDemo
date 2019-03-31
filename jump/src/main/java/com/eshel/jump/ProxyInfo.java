package com.eshel.jump;

/**
 * createBy Eshel
 * createTime: 2019/3/31 18:19
 * desc: TODO
 */
public class ProxyInfo {

	private String proxyInterfacesName;
	private String methodName;

	public ProxyInfo(String proxyInterfacesName, String methodName) {
		this.proxyInterfacesName = proxyInterfacesName;
		this.methodName = methodName;
	}

	public String getProxyInterfacesName() {
		return proxyInterfacesName;
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	public String toString() {
		return "[" + "interface = " + proxyInterfacesName + " --- methodName = " + methodName + ']';
	}
}
