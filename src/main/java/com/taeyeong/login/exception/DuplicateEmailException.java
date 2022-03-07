package com.taeyeong.login.exception;

public class DuplicateEmailException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public DuplicateEmailException() {
		super();
	}
	
	public DuplicateEmailException(String message) {
		super(message);
	}
	
	public DuplicateEmailException(String message, Throwable th) {
		super(message, th);
	}
}
