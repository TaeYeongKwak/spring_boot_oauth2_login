package com.taeyeong.login.oauth.service;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.taeyeong.login.exception.NotConsentEmailException;
import com.taeyeong.login.exception.OAuth2ProviderMissMatchException;
import com.taeyeong.login.member.entity.Member;
import com.taeyeong.login.member.entity.ProviderType;
import com.taeyeong.login.member.repository.MemberRepository;
import com.taeyeong.login.oauth.dto.MemberPrincipal;
import com.taeyeong.login.oauth.dto.OAuth2Attributes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauthUser = super.loadUser(userRequest);
		ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuth2Attributes attributes = OAuth2Attributes.of(providerType, userNameAttributeName, oauthUser.getAttributes());
		if(attributes.getEmail() == null) throw new NotConsentEmailException();
			
		Optional<Member> optMember = memberRepository.findByEmail(attributes.getEmail());
		Member member = optMember.isEmpty()? memberRepository.save(attributes.toEntity()) : optMember.get();
		
		if(providerType != member.getProviderType())
			throw new OAuth2ProviderMissMatchException();

		if(!member.getName().equals(attributes.getName())) memberRepository.save(attributes.toEntity());
		
		return MemberPrincipal.create(member, attributes.getAttributes(), userNameAttributeName);
	}
	
}
