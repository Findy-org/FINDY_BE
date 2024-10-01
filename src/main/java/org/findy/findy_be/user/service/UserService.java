package org.findy.findy_be.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.findy.findy_be.user.dto.response.UserResponse;
import org.findy.findy_be.user.entity.Role;
import org.findy.findy_be.user.entity.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User findUser(final Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("[Error] 사용자를 찾을 수 없습니다."));
	}

	@Transactional
	public Long changeRole(final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("[Error] 사용자를 찾을 수 없습니다."));
		if (!isAdmin(user)) {
			user.updateRole(Role.ADMIN);
			return user.getId();
		}
		user.updateRole(Role.USER);
		return user.getId();
	}

	public List<UserResponse> findAll() {
		List<User> users = userRepository.findAll();
		return users.stream()
			.map(UserResponse::of)
			.collect(Collectors.toList());
	}

	private Boolean isAdmin(User user) {
		return user.getRole().name().equals("ADMIN");
	}

}
