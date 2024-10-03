package org.findy.findy_be.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.auth.oauth.info.OAuth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

	private User user;

	@BeforeEach
	void setUp() {
		user = User.create(
			"N49sfgdahdKz_fp-223424er1N3D6kd",
			"나경호",
			"hoyana@naver.com",
			"Y",
			"https://github.com/account",
			SocialProviderType.NAVER,
			RoleType.USER,
			LocalDateTime.now(),
			LocalDateTime.now()
		);
	}

	@DisplayName("User 객체 생성 성공")
	@Test
	void userCreationSuccess() {
		// given
		String expectedUsername = "나경호";
		String expectedEmail = "hoyana@naver.com";
		String expectedUserId = "N49sfgdahdKz_fp-223424er1N3D6kd";

		// when
		// user 객체는 @BeforeEach에서 초기화되므로 별도로 when 단계 없음

		// then
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo(expectedUsername);
		assertThat(user.getEmail()).isEqualTo(expectedEmail);
		assertThat(user.getUserId()).isEqualTo(expectedUserId);
	}

	@DisplayName("User 정보 업데이트 성공")
	@Test
	void updateUserInfoSuccess() {
		// given
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("name", "새로운 이름");
		attributes.put("imageUrl", "https://new-image-url.com");

		OAuth2UserInfo userInfo = new OAuth2UserInfo(attributes) {
			@Override
			public String getId() {
				return user.getUserId(); // 기존 User의 ID 사용
			}

			@Override
			public String getName() {
				return (String)attributes.get("name");
			}

			@Override
			public String getEmail() {
				return user.getEmail(); // 기존 Email 사용
			}

			@Override
			public String getImageUrl() {
				return (String)attributes.get("imageUrl");
			}
		};

		// when
		user.updateUser(userInfo);

		// then
		assertThat(user.getUsername()).isEqualTo("새로운 이름");
		assertThat(user.getProfileImageUrl()).isEqualTo("https://new-image-url.com");
		assertThat(user.getEmail()).isEqualTo("hoyana@naver.com"); // 이메일은 업데이트 되지 않아야 함
	}

	@DisplayName("User 객체의 createdAt과 updatedAt이 잘 설정되는지 확인")
	@Test
	void userTimestampsInitialization() {
		// given
		LocalDateTime now = LocalDateTime.now();

		// when
		User newUser = User.create(
			"testUserId",
			"테스트 사용자",
			"test@example.com",
			"N",
			"https://test.com/profile",
			SocialProviderType.NAVER,
			RoleType.USER,
			now,
			now
		);

		// then
		assertThat(newUser.getCreatedAt()).isEqualTo(now);
		assertThat(newUser.getUpdatedAt()).isEqualTo(now);
	}
}