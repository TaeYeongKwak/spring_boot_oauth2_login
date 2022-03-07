package com.taeyeong.login.exception;

public class AuthenticationEntryPointException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public AuthenticationEntryPointException() {
		super();
	}
	
	public AuthenticationEntryPointException(String message) {
		super(message);
	}
	
	public AuthenticationEntryPointException(String message, Throwable th) {
		super(message, th);
	}
}
