package com.eshel.jump;

/**
 * createBy Eshel
 * createTime: 2019/3/31 18:19
 * desc: TODO
 */
public class ProxyInfo {

	private String proxyInterfacesName;
	private String methodName;

	public ProxyInfo(Class<?>[] proxyInterfacesName, String methodName) {
		this.proxyInterfacesName = getInterfacesName(proxyInterfacesName);
		this.methodName = methodName;
	}
	public ProxyInfo(String proxyInterfacesName, String methodName) {
		this.proxyInterfacesName = proxyInterfacesName;
		this.methodName = methodName;
	}

	private String getInterfacesName(Class<?>[] proxyInterfaces) {
		if(proxyInterfaces == null || proxyInterfaces.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < proxyInterfaces.length; i++) {
			if(i != 0)
				sb.append(", ");
			sb.append(proxyInterfaces[i].getName());
		}
		return sb.toString();
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
