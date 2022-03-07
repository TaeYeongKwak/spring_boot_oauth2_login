package com.taeyeong.login.advice;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.exception.DuplicateEmailException;
import com.taeyeong.login.exception.MemberNotFoundException;
import com.taeyeong.login.member.service.ResponseService;

import dev.akkinoc.util.YamlResourceBundle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class MemberExceptionAdvice {
	
	private final ResponseService responseService;
	
	@Value("${spring.messages.basename}")
	private String baseName;
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult unknownException() {
		return responseService.getFailResult(getCode("unknown"), getMessage("unknown"));
	}
	
	@ExceptionHandler(MemberNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult memberNotFoundException() {
		return responseService.getFailResult(getCode("memberNotFound"), getMessage("memberNotFound"));
	}
	
	@ExceptionHandler(DuplicateEmailException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult duplicateEmailException(){
		return responseService.getFailResult(getCode("duplicateEmail"), getMessage("duplicateEmail"));
	}
	
	private String getMessage(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, YamlResourceBundle.Control.INSTANCE);
		return bundle.getString(key + ".message");
	}
	
	private int getCode(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, YamlResourceBundle.Control.INSTANCE);
		return Integer.parseInt(bundle.getString(key + ".code"));
	}
	
}
