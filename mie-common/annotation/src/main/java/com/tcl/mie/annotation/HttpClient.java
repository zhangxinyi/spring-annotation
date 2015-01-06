package com.tcl.mie.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowire;

/**
 * @author	xinyizhang
 * @Date	2015-1-6 下午2:17:17 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
@Documented
public @interface HttpClient
{
	String url();
	String name() default "";
	boolean overloadEnabled() default true;
	boolean lazy() default false;
	Autowire autoWire() default Autowire.BY_NAME;
	String init() default "";
	String destroy() default "";

}
