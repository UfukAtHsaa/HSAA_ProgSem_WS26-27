package com.example.hello.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Spring fragt bei jedem Login hier nach: Wer ist das, wie lautet der Hash, welche Rolle? */
@Service
public class DbUserDetailsService implements UserDetailsService {

	private final AppUserRepository users;

	public DbUserDetailsService(AppUserRepository users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		AppUser u = users.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(email));
		return User.withUsername(u.getEmail())
			.password(u.getPasswordHash())
			.roles(u.getRole())
			.build();
	}
}
