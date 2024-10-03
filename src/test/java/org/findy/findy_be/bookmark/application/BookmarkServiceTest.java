package org.findy.findy_be.bookmark.application;

import static org.mockito.Mockito.*;

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

class BookmarkServiceTest extends MockTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private BookmarkService bookmarkService;

	private Bookmark bookmark;
	private User user;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		bookmark = mock(Bookmark.class);
		user = mock(User.class);
	}

	@DisplayName("즐겨찾기 초기화 성공")
	@Test
	public void 즐겨찾기_초기화_성공() throws Exception {
		// given // when
		bookmarkService.initBookmarks(user);

		// then
		verify(bookmarkRepository, times(1)).saveAll(anyList());
	}
}