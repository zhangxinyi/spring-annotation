package com.tcl.mie.annotation.handler.http;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.tcl.mie.annotation.HttpClient;
import com.tcl.mie.annotation.IllegalConfigException;
import com.tcl.mie.annotation.factorybean.HttpClientProxyFactoryBean;
import com.tcl.mie.annotation.handler.AbstractSpringListAnnotationHandler;

/**
 * @author xinyizhang
 * @Date 2015-1-6 下午2:15:10
 */
public class HttpClientAnnotationHandler extends AbstractSpringListAnnotationHandler<HttpClient> {
	@Override
	protected void handle(HttpClient s, Class target) {
		registServiceBean(s, target);
	}

	public Class annotation() {
		return HttpClient.class;
	}

	private void registServiceBean(HttpClient s, Class target) {
		if (!target.isInterface()) {
			throw new IllegalConfigException("Target[" + target.getName() + "] must be interface");
		}
		String simpleName = target.getSimpleName();
		String name = s.name();
		if (name.equals("")) {
			name = simpleName;
			name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
		}
		RootBeanDefinition definition = new RootBeanDefinition();
		definition.setAbstract(false);
		definition.setBeanClass(HttpClientProxyFactoryBean.class);
		definition.setLazyInit(s.lazy());
		definition.setAutowireCandidate(true);
		definition.setAutowireMode(s.autoWire().value());

		MutablePropertyValues mpv = new MutablePropertyValues();

		mpv.addPropertyValue("url", s.url());
		mpv.addPropertyValue("clazz", target);

		definition.setPropertyValues(mpv);

		registerBeanDefinition(name, definition);
	}

}
