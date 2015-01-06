package com.tcl.mie.annotation.handler;

public class ClassAnnotationInfo<ANNOTATION> {
	private ANNOTATION annotation;
	private Class target;

	public ClassAnnotationInfo(ANNOTATION annotation, Class target) {
		this.annotation = annotation;
		this.target = target;
	}

	public ANNOTATION getAnnotation() {
		return annotation;
	}

	public Class getTarget() {
		return target;
	}
}
