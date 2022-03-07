package com.taeyeong.login.oauth.dto;

import java.util.Map;

import com.taeyeong.login.member.entity.Member;
import com.taeyeong.login.member.entity.ProviderType;
import com.taeyeong.login.member.entity.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuth2Attributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private ProviderType providerType;
	
	public static OAuth2Attributes of(ProviderType providerType, String userNameAttributeName, Map<String, Object> attributes) {
		switch(providerType) {
			case KAKAO: return ofKakao(userNameAttributeName, attributes);
			case NAVER: return ofNaver(userNameAttributeName, attributes);
			default: throw new IllegalArgumentException("Invalid Provider Type.");
		}
	}
	
	private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		return OAuth2Attributes.builder()
				.name((String)properties.get("nickname"))
				.email((String)kakaoAccount.get("email"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.providerType(ProviderType.KAKAO)
				.build();
	}
	
	private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		return null;
	}
	
	public Member toEntity() {
		return Member.builder()
				.email(email)
				.name(name)
				.role(Role.USER)
				.providerType(providerType)
				.build();
	}
}
