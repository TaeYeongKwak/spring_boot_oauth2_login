package com.taeyeong.login.oauth.token;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthTokenProvider {
	
	@Value("${spring.jwt.secret}")
	private String secretKey;
	private Key key;
	
	@PostConstruct
	public void init(){
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public AuthToken createAuthToken(String id, String role, Date expiry) {
		return new AuthToken(id, role, expiry, key);
	}
	
	public AuthToken convertToken(String token) {
		return new AuthToken(token, key);
	}
	
	public Authentication getAuthentication(AuthToken authToken) {
		if(authToken.validateToken()) {
			Claims claims = authToken.getTokenClaims();
			Collection<? extends GrantedAuthority> authorities = 
					Arrays.stream(new String[] {claims.get("role").toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
			User principal = new User(claims.getSubject(), "", authorities);
			return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
		}
		return null;
	}
	
	

}
