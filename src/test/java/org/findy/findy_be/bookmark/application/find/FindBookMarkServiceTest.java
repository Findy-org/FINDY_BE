package org.findy.findy_be.bookmark.application.find;

import static org.assertj.core.api.Assertions.*;
import static org.findy.findy_be.common.exception.ErrorCode.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;

class FindBookMarkServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private FindBookMarkService findBookMarkService;

	private User user;
	private Bookmark bookmark;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.create("N49sfgdahdKz_fp-223424er1N3D6kd", "나경호", "hoyana@naver.com", "Y",
			"https://github.com/account", SocialProviderType.NAVER, RoleType.USER,
			LocalDateTime.now(), LocalDateTime.now());
		bookmark = Bookmark.createCustomType("서촌", user);
	}

	@DisplayName("즐겨찾기 찾기에 성공한 경우")
	@Test
	void findBookMark_Success() throws Exception {
		// given
		Long customBookmarkId = bookmark.getId();
		when(bookmarkRepository.findById(customBookmarkId)).thenReturn(Optional.of(bookmark));

		// when
		Bookmark foundBookmark = findBookMarkService.invokeById(customBookmarkId);

		// then
		assertThat(foundBookmark).isEqualTo(bookmark);
		assertThat(foundBookmark.getBookmarkType()).isEqualTo(BookmarkType.CUSTOM);
	}

	@DisplayName("존재하지 않는 즐겨찾기 ID일 경우 EntityNotFoundException 발생")
	@Test
	void findBookMark_NotFound_ThrowsException() {
		// given
		Long nonExistentBookmarkId = 999L;
		when(bookmarkRepository.findById(nonExistentBookmarkId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> findBookMarkService.invokeById(nonExistentBookmarkId))
			.isInstanceOf(EntityNotFoundException.class)
			.hasMessageContaining(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), nonExistentBookmarkId));
	}
}