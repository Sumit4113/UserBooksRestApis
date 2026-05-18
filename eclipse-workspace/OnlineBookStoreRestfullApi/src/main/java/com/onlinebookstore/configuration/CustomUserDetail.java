package com.onlinebookstore.configuration;

import java.util.Collection;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.onlinebookstore.entity.AppUser;

public class CustomUserDetail implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AppUser user;

	public CustomUserDetail(AppUser user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserRole());

		return List.of(grantedAuthority);
	}

	@Override
	public String getPassword() {

		return user.getUserPassword();
	}

	@Override
	public String getUsername() {

		return user.getUserEmail();
	}
	
	public String getUserRole() {
		
		return user.getUserRole();
	}

}
