package com.taeyeong.login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.exception.MemberNotFoundException;
import com.taeyeong.login.member.repository.MemberRepository;
import com.taeyeong.login.member.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = {"2. Member"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

	private final MemberRepository memberRepository;
	private final ResponseService responseService;
	
	@ApiOperation(value = "회원 조회", notes = "로그인에 성공한 회원의 정보를 조회")
	@GetMapping("/me")
	public CommonResult showInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return responseService.getSingleResult(
				memberRepository.findByEmail(auth.getName()).orElseThrow(MemberNotFoundException::new));
	}
	
}
