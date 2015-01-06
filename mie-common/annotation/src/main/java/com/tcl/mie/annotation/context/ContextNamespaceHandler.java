package com.tcl.mie.annotation.context;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.ClassUtils;

/**
 * 
 * @author	xinyizhang
 * @Date	2015-1-6 上午11:06:34
 */
public class ContextNamespaceHandler extends org.springframework.context.config.ContextNamespaceHandler {

	@Override
	public void init() {
		super.init();
		registerJava5DependentParser("scan", "com.tcl.mie.annotation.context.ComponentScanBeanDefinitionParser");
	}

	private void registerJava5DependentParser(final String elementName, final String parserClassName) {
		BeanDefinitionParser parser = null;
		try {
			Class parserClass = ClassUtils.forName(parserClassName, ContextNamespaceHandler.class.getClassLoader());
			parser = (BeanDefinitionParser) parserClass.newInstance();
		} catch (Throwable ex) {
			throw new IllegalStateException("Unable to create Java 1.5 dependent parser: " + parserClassName, ex);
		}
		registerBeanDefinitionParser(elementName, parser);
	}
}
