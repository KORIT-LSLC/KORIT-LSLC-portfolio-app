package com.korea.triplocation.domain.user.entity;

import java.time.LocalDate;
import java.util.List;

import com.korea.triplocation.security.PrincipalUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private int userId;
//	role_id => roleId 수정
	private String email;
	private String password;
	private String name;
	private String phone;
	private String address;
	private String profileImg;

	private LocalDate createDate;	// 계정 생성 일자
	
	private List<Authority> authorities;
	
	public PrincipalUser toPrincipal() {
		return PrincipalUser.builder()
				.userId(userId)
				.email(email)
				.password(password)
				.authorities(authorities)
				.build();
	}
	
	
}