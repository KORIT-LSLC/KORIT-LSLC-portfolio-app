package com.korea.triplocation.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.korea.triplocation.domain.user.entity.Authority;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PrincipalUser implements UserDetails {

	private static final long serialVersionUID = -6984381303716862634L;
	private int userId;
	private String email;
	private String password;
	private int postsImgId;
	private List<Authority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		this.authorities.forEach(authority -> {
			authorities.add(new SimpleGrantedAuthority(authority.getRole().getRoleName()));
		});
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
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
