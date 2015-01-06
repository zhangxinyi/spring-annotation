package com.tcl.mie.annotation.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.tcl.mie.sdk.core.ApiFactory;

/**
 * @author xinyizhang
 * @Date 2015-1-6 下午2:22:42
 */
public class HttpClientProxyFactoryBean implements FactoryBean, InitializingBean {
	private Class clazz;
	private String url;
	private Object serviceProxy;

	@Override
	public Object getObject() throws Exception {
		return serviceProxy;
	}

	@Override
	public Class getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.serviceProxy = ApiFactory.getInstance(url).getApi(clazz);
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getServiceProxy() {
		return serviceProxy;
	}

	public void setServiceProxy(Object serviceProxy) {
		this.serviceProxy = serviceProxy;
	}
}
