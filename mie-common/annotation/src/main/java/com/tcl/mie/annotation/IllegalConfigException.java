package com.tcl.mie.annotation;

public class IllegalConfigException extends RuntimeException {
	private static final long serialVersionUID = 5290942025127199295L;

	public IllegalConfigException() {
		super();
	}

	public IllegalConfigException(String message) {
		super(message);
	}

	public IllegalConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalConfigException(Throwable cause) {
		super(cause);
	}
}
