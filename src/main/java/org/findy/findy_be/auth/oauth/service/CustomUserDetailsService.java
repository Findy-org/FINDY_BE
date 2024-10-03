package org.findy.findy_be.auth.oauth.service;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.auth.oauth.domain.UserPrincipal;
import org.findy.findy_be.user.entity.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(Long.valueOf(username))
			.orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_USER_BY_ID.getMessage())));
		if (user == null) {
			throw new UsernameNotFoundException("Can not find username.");
		}
		return UserPrincipal.create(user);
	}
}
