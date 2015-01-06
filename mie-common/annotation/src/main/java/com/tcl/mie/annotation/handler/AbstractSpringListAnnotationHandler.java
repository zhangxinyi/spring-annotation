package com.tcl.mie.annotation.handler;

import java.util.List;

import org.springframework.beans.factory.support.RootBeanDefinition;

public abstract class AbstractSpringListAnnotationHandler<ANNOTATION> extends AbstractSpringAnnotationHandler<ANNOTATION> {

	public void handle(List<ClassAnnotationInfo<ANNOTATION>> classList) {
		for (ClassAnnotationInfo<ANNOTATION> info : classList) {
			handle(info.getAnnotation(), info.getTarget());
		}
	}

	protected RootBeanDefinition createBeanDefinition(Class targetCls) {
		RootBeanDefinition definition = new RootBeanDefinition();
		definition.setAbstract(false);
		definition.setBeanClass(targetCls);
		definition.setLazyInit(false);
		definition.setAutowireCandidate(true);
		return definition;
	}

	protected abstract void handle(ANNOTATION b, Class target);
}
