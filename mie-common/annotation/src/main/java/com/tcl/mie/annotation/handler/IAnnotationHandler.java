package com.tcl.mie.annotation.handler;

import java.util.List;

public interface IAnnotationHandler<ANNOTATION> {
	Class annotation();

	void handle(List<ClassAnnotationInfo<ANNOTATION>> classList);
}
