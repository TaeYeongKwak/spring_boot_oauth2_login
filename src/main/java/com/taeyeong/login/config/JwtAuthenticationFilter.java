package com.taeyeong.login.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taeyeong.login.oauth.token.AuthToken;
import com.taeyeong.login.oauth.token.AuthTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final AuthTokenProvider jwtProvider;
	private final String TOKEN_PREFIX = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getAuthHeaderValue(request);
		AuthToken authToken = null;
		if(StringUtils.hasText(token)) {
			authToken = jwtProvider.convertToken(token);
			if(authToken.validateToken()) {
				Authentication auth = jwtProvider.getAuthentication(authToken);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
	
		filterChain.doFilter(request, response);
	}
	
	private String getAuthHeaderValue(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
	}

}
