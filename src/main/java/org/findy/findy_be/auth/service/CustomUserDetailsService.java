package org.findy.findy_be.auth.service;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.auth.UserAdapter;
import org.findy.findy_be.user.entity.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() ->
			new UsernameNotFoundException(NOT_FOUND_USER.getMessage()));
		return new UserAdapter(user);
	}
}
