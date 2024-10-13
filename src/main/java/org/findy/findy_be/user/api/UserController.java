package org.findy.findy_be.user.api;

import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.user.api.swagger.UserAPIPresentation;
import org.findy.findy_be.user.application.UserService;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.dto.UserInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserAPIPresentation {

	private final UserService userService;

	@GetMapping
	public UserInfoResponse getUser(@LoginUser User user) {
		return UserInfoResponse.of(user.getUsername(), user.getProfileImageUrl());
	}
}
