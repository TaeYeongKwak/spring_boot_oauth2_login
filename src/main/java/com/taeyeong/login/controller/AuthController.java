package com.taeyeong.login.controller;

import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.common.LoginRequest;
import com.taeyeong.login.common.SignUpRequest;
import com.taeyeong.login.exception.DuplicateEmailException;
import com.taeyeong.login.member.entity.Member;
import com.taeyeong.login.member.entity.ProviderType;
import com.taeyeong.login.member.entity.Role;
import com.taeyeong.login.member.repository.MemberRepository;
import com.taeyeong.login.member.service.ResponseService;
import com.taeyeong.login.oauth.dto.MemberPrincipal;
import com.taeyeong.login.oauth.token.AuthToken;
import com.taeyeong.login.oauth.token.AuthTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. Auth"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final ResponseService responseService;
	private final AuthenticationManager authenticationManager;
	private final AuthTokenProvider authTokenProvider;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	private long tokenValidMilisecond = 1000L * 60 * 60;
	
	@ApiOperation(value = "로그인", notes = "회원 로그인 기능")
	@PostMapping("/login")
	public CommonResult login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()
					));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		MemberPrincipal principal = (MemberPrincipal)authentication.getPrincipal();
		Date now = new Date();
		AuthToken accessToken = authTokenProvider.createAuthToken(
				loginRequest.getEmail(), 
				principal.getRole().getKey(), 
				new Date(now.getTime() + tokenValidMilisecond));
		
		return responseService.getSingleResult(accessToken.getToken());
	}
	
	@ApiOperation(value = "회원가입", notes = "회원가입 기능")
	@PostMapping("/signup")
	public CommonResult signUp(SignUpRequest signUpRequest) {
		if(memberRepository.findByEmail(signUpRequest.getEmail()).isPresent())
			throw new DuplicateEmailException();
		
		Member member = Member.builder()
							.email(signUpRequest.getEmail())
							.password(passwordEncoder.encode(signUpRequest.getPassword()))
							.name(signUpRequest.getName())
							.role(Role.USER)
							.providerType(ProviderType.LOCAL)
							.build();
		
		memberRepository.save(member);
		return responseService.getSuccessfulResult();
	}
	
	@ApiOperation(value = "kakao 연동해제", notes = "연동 해제")
	@GetMapping("/unlink")
	public CommonResult unlink(String accessToken){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String,String>>(null,headers);
		ResponseEntity<String> response = restTemplate.postForEntity("https://kapi.kakao.com/v1/user/unlink", request, String.class);
		if(response.getStatusCode() == HttpStatus.OK)
			return responseService.getSuccessfulResult();
		else
			return responseService.getFailResult(-999, "실패하였습니다.");
	}
	
}
