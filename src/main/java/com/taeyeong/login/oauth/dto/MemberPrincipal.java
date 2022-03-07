package com.taeyeong.login.oauth.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.taeyeong.login.member.entity.Member;
import com.taeyeong.login.member.entity.ProviderType;
import com.taeyeong.login.member.entity.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberPrincipal implements OAuth2User, UserDetails, OidcUser{
	
	private static final long serialVersionUID = 1L;
	
	private final String email;
    private final String password;
    private final ProviderType providerType;
    private final Role role;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    
    public static MemberPrincipal create(Member member) {
		return new MemberPrincipal(
				member.getEmail(), 
				member.getPassword(), 
				member.getProviderType(), 
				Role.USER, 
				Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getKey())));
	}
    
    public static MemberPrincipal create(Member member, Map<String, Object> attributes, String nameAttributeKey) {
    	MemberPrincipal memberPrincipal = new MemberPrincipal(
				member.getEmail(), 
				member.getPassword(), 
				member.getProviderType(), 
				Role.USER, 
				Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getKey())));
    	memberPrincipal.setAttributes(attributes);
    	memberPrincipal.setNameAttributeKey(nameAttributeKey);
    	return memberPrincipal;
	}
    
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Map<String, Object> getClaims() {
		return null;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return null;
	}

	@Override
	public OidcIdToken getIdToken() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getName() {
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
