package com.taeyeong.login.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpRequest {
	private String email;
	private String password;
	private String name;
}
