package org.findy.findy_be.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest extends MockTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@DisplayName("존재하는 사용자를 찾을 수 있는 경우")
	@Test
	void 존재하는_사용자_조회() {
		// given
		String userId = "USERIDSKJFK";
		User user = User.builder()
			.userSeq(1L)
			.userId(userId)
			.username("Test User")
			.email("test@example.com")
			.emailVerifiedYn("Y")
			.profileImageUrl("http://example.com/image.png")
			.socialProviderType(null)
			.roleType(null)
			.build();

		when(userRepository.findByUserId(userId)).thenReturn(user);

		// when
		User foundUser = userService.findUser(userId);

		// then
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getUsername()).isEqualTo("Test User");
		verify(userRepository, times(1)).findByUserId(userId);
	}
}