package com.taeyeong.login.exception;

public class OAuth2ProviderMissMatchException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public OAuth2ProviderMissMatchException() {
		super();
	}
	
	public OAuth2ProviderMissMatchException(String message) {
		super(message);
	}
	
	public OAuth2ProviderMissMatchException(String message, Throwable th) {
		super(message, th);
	}
}
