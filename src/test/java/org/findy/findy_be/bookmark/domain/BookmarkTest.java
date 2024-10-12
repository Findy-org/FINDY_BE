package org.findy.findy_be.bookmark.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookmarkTest {

	private User user;

	@BeforeEach
	void setUp() {
		user = User.create("N49sfgdahdKz_fp-223424er1N3D6kd", "나경호", "hoyana@naver.com", "Y",
			"https://github.com/account", SocialProviderType.NAVER, RoleType.USER,
			LocalDateTime.now(), LocalDateTime.now());
	}

	@DisplayName("Youtube 즐겨찾기 생성 성공")
	@Test
	public void Youtube_즐겨찾기_생성_성공() throws Exception {
		// given
		String name = "걍밍경";
		String youtuberId = "@iammingki";

		// when
		Bookmark bookmark = Bookmark.createYoutubeType(name, youtuberId, user);

		// then
		assertThat(bookmark.getBookmarkType()).isEqualTo(BookmarkType.YOUTUBE);
	}

	@DisplayName("Custom 즐겨찾기 생성 성공")
	@Test
	public void Custom_즐겨찾기_생성_성공() throws Exception {
		// given
		String name = "서촌";

		// when
		Bookmark bookmark = Bookmark.createCustomType(name, user);

		// then
		assertThat(bookmark.getBookmarkType()).isEqualTo(BookmarkType.CUSTOM);
		assertThat(bookmark.getYoutuberId()).isNull();
	}

}