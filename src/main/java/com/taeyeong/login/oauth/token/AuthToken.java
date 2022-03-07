package com.taeyeong.login.oauth.token;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthToken {
	private final String token;
	private final Key key;
	
    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }
	
	private String createAuthToken(String id, String role, Date expiry) {
		Claims claims = Jwts.claims().setSubject(id);
		claims.put("role", role);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expiry)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public boolean validateToken() {
		try {
			Claims claims = this.getTokenClaims();
			if(claims == null) return false;
			return this.getTokenClaims().getExpiration().after(new Date());
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
