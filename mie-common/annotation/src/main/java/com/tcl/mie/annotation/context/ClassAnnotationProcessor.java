package com.tcl.mie.annotation.context;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import com.tcl.mie.annotation.handler.ClassAnnotationInfo;
import com.tcl.mie.annotation.handler.IAnnotationHandler;

public class ClassAnnotationProcessor {
	private Map<Class, IAnnotationHandler> handlersMap = new HashMap<Class, IAnnotationHandler>();

	public Set<Class> getAnnotations() {
		return handlersMap.keySet();
	}

	public void registHandler(IAnnotationHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("Handler can't be null");
		}
		handlersMap.put(handler.annotation(), handler);
	}

	public void process(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) definitionHolder.getBeanDefinition();
		Class target = null;
		try {
			target = beanDefinition.resolveBeanClass(null);
		} catch (ClassNotFoundException e) {
			return;
		}
		boolean handled = false;
		Annotation[] annotations = target.getAnnotations();
		for (Annotation a : annotations) {
			IAnnotationHandler handler = handlersMap.get(a.annotationType());
			if (handler != null) {
				List<ClassAnnotationInfo> targetList = Arrays.asList(new ClassAnnotationInfo(a, target));
				handler.handle(targetList);
				handled = true;
			}
		}
		if (!handled) {
			BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
		}
	}
}
