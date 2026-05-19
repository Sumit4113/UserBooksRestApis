package com.onlinebookstore.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.repository.UserRepository;

@Service
public class UserDetailImpService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AppUser user = userRepo.findByUserEmail(username);

		CustomUserDetail userDetail = new CustomUserDetail(user);

		return userDetail;
	}

}
