package com.taeyeong.login.exception;

public class NotConsentEmailException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NotConsentEmailException() {
		super();
	}
	
	public NotConsentEmailException(String message) {
		super(message);
	}

	public NotConsentEmailException(String message, Throwable th) {
		super(message, th);
	}
	
}
