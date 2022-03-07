package com.taeyeong.login.exception;

public class MemberNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MemberNotFoundException() {
		super();
	}
	
	public MemberNotFoundException(String message) {
		super(message);
	}
	
	public MemberNotFoundException(String message, Throwable th) {
		super(message, th);
	}
	
}
