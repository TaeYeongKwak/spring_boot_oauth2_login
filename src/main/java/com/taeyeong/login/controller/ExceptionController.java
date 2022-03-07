package com.taeyeong.login.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.exception.AuthenticationEntryPointException;

@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
	
	@GetMapping("/entrypoint")
	public CommonResult entrypointException() {
		throw new AuthenticationEntryPointException();
	}
	
	@GetMapping("/accessdenied")
	public CommonResult accessDeniedException() {
		throw new AccessDeniedException("");
	}
}
