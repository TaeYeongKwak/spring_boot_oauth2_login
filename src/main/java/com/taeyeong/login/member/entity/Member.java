package com.taeyeong.login.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mid;
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	@Column(length = 100)
	private String password;
	@Column(nullable = false, length = 100)
	private String name;
	@Column(length = 20)
    @Enumerated(EnumType.STRING)
	private Role role;
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;
	
	public Member update(String name) {
		this.name = name;
		return this;
	}
	
}
