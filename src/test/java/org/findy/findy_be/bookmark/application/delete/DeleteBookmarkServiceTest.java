package org.findy.findy_be.bookmark.application.delete;

import static org.findy.findy_be.common.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.MockTest;
import org.findy.findy_be.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DeleteBookmarkServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private DeleteBookmarkService deleteBookmarkService;

	private User testUser;
	private Bookmark bookmark;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		testUser = mock(User.class);
		bookmark = Bookmark.createCustomType("내장소1", testUser);
		when(testUser.getUserId()).thenReturn("N49sfgdahdKz_fp-223424er1N3D6kd");
	}

	@DisplayName("[성공] 북마크 삭제")
	@Test
	public void 북마크_삭제() throws Exception {
		// given
		Long bookmarkId = 1L;
		String userId = "N49sfgdahdKz_fp-223424er1N3D6kd";
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

		// when
		deleteBookmarkService.invoke(userId, bookmarkId);

		// then
		verify(bookmarkRepository, times(1)).delete(bookmark);
	}

	@DisplayName("[실패] 다른 유저의 북마크 삭제 시도")
	@Test
	void 북마크_삭제_실패_다른유저() {
		// given
		Long bookmarkId = 1L;
		String userId = "differentUserId";
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> deleteBookmarkService.invoke(userId, bookmarkId));

		assertEquals(FORBIDDEN_BOOKMARK_ACCESS.getMessage(), exception.getMessage());
	}
}