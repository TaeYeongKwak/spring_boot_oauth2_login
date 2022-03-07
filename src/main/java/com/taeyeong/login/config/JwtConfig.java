package com.taeyeong.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taeyeong.login.oauth.token.AuthTokenProvider;

@Configuration
public class JwtConfig {
	
	@Bean
	public AuthTokenProvider jwtProvider() {
		return new AuthTokenProvider();
	}
	
}
