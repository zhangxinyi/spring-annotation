package com.tcl.mie.annotation.handler;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.tcl.mie.annotation.IllegalConfigException;

public abstract class AbstractSpringAnnotationHandler<ANNOTATION> implements IAnnotationHandler<ANNOTATION> {
	private BeanDefinitionRegistry registry;

	public void setBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	protected BeanDefinitionRegistry getRegistry() {
		return registry;
	}

	protected void registerBeanDefinition(String name, BeanDefinition definition) {
		if (registry.containsBeanDefinition(name)) {
			throw new IllegalConfigException("Duplicate bean name [" + name + "]");
		}
		
		this.registry.registerBeanDefinition(name, definition);
	}
}
