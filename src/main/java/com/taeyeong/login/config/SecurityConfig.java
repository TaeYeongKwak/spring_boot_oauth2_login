package com.taeyeong.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.taeyeong.login.member.service.CustomUserDetailService;
import com.taeyeong.login.oauth.Repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.taeyeong.login.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.taeyeong.login.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.taeyeong.login.oauth.service.CustomOAuth2UserService;
import com.taeyeong.login.oauth.token.AuthTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthTokenProvider jwtProvider;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomUserDetailService cUserDetailService;
	
	@Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(cUserDetailService)
                .passwordEncoder(passwordEncoder());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors()
			.and()	
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.exceptionHandling()
					.authenticationEntryPoint(new CAuthenticationEntryPoint())
					.accessDeniedHandler(new CAccessDeniedHandler())
			.and()
				.authorizeRequests()
				.antMatchers("/auth/login", "/auth/signup", "/auth/unlink").permitAll()
				.anyRequest().permitAll()
			.and()
				.oauth2Login()
					.authorizationEndpoint()
					.baseUri("/oauth2/authorization")
					.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository())
			.and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*")
			.and()
				.userInfoEndpoint()
					.userService(customOAuth2UserService)
			.and()
				.successHandler(oAuth2AuthenticationSuccessHandler())
				.failureHandler(oAuth2AuthenticationFailureHandler());
				
				
		http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				 "/favicon.ico"
	             ,"/error"
	             ,"/webjars/**"
	             ,"/swagger-ui/**"
	             ,"/swagger-resources/**"
	             ,"/v3/api-docs"
	             ,"/swagger-ui.html");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Bean
    public HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
        		jwtProvider,
        		httpCookieOAuth2AuthorizationRequestRepository()
        );
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(httpCookieOAuth2AuthorizationRequestRepository());
    }


}
