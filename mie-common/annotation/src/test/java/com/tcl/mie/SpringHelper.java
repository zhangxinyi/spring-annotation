package com.tcl.mie;


import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHelper {
	private static final String SPRING_XML_CLASS_PATH_STR = "classpath*:spring*.xml";

	public static ApplicationContext getContext(){
		return  new ClassPathXmlApplicationContext(new String[]{SPRING_XML_CLASS_PATH_STR});
	}
	
	public static <T> T getSingleBeanByType(Class<T> beanType){
		Map m = getContext().getBeansOfType(beanType);
		Iterator i = m.values().iterator();
		while(i.hasNext())
			return (T)i.next();
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name, Class<T> clazz) {
		
		return (T)getContext().getBean(name, clazz);
	}


	public static void main(String[] args) {
		System.err.println(getContext() + "");
	}
}
