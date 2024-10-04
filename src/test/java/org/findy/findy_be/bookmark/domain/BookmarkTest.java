package org.findy.findy_be.bookmark.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookmarkTest {

	private User user;
	private Bookmark bookmark;

	@BeforeEach
	void setUp() {
		user = User.create("N49sfgdahdKz_fp-223424er1N3D6kd", "나경호", "hoyana@naver.com", "Y",
			"https://github.com/account", SocialProviderType.NAVER, RoleType.USER,
			LocalDateTime.now(), LocalDateTime.now());
		bookmark = Bookmark.of(MajorCategory.RESTAURANT.getLabel(), MajorCategory.RESTAURANT, user);
	}

	@DisplayName("즐겨찾기 초기화 성공")
	@Test
	public void 즐겨찾기_초기화_성공() throws Exception {
		// given
		List<Bookmark> bookmarks = Bookmark.initBookmarks(user);

		// when
		List<MajorCategory> majorCategories = List.of(MajorCategory.values());

		// then
		assertThat(bookmarks)
			.hasSize(majorCategories.size())
			.extracting(Bookmark::getMajorCategory)
			.containsExactlyInAnyOrderElementsOf(majorCategories);
	}

}