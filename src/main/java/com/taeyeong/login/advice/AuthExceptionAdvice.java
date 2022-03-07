package com.taeyeong.login.advice;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.exception.AuthenticationEntryPointException;
import com.taeyeong.login.exception.NotConsentEmailException;
import com.taeyeong.login.member.service.ResponseService;

import dev.akkinoc.util.YamlResourceBundle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class AuthExceptionAdvice {
	
	@Value("${spring.messages.basename}")
	private String baseName;
	
	private final ResponseService responseService;
	
	@ExceptionHandler(AuthenticationEntryPointException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult unknownException() {
		return responseService.getFailResult(getCode("entryPoint"), getMessage("entryPoint"));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult accessDeniedException() {
		return responseService.getFailResult(getCode("accessDenied"), getMessage("accessDenied"));
	}
	
	@ExceptionHandler(NotConsentEmailException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult notConsentEmailException() {
		return responseService.getFailResult(getCode("notConsentEmail"), getMessage("notConsentEmail"));
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
